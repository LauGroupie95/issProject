/* Generated by AN DISI Unibo */ 
package it.unibo.prepare

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Prepare ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var inTable = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("PREPARE AVVIATA")
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("in attesa di un comando")
					}
					 transition(edgeName="t00",targetState="doPrepare",cond=whenDispatch("prepare"))
				}	 
				state("doPrepare") { //this:State
					action { //it:State
						println("")
						println("State doPrepare")
						println("Obiettivo PANTRY")
						forward("reachAppliance", "reachAppliance(prepare,pantry)" ,"navi" ) 
					}
					 transition(edgeName="t11",targetState="inPantry",cond=whenDispatch("pantryReached"))
				}	 
				state("inPantry") { //this:State
					action { //it:State
						println("")
						println("State inPantry")
						delay(500) 
						forward("moveObjOnRobot", "moveObjOnRobot(pantry)" ,"kb" ) 
						println("Obiettivo TABLE")
						forward("reachTable", "reachTable(prepare)" ,"navi" ) 
					}
					 transition(edgeName="t22",targetState="inTable",cond=whenDispatch("tableReached"))
				}	 
				state("inTable") { //this:State
					action { //it:State
						println("")
						println("State inTable")
						delay(1000) 
						if(inTable == 0){
						forward("moveObjToDomesticResource", "moveObjToDomesticResource(table)" ,"kb" ) 
						delay(2000) 
						println("Obiettivo FRIDGE")
						forward("reachAppliance", "reachAppliance(prepare,fridge)" ,"navi" ) 
						inTable = inTable + 1
								}else{
						forward("moveFoodToDomesticResource", "moveFoodToDomesticResource(table)" ,"kb" ) 
						delay(2000) 
						println("Obiettivo RH")
						forward("goHome", "goHome(prepare)" ,"navi" ) 
						}
					}
					 transition(edgeName="t33",targetState="inFridge",cond=whenDispatch("fridgeReached"))
					transition(edgeName="t34",targetState="endPrepare",cond=whenDispatch("homeReached"))
				}	 
				state("inFridge") { //this:State
					action { //it:State
						println("")
						println("State inFridge")
						delay(500) 
						val FoodCodeList = "c001_c000_c000_c002_c004_c003_c003_c005_c006_c002_c007_c010_c010_c010_c006_c006"
						println("FoodCodeList = ${FoodCodeList}")
						forward("moveSpecificFoodOnRobot", "moveSpecificFoodOnRobot($FoodCodeList)" ,"fridge" ) 
						delay(1000) 
						println("Obiettivo TABLE")
						forward("reachTable", "reachTable(prepare)" ,"navi" ) 
					}
					 transition(edgeName="t45",targetState="inTable",cond=whenDispatch("tableReached"))
				}	 
				state("endPrepare") { //this:State
					action { //it:State
						println("")
						println("State endPrepare")
						println("")
						forward("endPrepare", "endPrepare(prepare)" ,"rbr" ) 
						println("PREPARE terminata")
					}
				}	 
			}
		}
}