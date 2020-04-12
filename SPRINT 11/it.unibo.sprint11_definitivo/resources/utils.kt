import aima.core.agent.Action
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.*
import alice.tuprolog.*
import java.io.PrintWriter
import java.io.FileWriter
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileOutputStream
import java.io.File
import itunibo.planner.plannerUtil
import itunibo.planner.model.RoomMap
import itunibo.planner.model.Box

object utils {
 
	private var CurX        = "0"
	private var CurY        = "0"
	private var CurDir		= "sud"
	
	/*
	* ------------------------------------------------
	* UTILITY
	* ------------------------------------------------
	*/
	
	fun printPosition(actor : ActorBasic) : String {
		return "(" + itunibo.planner.plannerUtil.getPosX() + ", " +
				itunibo.planner.plannerUtil.getPosY() + " - " +
				itunibo.planner.moveUtils.getDirection(actor) + ")"
	}
	
	fun prepareToSend(appliance: String, msg: String, resourceType : String) : String {
		if (msg == "[]" || msg == "[]%[]") {
			return appliance + "(" + "No " + resourceType + ")" 
		}
		if (appliance == "table" || appliance == "robot"){
			var lists = msg.split("%")
			var listObjFormatted = formatListProlog(lists[0])
			var listFoodFormatted = formatListPrologFood(lists[1])
			return appliance + "(" + listObjFormatted + "%" + listFoodFormatted + ")"
		}
		return appliance + "(" + formatListProlog(msg) + ")"
	}
	
	//Formatta lista per assert e retract
	fun formatListProlog(list: String) : String{
		return list.replace("[", "").replace("item(", "").replace(")","").replace("]", "");
	}
	
	//Formatta lista per assert, retract e frontend
	fun formatListPrologFood(list: String) : String{
		return list.replace("[", "").replace("item(", "").replace("),","-").replace(")","").replace("]", "");
	}
	
	// ***********************************************************************************************************************
	// Noi abbiamo previsto una funzione di controllo della direzione statica basata sulle celle che sappiamo essere quelle
	// attorno al tavolo. Lo sappiamo perché conosciamo le dimensioni della stanza e la posizione del tavolo.
	// Eventualmente una estensione dinamica potrebbe operare come segue:
	// 		1) con una solveTable(L) ottengo la lista delle celle accessibili
	//		2) ordino le celle in base alla coordinata X e a parità di coord. X, in base alla coord. Y
	//		3) divido le celle in 4 gruppi:
	//				. un gruppo avrà la stessa x1
	//				. un gruppo avrà la stessa x2 ma diversa dalla x1
	//				. un gruppo avrà la stessa y1
	//				. un gruppo avrà la stessa y2 ma diversa dalla y1
	//		4) a questo punto assegno la direzione:
	//				. le celle con stessa x1 < x2 avranno direzione destra
	//				. le celle con stessa x2 > x1 avranno direzione sinistra
	//				. le celle con stessa y1 < y2 avranno direzione giù
	//				. le celle con stessa y2 > y1 avranno direzione su
	//	Es. Celle: 41, 51, 62, 63, 44, 54, 32, 33
	//	Gruppo 1: 32, 33 --> stessa x1, direzione destra
	//  Gruppo 2: 62, 63 --> stessa x2, direzione sinistra
	//  Gruppo 3: 41, 51 --> stessa y1, direzione su
	//  Gruppo 4: 44, 54 --> stessa y2, direzione giù
	// ***********************************************************************************************************************		
	suspend fun controlDirection(actor : ActorBasic, reachDestination : String, goalX : Int, goalY : Int){
		println("")
		var goalDir = ""
		when(reachDestination) {
	    	"RH" -> goalDir = "downDir" 
		    "pantry" -> goalDir = "downDir"
		    "fridge" -> goalDir = "upDir" 		
			"dishWasher" -> goalDir = "downDir"
			"table" -> {
				if(goalX == 2){
					goalDir = "rightDir"
				}
				else if(goalX == 5){
					goalDir = "leftDir"
				}
				else if(goalY == 1){
					goalDir = "downDir"
				}
				else if(goalY == 4){
					goalDir = "upDir"
				}
			}
		}
		println("fun controlDirection: ${reachDestination}, ${goalX}, ${goalY}, goalDir = ${goalDir}")
		while(itunibo.planner.moveUtils.getDirection(actor) != goalDir){
			itunibo.planner.moveUtils.rotateLeft(actor, 300)
			delay(500)
		}
	}
	
	/*
	* ------------------------------------------------
	* TEST
	* ------------------------------------------------
	*/
	
	fun savePos(x: String, y: String, d: String) {
		CurX = x
		CurY = y
		CurDir = d
	}
	
	fun readLastPos(fname : String) : Triple<Int, Int, String> {
		println("readLastPos in $fname")
		var pw = BufferedReader( FileReader(fname+".txt"))
		
		val x = pw.readLine().toInt()
		val y = pw.readLine().toInt()
		val d = pw.readLine()
		pw.close()
		return Triple(x, y, d)
	}
	
	fun writeLastPos(x: String, y: String, d: String, fname : String) {
		println("saveLastPos in $fname")
		var pw = PrintWriter( FileWriter(fname+".txt") )
		pw.println( x.toString() )
		pw.println( y.toString() )
		pw.println( d.toString() )
		pw.close()
	}
	
	fun resetFile(fname : String) {
		File(fname+".txt").writeText("")
	}
	
	fun readGoalCoord_FileLog(appliance: String) : Pair<Int, Int> {
		var xGoal = -1
		var yGoal = -1
		var xGoalReached = -1
		var yGoalReached = -1
		
        var pw = BufferedReader( FileReader(fileLogName+".txt"))
        var line = ""
		var applianceGoal = ""
		var applianceGoalReached = ""
		
		var goalfound = false;
		var goalreachedfound = false;
		
        do {
			line = pw.readLine()
			
			if(line.startsWith("GOAL:")){            
				var parts = line.split("GOAL: ", "| ", ", ")
				applianceGoal = parts[1].trim()
				xGoal = parts[2].trim().toInt()
				yGoal = parts[3].trim().toInt()
				
				if (applianceGoal.equals(appliance))
					goalfound = true;
			}
			if(line.startsWith("GOAL REACHED:")){
				var parts = line.split("GOAL REACHED: ", "| ", ", ")
				applianceGoalReached = parts[1].trim()
				xGoalReached = parts[2].trim().toInt();
				yGoalReached = parts[3].trim().toInt();
				
				if (applianceGoalReached.equals(appliance))
					goalreachedfound=true;
			}
			if (goalfound && goalreachedfound) {
				if (xGoal == xGoalReached && yGoal == yGoalReached) {
					println("trovato!")
					return Pair (xGoal, yGoal)
				}
			}
		} while (line != null)
		pw.close()
		return Pair(xGoal, yGoal)
	}
	
	//con occorrenza perché se è già stato al tavolo 1 volta, magari vuole sapere la posizione della 2° volta
	fun readGoalCoord_FileLog(appliance: String, occ: Int) : Pair<Int, Int> {
		var xGoal = -1
		var yGoal = -1
		var xGoalReached = -1
		var yGoalReached = -1
		
        var pw = BufferedReader( FileReader(fileLogName+".txt"))
        var line = ""
		var applianceGoal = ""
		var applianceGoalReached = ""
		
		var goalfound = false;
		var goalreachedfound = false;
		var occurrences = 0
		
        do {
			line = pw.readLine()
			if(line.startsWith("GOAL:")){
				var parts = line.split("GOAL: ", "| ", ", ")
				applianceGoal = parts[1].trim()
				xGoal = parts[2].trim().toInt()
				yGoal = parts[3].trim().toInt()

				if (applianceGoal.equals(appliance))
					goalfound = true;
			}
			if(line.startsWith("GOAL REACHED:")){
				var parts = line.split("GOAL REACHED: ", "| ", ", ")
				applianceGoalReached = parts[1].trim()
				xGoalReached = parts[2].trim().toInt();
				yGoalReached = parts[3].trim().toInt();

				if (applianceGoalReached.equals(appliance))
					goalreachedfound = true;
			}
			if (goalfound && goalreachedfound) {
				if (xGoal == xGoalReached && yGoal == yGoalReached) {
					if (occurrences == occ-1)
						println("trovato!")
					goalfound = false
					goalreachedfound = false
					occurrences++
				}
			}
		} while (line != null && occurrences != occ)
		
		pw.close()
		return Pair(xGoal, yGoal)
	}
	
	//Funzionante in test sprint 2. Eventualmente da rivedere per test finale se necessario
	fun readLogPrepare(appliance: String) : List<Triple<Char, String, String>> {
		var result = mutableListOf<Triple<Char, String, String>>()
		
        var pw = BufferedReader( FileReader(fileLogKb+".txt"))
        var line = pw.readLine()
		
		while(line != null){
			if(line.startsWith(appliance)){
				line = pw.readLine()
				while(line != ""){					
					//-P.dishes
					var parts = line.split(".")//-PA | dishes
					var operation = parts[0].get(0)  //-
					var domesticAppliance = parts[1].trim() //PA
					var obj = parts[2].trim() //dishes
					
					result.add(Triple(operation, domesticAppliance, obj))
					
					line = pw.readLine()
				}
			}
			line = pw.readLine()
		}
		pw.close()
		
		return result
	}
	
	//Funzionante in test sprint 2. Eventualmente da rivedere per test finale se necessario
	fun readLogPrepare(appliance: String, occ: Int) : List<Triple<Char, String, String>> {
		var result = mutableListOf<Triple<Char, String, String>>()
		
        var pw = BufferedReader( FileReader(fileLogKb+".txt"))
        var line = pw.readLine()
		var occurances = 0
		
		while(line != null){
			if(line.startsWith(appliance)){
				occurances++
				if(occurances == occ){
					line = pw.readLine()
					while(line != ""){					
						//-P.dishes
						var parts = line.split(".")//-PA | dishes
						var operation = parts[0].get(0)  //-
						var domesticAppliance = parts[1].trim() //PA
						var obj = parts[2].trim() //dishes
						
						result.add(Triple(operation, domesticAppliance, obj))
						
						line = pw.readLine()
					}
					return result
				}
			}
			line = pw.readLine()
		}
		pw.close()
		
		return result
	}
	
	//Funzionante in test sprint 2. Eventualmente da rivedere per test finale se necessario
	fun readLogPrepareLists(startingLine: String, applianceList: String) : MutableList<String> {
		var result = mutableListOf<String>()
		
		 var pw = BufferedReader( FileReader(fileLogKb+".txt"))
        var line = pw.readLine()
		var occurances = 0
		
		while(line != null){
			if(line.startsWith(startingLine)){
				line = pw.readLine()
				
				while(line != ""){
					if(line.startsWith(applianceList) && line.contains("[]")){
						return result
					} else if(line.startsWith(applianceList)) {
						when(applianceList){
							"PA", "OT", "DW", "OR" -> {		
								var parts = line.split("[", ",", "]")
	
								for(x in 1 until parts.size - 1){
									result.add(parts[x])
								}
															
								return result
							}
							"FT", "FD", "FR" -> {
								var parts = line.split("[  ", ",  ", "]")
								
								for(x in 1 until parts.size - 1){
									result.add(parts[x])
								}
														
								return result
							}
						}//when
					}//else if
					line = pw.readLine()
				}
			}
			line = pw.readLine()
		}
		pw.close()
		
		return result
	}
	
	//Controllo se tutti gli elementi in lista1 sono anche in lista2
	fun checkListsContent(list1: List<String>, list2: List<String>) : Boolean {
		for(el1 in list1){
			if(!list2.contains(el1)){
				return false
			}
		}

		return true
	}
	
	/*
	* ------------------------------------------------
	* LOGGER
	* ------------------------------------------------
	*/
	
	private var fileLogName : String = "fileLog"
	private var fileLogKb : String = "fileLogKb"
	
	//funzione per scrivere/sovrascrivere il file log.old
	fun backupLastLog() {
		if (File(fileLogName+".txt").exists()) {
			File(fileLogName+".txt").copyTo(File(fileLogName+"_old.txt"), true);
		}
		if (File(fileLogKb+".txt").exists()) {
			File(fileLogKb+".txt").copyTo(File(fileLogKb+"_old.txt"), true);
		}
	}
	
	fun writeLog(log: String) {
		File(fileLogName+".txt").appendText(log+"\n")
	}
	
	fun writeLogKb(log: String) {
		File(fileLogKb+".txt").appendText(log+"\n")
	}
	
}
