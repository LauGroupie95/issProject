/* Generated by AN DISI Unibo */ 
package it.unibo.clear

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Clear ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var inTable = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("CLEAR AVVIATA")
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("CLEAR: in attesa di un comando")
					}
					 transition(edgeName="t012",targetState="reachTable",cond=whenDispatch("clear"))
				}	 
				state("reachTable") { //this:State
					action { //it:State
						println("")
						println("CLEAR: State doClear")
						println("CLEAR: Obiettivo table")
						forward("reachTable", "reachTable(clear)" ,"navi" ) 
					}
					 transition(edgeName="t113",targetState="inTable",cond=whenDispatch("tableReached"))
				}	 
				state("inTable") { //this:State
					action { //it:State
						println("")
						println("CLEAR: State inTable")
						delay(1000) 
						if(inTable == 0){
						forward("moveObjOnRobot", "moveObjOnRobot(table)" ,"kb" ) 
						delay(1000) 
						println("CLEAR: Obiettivo DISHWASHER")
						forward("reachAppliance", "reachAppliance(clear,dishWasher)" ,"navi" ) 
						inTable = inTable + 1
								}else{
						forward("moveFoodOnRobot", "moveFoodOnRobot(table,all_all)" ,"kb" ) 
						delay(1000) 
						println("CLEAR: Obiettivo FRIDGE")
						forward("reachAppliance", "reachAppliance(clear,fridge)" ,"navi" ) 
						}
					}
					 transition(edgeName="t214",targetState="inFridge",cond=whenDispatch("fridgeReached"))
					transition(edgeName="t215",targetState="inDishWasher",cond=whenDispatch("dishWasherReached"))
				}	 
				state("inDishWasher") { //this:State
					action { //it:State
						println("")
						println("CLEAR: State inDishWasher")
						delay(1000) 
						forward("moveObjToDomesticResource", "moveObjToDomesticResource(dishwasher)" ,"kb" ) 
					}
					 transition( edgeName="goto",targetState="reachTable", cond=doswitch() )
				}	 
				state("inFridge") { //this:State
					action { //it:State
						println("")
						println("CLEAR: State inFridge")
						forward("moveFoodToDomesticResource", "moveFoodToDomesticResource(fridge)" ,"kb" ) 
						delay(1000) 
						println("CLEAR: Obiettivo RH")
						forward("goHome", "goHome(clear)" ,"navi" ) 
					}
					 transition(edgeName="t416",targetState="endClear",cond=whenDispatch("homeReached"))
				}	 
				state("endClear") { //this:State
					action { //it:State
						println("")
						println("CLEAR: State endClear")
						forward("endClear", "endClear(clear)" ,"rbr" ) 
						println("CLEAR terminata")
					}
				}	 
			}
		}
}
