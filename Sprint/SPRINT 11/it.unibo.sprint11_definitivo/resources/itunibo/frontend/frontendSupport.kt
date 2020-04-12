package itunibo.frontend

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoap

object frontendSupport{
	lateinit var resourcecoap : modelResourceCoap
	
	fun setCoapResource( rescoap : modelResourceCoap ){
		resourcecoap = rescoap
	}
	
	fun updateCurrentPositionToFrontend( actor: ActorBasic, content: String ){
		println("frontendSupport updateCurrentPositionToFrontend content = $content")
		actor.scope.launch{
			actor.emit("currentPosition", content)
		}	
	}
	
	fun updateCurrentTaskToFrontend( actor: ActorBasic, content: String ){
		println("frontendSupport updateCurrentTaskToFrontend content = $content")
		actor.scope.launch{
			actor.emit("currentTask", content)
		}	
	}
	
	fun updateGoalToFrontend( actor: ActorBasic, content: String ){
		println("frontendSupport updateGoalToFrontend content = $content")
		actor.scope.launch{
			actor.emit("goal", content)
		}	
	}
	
	fun updateFrontend( actor: ActorBasic, content: String ){
		var outS = ""
		var contentEmit = content
		when(content){
			"systemStarted" -> outS = "content(taskCompleted(systemStarted))" 
			"endPrepare"  	-> outS = "content(taskCompleted(endPrepare))"
			"foodAdded"   	-> outS = "content(taskCompleted(foodAdded))"
			"warning"     	-> outS = "content(taskCompleted(warning))"
			"endClear"    	-> outS = "content(taskCompleted(endClear))"
			else 			-> {
				outS = "content(" + content + ")"
				contentEmit = "consultKb"
			}
		}
		println("frontendSupport updateFrontend content=$content outS=$outS contentEmit=$contentEmit")
		actor.scope.launch{
			actor.emit(contentEmit, outS) //NOTA: il frontend riceve 6 eventi uguali
		}	
	}
}

