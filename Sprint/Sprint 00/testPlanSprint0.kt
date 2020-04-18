//import librerie necessarie

class TestPlanSprint0 {
	
	var cellaDestinazione = (2,1)
	var finalDir = "sud"
	
	@Before
	fun systemSetUp() {
  	 		//lancia scope dell'attore
	}
 
	@Test
	fun moveTest() {
		checkPositionRobot()
 	}

	fun checkPositionRobot() {
		//recupera posizione finale del robot
		var position = getFinalPos()
		var dir = getFinalDir()
		
		//check posizione: ci aspettiamo che sia nella cella di destinazione
		assertTrue( "", position.equals(cellaDestinazione) && dir.equals(finalDir) )
	}
}
