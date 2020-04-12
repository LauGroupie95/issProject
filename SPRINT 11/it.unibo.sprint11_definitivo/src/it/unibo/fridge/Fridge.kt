/* Generated by AN DISI Unibo */ 
package it.unibo.fridge

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fridge ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var KbConsulted = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("FRIDGE AVVIATO")
						solve("consult('fridgekb.pl')","") //set resVar	
						itunibo.coap.fridge.fridgeCoap.create(myself ,"serverfridge" )
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("")
						println("State wait")
						println("in attesa di un comando")
					}
					 transition(edgeName="t00",targetState="takeFromFridge",cond=whenDispatch("moveSpecificFoodOnRobot"))
					transition(edgeName="t01",targetState="putInFridge",cond=whenDispatch("moveFoodToFridge"))
					transition(edgeName="t02",targetState="coapAnswer",cond=whenDispatch("handleAnswerReply"))
					transition(edgeName="t03",targetState="writeLog",cond=whenDispatch("log"))
				}	 
				state("coapAnswer") { //this:State
					action { //it:State
						println("")
						println("State coapAnswer")
						if( checkMsgContent( Term.createTerm("handleAnswerReply(ANSWER)"), Term.createTerm("handleAnswerReply(ANSWER)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var Answer = payloadArg(0).toString()
								println("answer = ${Answer}")
								forward("answerFromFridgeCoap", "answerFromFridgeCoap($Answer)" ,"addfood" ) 
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("takeFromFridge") { //this:State
					action { //it:State
						println("")
						println("State takeFromFridge")
						if( checkMsgContent( Term.createTerm("moveSpecificFoodOnRobot(FCL)"), Term.createTerm("moveSpecificFoodOnRobot(FCL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var FoodCodeList = payloadArg(0).toString()
								println("onMsg lista dei codici ricevuta: ${FoodCodeList}")
								for(foodCode in FoodCodeList.split("_")){
								println("ricerca per codice ${foodCode}")
								solve("iteminfridge(NAME,'${foodCode}',QTA)","") //set resVar	
								if(currentSolution.isSuccess()) { println("risolta ricerca codice-cibo")
								var name = getCurSol("NAME").toString()
													var qty = Integer.parseInt(getCurSol("QTA").toString())-1
													val ToSend = "${name}_${foodCode}" 
								solve("retract(iteminfridge('${name}','${foodCode}',QTA))","") //set resVar	
								utils.writeLogKb( "-.FD.${name},${foodCode},${qty}"  )
								if(qty != 0){ solve("assert(iteminfridge('${name}','${foodCode}',${qty}))","")
								utils.writeLogKb( "+.FD.${name},${foodCode},${qty}"  )
								 }
								forward("moveFoodOnRobot", "moveFoodOnRobot(fridge,$ToSend)" ,"kb" ) 
								 }
								}
						}
						itunibo.frontend.frontendSupport.updateFrontend(myself ,"fridge()" )
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("putInFridge") { //this:State
					action { //it:State
						println("")
						println("State putInFridge")
						if( checkMsgContent( Term.createTerm("moveFoodToFridge(NAMECODEQTA)"), Term.createTerm("moveFoodToFridge(NAMECODEQTA)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var nameFood = payloadArg(0).toString().split("_").get(0).toString()
											var codeFood = payloadArg(0).toString().split("_").get(1).toString()				
											var qtyRobot = payloadArg(0).toString().split("_").get(2).toString()
								println("${nameFood}, ${codeFood}, ${qtyRobot}")
								solve("iteminfridge('${nameFood}','${codeFood}',QTA)","") //set resVar	
								if(currentSolution.isSuccess()) { var qtyFridge = Integer.parseInt(getCurSol("QTA").toString())
												var qtySum = Integer.parseInt(qtyRobot)+qtyFridge 
								solve("retract(iteminfridge(NAME,'${codeFood}',QTA))","") //set resVar	
								utils.writeLogKb( "-.FD.${nameFood},${codeFood},${qtyFridge}"  )
								solve("assert(iteminfridge('${nameFood}','${codeFood}','${qtySum}'))","") //set resVar	
								utils.writeLogKb( "+.FD.${nameFood},${codeFood},${qtySum}"  )
								 }
								else
								{ solve("assert(iteminfridge('${nameFood}','${codeFood}','${qtyRobot}'))","") //set resVar	
								utils.writeLogKb( "+.FD.${nameFood},${codeFood},${qtyRobot}"  )
								 }
						}
						itunibo.frontend.frontendSupport.updateFrontend(myself ,"fridge()" )
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("writeLog") { //this:State
					action { //it:State
						println("")
						println("State writeLog")
						solve("exposeFridge(F)","") //set resVar	
						if(currentSolution.isSuccess()) { val lista = getCurSol("F").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")
						utils.writeLogKb( "FD: $lista"  )
						 }
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
