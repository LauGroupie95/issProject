%====================================================================================
% sprint6 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxsprint6, "localhost",  "MQTT", "0").
context(ctxdummyforroombutler, "localhost",  "MQTT", "0").
 qactor( rbr, ctxdummyforroombutler, "external").
  qactor( master6, ctxsprint6, "it.unibo.master6.Master6").
