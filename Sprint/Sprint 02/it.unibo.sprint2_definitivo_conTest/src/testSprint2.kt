import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.After
import org.junit.Test
import kotlinx.coroutines.*
import it.unibo.kactor.*
import alice.tuprolog.*
import kotlin.random.Random
import org.junit.runners.MethodSorters
import org.junit.FixMethodOrder
import java.io.BufferedReader
import java.io.FileReader
import org.junit.jupiter.api.BeforeAll

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class testSprint2 {
	
	var robotPositionX = "0"
	var robotPositionY = "0"
	
	var p = mutableListOf<String>()
	var d = mutableListOf<String>()
	var f = mutableListOf<String>()
	var ot = mutableListOf<String>()
	var ft = mutableListOf<String>()
	var or = mutableListOf<String>()
	var fr = mutableListOf<String>()
	
	/**
	 * Test plan:
	 * 1) Dispensa
	 *    a) controllo posizione in (0,4)
	 *    b) controllo operazioni sulle liste Dipensa e Robot
	 * 2) Tavolo
	 *    a) controllo posizione in (4,4)
	 *    b) controllo operazioni sulle liste Tavolo e Robot
	 * 3) Frigo
	 *    a) controllo posizione in (6,0)
	 *    b) controllo operazioni sulle liste Frigo e Robot
	 * 4) Tavolo
	 *    a) controllo posizione in (4,4)
	 *    b) controllo operazioni sulle liste Tavolo e Robot
	 * 5) RH
	 *    a) controllo posizione in (0,0)
	 */
	
	@Test
	fun cumulativeTest(){
		p = utils.readLogPrepareLists("S0 PREPARE", "PA")
		d = utils.readLogPrepareLists("S0 PREPARE", "DW")
		f = utils.readLogPrepareLists("S0 PREPARE", "FD")
		ot = utils.readLogPrepareLists("S0 PREPARE", "OT")
		ft = utils.readLogPrepareLists("S0 PREPARE", "FT")
		or = utils.readLogPrepareLists("S0 PREPARE", "OR")
		fr = utils.readLogPrepareLists("S0 PREPARE", "FR")
		 
		println("p = $p")
		println("d = $d")
		println("f = $f")
		println("ot = $ot")
		println("ft = $ft")
		println("or = $or")
		println("fr = $fr")
		
		b_moveToPantry()
		c_moveToTableFromPantry()
		d_moveToFridge()
		e_moveToTableFromFridge()
		f_returnHome()
	}

	fun b_moveToPantry(){
		println("\n-------- TestPrepare moveToPantry --------\n")
		
		val positions = utils.readGoalCoord_FileLog("pantry")	
  		println("positions = $positions")
		
		val listOperations = utils.readLogPrepare("PANTRY")
		println("listOperations = $listOperations")
		
		for(op in listOperations){
			//op = (-, PA, dishes)
			if(op.first == '-' && op.second == "PA"){
				p.remove(op.third)
			} else if (op.first == '+' && op.second == "OR"){
				or.add(op.third)
			}
		}
		
		println("p = $p")
		println("d = $d")
		println("f = $f")
		println("ot = $ot")
		println("ft = $ft")
		println("or = $or")
		println("fr = $fr")
			
		assertTrue("", positions.first == 0 && positions.second == 4 && p.isEmpty() && (!or.isEmpty() && or.size == 3))
	}
	
	fun c_moveToTableFromPantry(){
		println("\n-------- TestPrepare moveToTableFromPantry --------\n")
		
		val positions = utils.readGoalCoord_FileLog("table", 1) //1 perché è la prima volta che va al tavolo
  		println("positions = $positions")
		
		val listOperations = utils.readLogPrepare("TABLE", 1)
		println("listOperations = $listOperations")
		
		for(op in listOperations){
			//op = (+, OT, dishes)
			if(op.first == '-' && op.second == "OR"){
				or.remove(op.third)
			} else if (op.first == '+' && op.second == "OT"){
				ot.add(op.third)
			}
		}
		
		println("p = $p")
		println("d = $d")
		println("f = $f")
		println("ot = $ot")
		println("ft = $ft")
		println("or = $or")
		println("fr = $fr")
		
		assertTrue("", positions.first == 4 && positions.second == 4 && or.isEmpty() && (!ot.isEmpty() && ot.size == 3))
	}
	
	fun d_moveToFridge(){
		println("\n-------- TestPrepare moveToFridge --------\n")
		
		val positions = utils.readGoalCoord_FileLog("fridge")	
  		println("positions = $positions")
		
		val listOperations = utils.readLogPrepare("FRIDGE")
		println("listOperations = $listOperations")
		
		for(op in listOperations){
			//op = (-, FD, c001,meat,5)
			if(op.first == '-' && op.second == "FD"){
				f.remove(op.third)
			} else if (op.first == '+' && op.second == "FR"){
				fr.add(op.third)
			}
		}
		
		println("p = $p")
		println("d = $d")
		println("f = $f")
		println("ot = $ot")
		println("ft = $ft")
		println("or = $or")
		println("fr = $fr")
		
		assertTrue("", positions.first == 6 && positions.second == 0 && f.size == 4 && (!fr.isEmpty() && fr.size == 3))
	}
	
	fun e_moveToTableFromFridge(){
		println("\n-------- TestPrepare moveToTableFromFridge --------\n")
		
		val positions = utils.readGoalCoord_FileLog("table", 2) //2 perché è la seconda volta che va al tavolo
  		println("positions = $positions")
		
		val listOperations = utils.readLogPrepare("TABLE", 2)
		println("listOperations = $listOperations")
		
		for(op in listOperations){
			//op = (+, FT, c001,meat,5)
			if(op.first == '-' && op.second == "FR"){
				fr.remove(op.third)
			} else if (op.first == '+' && op.second == "FT"){
				ft.add(op.third)
			}
		}
		
		println("p = $p")
		println("d = $d")
		println("f = $f")
		println("ot = $ot")
		println("ft = $ft")
		println("or = $or")
		println("fr = $fr")
		
		assertTrue("", positions.first == 5 && positions.second == 1 && fr.isEmpty() && (!ft.isEmpty() && ft.size == 3))
	}

	fun f_returnHome(){
		println("\n-------- TestPrepare returnHome --------\n")
		
		val positions = utils.readGoalCoord_FileLog("RH")	
  		println("positions = $positions")
		
		val PA = utils.readLogPrepareLists("HOME","PA")
		val DW = utils.readLogPrepareLists("HOME","DW")
		val FD = utils.readLogPrepareLists("HOME","FD")
		val OR = utils.readLogPrepareLists("HOME","OR")
		val OT = utils.readLogPrepareLists("HOME","OT")
		val FR = utils.readLogPrepareLists("HOME","FR")
		val FT = utils.readLogPrepareLists("HOME","FT")
		
		println("PA = $PA")
		println("DW = $DW")
		println("FD = $FD")
		println("OR = $OR")
		println("OT = $OT")
		println("FR = $FR")
		println("FT = $FT")
		println("")
		println("p = $p")
		println("d = $d")
		println("f = $f")
		println("ot = $ot")
		println("ft = $ft")
		println("or = $or")
		println("fr = $fr")
		
		assertTrue("", positions.first == 0 && positions.second == 0 &&
				utils.checkListsContent(p, PA) && utils.checkListsContent(d, DW) && utils.checkListsContent(f, FD) &&
				utils.checkListsContent(ot, OT) && utils.checkListsContent(ft, FT) && utils.checkListsContent(or, OR) &&
				utils.checkListsContent(fr, FR)
		)
	}
	
	/*
 	 * Mappa della stanza
     *    0  1  2  3  4  5  6  7
 	 * 0 |r, 1, 1, 1, 1, 1, F, X, 
	 * 1 |1, 0, 0, 0, 0, 0, 1, X, 
	 * 2 |1, 0, 0, T, T, 0, 1, X, 
	 * 3 |1, 0, 0, T, T, 0, 1, X, 
	 * 4 |P, 1, 1, 1, 1, 1, D, X, 
	 * 5 |X, X, X, X, X, X, X, X,
 	 */
	
}