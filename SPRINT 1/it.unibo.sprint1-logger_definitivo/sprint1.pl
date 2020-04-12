%====================================================================================
% sprint1 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxsprint1, "localhost",  "MQTT", "0").
context(ctxdummyscanningroom, "localhost",  "MQTT", "0").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
 qactor( roomperimeterexplorer, ctxdummyscanningroom, "external").
  qactor( findtable, ctxdummyscanningroom, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( master1, ctxsprint1, "it.unibo.master1.Master1").
