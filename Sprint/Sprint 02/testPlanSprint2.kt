//import librerie JUnit

class testPlanSprint2 {
	
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
	
	@Before
	fun init(){
		p = readInitialLists("S0 PREPARE", "PA") 	//lista per la pantry
		d = readInitialLists("S0 PREPARE", "DW")	//lista per il dishwasher
		f = readInitialLists("S0 PREPARE", "FD")	//lista per il fridge
		ot = readInitialLists("S0 PREPARE", "OT")	//lista per oggetto sul tabolo
		ft = readInitialLists("S0 PREPARE", "FT")	//lista per cibo sul tavolo
		or = readInitialLists("S0 PREPARE", "OR")	//lista per oggetti sul robot
		fr = readInitialLists("S0 PREPARE", "FR")	//lista per cibo sul robot
	}

	//1) Dispensa
	//	a) controllo posizione in (0,4)
	//	b) controllo operazioni sulle liste Dipensa e Robot
	
	@Test
	fun moveToPantry(){
		val positions = readGoalCoord_FileLog("pantry")	
		val listOperations = readLogPrepare("PANTRY")
		
		//update list "p" and "or"
		
		assertTrue("", positions.first == 0 && positions.second == 4 && p.isEmpty() && (!or.isEmpty() && or.size == 3))
	}
	
	//2) Tavolo
	//	a) controllo posizione in (4,4)
	//  b) controllo operazioni sulle liste Tavolo e Robot
	
	@Test
	fun moveToTableFromPantry(){
		val positions = readGoalCoord_FileLog("table", 1) //1 perché è la prima volta che va al tavolo
		
		val listOperations = readLogPrepare("TABLE", 1)
		
		//update list "or" and "ot"
		
		assertTrue("", positions.first == 4 && positions.second == 4 && or.isEmpty() && (!ot.isEmpty() && ot.size == 3))
	}
	
	//3) Frigo
	//	a) controllo posizione in (6,0)
	//	b) controllo operazioni sulle liste Frigo e Robot
	
	@Test
	fun moveToFridge(){
		val positions = readGoalCoord_FileLog("fridge")	
		val listOperations = readLogPrepare("FRIDGE")
		
		//update list "f" and "fr"
		
		assertTrue("", positions.first == 6 && positions.second == 0 && f.size == 4 && (!fr.isEmpty() && fr.size == 3))
	}
	
	//4) Tavolo
	//	a) controllo posizione in (4,4)
	//	b) controllo operazioni sulle liste Tavolo e Robot
	
	@Test
	fun moveToTableFromFridge(){
		val positions = readGoalCoord_FileLog("table", 2) //2 perché è la seconda volta che va al tavolo
  
		val listOperations = readLogPrepare("TABLE", 2)
		
		//update list "fr" and "ft"
		
		assertTrue("", positions.first == 5 && positions.second == 1 && fr.isEmpty() && (!ft.isEmpty() && ft.size == 3))
	}

	//5) RH
	//	a) controllo posizione in (0,0)

	@Test
	fun returnHome() {
		
		val positions = readGoalCoord_FileLog("RH")	
		
		val PA = readFinalLists("HOME","PA")
		val DW = readFinalLists("HOME","DW")
		val FD = readFinalLists("HOME","FD")
		val OR = readFinalLists("HOME","OR")
		val OT = readFinalLists("HOME","OT")
		val FR = readFinalLists("HOME","FR")
		val FT = readFinalLists("HOME","FT")
		
		assertTrue("", positions.first == 0 && positions.second == 0 &&
				  checkListsContent(p, PA) &&   checkListsContent(d, DW) &&   checkListsContent(f, FD) &&
				  checkListsContent(ot, OT) &&   checkListsContent(ft, FT) &&   checkListsContent(or, OR) &&
				  checkListsContent(fr, FR)
		)
	}
}