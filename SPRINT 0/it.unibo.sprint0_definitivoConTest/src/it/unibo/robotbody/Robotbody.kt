/* Generated by AN DISI Unibo */ 
package it.unibo.robotbody

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Robotbody ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						itunibo.robot.robotSupport.create(myself ,"virtual", "8080" )
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						delay(300) 
					}
					 transition(edgeName="t01",targetState="handleCmd",cond=whenDispatch("robotCmd"))
				}	 
				state("handleCmd") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("robotCmd(X)"), Term.createTerm("robotCmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.robotSupport.move( "msg(${payloadArg(0)})"  )
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
