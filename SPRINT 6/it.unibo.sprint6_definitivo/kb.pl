%====================================================================================
% kb description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxkb, "localhost",  "MQTT", "0").
context(ctxdummyforsprint6, "localhost",  "MQTT", "0").
context(ctxdummyforprepare, "localhost",  "MQTT", "0").
context(ctxdummyforclear, "localhost",  "MQTT", "0").
context(ctxdummyforfridge, "localhost",  "MQTT", "0").
 qactor( master6, ctxdummyforsprint6, "external").
  qactor( prepare, ctxdummyforprepare, "external").
  qactor( clear, ctxdummyforclear, "external").
  qactor( fridge, ctxdummyforfridge, "external").
  qactor( kb, ctxkb, "it.unibo.kb.Kb").
