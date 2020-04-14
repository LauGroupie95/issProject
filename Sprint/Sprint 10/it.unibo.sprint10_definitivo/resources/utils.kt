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
	
	fun replaceUglyString (x : String, resourceType : String) : String {
		if ( x == "[]" ) {
			return " No " + resourceType;
		}
		return x.replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ").trim()
		//return x.replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ").trim()
		//return x.replace("/", "").replace(")", "").replace("'", "").replace("(", " ").trim()
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
		CurX=x
		CurY=y
		CurDir=d
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
	
	/*
	* ------------------------------------------------
	* LOGGER
	* ------------------------------------------------
	*/
	
	private var fileLogName : String = "fileLog"
	
	//funzione per scrivere/sovrascrivere il file log.old
	fun backupLastLog() {
		if (File(fileLogName+".txt").exists()) {
			//Overwrite if the target fileName is already present
			File(fileLogName+".txt").copyTo(File(fileLogName+"_old.txt"), true);
		}
	}
	
	//funzione per scrivere il file corrente dei log
	fun writeLog(log: String) {
		File(fileLogName+".txt").appendText(log+"\n")
	}
	
}
