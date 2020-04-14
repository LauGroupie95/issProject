/*
	frontend/uniboSupports/coapClientToResourceModel
*/
var app = require('../applCode');  //previously was app;
const coap = require("node-coap-client").CoapClient; 
//var coapAddr = "coap://192.168.1.8:5683"	//RESOURCE ON RASPBERRY PI
var coapAddr = "coap://localhost:5684"
var coapResourceAddr = coapAddr + "/serverfridge"
const mqttUtils = require('./mqttUtils');  

exports.setcoapAddr = function(addr){
	coapAddr = "coap://"+ addr + ":5684";
	coapResourceAddr = coapAddr + "/serverfridge"
	console.log("coap coapResourceAddr  " + coapResourceAddr);
}

exports.coapGet = function (  ){
	coap
	    .request(
			coapResourceAddr,
	        "get" 		/* "get" | "post" | "put" | "delete" */
 	        //[payload 	/* Buffer */,
	        //[options 	/* RequestOptions */]]
	    )
	    .then(response => {
			/* handle response */
			console.log("coap get done> " + response.payload );
		})
	    .catch(err => {
			/* handle error */ 
			console.log("coap get error> " + err );
		});
}//coapGet

exports.coapPut = function (  cmd ){ 
	coap
	    .request(
	        coapResourceAddr,     
	        "put" , 		// "get" | "post" | "put" | "delete"   
	        new Buffer(cmd) // payload Buffer 
 	        //[options]]	// RequestOptions 
	    )
	    .then(response => {
			/* handle response */  
			console.log("coap put done> " + cmd);
			console.log("response: " + response.payload);
			//response: consultFridge: <contenuto_frigo>

			if(cmd === "consult"){
				app.getIoSocket().emit('consultFridge', response.payload.toString());
			} else {
				//expose e answer
				app.getIoSocket().emit('printOutput', response.payload.toString());
			}
		})
	    .catch(err => {
			/* handle error */
			console.log("coap put error> " + err + " for cmd=" + cmd);
		});
}//coapPut

const myself = require('./coapClientToResourceModel');

function test(){
 	console.log("CoAP support started");
}

test()
