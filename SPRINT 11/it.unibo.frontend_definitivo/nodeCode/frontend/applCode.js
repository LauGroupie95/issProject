/*
	frontend/applCode
*/
const express = require('express');
const path = require('path');
const favicon = require('serve-favicon');
const logger = require('morgan'); //see 10.1 of nodeExpressWeb.pdf;
const bodyParser = require('body-parser');
const fs = require('fs');
// const index = require('./appServer/routes/index');				 
var io; //Upgrade for socketIo;

//for delegate
const mqttUtils = require('./uniboSupports/mqttUtils');  
const coap = require('./uniboSupports/coapClientFridge');

var app = express();

// view engine setup;
app.set('views', path.join(__dirname, 'appServer', 'views'));	 
app.set('view engine', 'ejs');

//create a write stream (in append mode) ;
var accessLogStream = fs.createWriteStream(path.join(__dirname, 'morganLog.log'), {flags: 'a'})
app.use(logger("short", {stream: accessLogStream}));

// uncomment after placing your favicon in /public
app.use(favicon(path.join(__dirname, 'public', 'raguLovers.ico')));
app.use(logger('dev'));	//shows commands, e.g. GET /pi 304 23.123 ms - -;
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'jsCode'))); //(***)

/*
 * ================ ROUTES ================
 */
//come inizia il programma
app.get('/', function(req, res){
	res.render("homeApplication");
});

app.get('/info', function(req, res){
	res.send('This is our frontend! Made by: Castagnini Lorenzo, Gruppioni Laura, Zanca Giovanni');
});

/*
 * ================ COMMANDS ================
 */
var patternFoodCode = new RegExp("c[0-9]{3}$");

//Invia "dynamicStartTheSystem", "consult" e "expose" all'applicazione
app.post("/dynamicStartAppl", function(req, res, next) {
	console.log("\nPremuto il pulsante START");

	//Dispatch dynamicStartTheSystem : dynamicStartTheSystem(N)
	publishMsgToRrb("dynamicStartTheSystem", "");

	//Attendo il caricamento della pagina per popolare le console
	setTimeout(function(){
		publishEmitEvent("consult", "");
	}, 1000);
	setTimeout(function(){
		coapToFridge("expose");
	}, 1000);

	res.render("frontendApplication");
	next();
});

//Invia "staticStartTheSystem", "consult" e "expose" all'applicazione
app.post("/staticStartAppl", function(req, res, next) {
	console.log("\nPremuto il pulsante START");

	//Dispatch staticStartTheSystem : staticStartTheSystem(N)
	publishMsgToRrb("staticStartTheSystem", "");

	//Attendo il caricamento della pagina per popolare le console
	setTimeout(function(){
		publishEmitEvent("consult", "");
	}, 1000);
	setTimeout(function(){
		coapToFridge("expose");
	}, 1000);

	res.render("frontendApplication");
	next();
});

//Invia il messaggio "prepare"
app.post("/prepare", function(req, res, next) {
	console.log("\nPremuto il pulsante PREPARE");

	//Dispatch prepare : prepare(N)
	publishMsgToRrb("prepare", "");
	
	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//Invia il messaggio "clear"
app.post("/clear", function(req, res, next) {
	console.log("\nPremuto il pulsante CLEAR");
	  
	//Dispatch clear : clear(N)
	publishMsgToRrb("clear", "");
	  
  	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//Invia il messaggio "answer" tramite CoAP
app.post("/answer", function(req, res, next){
	console.log("\nPremuto il pulsante ANSWER");

	//run itunibo.coap.fridge.put("<codice_cibo>")
	if(patternFoodCode.test(req.body.foodcode)){
		console.log(req.body.foodcode);
		coapToFridge(req.body.foodcode);
	}

	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//Invia il messaggio "addfood"
app.post("/addfood", function(req, res, next){
	console.log("\nPremuto il pulsante ADDFOOD");

	//Dispatch addFood : addFood(FOODCODE)
	if(patternFoodCode.test(req.body.foodcode)){
		console.log(req.body.foodcode);
		publishMsgToRrb("addFood", req.body.foodcode);
	}

	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//Invia l'evento "stop"
app.post("/stop", function(req, res, next) {
	console.log("\nPremuto il pulsante STOP");
	publishEmitEvent("stop", "");
  	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//Invia l'evento "reactivate"
app.post("/reactivate", function(req, res, next) {
	console.log("\nPremuto il pulsante REACTIVATE");
	publishEmitEvent("reactivate", "");
  	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

/*
 * ================ UTILITIES ================
 */
var result = "";

app.setIoSocket = function(iosock){
	io = iosock;
	mqttUtils.setIoSocket(iosock);
	console.log("app SETIOSOCKET io = " + io);
}

app.getIoSocket = function(){
	return io;
}

/*
 * ================ TO THE BUSINESS LOGIC ================
 */
//mqttUtils.publish(<messaggio/evento_da_mandare>, "unibo/qak/<nome_attore>" );
//<messaggio/evento_da_mandare> deve avere il formato dei messaggi ed eventi qak, quindi:
//1) messaggio => "msg(<nome_messaggio>,dispatch,js,<ricevente>,<nome_messaggio>("+ <contenuto> +"),1)"
//2) evento => "msg(<nome_evento>,event,js,none,<nome_evento>("+ <contenuto_evento> +"),1)"

//Publish (via MQTT) un evento qak al topic unibo/qak/events
var publishEmitEvent = function(ev, evContent){
	var eventstr = evContent !== ""
		? "msg("+ev+",event,js,rbr,"+ev+"("+evContent+"),1)"
		: "msg("+ev+",event,js,rbr,"+ev+",1)";

    console.log("publishEmitEvent emits > "+ eventstr);
	mqttUtils.publish(eventstr, "unibo/qak/events");	 
}

//Publish (via MQTT) un dispatch qak al topic unibo/qak/rbr
var publishMsgToRrb = function(cmd, msgContent){
	var msgstr = msgContent !== ""
		? "msg("+cmd+",dispatch,js,rbr,"+cmd+"("+msgContent+"),1)"
		: "msg("+cmd+",dispatch,js,rbr,"+cmd+",1)";
	
	console.log("publishMsgToRrb forward > "+ msgstr);
	mqttUtils.publish(msgstr, "unibo/qak/rbr");
}

//Put/Get via CoAP
var coapToFridge = function(cmd){
	if(cmd === "expose"){
		console.log("coap GET > "+ cmd);
		coap.coapGet(cmd);	//see coapClientFridge
	}else{
		console.log("coap PUT > "+ cmd);
		coap.coapPut(cmd);	//see coapClientFridge
	}
}

/*
 * ================ REPRESENTATION ================
 */
app.use(function(req, res){
	console.info("SENDING THE ANSWER " + res + " json: " + req.accepts('json') );
	try{
		console.log("answer > "+ res);
	}catch(e){
		console.info("SORRY ..." + e);
	}
});

/*
 * ================ ERROR HANDLING ================
 */
// catch 404 and forward to error handler;
app.use(function(req, res, next){
	var err = new Error('Not Found');
	err.status = 404;
	next(err);
});

// error handler;
app.use(function(err, req, res, next){
	// set locals, only providing error in development
	res.locals.message = err.message;
	res.locals.error = req.app.get('env') === 'development' ? err : {};

	// render the error page;
	res.status(err.status || 500);
	res.render('error');
});

/*
 * ================ EXPORTS ================
 */

module.exports = app;