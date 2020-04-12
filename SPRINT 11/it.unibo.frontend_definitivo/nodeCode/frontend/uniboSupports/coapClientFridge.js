/*
	frontend/uniboSupports/coapClientFridge
*/
var app = require('../applCode');  //previously was app;
const coap = require("node-coap-client").CoapClient; 
//var coapAddr = "coap://192.168.1.8:5684"	//RESOURCE ON RASPBERRY PI
var coapAddr = "coap://localhost:5684"
var coapResourceAddr = coapAddr + "/serverfridge"

exports.setcoapAddr = function(addr){
	coapAddr = "coap://"+ addr + ":5684";
	coapResourceAddr = coapAddr + "/serverfridge"
	console.log("coap coapResourceAddr " + coapResourceAddr);
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
			console.log("coap get done > " + response.payload);
			var expose = response.payload.toString().split(":")[1];
			app.getIoSocket().emit('fridgeState', expose);
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
			console.log("coap put done > " + cmd);
			console.log("response: " + response.payload);

			app.getIoSocket().emit('request', response.payload.toString());
		})
	    .catch(err => {
			/* handle error */
			console.log("coap put error> " + err + " for cmd=" + cmd);
		});
}//coapPut

const myself = require('./coapClientFridge');

function test(){
 	console.log("CoAP support started");
}

test()
