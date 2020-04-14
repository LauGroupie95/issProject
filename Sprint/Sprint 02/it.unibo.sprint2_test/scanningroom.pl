%====================================================================================
% scanningroom description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxscanningroom, "localhost",  "MQTT", "0").
context(ctxdummyformind, "localhost",  "MQTT", "0").
context(ctxdummyforsprint2, "localhost",  "MQTT", "0").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
 qactor( resourcemodel, ctxdummyformind, "external").
  qactor( master2, ctxdummyforsprint2, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( roomperimeterexplorer, ctxscanningroom, "it.unibo.roomperimeterexplorer.Roomperimeterexplorer").
  qactor( findtable, ctxscanningroom, "it.unibo.findtable.Findtable").
