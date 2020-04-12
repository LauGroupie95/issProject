%====================================================================================
% roomperimeter description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxroomperimeter, "localhost",  "MQTT", "0" ).
context(ctxfindtable, "localhost",  "MQTT", "0" ).
context(ctxdummyformind, "localhost",  "MQTT", "0" ).
context(ctxdummyforsprint1, "localhost",  "MQTT", "0" ).
 qactor( resourcemodel, ctxdummyformind, "external").
  qactor( onestepahead, ctxdummyformind, "external").
  qactor( master1, ctxdummyforsprint1, "external").
  qactor( roomperimeterexplorer, ctxroomperimeter, "it.unibo.roomperimeterexplorer.Roomperimeterexplorer").
  qactor( findtable, ctxfindtable, "it.unibo.findtable.Findtable").
