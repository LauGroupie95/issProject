package itunibo.coap.fridge

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.Utils
import org.eclipse.californium.core.coap.MediaTypeRegistry
import itunibo.coap.fridge.AsynchListener

	private lateinit var coapClient: CoapClient
	
	fun createClient(serverAddr: String, port: Int, resourceName: String?) {
		coapClient = CoapClient("coap://$serverAddr:" + port + "/" + resourceName)
		println("Client started")
	}

/*
	fun synchGet() { //Synchronously send the GET message (blocking call)
		println("%%% synchGet ")
//		CoapResponse coapResp = coapClient.advanced(request);
		val coapResp = coapClient.get()
//The "CoapResponse" message contains the response.
 		//println(Utils.prettyPrint(coapResp))
		println(coapResp.responseText)
	}
*/

	fun put(v: String) {
		val coapResp = coapClient.put(v, MediaTypeRegistry.TEXT_PLAIN)
		//The "CoapResponse" message contains the response.
		println("%%% RECEIVED put $v:")
		//println(Utils.prettyPrint(coapResp))
		println(coapResp.responseText)
	}

/*
 	fun asynchGet() {
 		coapClient.get( AsynchListener );
	}
*/
