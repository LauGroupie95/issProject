%====================================================================================
% robotappl description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxrobotappl, "localhost",  "TCP", "8038").
context(ctxdummyformind, "dontcarehost",  "TCP", "8035").
 qactor( basicrobot, ctxdummyformind, "external").
  qactor( resourcemodel, ctxdummyformind, "external").
  qactor( onestepahead, ctxdummyformind, "external").
  qactor( robotmindapplication, ctxrobotappl, "it.unibo.robotmindapplication.Robotmindapplication").
  qactor( g521reader, ctxrobotappl, "it.unibo.g521reader.G521reader").
