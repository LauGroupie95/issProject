%====================================================================================
% fridgecoap description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxfridge, "localhost",  "MQTT", "0").
context(ctxdummykb, "localhost",  "MQTT", "0").
context(ctxdummyparty, "localhost",  "MQTT", "0").
 qactor( addfood, ctxdummyparty, "external").
  qactor( kb, ctxdummykb, "external").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
