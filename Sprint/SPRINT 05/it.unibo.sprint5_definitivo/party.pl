%====================================================================================
% party description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxdummynavigator, "localhost",  "MQTT", "0").
context(ctxparty, "localhost",  "MQTT", "0").
context(ctxdummyforsprint5, "localhost",  "MQTT", "0").
context(ctxdummyfridge, "localhost",  "MQTT", "0").
context(ctxdummykb, "localhost",  "MQTT", "0").
 qactor( fridge, ctxdummyfridge, "external").
  qactor( master5, ctxdummyforsprint5, "external").
  qactor( navi, ctxdummynavigator, "external").
  qactor( kb, ctxdummykb, "external").
  qactor( addfood, ctxparty, "it.unibo.addfood.Addfood").
