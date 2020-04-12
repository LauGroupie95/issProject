%====================================================================================
% scanningroom description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxscanningroom, "localhost",  "MQTT", "0").
context(ctxdummyformind, "localhost",  "MQTT", "0").
context(ctxdummyfornavigator, "localhost",  "MQTT", "0").
context(ctxdummyforroombutler, "localhost",  "MQTT", "0").
 qactor( resourcemodel, ctxdummyformind, "external").
  qactor( navi, ctxdummyfornavigator, "external").
  qactor( rbr, ctxdummyforroombutler, "external").
  qactor( roomperimeterexplorer, ctxscanningroom, "it.unibo.roomperimeterexplorer.Roomperimeterexplorer").
  qactor( findtable, ctxscanningroom, "it.unibo.findtable.Findtable").
