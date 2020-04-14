package itunibo.coap.fridge

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse
import itunibo.outgui.outguiSupport
import java.awt.Color
 
//uguale alla classe del prof: resourceObserverCoapClient.kt  
object fridgeObserverCoapClient : CoapHandler {
	val robotResourceAddr = "coap://localhost:5684/resourcemodel" // "coap://192.168.43.67:5683"
	val outDev            = outguiSupport.create("Resource Coap OBSERVER", Color.green)
	
	override fun onLoad(response: CoapResponse?) {
		val content = response!!.getResponseText()
		outguiSupport.output("$content"  )
	}
	override fun onError() {
		outguiSupport.output("resourceObserverCoapClient Error")
	}
	
	fun create(resourceAddr : String = robotResourceAddr){
		val client   = CoapClient( resourceAddr )
		client.observe(  fridgeObserverCoapClient )   
	}
}

fun main( ) {
	fridgeObserverCoapClient.create( "${fridgeObserverCoapClient.robotResourceAddr}" )
	println("CoapLedObserverClient.java: OBSERVE (press enter to exit)")
 
		val br = BufferedReader(InputStreamReader(System.`in`))
		try {
			br.readLine()
			println("CoapLedObserverClient.java: CANCELLATION")
	} catch (e: IOException) {
	}
}
 