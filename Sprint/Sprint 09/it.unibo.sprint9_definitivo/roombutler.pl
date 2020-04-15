%====================================================================================
% roombutler description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxroombutler, "localhost",  "MQTT", "0").
context(ctxdummyforscanningroom, "localhost",  "MQTT", "0").
context(ctxdummyfornavigator, "localhost",  "MQTT", "0").
context(ctxdummyforparty, "localhost",  "MQTT", "0").
context(ctxdummyforguest, "localhost",  "MQTT", "0").
 qactor( roomperimeterexplorer, ctxdummyforscanningroom, "external").
  qactor( findtable, ctxdummyforscanningroom, "external").
  qactor( navi, ctxdummyfornavigator, "external").
  qactor( prepare, ctxdummyforparty, "external").
  qactor( clear, ctxdummyforparty, "external").
  qactor( addfood, ctxdummyforparty, "external").
  qactor( greedy, ctxdummyforguest, "external").
  qactor( rbr, ctxroombutler, "it.unibo.rbr.Rbr").
