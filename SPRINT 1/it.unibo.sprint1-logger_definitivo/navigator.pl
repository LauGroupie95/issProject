%====================================================================================
% navigator description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxnavigator, "localhost",  "MQTT", "0").
context(ctxdummyformind, "localhost",  "MQTT", "0").
context(ctxdummysprint1, "localhost",  "MQTT", "0").
 qactor( resourcemodel, ctxdummyformind, "external").
  qactor( onestepahead, ctxdummyformind, "external").
  qactor( master1, ctxdummysprint1, "external").
  qactor( navi, ctxnavigator, "it.unibo.navi.Navi").
