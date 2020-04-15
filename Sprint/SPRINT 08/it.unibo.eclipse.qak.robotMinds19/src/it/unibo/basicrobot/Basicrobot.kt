/* Generated by AN DISI Unibo */ 
package it.unibo.basicrobot

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Basicrobot ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						  
						//CREATE A PIPE for the sonar-data stream
						
						val sonaractorfilter = itunibo.robot.sonaractorfilter( "sonaractorfilter"  ) 
						val obstacleDetector = itunibo.robot.obstacledetector( "obstacledetector" )
						val logger           = itunibo.robot.Logger("logFiltered")
						
						 sonaractorfilter.subscribe(obstacleDetector) 
						 sonaractorfilter.subscribe(myself)   //to allow handling of sonarData
						 obstacleDetector.subscribeLocalActor( "onestepahead" ) 
						 obstacleDetector.subscribe(logger) 
						//sonaractorfilter.subscribe(logger)  
						
						//sonaractorfilter.subscribe(obstacleDetector).subscribeLocalActor( "onestepahead" ).subscribe(logger) 
						solve("consult('basicRobotConfig.pl')","") //set resVar	
						solve("robot(R,PORT)","") //set resVar	
						if(currentSolution.isSuccess()) { println("USING ROBOT : ${getCurSol("R")},  port= ${getCurSol("PORT")} ")
						itunibo.robot.robotSupport.create(myself ,getCurSol("R").toString(), getCurSol("PORT").toString(), sonaractorfilter )
						 }
						else
						{ println("no robot")
						 }
						delay(1000) 
						itunibo.robot.robotSupport.move( "msg(a)"  )
						delay(700) 
						itunibo.robot.robotSupport.move( "msg(d)"  )
						delay(700) 
						itunibo.robot.robotSupport.move( "msg(h)"  )
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t010",targetState="handleRobotCmd",cond=whenDispatch("robotCmd"))
					transition(edgeName="t011",targetState="emitEventForAppl",cond=whenEvent("sonarData"))
				}	 
				state("handleRobotCmd") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("robotCmd(CMD)"), Term.createTerm("robotCmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.robotSupport.move( "msg(${payloadArg(0)})"  )
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("emitEventForAppl") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("sonarData(D)"), Term.createTerm("sonarData(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								emit("polar", "p(${payloadArg(0)},90)" ) 
						}
						if( checkMsgContent( Term.createTerm("sonarData(D)"), Term.createTerm("sonarData(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								emit("sonarRobot", "sonar(${payloadArg(0)})" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
