/* Generated by AN DISI Unibo */ 
package it.unibo.ctxDummyScanningRoom
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sprint1.pl", "sysRules.pl"
	)
}

