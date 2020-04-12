%====================================================================================
% guest description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxguest, "localhost",  "MQTT", "0").
context(ctxdummyforroombutler, "localhost",  "MQTT", "0").
context(ctxdummyforkb, "localhost",  "MQTT", "0").
 qactor( kb, ctxdummyforkb, "external").
  qactor( rbr, ctxdummyforroombutler, "external").
  qactor( greedy, ctxguest, "it.unibo.greedy.Greedy").
