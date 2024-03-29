package itunibo.planner

import aima.core.agent.Action
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import itunibo.planner.model.RobotState.Direction

object moveUtils{
    private val actions : List<Action>? = null
    private var existPlan = false

	private var mapDims   : Pair<Int,Int> = Pair(0,0)
	private var curPos    : Pair<Int,Int> = Pair(0,0)
	private var curGoal   : Pair<Int,Int> = Pair(0,0)
	private var direction = "downDir"
	private val PauseTime = 250
	
	private var MaxX        = 0
	private var MaxY        = 0
	private var CurX        = 0
	private var CurY        = 0
	
	
    private fun storeMovesInActor( actor : ActorBasic, actions : List<Action>?  ) {
        if( actions == null ) return
        val iter = actions.iterator()
        while (iter.hasNext()) {
            val a = iter.next()
            actor.solve("assert( move($a) )")
        }
    }
	fun loadRoomMap( actor : ActorBasic,  fname : String ){
		val dims = plannerUtil.loadRoomMap( fname )
		memoMapDims( actor, dims )
 	}
	fun saveMap( actor : ActorBasic, fname : String) {
		val dims = plannerUtil.saveMap( fname )
		memoMapDims( actor, dims )
 	}	
	fun memoMapDims( actor : ActorBasic, dims : Pair<Int,Int> ){
		mapDims = dims
		MaxX    = dims.first
		MaxY    = dims.second
		actor.solve("retract( mapdims(_,_) )")		//remove old data
		actor.solve("assert(  mapdims( ${dims.first},${dims.second} ) )")				
	}
	
 	fun getMapDimX( ) 	: Int{ return mapDims.first }
	fun getMapDimY( ) 	: Int{ return mapDims.second }
 	fun getPosX(actor : ActorBasic)    	  : Int{ setPosition(actor); return curPos.first }
	fun getPosY(actor : ActorBasic)    	  : Int{ setPosition(actor);return curPos.second }
	fun getDirection(actor : ActorBasic)  : String{ setPosition(actor);return direction.toString() }
	fun mapIsEmpty() : Boolean{return (getMapDimX( )==0 &&  getMapDimY( )==0 ) }
	
	
	fun showCurrentRobotState(){
		println("===================================================")
		plannerUtil.showMap()
		println("RobotPos=($CurX,$CurY) in map($MaxX,$MaxY) direction=$direction")
		println("===================================================")
	}
 	fun setObstacleOnCurrentDirection( actor : ActorBasic ){
		println("")
		println("************ setto l'ostacolo nella direzione corrente *********^^^^ direction: " + direction)
		doPlannedMove(actor, direction )
	}
	
	fun setDuration( actor : ActorBasic ){
		val time = plannerUtil.getDuration()
		actor.solve("retract( wduration(_) )")		//remove old data
		actor.solve("assert( wduration($time) )")
 	}
	
	fun setDirection( actor : ActorBasic )  {
		direction = plannerUtil.getDirection()
		//println("moveUtils direction=$direction")
		actor.solve("retract( direction(_) )")		//remove old data
		actor.solve("assert( direction($direction) )")
 	}
	
	fun setGoal( actor : ActorBasic, x: String, y: String) {
		val xv = Integer.parseInt(x)
		val yv = Integer.parseInt(y)
		plannerUtil.setGoal( xv,yv )
		curGoal=Pair(xv,yv)
		actor.solve("retract( curGoal(_,_) )")		//remove old data
		actor.solve("assert( curGoal($x,$y) )")
	}	

	
	fun doPlan(actor : ActorBasic ){
		val plan = plannerUtil.doPlan(  )
		existPlan = plan != null
		if( existPlan ) storeMovesInActor(actor,plan) 
	}
	
	fun existPlan() : Boolean{ return existPlan }

	fun doPlannedMove(actor : ActorBasic, move: String){
		var oldRobotPosition = "(${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}, ${getDirection(actor)})"
		var actorName = actor.name
		
		plannerUtil.doMove( move )
		setPosition(actor)
		//setDirection( actor )

		utils.writeLog("$actorName | $oldRobotPosition | $move | (${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}, ${getDirection(actor)})")
		var ToSend = "pos([${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}], ${getDirection(actor)})"
		println("ToSend doPlannedMove: ${ToSend}")
		itunibo.frontend.frontendSupport.updateCurrentPositionToFrontend(actor, ToSend)
	}
	
	fun setPosition(actor : ActorBasic){
		direction     = plannerUtil.getDirection()
		val posx      = plannerUtil.getPosX()
		val posy      = plannerUtil.getPosY()
		curPos        = Pair( posx,posy )
		CurX = curPos.first
		CurY = curPos.second
		
		//println("setPosition curPos=($posx,$posy,$direction)")
		actor.solve("retract( curPos(_,_) )")		//remove old data
		actor.solve("assert( curPos($posx,$posy) )")			
		actor.solve("retract( curPos(_,_,_) )")		//remove old data
		actor.solve("assert( curPos($posx,$posy,$direction) )")			
	}
	
	suspend fun rotate(actor:ActorBasic,move:String,pauseTime:Int=PauseTime){
		when( move ){
			"a" -> rotateLeft(actor, pauseTime)
			"d" -> rotateRight(actor, pauseTime)
			else -> println("rotate $move unknown")
		}
 	}
 	suspend fun rotateRight(actor : ActorBasic, pauseTime : Int = PauseTime){
 		actor.forward("modelChange", "modelChange(robot,d)", "resourcemodel")
 		doPlannedMove(actor, "d" )	    //update map
		delay( pauseTime.toLong() )
	}
	suspend fun rotateLeft(actor : ActorBasic, pauseTime : Int = PauseTime){
		actor.forward("modelChange", "modelChange(robot,a)", "resourcemodel")
 		doPlannedMove(actor, "a" )	    //update map	
		delay( pauseTime.toLong() )
	}
 	suspend fun moveAhead(actor:ActorBasic, stepTime:Int, pauseTime:Int = PauseTime, dest:String ="resourcemodel"){
		println("moveUtils moveAhead stepTime=$stepTime")
		actor.forward("modelChange", "modelChange(robot,w)", dest)
		delay( stepTime.toLong() )
		actor.forward("modelChange", "modelChange(robot,h)", dest)
		doPlannedMove(actor, "w" )	//update map	
		delay( pauseTime.toLong() )
	} 
	suspend fun attemptTomoveAhead(actor:ActorBasic,stepTime:Int, dest:String ="onestepahead"){
 		println("moveUtils attemptTomoveAhead stepTime=$stepTime")
		actor.forward("onestep", "onestep(${stepTime})", dest)
   	}
	fun updateMapAfterAheadOk(actor : ActorBasic ){
		doPlannedMove(actor  , "w")
	}
	suspend fun backToCompensate(actor : ActorBasic, stepTime : Int, pauseTime : Int = PauseTime){
		var oldRobotPosition = "backToCompensate - (${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}, ${getDirection(actor)})"
		var actorName = actor.name
		
		println("moveUtils backToCompensate stepTime=$stepTime")
		actor.forward("modelChange", "modelChange(robot,s)", "resourcemodel")
		delay( stepTime.toLong() )
		
		utils.writeLog("$actorName | $oldRobotPosition | s | (${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}, ${getDirection(actor)})")
		var ToSend = "pos([${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}], ${getDirection(actor)})"
		itunibo.frontend.frontendSupport.updateCurrentPositionToFrontend(actor, ToSend)
		oldRobotPosition = "backToCompensate - (${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}, ${getDirection(actor)})"
		
		actor.forward("modelChange", "modelChange(robot,h)", "resourcemodel")
		delay( pauseTime.toLong() )
		
		utils.writeLog("$actorName | $oldRobotPosition | h | (${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}, ${getDirection(actor)})")
		ToSend = "pos([${plannerUtil.getPosX()}, ${plannerUtil.getPosY()}], ${getDirection(actor)})"
		itunibo.frontend.frontendSupport.updateCurrentPositionToFrontend(actor, ToSend)
   	}
	
}
