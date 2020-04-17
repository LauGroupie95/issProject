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
import java.util.StringTokenizer

object utils {

	private var CurX        = "0"
	private var CurY        = "0"
	private var CurXTest    = 0
	private var CurYTest    = 0
	private var CurDir		= "sud"
	
	fun savePos(x: String, y: String, d: String) {
		CurX = x
		CurY = y
		CurDir = d
	}
	
	fun savePosTest(x: Int, y: Int) {
		CurXTest = x
		CurYTest = y
	}
	
	fun getCurX() : String {
		return CurX
	}
	fun getCurY() : String {
		return CurY
	}
	
	fun getCurXTest() : Int {
		return CurXTest
	}
	fun getCurYTest() : Int {
		return CurYTest
	}
	
	fun readLastPos(fname : String) : Pair<Int, Int> {
		println("readLastPos in $fname")
		var pw = BufferedReader( FileReader(fname+".txt"))
	
		val line = pw.readLine().split(", ")
		var x = line.get(0).trim().toInt()
		val y = line.get(1).trim().toInt()
		pw.close()
		return Pair(x, y)
	}
	
	fun writeLastPos(x: String, y: String, fname : String) {
		resetFile(fname)
		println("saveLastPos in $fname")
		var pw = PrintWriter( FileWriter(fname+".txt") )
		pw.println( "${x.toString()}, ${y.toString()}")
		pw.close()
	}
	
	fun resetFile(fname : String) {
		File(fname+".txt").writeText("")
	}
	
	/*
	* ------------------------------------------------
	* LOGGER
	* ------------------------------------------------
	*/
	
	private var fileLogName : String = "fileLog"
	private var fileLogPrepare : String = "fileLogPrepare"
	
	//funzione per scrivere/sovrascrivere il file log.old
	fun backupLastLog() {
		if (File(fileLogName+".txt").exists()) {
			//Overwrite if the target fileName is already present
			File(fileLogName+".txt").copyTo(File(fileLogName+"_old.txt"), true);
		}
		if (File(fileLogPrepare+".txt").exists()) {
			//Overwrite if the target fileName is already present
			File(fileLogPrepare+".txt").copyTo(File(fileLogPrepare+"_old.txt"), true);
		}
	}
	
	//funzione per scrivere il file corrente dei log
	fun writeLog(log: String) {
		File(fileLogName+".txt").appendText(log+"\n")
	}
	
	//funzione per scrivere il file corrente dei log
	fun writeLogPrepare(log: String) {
		File(fileLogPrepare+".txt").appendText(log+"\n")
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
				//GOAL: fridge | 5, 0
                
				var parts = line.split("GOAL: ", "| ", ", ")
				applianceGoal = parts[1].trim()
				xGoal = parts[2].trim().toInt()
				yGoal = parts[3].trim().toInt()
				/*
				println()
				println("goal")
				println(applianceGoal)
				println(xGoal)
				println(yGoal)
				*/
				if (applianceGoal.equals(appliance))
					goalfound = true;
			}
			if(line.startsWith("GOAL REACHED:")){
				//GOAL REACHED: fridge | 5, 0
				
				var parts = line.split("GOAL REACHED: ", "| ", ", ")
				applianceGoalReached = parts[1].trim()
				xGoalReached = parts[2].trim().toInt();
				yGoalReached = parts[3].trim().toInt();
				/*
				println()
				println("goalreached")
				println(applianceGoalReached)
				println(xGoalReached)
				println(yGoalReached)
				*/
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
				//GOAL: fridge | 5, 0
                
				var parts = line.split("GOAL: ", "| ", ", ")
				applianceGoal = parts[1].trim()
				xGoal = parts[2].trim().toInt()
				yGoal = parts[3].trim().toInt()
				/*
				println()
				println("goal")
				println(applianceGoal)
				println(xGoal)
				println(yGoal)
				*/
				if (applianceGoal.equals(appliance))
					goalfound = true;
			}
			if(line.startsWith("GOAL REACHED:")){
				//GOAL REACHED: fridge | 5, 0
				
				var parts = line.split("GOAL REACHED: ", "| ", ", ")
				applianceGoalReached = parts[1].trim()
				xGoalReached = parts[2].trim().toInt();
				yGoalReached = parts[3].trim().toInt();
				/*
				println()
				println("goalreached")
				println(applianceGoalReached)
				println(xGoalReached)
				println(yGoalReached)
				*/
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
	
	fun readLogPrepare(appliance: String) : List<Triple<Char, String, String>> {
		var result = mutableListOf<Triple<Char, String, String>>()
		
        var pw = BufferedReader( FileReader(fileLogPrepare+".txt"))
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
	
	fun readLogPrepare(appliance: String, occ: Int) : List<Triple<Char, String, String>> {
		var result = mutableListOf<Triple<Char, String, String>>()
		
        var pw = BufferedReader( FileReader(fileLogPrepare+".txt"))
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
	
	fun readLogPrepareLists(startingLine: String, applianceList: String) : MutableList<String> {
		var result = mutableListOf<String>()
		
		 var pw = BufferedReader( FileReader(fileLogPrepare+".txt"))
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
								var parts = line.split("[", ",", "]")//OT: | dishes | forks | glasses | ""
	
								//for(var i = 1; i < parts.size-1; i++)
								for(x in 1 until parts.size - 1){
									result.add(parts[x])
								}
															
								return result
							}
							"FT", "FD", "FR" -> {
								//FT: [  meat,c001,5,  cola,c002,5,  pizza,c004,10]
								var parts = line.split("[  ", ",  ", "]")
								
								//for(var i = 1; i < parts.size-1; i++)
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
	
	fun checkListsContent(list1: List<String>, list2: List<String>) : Boolean {
		for(el1 in list1){
			if(!list2.contains(el1)){
				return false
			}
		}

		return true
	}
}
