/* Generated by AN DISI Unibo */ 
package it.unibo.context_room
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sprint0.pl", "sysRules.pl"
	)
}

