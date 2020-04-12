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

fun put(v: String) {
	val coapResp = coapClient.put(v, MediaTypeRegistry.TEXT_PLAIN)
	println("%%% RECEIVED put $v:")
	println(coapResp.responseText)
}
