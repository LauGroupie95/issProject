%====================================================================================
% prepare description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
context(ctxdummykb, "localhost",  "MQTT", "0").
context(ctxprepare, "localhost",  "MQTT", "0").
context(ctxdummyforsprint3, "localhost",  "MQTT", "0").
 qactor( master3, ctxdummyforsprint3, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( kb, ctxdummykb, "external").
  qactor( prepare, ctxprepare, "it.unibo.prepare.Prepare").
