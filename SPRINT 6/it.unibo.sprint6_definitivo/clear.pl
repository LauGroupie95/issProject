%====================================================================================
% clear description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxclear, "localhost",  "MQTT", "0").
context(ctxdummyfornavigator, "localhost",  "MQTT", "0").
context(ctxdummyforkb, "localhost",  "MQTT", "0").
context(ctxdummyforroombutler, "localhost",  "MQTT", "0").
 qactor( navi, ctxdummyfornavigator, "external").
  qactor( kb, ctxdummyforkb, "external").
  qactor( rbr, ctxdummyforroombutler, "external").
  qactor( clear, ctxclear, "it.unibo.clear.Clear").
