/* Generated by AN DISI Unibo */ 
package it.unibo.roomperimeterexplorer

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Roomperimeterexplorer ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
			//variabile per indicare il nome del file in cui salvare la mappa
			val mapname = "roomMap"
		
			//varibile per indicare il tempo che impiega il robot per tornare indietro di un passo
			var Tback = 0
		
			//variabile per indicare il numero di muri scansionati
			var NumWallsFound = 0
			
			//VIRTUAL ROBOT
			//varibile per indicare il tempo impiegato per effettuare un passo in avanti
			var StepTime = 330
			//variabile per indicare il tempo di ritardo	 
			var PauseTime = 300
			var PauseTimeL  = PauseTime.toLong()
		
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.moveUtils.showCurrentRobotState(  )
					}
					 transition( edgeName="goto",targetState="waitComand", cond=doswitch() )
				}	 
				state("waitComand") { //this:State
					action { //it:State
						itunibo.robot.utils.backupLastLog(  )
						
								var log = "==================== roomperimeterexplorer ====================\n[backToCompensate - ] currentPosition | move | result\n==============================================================="
						itunibo.robot.utils.writeLog( log  )
						println("in attesa di un comando")
					}
					 transition(edgeName="t10",targetState="detectWall",cond=whenDispatch("scanningPerimeter"))
				}	 
				state("detectWall") { //this:State
					action { //it:State
						
								NumWallsFound++
						itunibo.planner.plannerUtil.showMap(  )
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitchGuarded({(NumWallsFound<5)}) )
					transition( edgeName="goto",targetState="wallFound", cond=doswitchGuarded({! (NumWallsFound<5)}) )
				}	 
				state("doAheadMove") { //this:State
					action { //it:State
						itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
					}
					 transition(edgeName="t01",targetState="stepDone",cond=whenDispatch("stepOk"))
					transition(edgeName="t02",targetState="stepFailed",cond=whenDispatch("stepFail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
						delay(PauseTimeL)
						println("MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
					}
					 transition( edgeName="goto",targetState="doAheadMove", cond=doswitch() )
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						println("FOUND WALL")
						val MapStr = itunibo.planner.plannerUtil.getMapOneLine()  
						forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
						if( checkMsgContent( Term.createTerm("stepFail(R,T)"), Term.createTerm("stepFail(Obs,Time)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Tback=payloadArg(1).toString().toInt() / 4 
								println("stepFailed ${payloadArg(1).toString()}")
						}
						itunibo.planner.moveUtils.backToCompensate(myself ,Tback, PauseTime )
						itunibo.planner.plannerUtil.wallFound(  )
						itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
					}
					 transition( edgeName="goto",targetState="detectWall", cond=doswitch() )
				}	 
				state("wallFound") { //this:State
					action { //it:State
						itunibo.planner.plannerUtil.saveMap( mapname  )
						itunibo.planner.moveUtils.saveMap(myself ,mapname )
						println("FINAL MAP")
						itunibo.planner.moveUtils.showCurrentRobotState(  )
						 var mapMaxX = itunibo.planner.moveUtils.getMapDimX() - 2
								var mapMaxY = itunibo.planner.moveUtils.getMapDimY() - 2 
						solve("assert(fridge('${mapMaxX}',0))","") //set resVar	
						solve("assert(dishWasher('${mapMaxX}','${mapMaxY}'))","") //set resVar	
						solve("assert(pantry(0,'${mapMaxY}'))","") //set resVar	
						solve("consult('domesticAppliancesPos.pl')","") //set resVar	
						if(currentSolution.isSuccess()) { solve("fridge(X,Y)","") //set resVar	
						if(currentSolution.isSuccess()) { delay(100) 
						println("fridge at: ${getCurSol("X").toString()}, ${getCurSol("Y").toString()}")
						forward("sendInfoPos", "sendInfoPos(fridge,${getCurSol("X")},${getCurSol("Y")})" ,"navi" ) 
						 }
						solve("dishWasher(X,Y)","") //set resVar	
						if(currentSolution.isSuccess()) { delay(100) 
						println("dishWasher at: ${getCurSol("X").toString()}, ${getCurSol("Y").toString()}")
						forward("sendInfoPos", "sendInfoPos(dishWasher,${getCurSol("X")},${getCurSol("Y")})" ,"navi" ) 
						 }
						solve("pantry(X,Y)","") //set resVar	
						if(currentSolution.isSuccess()) { delay(100) 
						println("pantry at: ${getCurSol("X").toString()}, ${getCurSol("Y").toString()}")
						forward("sendInfoPos", "sendInfoPos(pantry,${getCurSol("X")},${getCurSol("Y")})" ,"navi" ) 
						 }
						 }
						forward("endPerimeter", "endPerimeter" ,"master1" ) 
					}
				}	 
			}
		}
}
