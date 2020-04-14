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
//			} else if (reqText == "consult"){
//				handleConsult(exchange)
			} else { //se non è expose, è un codice nel formato cxxx
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
			//da rivedere qui
			exchange!!.respond(ResponseCode.CONTENT, "Contenuto frigo: "+lista)
		}
		else {
			exchange!!.respond(ResponseCode.CONTENT, "Fai schifo D:")
		}
	}

//	fun handleConsult(exchange: CoapExchange?) {
//		GlobalScope.launch{
//			actor.solve("exposeFridge(F)")
//			if(actor.solveOk()){
//				//println(actor.getCurSol("F").toString())
//				val lista = actor.getCurSol("F").toString().replace("[", "").replace("]", "").replace("/", "").replace(")", "").replace("'", "").replace("(", " ")
//				//da rivedere qui
//				MsgUtil.sendMsg( "handleConsultReply", "handleConsultReply($lista)", actor ) //serve all'attore
//				exchange!!.respond(ResponseCode.CONTENT, "Contenuto frigo: "+lista)
//			}
//			else {
//				exchange!!.respond(ResponseCode.CONTENT, "Fai schifo D:")
//			}
//		}
//	}
			
	fun handleAnswer(exchange: CoapExchange?, code: String?){
		GlobalScope.launch{
			actor.solve("iteminfridge(NOME,"+code!!.toLowerCase()+",QTA)")
			//toLowerCase è fondamentale se no prolog lo interpreta come variabile!
			
			//it.unibo.ctxParty.main()
			//var destActor = sysUtil.getActor("addfood")
			//println("getActor destActor=${destActor}")
			
			if(actor.solveOk()){
				println(actor.getCurSol("NOME").toString())
				println(actor.getCurSol("QTA").toString())
				MsgUtil.sendMsg( "handleAnswerReply", "handleAnswerReply(yes)", actor ) //serve all'attore
				exchange!!.respond(ResponseCode.CONTENT, "yes") //serve al frontend
			}
			else {
				MsgUtil.sendMsg( "handleAnswerReply", "handleAnswerReply(no)", actor ) //serve all'attore
				exchange!!.respond(ResponseCode.CONTENT, "no") //serve al frontend
			}
		}
	}	
}