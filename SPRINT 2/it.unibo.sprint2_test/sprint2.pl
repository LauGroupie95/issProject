%====================================================================================
% sprint2 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxsprint2, "localhost",  "MQTT", "0").
context(ctxdummyscanningroom, "localhost",  "MQTT", "0").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
 qactor( roomperimeterexplorer, ctxdummyscanningroom, "external").
  qactor( findtable, ctxdummyscanningroom, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( master2, ctxsprint2, "it.unibo.master2.Master2").
  qactor( prepare, ctxsprint2, "it.unibo.prepare.Prepare").
