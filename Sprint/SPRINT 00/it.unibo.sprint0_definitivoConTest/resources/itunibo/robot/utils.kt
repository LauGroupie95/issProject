package itunibo.robot

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
	private var CurDir		= "sud"
	
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
	
	fun writeLastPos(actor : ActorBasic, x: String, y: String, d: String, fname : String) {
		println("saveLastPos in $fname")
		var pw = PrintWriter( FileWriter(fname+".txt") )
		pw.println( x.toString() )
		pw.println( y.toString() )
		pw.println( d.toString() )
		pw.close()
	}
	
	fun resetFilePos(fname : String) {
		File(fname+".txt").writeText("")
	}
	
}
