%====================================================================================
% roombutler description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxroombutler, "localhost",  "MQTT", "0").
context(ctxdummyforsprint6, "localhost",  "MQTT", "0").
context(ctxdummyforscanningroom, "localhost",  "MQTT", "0").
context(ctxdummyfornavigator, "localhost",  "MQTT", "0").
context(ctxdummyforprepare, "localhost",  "MQTT", "0").
context(ctxdummyforclear, "localhost",  "MQTT", "0").
context(ctxdummyforparty, "localhost",  "MQTT", "0").
 qactor( master6, ctxdummyforsprint6, "external").
  qactor( roomperimeterexplorer, ctxdummyforscanningroom, "external").
  qactor( findtable, ctxdummyforscanningroom, "external").
  qactor( navi, ctxdummyfornavigator, "external").
  qactor( prepare, ctxdummyforprepare, "external").
  qactor( clear, ctxdummyforclear, "external").
  qactor( addfood, ctxdummyforparty, "external").
  qactor( rbr, ctxroombutler, "it.unibo.rbr.Rbr").
