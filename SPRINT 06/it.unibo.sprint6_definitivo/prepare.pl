%====================================================================================
% prepare description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxprepare, "localhost",  "MQTT", "0").
context(ctxdummyfornavigator, "localhost",  "MQTT", "0").
context(ctxdummyforkb, "localhost",  "MQTT", "0").
context(ctxdummyforfridge, "localhost",  "MQTT", "0").
context(ctxdummyforroombutler, "localhost",  "MQTT", "0").
 qactor( navi, ctxdummyfornavigator, "external").
  qactor( kb, ctxdummyforkb, "external").
  qactor( fridge, ctxdummyforfridge, "external").
  qactor( rbr, ctxdummyforroombutler, "external").
  qactor( prepare, ctxprepare, "it.unibo.prepare.Prepare").
