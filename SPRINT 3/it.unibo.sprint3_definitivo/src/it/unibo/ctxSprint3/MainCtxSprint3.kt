/* Generated by AN DISI Unibo */ 
package it.unibo.ctxSprint3
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sprint3.pl", "sysRules.pl"
	)
}

