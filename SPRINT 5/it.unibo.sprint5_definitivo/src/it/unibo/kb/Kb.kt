/* Generated by AN DISI Unibo */ 
package it.unibo.kb

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Kb ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var DomesticResource = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("KB AVVIATA")
						solve("consult('roomState.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						println("in attesa di un comando")
					}
					 transition(edgeName="t00",targetState="objFromRobot",cond=whenDispatch("moveObjToDomesticResource"))
					transition(edgeName="t01",targetState="foodFromRobot",cond=whenDispatch("moveFoodToDomesticResource"))
					transition(edgeName="t02",targetState="objToRobot",cond=whenDispatch("moveObjOnRobot"))
					transition(edgeName="t03",targetState="foodToRobot",cond=whenDispatch("moveFoodOnRobot"))
					transition(edgeName="t04",targetState="doExpose",cond=whenDispatch("expose"))
				}	 
				state("objFromRobot") { //this:State
					action { //it:State
						println("")
						println("State objFromRobot")
						if( checkMsgContent( Term.createTerm("moveObjToDomesticResource(DR)"), Term.createTerm("moveObjToDomesticResource(DR)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								DomesticResource = payloadArg(0).toString()
						}
						solve("exposeObjectOnRobot(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("exposeObjectOnRobot = ${getCurSol("L").toString()}")
						var objOnRobot = getCurSol("L").toString().replace("[", "").replace("]", "")
						println("Oggetti presenti sul robot: ${objOnRobot}")
						for(obj in objOnRobot.split(",")){
						println("inserisco sul ${DomesticResource}: ${obj}")
						when(DomesticResource) {
									  		"table" -> {
						solve("assert(itemontable('${obj}'))","") //set resVar	
						solve("retract(itemonrobot('${obj}'))","") //set resVar	
						}
											"pantry" -> {
						solve("assert(iteminpantry('${obj}'))","") //set resVar	
						solve("retract(itemonrobot('${obj}'))","") //set resVar	
						}
											"dishwasher" -> {
						solve("assert(itemindishwasher('${obj}'))","") //set resVar	
						solve("retract(itemonrobot('${obj}'))","") //set resVar	
						}
										}
									}
						 }
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("foodFromRobot") { //this:State
					action { //it:State
						println("")
						println("State foodFromRobot")
						if( checkMsgContent( Term.createTerm("moveFoodToDomesticResource(DR)"), Term.createTerm("moveFoodToDomesticResource(DR)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								DomesticResource = payloadArg(0).toString()
						}
						solve("exposeFoodOnRobot(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("exposeFoodOnRobot = ${getCurSol("L").toString()}")
						var foodOnRobot = getCurSol("L").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "")
						println("foodOnRobot = ${foodOnRobot}")
						for(food in foodOnRobot.split("((")){
										if(food != ""){
											var f = food.split(",")
											var name = f.get(0).toString()
											var code = f.get(1).toString()
											var qtyRobot = Integer.parseInt(f.get(2).toString())
						println("inserisco sul ${DomesticResource}: ${name} ${code} ${qtyRobot}")
						when(DomesticResource) {
										  		"table" -> {
						solve("itemontable(NAME,'${code}',QTA)","") //set resVar	
						if(currentSolution.isSuccess()) { var qtyTable = Integer.parseInt(getCurSol("QTA").toString())
						solve("retract(itemontable(NAME,'${code}',QTA))","") //set resVar	
						var qtySum = qtyRobot+qtyTable
						solve("assert(itemonrobot('${name}','${code}',${qtySum}))","")
						 }
						else
						{ solve("assert(itemontable('${name}','${code}',${qtyRobot}))","")
						 }
						solve("retract(itemonrobot('${name}','${code}',QTA))","") //set resVar	
						}
												"fridge" -> {
						 val ToSend = "${name}_${code}_${qtyRobot}" 
						forward("moveFoodToFridge", "moveFoodToFridge($ToSend)" ,"fridge" ) 
						solve("retract(itemonrobot('${name}','${code}',QTA))","") //set resVar	
						}
											}
										}
									}
						 }
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("objToRobot") { //this:State
					action { //it:State
						println("")
						println("State objToRobot")
						if( checkMsgContent( Term.createTerm("moveObjOnRobot(DR)"), Term.createTerm("moveObjOnRobot(DR)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								DomesticResource = payloadArg(0).toString()
						}
						when(DomesticResource) {
									"table" -> {
						solve("exposeObjectOnTable(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("exposeObjectOnTable = ${getCurSol("L").toString()}")
						var objOnTable = getCurSol("L").toString().replace("[", "").replace("]", "")
						println("Oggetti presenti sul tavolo: ${objOnTable}")
						for(obj in objOnTable.split(",")){
						println("inserisco sul robot: ${obj}")
						solve("assert(itemonrobot('${obj}'))","") //set resVar	
						solve("retract(itemontable('${obj}'))","") //set resVar	
						}
						 }
						}
									"pantry" -> {
						solve("exposePantry(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("exposePantry = ${getCurSol("L").toString()}")
						var objInPantry = getCurSol("L").toString().replace("[", "").replace("]", "")
						println("Oggetti presenti nella dispensa: ${objInPantry}")
						for(obj in objInPantry.split(",")){
						println("inserisco sul robot: ${obj}")
						solve("assert(itemonrobot('${obj}'))","") //set resVar	
						solve("retract(iteminpantry('${obj}'))","") //set resVar	
						}
						 }
						}
									"dishwasher" -> {
						solve("exposeDishwasher(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("exposeDishwasher = ${getCurSol("L").toString()}")
						var objInDishwasher = getCurSol("L").toString().replace("[", "").replace("]", "")
						println("Oggetti presenti nella lavastoviglie: ${objInDishwasher}")
						for(obj in objInDishwasher.split(",")){
						println("inserisco sul robot: ${obj}")
						solve("assert(itemonrobot('${obj}'))","") //set resVar	
						solve("retract(itemindishwasher('${obj}'))","") //set resVar	
						}
						 }
						}
								}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("foodToRobot") { //this:State
					action { //it:State
						println("")
						println("State foodToRobot")
						var name = ""
								var code = ""
						if( checkMsgContent( Term.createTerm("moveFoodOnRobot(DR,NAMECODE)"), Term.createTerm("moveFoodOnRobot(DR,NAMECODE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								DomesticResource = payloadArg(0).toString()
											name = payloadArg(1).toString().split("_").get(0).toString()				
											code = payloadArg(1).toString().split("_").get(1).toString()
								println("${name}, ${code}")
						}
						when(DomesticResource) {
									"table" -> {
						solve("exposeFoodOnTable(L)","") //set resVar	
						if(currentSolution.isSuccess()) { println("exposeFoodOnTable = ${getCurSol("L").toString()}")
						var foodOnTable = getCurSol("L").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "")
						println("Cibo presente sul tavolo: ${foodOnTable}")
						for(food in foodOnTable.split("((")){
												if(food != ""){
													var f = food.split(",")
													var nameFood = f.get(0).toString()
													var codeFood = f.get(1).toString()
													var qtyFood = Integer.parseInt(f.get(2).toString())
						println("inserisco sul robot: ${nameFood} ${codeFood} ${qtyFood}")
						solve("assert(itemonrobot('${nameFood}','${codeFood}','${qtyFood}'))","") //set resVar	
						solve("retract(itemontable('${nameFood}','${codeFood}',QTA))","") //set resVar	
						}
											}
						 }
						}
									"fridge" -> {
						solve("itemonrobot('${name}','${code}',QTA)","") //set resVar	
						if(currentSolution.isSuccess()) { println("risolta ricerca codice-cibo")
						var qty = Integer.parseInt(getCurSol("QTA").toString())+1
						solve("retract(itemonrobot('${name}','${code}',QTA))","") //set resVar	
						solve("assert(itemonrobot('${name}','${code}',${qty}))","")
						 }
						else
						{ solve("assert(itemonrobot('${name}','${code}',1))","") //set resVar	
						 }
						}
								}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("doExpose") { //this:State
					action { //it:State
						println("")
						println("State doExpose")
						solve("expose(OT,FT,P,D,OR,FR)","") //set resVar	
						if(currentSolution.isSuccess()) { println("Cibo sul table: ${getCurSol("FT").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("Oggetti sul table: ${getCurSol("OT").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("Oggetti rimasti nel pantry: ${getCurSol("P").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("Cibo rimasto sul robot: ${getCurSol("FR").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("Oggetti rimasti sul robot: ${getCurSol("OR").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						println("Oggetti rimasti nel dishwasher: ${getCurSol("D").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")}")
						 }
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
