%====================================================================================
% sprint0 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxrobotmind, "localhost",  "MQTT", "0").
 qactor( resourcemodel, ctxrobotmind, "it.unibo.resourcemodel.Resourcemodel").
  qactor( sonarhandler, ctxrobotmind, "it.unibo.sonarhandler.Sonarhandler").
  qactor( basicrobot, ctxrobotmind, "it.unibo.basicrobot.Basicrobot").
  qactor( robotmindapplication, ctxrobotmind, "it.unibo.robotmindapplication.Robotmindapplication").
  qactor( onecellforward, ctxrobotmind, "it.unibo.onecellforward.Onecellforward").
