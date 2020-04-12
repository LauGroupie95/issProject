%====================================================================================
% navigator description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxnavigator, "localhost",  "MQTT", "0").
context(ctxdummyformind, "localhost",  "MQTT", "0").
context(ctxdummysprint2, "localhost",  "MQTT", "0").
 qactor( resourcemodel, ctxdummyformind, "external").
  qactor( prepare, ctxdummysprint2, "external").
  qactor( master2, ctxdummysprint2, "external").
  qactor( navi, ctxnavigator, "it.unibo.navi.Navi").
