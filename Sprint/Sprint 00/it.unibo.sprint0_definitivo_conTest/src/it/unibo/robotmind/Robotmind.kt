/* Generated by AN DISI Unibo */ 
package it.unibo.robotmind

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Robotmind ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						forward("robotCmd", "robotCmd(w)" ,"robotmind" ) 
					}
					 transition(edgeName="t00",targetState="handleCmd",cond=whenDispatch("robotCmd"))
				}	 
				state("handleCmd") { //this:State
					action { //it:State
						forward("robotCmd", "robotCmd(a)" ,"robotbody" ) 
						delay(1000) 
						forward("robotCmd", "robotCmd(w)" ,"robotbody" ) 
						delay(1000) 
						forward("robotCmd", "robotCmd(w)" ,"robotbody" ) 
						delay(1000) 
						forward("robotCmd", "robotCmd(d)" ,"robotbody" ) 
						delay(1000) 
						forward("robotCmd", "robotCmd(w)" ,"robotbody" ) 
						delay(1000) 
						forward("robotCmd", "robotCmd(s)" ,"robotbody" ) 
						delay(1000) 
						forward("robotCmd", "robotCmd(s)" ,"robotbody" ) 
					}
				}	 
			}
		}
}
