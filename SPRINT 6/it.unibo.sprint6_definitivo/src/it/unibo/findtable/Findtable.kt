/* Generated by AN DISI Unibo */ 
package it.unibo.findtable

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Findtable ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//flag per indicare se la mappa esiste o meno
			var mapEmpty = true
		
			//nome del file in cui � salvata/salvare la mappa
			val mapname = "roomMap"
		
			//VIRTUAL ROBOT
			//tempo impiegato per un passo in avanti
			var StepTime = 330
			//tempo di ritardo	 
			var PauseTime = 300
			var PauseTimeL  = PauseTime.toLong()
		
			//numero righe ispezionate
			var Row = 0
			//numero passi, viene resettata ad ogni cambio riga
			var NumSteps = 0 
			//numero di angoli del tavolo scansionati
			var StepAroundTable = 0
			//tempo che il virtual robot impiega per un passo indietro
			var Tback = 0
			//posizione in cui robot trova tavolo la prima volta
			var InitialDirection = ""
			var EndScanningTableFlag = false
			var mapMaxX = 0
			var mapMaxY = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("FINDTABLE AVVIATO")
						solve("consult('moves.pl')","") //set resVar	
					}
					 transition(edgeName="t03",targetState="loadmap",cond=whenDispatch("scanningTable"))
				}	 
				state("loadmap") { //this:State
					action { //it:State
						solve("consult('domesticAppliancesPos.pl')","") //set resVar	
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.moveUtils.loadRoomMap(myself ,mapname )
						mapEmpty = itunibo.planner.moveUtils.mapIsEmpty()
						if(!mapEmpty){ val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()
									mapMaxX = itunibo.planner.moveUtils.getMapDimX()
									mapMaxY = itunibo.planner.moveUtils.getMapDimY()
						println("mapMaxX = ${mapMaxX}, mapMaxY = ${mapMaxY}")
						forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
						 }
					}
					 transition( edgeName="goto",targetState="warning", cond=doswitchGuarded({mapEmpty}) )
					transition( edgeName="goto",targetState="init", cond=doswitchGuarded({! mapEmpty}) )
				}	 
				state("warning") { //this:State
					action { //it:State
						println("========================================")
						println("WARNING: map not found")
						println("========================================")
					}
				}	 
				state("init") { //this:State
					action { //it:State
						println("")
						println("#######################################")
						println("")
						println("State init")
						println("INIT - MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						delay(PauseTimeL)
						itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
						delay(PauseTimeL)
						itunibo.planner.moveUtils.moveAhead(myself ,StepTime, PauseTime )
						delay(PauseTimeL)
						println("AFTER INIT - MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						println("Setup completato")
						itunibo.planner.plannerUtil.showMap(  )
						println("")
						forward("startStep", "startStep" ,"findtable" ) 
						delay(PauseTimeL)
					}
					 transition(edgeName="t14",targetState="doStep",cond=whenDispatch("startStep"))
				}	 
				state("doStep") { //this:State
					action { //it:State
						println("")
						println("State doStep")
						itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
						delay(PauseTimeL)
					}
					 transition(edgeName="t25",targetState="stepDone",cond=whenDispatch("stepOk"))
					transition(edgeName="t26",targetState="tableFound",cond=whenDispatch("stepFail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						println("")
						println("State stepDone")
						itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
						delay(PauseTimeL)
						itunibo.planner.plannerUtil.showMap(  )
						NumSteps++
						println("MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						println("stepDone: NumSteps= ${NumSteps}, Row= ${Row}")
						if((NumSteps==mapMaxX-4)){ forward("endRow", "endRow" ,"findtable" ) 
						 }
						else
						 { forward("continueStep", "continueStep" ,"findtable" ) 
						  }
					}
					 transition(edgeName="t37",targetState="rotate",cond=whenDispatch("endRow"))
					transition(edgeName="t38",targetState="doStep",cond=whenDispatch("continueStep"))
					transition(edgeName="t39",targetState="tableFound",cond=whenDispatch("stepFail"))
				}	 
				state("rotate") { //this:State
					action { //it:State
						println("")
						println("State rotate")
						NumSteps = 0
						println("Rotate - CurrentRow = ${Row}")
						if((Row!=mapMaxY-2)){ if((Row%2!=0)){ itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
						itunibo.planner.moveUtils.setPosition(myself)
						delay(PauseTimeL)
						itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
						delay(PauseTimeL)
						itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
						Row += 1
						itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
						itunibo.planner.moveUtils.setPosition(myself)
						delay(PauseTimeL)
						 }
						else
						 { itunibo.planner.moveUtils.rotateRight(myself ,PauseTime )
						 itunibo.planner.moveUtils.setPosition(myself)
						 delay(PauseTimeL)
						 itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
						 delay(PauseTimeL)
						 itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
						 Row += 1
						 itunibo.planner.moveUtils.rotateRight(myself ,PauseTime )
						 itunibo.planner.moveUtils.setPosition(myself)
						 delay(PauseTimeL)
						  }
						println("After Rotation - Row = ${Row}")
						itunibo.planner.plannerUtil.showMap(  )
						println("MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						 }
						if((Row==mapMaxY-2)){ println("Vai a casa")
						 }
					}
					 transition( edgeName="goto",targetState="doStep", cond=doswitchGuarded({(Row!=mapMaxY-2)}) )
					transition( edgeName="goto",targetState="goHome", cond=doswitchGuarded({! (Row!=mapMaxY-2)}) )
				}	 
				state("tableFound") { //this:State
					action { //it:State
						println("###############################")
						println("")
						println("State tableFound - trovato il tavolo")
						if(InitialDirection==""){ InitialDirection = itunibo.planner.moveUtils.getDirection(myself)
						 }
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("stepFail(R,T)"), Term.createTerm("stepFail(Obs,Time)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								Tback=payloadArg(1).toString().toInt() / 2
						}
						itunibo.planner.moveUtils.backToCompensate(myself ,Tback, PauseTime )
						itunibo.planner.moveUtils.setObstacleOnCurrentDirection(myself)
						if(StepAroundTable == 8){ println("State exploreEdge - Fine")
						EndScanningTableFlag = true
						 }
						else
						 { solve("table('${itunibo.planner.plannerUtil.getPosX()}','${itunibo.planner.plannerUtil.getPosY()}')","") //set resVar	
						 if(currentSolution.isSuccess()) {  }
						 else
						 { var PositionX = itunibo.planner.plannerUtil.getPosX()
						 				var PositionY = itunibo.planner.plannerUtil.getPosY()
						 solve("assert(table('${PositionX}','${PositionY}'))","") //set resVar	
						 solve("lastpostableinsert(X)","") //set resVar	
						 if(currentSolution.isSuccess()) { var pos = getCurSol("X").toString().replace("'", "")	//('3','2')
						 					pos = pos.toString().replace("(", "")
						 					pos = pos.toString().replace(")", "")
						 					pos = pos.toString().replace(",", "")//32
						 					var posX = pos.get(0)//3
						 					var posY = pos.get(1)//2
						 println("Invio la nuova posizione trovata del tavolo: $posX $posY")
						 forward("sendInfoPos", "sendInfoPos(table,$posX,$posY)", "navi")
						  }
						  }
						 if(InitialDirection=="downDir"){ itunibo.planner.moveUtils.rotateRight(myself ,PauseTime )
						  }
						 if(InitialDirection=="rightDir"){ itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
						  }
						 itunibo.planner.plannerUtil.showMap(  )
						 println("tableFound - MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						  }
					}
					 transition( edgeName="goto",targetState="prepareToExploreEdge", cond=doswitchGuarded({(StepAroundTable % 2 == 0)}) )
					transition( edgeName="goto",targetState="exploreEdge", cond=doswitchGuarded({! (StepAroundTable % 2 == 0)}) )
				}	 
				state("prepareToExploreEdge") { //this:State
					action { //it:State
						if(EndScanningTableFlag == true){  }
						else
						 { println("prepareToExploreEdge - passo in avanti")
						 itunibo.planner.moveUtils.moveAhead(myself ,StepTime, PauseTime )
						 println("prepareToExploreEdge - MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						 if(InitialDirection=="downDir"){ println("prepareToExploreEdge - prepareToExploreEdge - mi giro a sinistra")
						 itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
						  }
						 if(InitialDirection=="rightDir"){ println("prepareToExploreEdge - prepareToExploreEdge - mi giro a sinistra")
						 itunibo.planner.moveUtils.rotateRight(myself ,PauseTime )
						  }
						 println("MyRobotPos=(${itunibo.planner.plannerUtil.getPosX()}, ${itunibo.planner.plannerUtil.getPosY()}, ${itunibo.planner.moveUtils.getDirection(myself)})")
						 itunibo.planner.moveUtils.showCurrentRobotState(  )
						  }
					}
					 transition( edgeName="goto",targetState="exploreEdge", cond=doswitch() )
				}	 
				state("exploreEdge") { //this:State
					action { //it:State
						if(EndScanningTableFlag == true){ forward("endScanningTable", "endScanningTable" ,"findtable" ) 
						 }
						else
						 { StepAroundTable++
						 println("exploreEdge - StepAroundTable=${StepAroundTable}")
						 itunibo.planner.moveUtils.moveAhead(myself ,StepTime, PauseTime )
						 if(InitialDirection=="downDir"){ itunibo.planner.moveUtils.rotateLeft(myself ,PauseTime )
						  }
						 if(InitialDirection=="rightDir"){ itunibo.planner.moveUtils.rotateRight(myself ,PauseTime )
						  }
						 itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
						 itunibo.planner.plannerUtil.showMap(  )
						  }
					}
					 transition(edgeName="t410",targetState="tableFound",cond=whenDispatch("stepFail"))
					transition(edgeName="t411",targetState="goHome",cond=whenDispatch("endScanningTable"))
				}	 
				state("goHome") { //this:State
					action { //it:State
						println("State goHome")
						itunibo.planner.plannerUtil.saveMap( mapname  )
						itunibo.planner.moveUtils.saveMap(myself ,mapname )
						solve("retractall(move(_))","") //set resVar	
						solve("posTable(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("table near: ${getCurSol("L")}")
						 }
						var PositionX = itunibo.planner.plannerUtil.getPosX()
								var PositionY = itunibo.planner.plannerUtil.getPosY()
						solve("assert(robot('${PositionX}','${PositionY}','${itunibo.planner.moveUtils.getDirection(myself)}'))","") //set resVar	
						solve("robot(X,Y,D)","") //set resVar	
						if(currentSolution.isSuccess()) { println("Invio a navi posizione corrente robot: ${getCurSol("X")} ${getCurSol("Y")} ${getCurSol("D")}")
						emit("robotPosition", "robotPosition(${getCurSol("X")},${getCurSol("Y")},${getCurSol("D")})" ) 
						delay(1000) 
						 }
						println("Scanning table END")
						forward("goHome", "goHome(rbr)" ,"navi" ) 
					}
				}	 
			}
		}
}
