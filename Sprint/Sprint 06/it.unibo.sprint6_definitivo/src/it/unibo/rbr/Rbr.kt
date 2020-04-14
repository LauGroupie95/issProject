/* Generated by AN DISI Unibo */ 
package it.unibo.rbr

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Rbr ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var FoodCode = "" 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("RBR AVVIATO")
						itunibo.coap.fridge.createClient( "localhost", 5684, "serverfridge"  )
						utils.backupLastLog(  )
						utils.resetFile( "fileLog"  )
						utils.writeLog( "actorName | [backToCompensate - ] currentPosition | move | result\n"  )
					}
					 transition( edgeName="goto",targetState="waitcmd", cond=doswitch() )
				}	 
				state("waitcmd") { //this:State
					action { //it:State
						println("")
						println("State waitcmd")
						println("in attesa di un comando")
					}
					 transition(edgeName="t00",targetState="scanningperimeter",cond=whenDispatch("startTheSystem"))
					transition(edgeName="t01",targetState="updateMaster",cond=whenDispatch("homeReached"))
					transition(edgeName="t02",targetState="executePrepare",cond=whenDispatch("prepare"))
					transition(edgeName="t03",targetState="updateMaster",cond=whenDispatch("endPrepare"))
					transition(edgeName="t04",targetState="addFood",cond=whenDispatch("addFood"))
					transition(edgeName="t05",targetState="updateMaster",cond=whenDispatch("foodAdded"))
					transition(edgeName="t06",targetState="updateMaster",cond=whenDispatch("warning"))
					transition(edgeName="t07",targetState="executeClear",cond=whenDispatch("clear"))
					transition(edgeName="t08",targetState="updateMaster",cond=whenDispatch("endClear"))
				}	 
				state("scanningperimeter") { //this:State
					action { //it:State
						println("")
						println("State scanningperimeter")
						println("Inizio scansione della stanza.")
						forward("scanningPerimeter", "scanningPerimeter" ,"roomperimeterexplorer" ) 
					}
					 transition(edgeName="t19",targetState="findtable",cond=whenDispatch("endPerimeter"))
				}	 
				state("findtable") { //this:State
					action { //it:State
						println("")
						println("Fine scansione della stanza.")
						println("State findtable")
						println("Inizio ricerca del tavolo.")
						forward("scanningTable", "scanningTable" ,"findtable" ) 
					}
					 transition( edgeName="goto",targetState="waitcmd", cond=doswitch() )
				}	 
				state("executePrepare") { //this:State
					action { //it:State
						println("")
						println("State executePrepare")
						delay(500) 
						println("Inizio prepare.")
						forward("prepare", "prepare" ,"prepare" ) 
					}
					 transition( edgeName="goto",targetState="waitcmd", cond=doswitch() )
				}	 
				state("addFood") { //this:State
					action { //it:State
						println("")
						println("State addFood")
						if( checkMsgContent( Term.createTerm("addFood(FOODCODE)"), Term.createTerm("addFood(CODE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								FoodCode = payloadArg(0).toString() 
								println("addFood di ${FoodCode}")
								forward("addFood", "addFood($FoodCode)" ,"addfood" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitcmd", cond=doswitch() )
				}	 
				state("executeClear") { //this:State
					action { //it:State
						println("")
						println("State executeClear")
						forward("clear", "clear" ,"clear" ) 
					}
					 transition( edgeName="goto",targetState="waitcmd", cond=doswitch() )
				}	 
				state("updateMaster") { //this:State
					action { //it:State
						println("")
						println("State updateMaster")
						if( checkMsgContent( Term.createTerm("homeReached(MIT)"), Term.createTerm("homeReached(navi)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Fine ricerca del tavolo.")
								forward("systemStarted", "systemStarted" ,"master6" ) 
						}
						if( checkMsgContent( Term.createTerm("endPrepare(MIT)"), Term.createTerm("endPrepare(prepare)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Fine prepare")
								forward("endPrepare", "endPrepare" ,"master6" ) 
						}
						if( checkMsgContent( Term.createTerm("foodAdded(MIT)"), Term.createTerm("foodAdded(addfood)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Fine addFood")
								forward("foodAdded", "foodAdded" ,"master6" ) 
						}
						if( checkMsgContent( Term.createTerm("warning(FOODCODE)"), Term.createTerm("warning(FOODCODE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("addFood non riuscita")
								forward("warning", "warning($FoodCode)" ,"master6" ) 
						}
						if( checkMsgContent( Term.createTerm("endClear(MIT)"), Term.createTerm("endClear(clear)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Fine clear")
								forward("endClear", "endClear" ,"master6" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitcmd", cond=doswitch() )
				}	 
			}
		}
}