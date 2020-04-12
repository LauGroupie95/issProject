package itunibo.coap.fridge

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch

object fridgeCoapSupport{
	lateinit var resourcecoap : fridgeCoap
	
	fun setCoapResource( rescoap : fridgeCoap ){
		resourcecoap = rescoap
	}
}



