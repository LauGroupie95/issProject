/*
* =====================================
* frontend/uniboSupports/mqttUtils.js
* =====================================
*/
const mqtt = require ('mqtt');  //npm install --save mqtt
const topic = "unibo/qak/events";

var mqttAddr = 'mqtt://localhost'
//var mqttAddr = 'mqtt://192.168.43.229'
//var mqttAddr = 'mqtt://iot.eclipse.org'

var client = mqtt.connect(mqttAddr);
var io; //Upgrade for socketIo;
var robotModel = "none";
var sonarModel = "none";
var roomMapModel = "none";

console.log("mqtt client= " + client);

exports.setIoSocket = function ( iosock ) {
	io = iosock;
	console.log("mqtt SETIOSOCKET io =" + io);
}

client.on('connect', function (){
	client.subscribe( topic );
	console.log('client has connected successfully with ' + mqttAddr);
});

//The message usually arrives as buffer, so I had to convert it to string data type;
client.on('message', function (topic, message){
	console.log("mqtt RECEIVES: "+ message.toString()); //if toString is not given, the message comes as buffer
	//mqtt RECEIVES: msg(systemStarted,event,rbr,none,content(taskCompleted(systemStarted)),23)
	//mqtt RECEIVES: msg(consultKb,event,rbr,none,content(tavolo: ...., dishwasher: ...., contenuto_kb!),23)
	//mqtt RECEIVES: msg(goal,event,rbr,none,goal(${ReachDestination} | (${GoalX}, ${GoalY})),23)
	//mqtt RECEIVES: msg(currentTask,event,rbr,none,task(Executing the command PREPARE),23)
	//mqtt RECEIVES: msg(currentPosition,event,rbr,none,pos((${getPosX()}, ${getPosY()}), ${getDirection()}),23)

	var msgStr = message.toString();
	var msg = "";

	if(msgStr.indexOf("consultKb") >= 0){
		var arg = msgStr.split("content("); //tavolo: ...., dishwasher: ...., contenuto_kb!),23)
		var consultKb = arg[1].split("!", 1); //tavolo: ...., dishwasher: ...., contenuto_kb

		console.log("mqtt send on io.sockets| consultKb = " + consultKb);  
		io.sockets.emit('consultKb', consultKb);
	}
	else if(msgStr.indexOf("content") >= 0){
		var arg = msgStr.indexOf("taskCompleted");

		var msgStr = msgStr.substr(arg);
		var sp2 = msgStr.indexOf("))");	

		var content = message.toString().substr(arg,sp2+1);//taskCompleted(systemStarted)
		//taskCompleted(systemStarted)
		if(arg > 0) {
			msg = content === "taskCompleted(systemStarted)" ? "systemStarted: "
				: content === "taskCompleted(endPrepare)" ? "endPrepare: "
				: content === "taskCompleted(foodAdded)" ? "foodAdded: "
				: content === "taskCompleted(warning)" ? "warning: "
				: content === "taskCompleted(endClear)" ? "endClear: "
				: "TASK NON IDENTIFICATO: ";
		} 

		//systemStarted: taskCompleted(systemStarted) | TASK NON IDENTIFICATO: taskCompleted(x)
		msg = msg + content;

		console.log("mqtt send on io.sockets| msg = "+ msg  + " content = " + content);  
		io.sockets.send(msg);
	}
	else if(msgStr.indexOf("goal") >= 0){
		var arg = msgStr.indexOf("goal(");

		var msgStr = msgStr.substr(arg);
		var sp2 = msgStr.indexOf("))");	

		var content = message.toString().substr(arg,sp2+1); //goal(${ReachDestination} | (${GoalX}, ${GoalY})
		msg = "goal: " + content;
		
		console.log("mqtt send on io.sockets| msg = " +  msg);  
		io.sockets.send(msg);
	}
	else if(msgStr.indexOf("currentTask") >= 0){
		var arg = msgStr.indexOf("task(");

		var msgStr = msgStr.substr(arg);
		var sp2 = msgStr.indexOf(")");	

		var content = message.toString().substr(arg,sp2); //task(Executing the command PREPARE
		msg = "currentTask: " + content;
		
		console.log("mqtt send on io.sockets| msg = " +  msg);  
		io.sockets.send(msg);
	}
	else if(msgStr.indexOf("currentPosition") >= 0){
		var arg = msgStr.indexOf("pos(");

		var msgStr = msgStr.substr(arg);
		var sp2 = msgStr.indexOf(")");	

		var content = message.toString().substr(arg,sp2); //pos(','(${getPosX()}, ${getPosY()}), ${getDirection()}
		msg = "robotPosition: " + content;
		
		console.log("mqtt send on io.sockets| msg = " +  msg);  
		io.sockets.send(msg);
	}
});
 
exports.publish = function( msg, topic ){
	//console.log('mqtt publish ' + client);
	client.publish(topic, msg);
}

exports.getrobotmodel = function(){
	return robotModel;
}

exports.getsonarrobotmodel = function(){
	return sonarModel;
}