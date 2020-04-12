/*
frontend/uniboSupports/applCode
*/
const express = require('express');
const path = require('path');
const favicon = require('serve-favicon');
const logger = require('morgan'); //see 10.1 of nodeExpressWeb.pdf;
//const cookieParser= require('cookie-parser');
const bodyParser = require('body-parser');
const fs = require('fs');
const index = require('./appServer/routes/index');				 
var io; //Upgrade for socketIo;

//for delegate
const mqttUtils = require('./uniboSupports/mqttUtils');  
const coap = require('./uniboSupports/coapClientToResourceModel');  //require("node-coap-client").CoapClient; 

var app = express();

// view engine setup;
app.set('views', path.join(__dirname, 'appServer', 'views'));	 
app.set('view engine', 'ejs');

//create a write stream (in append mode) ;
var accessLogStream = fs.createWriteStream(path.join(__dirname, 'morganLog.log'), {flags: 'a'})
app.use(logger("short", {stream: accessLogStream}));

//Creates a default route. Overloads app.use('/', index);
//app.get("/", function(req,res){ res.send("Welcome to frontend Server"); } );

// uncomment after placing your favicon in /public
app.use(favicon(path.join(__dirname, 'public', 'raguLovers.ico')));
app.use(logger('dev'));	//shows commands, e.g. GET /pi 304 23.123 ms - -;
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
//app.use(cookieParser());

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'jsCode'))); //(***)

/*
 * ================ ROUTES ================
 */
//come inizia il programma
app.get('/', function(req, res) {
	res.render("homeApplication");
});

app.get('/application', function(req, res) {
	res.render("frontendApplication");
}); 

app.get('/info', function (req, res) {
	res.send('This is our frontend! Made by: Castagnini Lorenzo, Gruppioni Laura, Zanca Giovanni')
});

/*
app.get('/robotmodel', function(req, res) {
	res.send( mqttUtils.getrobotmodel() )
});	
app.get('/sonarrobotmodel', function(req, res) {
	res.send( mqttUtils.getsonarrobotmodel() )
});
 */

/*
 * ================ COMMANDS ================
 */
var patternFoodCode = new RegExp("c[0-9]{3}$");

//Richiama la pagina "frontendApplication" in "frontendApplication.ejs" e invia il messaggio "startTheSystem"
//all'applicazione
app.post("/startappl", function(req, res,next) {
	console.log("\nPremuto il pulsante START");
	res.render("frontendApplication");
	//delegateForAppl("startAppl", req, res );
	next();
});

//invia il messaggio "prepare"
app.post("/prepare", function(req, res,next) {
	console.log("\nPremuto il pulsante PREPARE");
	//publishMsgToButlerapplication("prepare");
	//next();
	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//invia il messaggio "clear"
app.post("/clear", function(req, res,next) {
  	console.log("\nPremuto il pulsante CLEAR");
  	//publishMsgToButlerapplication("clear");
  	//next();
  	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//invia il messaggio "answer"
app.post("/answer", function(req, res, next){
	console.log("\nPremuto il pulsante ANSWER");
	if(!patternFoodCode.test(req.body.foodcode)){
		console.log("Il codice del cibo deve iniziare con 'c' e contenere 3 numeri");
	} else {
		console.log(req.body.foodcode);
		io.emit('warning', "codice corretto " + req.body.foodcode);
	}
	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//invia il messaggio "addfood"
app.post("/addfood", function(req, res, next){
	console.log("\nPremuto il pulsante ADDFOOD");
	if(!patternFoodCode.test(req.body.foodcode)){
		console.log("Il codice del cibo deve iniziare con 'c' e contenere 3 numeri");
	} else {
		console.log(req.body.foodcode);
		io.emit('warning', "codice corretto " + req.body.foodcode);
	}
	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//
app.post("/stop", function(req, res, next) {
	console.log("\nPremuto il pulsante STOP");
  	//delegateForAppl( "stopAppl",  req, res );
  	//publishEmitEvent( "stopAppl","stopAppl(go)");
  	//next();
  	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//
app.post("/reactivate", function(req, res,next) {
	console.log("\nPremuto il pulsante REACTIVATE");
  	//publishEmitEvent("reactivateAppl","reactivateAppl(go)");
  	//next();
  	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//invia il messaggio "expose"
app.post("/expose", function(req, res,next) {
	console.log("\nPremuto il pulsante EXPOSE");
	//publishMsgToButlerapplication("expose");
	//next();
	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});

//emette l'evento "consult"
app.post("/consult", function(req, res,next) {
	console.log("\nPremuto il pulsante CONSULT");
	//publishEmitEvent("consult","consult");
	//next();
	res.status(204).send(); //per rimanere nella stessa pagina dopo un click
});		

function handlePostMove( cmd, msg, req, res, next ){
	result = "Web server done: " + cmd
	delegate( cmd, msg, req, res);	
	next();
}

/*
 * ================ UTILITIES ================
 */
var result = "";

app.setIoSocket = function( iosock ){
	io = iosock;
	mqttUtils.setIoSocket(iosock);
	console.log("app SETIOSOCKET io=" + io);
}

//sends commands to the application logic through CoAP
function delegate( cmd, newState, req, res ){
	//publishMsgToRobotmind(cmd);                  //interaction with the robotmind 
	//publishEmitUserCmd(cmd);                     //interaction with the basicrobot
	//publishMsgToResourceModel("robot",cmd);	    //for hexagonal mind
	changeResourceModelCoap(cmd);		            //for hexagonal mind RESTful m2m
}

//sends commands to the application logic through MQTT
function delegateForAppl( cmd, req, res  ){
	console.log("app delegateForAppl cmd=" + cmd); 
	result = "Web server delegateForAppl: " + cmd;
	publishMsgToRobotapplication( cmd );		     
} 

/*
 * ================ TO THE BUSINESS LOGIC ================
 */
//Publish (via MQTT) the qak dispatch robotCmd on the topic unibo/qak/robotmind
var publishMsgToRobotmind = function( cmd ){  
	var msgstr = "msg(robotCmd,dispatch,js,robotmind,robotCmd("+cmd +"),1)"  ;  
	console.log("publishMsgToRobotmind forward> "+ msgstr);
	mqttUtils.publish( msgstr, "unibo/qak/robotmind" );
}

//Publish (via MQTT) the qak dispatch modelChange on the topic unibo/qak/resourcemodel
var publishMsgToResourceModel = function( target, cmd ){  
	var msgstr = "msg(modelChange,dispatch,js,resourcemodel,modelChange("+target+", "+cmd +"),1)"  ;  
	console.log("publishMsgToResourceModel forward> "+ msgstr); 	
	mqttUtils.publish( msgstr, "unibo/qak/resourcemodel" );
}

//Publish (via CoAP) the qak dispatch modelChange on the topic unibo/qak/resourcemodel
var changeResourceModelCoap = function( cmd ){  
	console.log("coap PUT> "+ cmd);
	coap.coapPut(cmd);	//see coapClientToResourceModel
}

//Publish (via MQTT) the qak event userCmd on the topic unibo/qak/events
var publishEmitUserCmd = function( cmd ){  
	var eventstr = "msg(userCmd,event,js,none,userCmd("+cmd +"),1)"  ;  //TODO: replace 1 with counter
	console.log("emits> "+ eventstr);
	mqttUtils.publish( eventstr, "unibo/qak/events" );	 
}

var pythonExec = function( cmd ){  
	var eventstr = "msg(rotationCmd,event,js,none,rotationCmd("+cmd +"),1)"  ;  //TODO: replace 1 with counter
	console.log("terminatePythonExec emits> "+ eventstr);
	mqttUtils.publish( eventstr, "unibo/qak/events" );	 
}

//Publish (via MQTT) the qak dispatch robotCmd on the topic unibo/qak/robotmindapplication
var publishMsgToRobotapplication = function (cmd){
 	var msgstr = "msg(" + cmd + ",dispatch,js,robotmindapplication,"+ cmd +"(go),1)"  ;  //TODO: replace 1 with counter
	console.log("publishMsgToRobotapplication forward> "+ msgstr);
 	mqttUtils.publish( msgstr, "unibo/qak/robotmindapplication" );
}

//Towards the butler application => send to butlermind
var publishMsgToButlerapplication = function (cmd){
	var msgstr = "msg(" + cmd + ",dispatch,js,butlermind,"+ cmd +"(go),1)"  ;  //TODO: replace 1 with counter
	console.log("publishMsgToRobotapplication forward> "+ msgstr);
	mqttUtils.publish( msgstr, "unibo/qak/butlermind" );
}

//Publish (via MQTT) the qak event on the topic unibo/qak/events
var publishEmitEvent = function( ev, evContent ){  
	var eventstr = "msg("+ev+",event,js,none,"+evContent+",1)"  ;  	//TODO: replace 1 with counter
	console.log("emits> "+ eventstr);
	mqttUtils.publish( eventstr, "unibo/qak/events" );	 
}

/*
 * ================ REPRESENTATION ================
 */
app.use( function(req,res){
	console.info("SENDING THE ANSWER " + result + " json:" + req.accepts('josn') );
	try{
		console.log("answer> "+ result  );
	    /*
		if (req.accepts('json')) {
			return res.send(result);		//give answer to curl / postman
		} else {
			return res.render('index' );
		};
		*/
	   //return res.render('index' );  //NO: we loose the message sent via socket.io
	}catch(e){
		console.info("SORRY ..." + e);
	}
});

//app.use(converter());

/*
 * ================ ERROR HANDLING ================
 */
// catch 404 and forward to error handler;
app.use(function(req, res, next) {
	var err = new Error('Not Found');
	err.status = 404;
	next(err);
});

// error handler;
app.use(function(err, req, res, next) {
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