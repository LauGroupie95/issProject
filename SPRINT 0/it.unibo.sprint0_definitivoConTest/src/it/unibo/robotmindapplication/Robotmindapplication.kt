/* Generated by AN DISI Unibo */ 
package it.unibo.robotmindapplication

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Robotmindapplication ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var colonnadispari = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						itunibo.robot.utils.resetFilePos( "filePos"  )
						solve("consult('sysRules.pl')","") //set resVar	
						solve("consult('floorMap.pl')","") //set resVar	
						println("&&&  robotmindapplication STARTED")
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						forward("startAppl", "startAppl(inizia)" ,"robotmindapplication" ) 
					}
					 transition(edgeName="t05",targetState="stopApplication",cond=whenDispatch("stopAppl"))
					transition(edgeName="t06",targetState="startApplication",cond=whenDispatch("startAppl"))
				}	 
				state("stopApplication") { //this:State
					action { //it:State
						println("&&& robotmindapplication stopApplication ... ")
						forward("modelChange", "modelChange(robot,h)" ,"resourcemodel" ) 
						forward("stopAppl", "stopAppl(user)" ,"onecellforward" ) 
					}
				}	 
				state("startApplication") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						solve("initMap(sud)","") //set resVar	
					}
					 transition( edgeName="goto",targetState="doApplication", cond=doswitch() )
				}	 
				state("doApplication") { //this:State
					action { //it:State
						solve("cell(2,1,1)","") //set resVar	
						if(currentSolution.isSuccess()) { solve("direction(sud)","") //set resVar	
						if(currentSolution.isSuccess()) { println("TROVATO --- FINISCO---")
						itunibo.robot.utils.writeLastPos(myself ,"2", "1", "sud", "filePos" )
						forward("stopAppl", "stopAppl(stop)" ,"robotmindapplication" ) 
						 }
						 }
						else
						{ forward("onestep", "onestep" ,"onecellforward" ) 
						 }
					}
					 transition(edgeName="t07",targetState="stopApplication",cond=whenDispatch("stopAppl"))
					transition(edgeName="t08",targetState="hadleStepOk",cond=whenDispatch("stepOk"))
					transition(edgeName="t09",targetState="hadleStepFail",cond=whenDispatch("stepFail"))
					transition(edgeName="t010",targetState="stopApplication",cond=whenDispatch("stopAppl"))
				}	 
				state("hadleStepOk") { //this:State
					action { //it:State
						println("&&& robotmindapplication step ok")
						solve("updateMapAfterStep","") //set resVar	
						delay(500) 
					}
					 transition( edgeName="goto",targetState="doApplication", cond=doswitch() )
				}	 
				state("hadleStepFail") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("&&& robotmindapplication step failed")
						if(colonnadispari == false){ forward("modelChange", "modelChange(robot,a)" ,"resourcemodel" ) 
						solve("changeDirectionA","") //set resVar	
						solve("direction(D)","") //set resVar	
						forward("robotCmd", "robotCmd(a)" ,"basicrobot" ) 
						delay(1000) 
						forward("changeCol", "changeCol" ,"onecellforward" ) 
						solve("updateMapAfterStep","") //set resVar	
						delay(1000) 
						forward("modelChange", "modelChange(robot,a)" ,"resourcemodel" ) 
						solve("changeDirectionA","") //set resVar	
						solve("direction(D)","") //set resVar	
						forward("robotCmd", "robotCmd(a)" ,"basicrobot" ) 
						delay(1000) 
						colonnadispari=true
						 }
						else
						 { forward("modelChange", "modelChange(robot,d)" ,"resourcemodel" ) 
						 solve("changeDirectionD","") //set resVar	
						 solve("direction(D)","") //set resVar	
						 if(currentSolution.isSuccess()) { println("\nD change con d=${getCurSol("D")}\n")
						  }
						 forward("robotCmd", "robotCmd(d)" ,"basicrobot" ) 
						 delay(1000) 
						 forward("changeCol", "changeCol" ,"onecellforward" ) 
						 solve("updateMapAfterStep","") //set resVar	
						 delay(1000) 
						 forward("modelChange", "modelChange(robot,d)" ,"resourcemodel" ) 
						 solve("changeDirectionD","") //set resVar	
						 solve("direction(D)","") //set resVar	
						 if(currentSolution.isSuccess()) { println("\nD change con d=${getCurSol("D")}\n")
						  }
						 forward("robotCmd", "robotCmd(d)" ,"basicrobot" ) 
						 delay(1000) 
						 colonnadispari=false
						  }
						println(colonnadispari)
					}
					 transition( edgeName="goto",targetState="doApplication", cond=doswitch() )
				}	 
			}
		}
}
