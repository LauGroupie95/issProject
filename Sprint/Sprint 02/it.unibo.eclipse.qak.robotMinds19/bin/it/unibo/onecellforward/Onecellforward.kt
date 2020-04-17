/* Generated by AN DISI Unibo */ 
package it.unibo.onecellforward

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Onecellforward ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var foundObstacle = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("&&& robotmindapplication onecellforward waits ... ")
						foundObstacle = false 
					}
					 transition(edgeName="t05",targetState="doMoveForward",cond=whenDispatch("onestep"))
				}	 
				state("doMoveForward") { //this:State
					action { //it:State
						forward("modelChange", "modelChange(robot,w)" ,"resourcemodel" ) 
						stateTimer = TimerActor("timer_doMoveForward", 
							scope, context!!, "local_tout_onecellforward_doMoveForward", 300.toLong() )
					}
					 transition(edgeName="t06",targetState="endDoMoveForward",cond=whenTimeout("local_tout_onecellforward_doMoveForward"))   
					transition(edgeName="t07",targetState="s0",cond=whenDispatch("stopAppl"))
					transition(edgeName="t08",targetState="handleSonarRobot",cond=whenEvent("sonarRobot"))
				}	 
				state("endDoMoveForward") { //this:State
					action { //it:State
						forward("modelChange", "modelChange(robot,h)" ,"resourcemodel" ) 
						println("$name in ${currentState.stateName} | $currentMsg")
						println("&&& robotmindapplication endDoMoveForward ")
						forward("stepOk", "stepOk" ,"robotmindapplication" ) 
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleSonarRobot") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("sonar(DISTANCE)"), Term.createTerm("sonar(DISTANCE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								val distance = Integer.parseInt( payloadArg(0) ) 
								              foundObstacle = (distance<20) 
						}
					}
					 transition( edgeName="goto",targetState="stepFail", cond=doswitchGuarded({foundObstacle}) )
					transition( edgeName="goto",targetState="s0", cond=doswitchGuarded({! foundObstacle}) )
				}	 
				state("stepFail") { //this:State
					action { //it:State
						println("&&& robotmindapplication stepfail ")
						forward("stepFail", "stepFail(obstacle,150)" ,"robotmindapplication" ) 
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
