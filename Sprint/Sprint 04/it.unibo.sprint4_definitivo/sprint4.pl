%====================================================================================
% sprint4 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxsprint4, "localhost",  "MQTT", "0").
context(ctxdummyscanningroom, "localhost",  "MQTT", "0").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
context(ctxdummyprepare, "localhost",  "MQTT", "0").
context(ctxdummyclear, "localhost",  "MQTT", "0").
 qactor( roomperimeterexplorer, ctxdummyscanningroom, "external").
  qactor( findtable, ctxdummyscanningroom, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( prepare, ctxdummyprepare, "external").
  qactor( clear, ctxdummyclear, "external").
  qactor( master4, ctxsprint4, "it.unibo.master4.Master4").
