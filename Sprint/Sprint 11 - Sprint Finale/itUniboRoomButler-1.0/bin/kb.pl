%====================================================================================
% kb description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxkb, "localhost",  "MQTT", "0").
context(ctxdummyforfridge, "localhost",  "MQTT", "0").
context(ctxdummyforparty, "localhost",  "MQTT", "0").
 qactor( prepare, ctxdummyforparty, "external").
  qactor( clear, ctxdummyforparty, "external").
  qactor( fridge, ctxdummyforfridge, "external").
  qactor( kb, ctxkb, "it.unibo.kb.Kb").
