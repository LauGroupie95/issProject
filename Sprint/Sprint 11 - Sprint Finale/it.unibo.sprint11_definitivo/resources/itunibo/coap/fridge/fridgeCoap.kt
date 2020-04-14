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
import it.unibo.kactor.sysUtil

//questo sarebbe il server
//classe del prof: modelResourceCoap
class fridgeCoap (name : String ) : CoapResource(name) {
	
	companion object {
		lateinit var actor : ActorBasic
		lateinit var resourceCoap : fridgeCoap

		fun create( a: ActorBasic, name: String  ){
			actor = a
			val server   = CoapServer(5684);		//CoAP SERVER 5683
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
	
	override fun handleGET(exchange: CoapExchange?) {
 		try {
			handleExpose(exchange)
 		} catch (e: Exception) {
			exchange!!.respond(BAD_REQUEST, "Invalid String")
		}
	}
	
	override fun handlePUT(exchange: CoapExchange?) {
		try {
			val reqText = exchange!!.getRequestText()

			handleAnswer(exchange, reqText)
 		} catch (e: Exception) {
			exchange!!.respond(BAD_REQUEST, "Invalid String")
		}
	}
	
	fun handleExpose(exchange: CoapExchange?) {
		actor.solve("exposeFridge(F)")
		if(actor.solveOk()){
			val lista = utils.formatListPrologFood(actor.getCurSol("F").toString())
			exchange!!.respond(ResponseCode.CONTENT, "FRIDGE:"+lista)
		}
		else {
			exchange!!.respond(ResponseCode.CONTENT, "No food")
		}
	}
	
	fun handleAnswer(exchange: CoapExchange?, code: String?){
		GlobalScope.launch{
			actor.solve("iteminfridge(NOME,"+code!!.toLowerCase()+",QTA)")
			//toLowerCase è fondamentale se no prolog lo interpreta come variabile!
			
			if(actor.solveOk()){
				println("fridgeCoap KT = ricerca codice avvenuta")
				println("fridgeCoap KT = " + actor.getCurSol("NOME").toString())
				println("fridgeCoap KT = " + actor.getCurSol("QTA").toString())
				MsgUtil.sendMsg( "handleAnswerReply", "handleAnswerReply(yes)", actor ) //serve all'attore
				exchange!!.respond(ResponseCode.CONTENT, "yes") //serve al frontend
			}
			else {
				println("fridgeCoap KT = il codice non esiste")
				MsgUtil.sendMsg( "handleAnswerReply", "handleAnswerReply(no)", actor ) //serve all'attore
				exchange!!.respond(ResponseCode.CONTENT, "no") //serve al frontend
			}
		}
	}	
}