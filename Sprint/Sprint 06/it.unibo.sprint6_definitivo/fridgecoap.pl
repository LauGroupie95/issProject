%====================================================================================
% fridgecoap description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxfridge, "localhost",  "MQTT", "0").
context(ctxdummyforkb, "localhost",  "MQTT", "0").
context(ctxdummyforparty, "localhost",  "MQTT", "0").
context(ctxdummyforsprint6, "localhost",  "MQTT", "0").
 qactor( addfood, ctxdummyforparty, "external").
  qactor( kb, ctxdummyforkb, "external").
  qactor( master6, ctxdummyforsprint6, "external").
  qactor( fridge, ctxfridge, "it.unibo.fridge.Fridge").
