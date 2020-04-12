package itunibo.coap.fridge

import org.eclipse.californium.core.coap.CoAP.ResponseCode.BAD_REQUEST
import org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED
import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.coap.CoAP.ResponseCode
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.server.resources.CoapExchange
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import org.eclipse.californium.core.CoapServer
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import org.eclipse.californium.core.coap.CoAP.Type

//questo sarebbe il server
class fridgeCoap (name : String ) : CoapResource(name) {
	
	companion object {
		lateinit var actor : ActorBasic
		//var curmodelval = "unknown"
		lateinit var resourceCoap : fridgeCoap

		fun create( a: ActorBasic, name: String  ){
			actor = a
			val server   = CoapServer(5684);		//COAP SERVER 5683
			resourceCoap = fridgeCoap( name )
			server.add( resourceCoap );
			println("--------------------------------------------------")
			println("Fridge Coap Server started");
			println("--------------------------------------------------")
			server.start();
			fridgeCoapSupport.setCoapResource(resourceCoap)  //Injects a reference
 		}
	}
	
	init{ 
		println("--------------------------------------------------")
		println("fridgeCoap init")
		println("--------------------------------------------------")
		setObservable(true) 				// enable observing	!!!!!!!!!!!!!!
		setObserveType(Type.CON)			// configure the notification type to CONs
		//getAttributes().setObservable();	// mark observable in the Link-Format			
	}
	
	override fun handlePUT(exchange: CoapExchange?) {
		try {
			val reqText = exchange!!.getRequestText()
			println()
			//println("%%%%%%%%%%%%%%%% handlePUT value= $reqText"  )

			if(reqText == "expose"){
				handleExpose(exchange)
			}
			else { //se non è expose, è un codice nel formato cxxx
				handleAnswer(exchange, reqText)
			}
 		} catch (e: Exception) {
			exchange!!.respond(BAD_REQUEST, "Invalid String")
		}
	}
	
	fun handleExpose(exchange: CoapExchange?) {
		actor.solve("exposeFridge(F)")
		if(actor.solveOk()){
			//println(actor.getCurSol("F").toString())
			val lista = actor.getCurSol("F").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")
			exchange!!.respond(ResponseCode.CONTENT, "Contenuto frigo: "+lista)
		}
		else {
			exchange!!.respond(ResponseCode.CONTENT, "Fai schifo D:")
		}
	}
		
	fun handleAnswer(exchange: CoapExchange?, code: String?){
		GlobalScope.launch{
			actor.solve("iteminfridge(NOME,"+code!!.toLowerCase()+",QTA)")
			//toLowerCase è fondamentale se no prolog lo interpreta come variabile!
			
			if(actor.solveOk()){
				println(actor.getCurSol("NOME").toString())
				println(actor.getCurSol("QTA").toString())
				exchange!!.respond(ResponseCode.CONTENT, "yes")
			}
			else {
				exchange!!.respond(ResponseCode.CONTENT, "no")
			}
		}
	}	
}