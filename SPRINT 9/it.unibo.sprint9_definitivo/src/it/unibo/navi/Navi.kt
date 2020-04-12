/* Generated by AN DISI Unibo */ 
package it.unibo.navi

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Navi ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val mapname = "roomMap"
			val staticMapName = "staticRoomMap"
			var mapEmpty = true
			var Curmove = "" 
			var curmoveIsForward = false 
			
			//REAL ROBOT
			//var StepTime   = 1000 	 
			//var PauseTime  = 500 
			
			//VIRTUAL ROBOT
			var StepTime   = 330	//for virtual
			var PauseTime  = 300
			
			var PauseTimeL  = PauseTime.toLong()
			
			var GoalX = 0
			var GoalY = 0
			var ReachDestination = ""
			var ToSend = ""
			
			var Sender = ""
			var Tback = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("NAVI AVVIATO")
						solve("consult('moves.pl')","") //set resVar	
						solve("consult('domesticAppliancesPos.pl')","") //set resVar	
						itunibo.planner.plannerUtil.initAI(  )
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("in attesa di un comando")
					}
					 transition(edgeName="t00",targetState="planPathToAppliance",cond=whenDispatch("reachAppliance"))
					transition(edgeName="t01",targetState="planPathToTable",cond=whenDispatch("reachTable"))
					transition(edgeName="t02",targetState="returnHome",cond=whenDispatch("goHome"))
					transition(edgeName="t03",targetState="savePos",cond=whenDispatch("sendInfoPos"))
					transition(edgeName="t04",targetState="updateRobotPos",cond=whenDispatch("robotPosition"))
					transition(edgeName="t05",targetState="staticSetupRoom",cond=whenDispatch("staticStartTheSystem"))
				}	 
				state("staticSetupRoom") { //this:State
					action { //it:State
						println("")
						println("State staticSetupRoom")
						solve("consult('staticDomesticAppliancesPos.pl')","") //set resVar	
						itunibo.planner.moveUtils.loadRoomMap(myself ,staticMapName )
						mapEmpty = itunibo.planner.moveUtils.mapIsEmpty()
						if(!mapEmpty){ val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()
						forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
						itunibo.frontend.frontendSupport.updateFrontend(myself ,"systemStarted" )
						 }
					}
					 transition( edgeName="goto",targetState="warning", cond=doswitchGuarded({mapEmpty}) )
					transition( edgeName="goto",targetState="waitCmd", cond=doswitchGuarded({! mapEmpty}) )
				}	 
				state("warning") { //this:State
					action { //it:State
						println("========================================")
						println("WARNING: static map not found")
						println("========================================")
					}
				}	 
				state("handleEventStop") { //this:State
					action { //it:State
						println("")
						println("State handleEventStop")
					}
					 transition(edgeName="t56",targetState="handleEventReactivate",cond=whenEvent("reactivate"))
				}	 
				state("handleEventReactivate") { //this:State
					action { //it:State
						println("")
						println("State handleEventReactivate")
					}
					 transition(edgeName="t67",targetState="handleStepOk",cond=whenDispatch("stepOk"))
					transition(edgeName="t68",targetState="handleStepFail",cond=whenDispatch("stepFail"))
				}	 
				state("updateRobotPos") { //this:State
					action { //it:State
						println("")
						println("State updateRobotPos")
						println("Ricevuta posizione aggiornata del robot")
						if( checkMsgContent( Term.createTerm("robotPosition(X,Y,D)"), Term.createTerm("robotPosition(X,Y,D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.planner.moveUtils.loadRoomMap(myself ,mapname )
								var x = payloadArg(0).toString().toInt()
											var y = payloadArg(1).toString().toInt()
											var d = payloadArg(2).toString()
											itunibo.planner.plannerUtil.resetRobotPos(x, y, itunibo.planner.plannerUtil.getPosX(), itunibo.planner.plannerUtil.getPosY(), d )
								itunibo.planner.moveUtils.showCurrentRobotState(  )
								val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()
								forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("savePos") { //this:State
					action { //it:State
						println("")
						println("State savePos")
						if( checkMsgContent( Term.createTerm("sendInfoPos(NAME,X,Y)"), Term.createTerm("sendInfoPos(NAME,X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Posizione da salvare: ${payloadArg(0).toString()} ${payloadArg(1).toString()} ${payloadArg(2).toString()}")
								var salvaX = payloadArg(1).toInt()
											var salvaY = payloadArg(2).toInt()
											when( payloadArg(0).toString()) {
										  		"table" -> 
								solve("assert(table('${salvaX}','${salvaY}'))","") //set resVar	
								"fridge" ->
								solve("assert(fridge('${salvaX}','${salvaY}'))","") //set resVar	
								"pantry" ->
								solve("assert(pantry('${salvaX}','${salvaY}'))","") //set resVar	
								"dishWasher" ->
								solve("assert(dishWasher('${salvaX}','${salvaY}'))","") //set resVar	
								}
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("planPathToAppliance") { //this:State
					action { //it:State
						println("")
						println("State planPathToAppliance")
						if( checkMsgContent( Term.createTerm("reachAppliance(MIT,NAME)"), Term.createTerm("reachAppliance(MIT,NAME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								Sender = payloadArg(0).toString()
											ReachDestination = payloadArg(1).toString()
								println("Ricevuto messaggio per raggiungere il ${ReachDestination}")
								var destinazioneProlog = ReachDestination + "(X,Y)"
								println("${destinazioneProlog}")
								solve(destinazioneProlog,"")
								if(currentSolution.isSuccess()) { println("${ReachDestination}: ${getCurSol("X")}, ${getCurSol("Y")}")
								GoalX = Integer.parseInt(getCurSol("X").toString().replace("'", ""))
											 	GoalY =  Integer.parseInt(getCurSol("Y").toString().replace("'", ""))
											 	ToSend = "goal(${ReachDestination.toUpperCase()} (${GoalX}, ${GoalY}))"
								utils.writeLog( "GOAL: $ReachDestination | $GoalX $GoalY"  )
								itunibo.frontend.frontendSupport.updateGoalToFrontend(myself ,ToSend )
								forward("startGoal", "startGoal" ,"navi" ) 
								 }
						}
					}
					 transition(edgeName="t19",targetState="reachRequestedGoal",cond=whenDispatch("startGoal"))
				}	 
				state("returnHome") { //this:State
					action { //it:State
						println("")
						println("State returnHome")
						if( checkMsgContent( Term.createTerm("goHome(MIT)"), Term.createTerm("goHome(MIT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Ricevuto messaggio per tornare ad RH")
								GoalX = 0
										 	GoalY = 0
											ReachDestination = "RH"
											Sender = payloadArg(0).toString()
											ToSend = "goal(${ReachDestination.toUpperCase()} (${GoalX}, ${GoalY}))"
								utils.writeLog( "GOAL: $ReachDestination | $GoalX $GoalY"  )
								itunibo.frontend.frontendSupport.updateGoalToFrontend(myself ,ToSend )
								forward("startGoal", "startGoal(0,0)" ,"navi" ) 
						}
					}
					 transition(edgeName="t210",targetState="reachRequestedGoal",cond=whenDispatch("startGoal"))
				}	 
				state("planPathToTable") { //this:State
					action { //it:State
						println("")
						println("State planPathToTable")
						if( checkMsgContent( Term.createTerm("reachTable(MIT)"), Term.createTerm("reachTable(MIT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								Sender = payloadArg(0).toString()
						}
						var bestNum = 100
						solve("posTable(L)","") //set resVar	
						if(currentSolution.isSuccess()) { var listaPosizioniTavolo = getCurSol("L").toString().replace("[", "")
									listaPosizioniTavolo = listaPosizioniTavolo.toString().replace("]", "")
									listaPosizioniTavolo = listaPosizioniTavolo.replace(Regex("""[$,.''(]"""), "").dropLast(1)
									var listaPos = listaPosizioniTavolo.split(')')
									for(i in listaPos){
										var X = Integer.parseInt(i[0].toString())
								 		var Y = Integer.parseInt(i[1].toString())
						itunibo.planner.plannerUtil.setGoal( X, Y  )
						itunibo.planner.plannerUtil.doPlan(  )
						var num = itunibo.planner.plannerUtil.getActions().lastIndex
										if(num < bestNum){
											bestNum = num
											GoalX = X
											GoalY = Y
										}
									}
						 }
						itunibo.planner.plannerUtil.resetGoal( GoalX, GoalY  )
						ReachDestination = "table"
								  ToSend = "goal(${ReachDestination.toUpperCase()} (${GoalX}, ${GoalY}))"
						utils.writeLog( "GOAL: $ReachDestination | $GoalX $GoalY"  )
						itunibo.frontend.frontendSupport.updateGoalToFrontend(myself ,ToSend )
						forward("startGoal", "startGoal" ,"navi" ) 
					}
					 transition(edgeName="t311",targetState="reachRequestedGoal",cond=whenDispatch("startGoal"))
				}	 
				state("reachRequestedGoal") { //this:State
					action { //it:State
						println("")
						println("State reachRequestedGoal")
						val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()
						forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
					}
					 transition( edgeName="goto",targetState="makeThePlan", cond=doswitch() )
				}	 
				state("makeThePlan") { //this:State
					action { //it:State
						println("")
						println("State makeThePlan")
						itunibo.planner.plannerUtil.setGoal( GoalX, GoalY  )
						itunibo.planner.moveUtils.doPlan(myself)
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
				state("executePlannedActions") { //this:State
					action { //it:State
						println("")
						println("State executePlannedActions")
						solve("retract(move(M))","") //set resVar	
						if(currentSolution.isSuccess()) { Curmove = getCurSol("M").toString() 
						              curmoveIsForward=(Curmove == "w")
						 }
						else
						{ Curmove = ""; curmoveIsForward=false
						 }
					}
					 transition( edgeName="goto",targetState="checkAndDoAction", cond=doswitchGuarded({(Curmove.length>0) }) )
					transition( edgeName="goto",targetState="goalOk", cond=doswitchGuarded({! (Curmove.length>0) }) )
				}	 
				state("goalOk") { //this:State
					action { //it:State
						println("")
						println("State goalOk")
						utils.controlDirection(myself ,ReachDestination, GoalX, GoalY )
						itunibo.planner.moveUtils.showCurrentRobotState(  )
						val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()
						forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
						itunibo.planner.moveUtils.showCurrentRobotState(  )
						itunibo.planner.plannerUtil.saveMap( mapname  )
						delay(500) 
						when(ReachDestination) {
							  		"table" -> forward("tableReached", "tableReached", "$Sender")
							    	"RH" -> forward("homeReached", "homeReached(navi)", "$Sender")
								    "pantry" -> forward("pantryReached", "pantryReached", "$Sender")
								    "fridge" -> forward("fridgeReached", "fridgeReached", "$Sender")
									"dishWasher" -> forward("dishWasherReached", "dishWasherReached", "$Sender")
								}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("checkAndDoAction") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="doForwardMove", cond=doswitchGuarded({curmoveIsForward}) )
					transition( edgeName="goto",targetState="doRotation", cond=doswitchGuarded({! curmoveIsForward}) )
				}	 
				state("doRotation") { //this:State
					action { //it:State
						println("")
						println("State doRotation")
						itunibo.planner.moveUtils.rotate(myself ,Curmove, PauseTime )
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
				state("doForwardMove") { //this:State
					action { //it:State
						println("")
						println("State doForwardMove")
						delay(PauseTimeL)
						itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
					}
					 transition(edgeName="t412",targetState="handleEventStop",cond=whenEvent("stop"))
					transition(edgeName="t413",targetState="handleStepOk",cond=whenDispatch("stepOk"))
					transition(edgeName="t414",targetState="handleStepFail",cond=whenDispatch("stepFail"))
				}	 
				state("handleStepOk") { //this:State
					action { //it:State
						println("")
						println("State handleStepOk")
						itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
				state("handleStepFail") { //this:State
					action { //it:State
						println("")
						println("State handleStepFail")
						if( checkMsgContent( Term.createTerm("stepFail(R,T)"), Term.createTerm("stepFail(Obs,Time)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								Tback=payloadArg(1).toString().toInt() / 4
						}
						itunibo.planner.moveUtils.backToCompensate(myself ,Tback, PauseTime )
						itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
			}
		}
}
