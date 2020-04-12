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

object utils {

	private var CurX        = "0"
	private var CurY        = "0"
	private var CurXTest    = 0
	private var CurYTest    = 0
	private var CurDir		= "sud"
	
	fun savePos(x: String, y: String, d: String) {
		CurX=x
		CurY=y
		CurDir=d
	}
	
	fun savePosTest(x: Int, y: Int) {
		CurXTest=x
		CurYTest=y
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
