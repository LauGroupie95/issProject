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
						solve("consult('roomState.pl')","") //set resVar	
					}
					 transition(edgeName="t03",targetState="startprepare",cond=whenDispatch("startPrepare"))
				}	 
				state("startprepare") { //this:State
					action { //it:State
						println("INIZIO PREPARE")
						println("Obiettivo PANTRY")
						forward("reachAppliance", "reachAppliance(pantry)" ,"navi" ) 
					}
					 transition(edgeName="t14",targetState="inPantry",cond=whenDispatch("pantryReached"))
				}	 
				state("inPantry") { //this:State
					action { //it:State
						println("Sono nel PANTRY")
						delay(1000) 
						solve("iteminpantry(X)","") //set resVar	
						if(currentSolution.isSuccess()) { solve("exposePantry(L)","") //set resVar	
						println("Lista oggetti nel pantry: ${getCurSol("L").toString()}")
						solve("retract(iteminpantry(dish))","") //set resVar	
						solve("retract(iteminpantry(glasses))","") //set resVar	
						solve("retract(iteminpantry(forks))","") //set resVar	
						solve("assert(itemonrobot(dish))","") //set resVar	
						solve("assert(itemonrobot(forks))","") //set resVar	
						solve("assert(itemonrobot(glasses))","") //set resVar	
						solve("exposeObjectOnRobot(L)","") //set resVar	
						println("Lista oggetti sul robot: ${getCurSol("L").toString()}")
						 }
						println("Obiettivo TABLE")
						delay(5000) 
						forward("reachTable", "reachTable" ,"navi" ) 
					}
					 transition(edgeName="t25",targetState="intable",cond=whenDispatch("tableReached"))
				}	 
				state("intable") { //this:State
					action { //it:State
						println("Sono nel TABLE")
						delay(1000) 
						solve("exposeObjectOnRobot(L)","") //set resVar	
						if(currentSolution.isSuccess()) { var objOnRobot = getCurSol("L").toString()
									objOnRobot = objOnRobot.replace("[", "").replace("]", "")
						println("Oggetti presenti sul robot: ${objOnRobot}")
						for(obj in objOnRobot.split(",")){
						println("inserisco sul tavolo: ${obj}")
						solve("assert(itemontable('${obj}'))","") //set resVar	
						solve("retract(itemonrobot('${obj}'))","") //set resVar	
						}
						 }
						solve("exposeFoodOnRobot(L)","") //set resVar	
						if(currentSolution.isSuccess()) { var foodOnRobot = getCurSol("L").toString()
									foodOnRobot = foodOnRobot.replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "")
						println("Cibo presente sul ROBOT: ${foodOnRobot}")
						for(food in foodOnRobot.split("((")){
										if(food != ""){
											var f = food.split(",")
											var nome = f.get(0).toString()
											var codice = f.get(1).toString()
											var quantita = Integer.parseInt(f.get(2).toString())
						println("inserisco sul tavolo: ${nome} ${codice} ${quantita}")
						solve("assert(itemontable('${nome}','${codice}','${quantita}'))","") //set resVar	
						solve("retract(itemonrobot('${nome}','${codice}',Z))","") //set resVar	
						 }
						}
						}
						if(inTable == 0){
						delay(5000) 
						forward("reachAppliance", "reachAppliance(fridge)" ,"navi" ) 
						println("Obiettivo FRIDGE")
						inTable = inTable + 1
								}else{
						println("Obiettivo RH")
						forward("goHome", "goHome(prepare)" ,"navi" ) 
						}
					}
					 transition(edgeName="t36",targetState="inFridge",cond=whenDispatch("fridgeReached"))
					transition(edgeName="t37",targetState="endPrepare",cond=whenDispatch("homeReached"))
				}	 
				state("inFridge") { //this:State
					action { //it:State
						println("Sono nel FRIDGE")
						solve("exposeFridge(L)","") //set resVar	
						println("Oggetti inizialmente nel frigo: ${getCurSol("L").toString()}")
						delay(5000) 
						solve("retract(iteminfridge(meet,c001,5))","") //set resVar	
						solve("retract(iteminfridge(cola,c002,5))","") //set resVar	
						solve("retract(iteminfridge(pizza,c004,10))","") //set resVar	
						solve("assert(itemonrobot(meet,c001,5))","") //set resVar	
						solve("assert(itemonrobot(cola,c002,5))","") //set resVar	
						solve("assert(itemonrobot(pizza,c004,10))","") //set resVar	
						solve("exposeFridge(L)","") //set resVar	
						println("Oggetti rimasti nel frigo: ${getCurSol("L").toString()}")
						solve("exposeFoodOnRobot(L)","") //set resVar	
						println("Cibo sul robot: ${getCurSol("L").toString()}")
						println("Obiettivo TABLE")
						forward("reachTable", "reachTable" ,"navi" ) 
					}
					 transition(edgeName="t48",targetState="intable",cond=whenDispatch("tableReached"))
				}	 
				state("endPrepare") { //this:State
					action { //it:State
						println("PREPARE terminata")
						println("")
						println("STATO FINALE DELLA STANZA")
						solve("expose(OT,FT,F,P,D,OR,FR)","") //set resVar	
						if(currentSolution.isSuccess()) { println("cibo sul tavolo: ${getCurSol("FT").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("oggetti sul tavolo: ${getCurSol("OT").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("cibo nel frigo: ${getCurSol("F").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("oggetti rimasti nel pantry: ${getCurSol("P").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("cibo rimasto sul robot: ${getCurSol("FR").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("oggetti rimasti sul robot: ${getCurSol("OR").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("oggetti rimasti nel dishwasher: ${getCurSol("D").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						 }
					}
				}	 
			}
		}
}