%====================================================================================
% kb description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxkb, "localhost",  "MQTT", "0").
context(ctxdummyprepare, "localhost",  "MQTT", "0").
context(ctxdummyclear, "localhost",  "MQTT", "0").
 qactor( prepare, ctxdummyprepare, "external").
  qactor( clear, ctxdummyclear, "external").
  qactor( kb, ctxkb, "it.unibo.kb.Kb").
