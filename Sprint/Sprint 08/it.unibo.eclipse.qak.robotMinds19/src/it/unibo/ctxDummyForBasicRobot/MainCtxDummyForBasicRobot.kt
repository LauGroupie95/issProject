/* Generated by AN DISI Unibo */ 
package it.unibo.ctxDummyForBasicRobot
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "otherbasicrobotlhost", this, "robotmind.pl", "sysRules.pl"
	)
}

