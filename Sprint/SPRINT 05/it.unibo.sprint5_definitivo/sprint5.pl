%====================================================================================
% sprint5 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxsprint5, "localhost",  "MQTT", "0").
context(ctxdummyscanningroom, "localhost",  "MQTT", "0").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
context(ctxdummyprepare, "localhost",  "MQTT", "0").
context(ctxdummyclear, "localhost",  "MQTT", "0").
context(ctxdummyparty, "localhost",  "MQTT", "0").
 qactor( roomperimeterexplorer, ctxdummyscanningroom, "external").
  qactor( findtable, ctxdummyscanningroom, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( prepare, ctxdummyprepare, "external").
  qactor( clear, ctxdummyclear, "external").
  qactor( addfood, ctxdummyparty, "external").
  qactor( master5, ctxsprint5, "it.unibo.master5.Master5").
