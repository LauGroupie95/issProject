package test

import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import alice.tuprolog.SolveInfo
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay


class TestSprint0 {
	
	var resource : ActorBasic? = null
	
	@Before
	fun systemSetUp() {
  	 		GlobalScope.launch{
 			    println(" %%%%%%% Test Sprint0 starts the program ")
				it.unibo.ctxRobotMind.main()
 			}
			delay(5000)		//give the time to start
			resource = sysUtil.getActor("resourcemodel")	
		    println(" %%%%%%% Test Sprint0 getActors resource=${resource}")
 	}
 
	@After
	fun terminate() {
		println(" %%%%%%% Test Sprint0 terminate ")
	}
 
	@Test
	fun moveTest() {
		println(" %%%%%%% Test Sprint0  moveTest ")
		delay(18000) //necessaria se no controlla il file prima
					//che il robot abbia terminato il percorso
		checkPositionRobot()
 	}

	fun checkPositionRobot() {
		println(" %%%%%%% Test Sprint0  stoppedRobot %%%")
		
		val lastP = itunibo.robot.utils.readLastPos("filePos")	
  		println("lastP=$lastP")
		
		var result = false
		if (lastP.first == 2 && lastP.second == 1 && lastP.third == "sud")
			result = true
		else
			result = false
		
		assertTrue("", result == true )
	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}

}
