/* Generated by AN DISI Unibo */ 
package it.unibo.ctxDummyForRoomButler
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sprint6.pl", "sysRules.pl"
	)
}

