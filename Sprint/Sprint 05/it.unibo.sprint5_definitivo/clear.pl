%====================================================================================
% clear description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxclear, "localhost",  "MQTT", "0").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
context(ctxdummykb, "localhost",  "MQTT", "0").
context(ctxdummyforsprint5, "localhost",  "MQTT", "0").
 qactor( master5, ctxdummyforsprint5, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( kb, ctxdummykb, "external").
  qactor( clear, ctxclear, "it.unibo.clear.Clear").
