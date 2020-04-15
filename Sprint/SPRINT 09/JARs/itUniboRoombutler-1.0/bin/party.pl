%====================================================================================
% party description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxparty, "localhost",  "MQTT", "0").
context(ctxdummyfornavigator, "localhost",  "MQTT", "0").
context(ctxdummyforfridge, "localhost",  "MQTT", "0").
context(ctxdummyforkb, "localhost",  "MQTT", "0").
context(ctxdummyforroombutler, "localhost",  "MQTT", "0").
 qactor( fridge, ctxdummyforfridge, "external").
  qactor( navi, ctxdummyfornavigator, "external").
  qactor( kb, ctxdummyforkb, "external").
  qactor( rbr, ctxdummyforroombutler, "external").
  qactor( prepare, ctxparty, "it.unibo.prepare.Prepare").
  qactor( addfood, ctxparty, "it.unibo.addfood.Addfood").
  qactor( clear, ctxparty, "it.unibo.clear.Clear").
