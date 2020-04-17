# Sprint

L'organizzazione agile dettata dal metodo SCRUM è visibile nel Product Backlog presente in "/Documenti/**Sprint e Product Backlog.xlsx"**
che consigliamo di visionare in quanto contiene dettagliatamente quanto è stato fatto durante i vari sprint.

Di seguito riassumiamo per ogni sprint il relativo Sprint Goal:
- **Sprint preliminare:** analisi dei requisiti
- **Sprint 0:** muovere il robot in una determinata cella senza l'uso del planner
- **Sprint 1:** scansionare la stanza e individuare il tavolo utilizzando il planner
- <ins>**Sprint 2:** implementazione di prepare</ins> 
- **Sprint 3:** implementazione di clear 
- **Sprint 4:** frigo intelligente
- **Sprint 5:** implementazione "add food"
- **Sprint 6:** implementazione "consult"
- **Sprint 7:** implementazione parziale del frontend
- **Sprint 8:** implementazione definitiva del frontend
- **Sprint 9:** implementazione "stop" e "reactivate"
- **Sprint 10:** implementazione "avoid"
- **Sprint 11:** revisione dell'intero progetto

## Test
Per provare il test:
1) Installare npm tramite **installWebApplications.bat**
2) Far partire l'ambiente virtuale tramite **startWebApplications.bat**
3) Aprire il progetto in Eclipse
4) Far partire all'interno del progetto i seguenti file, nell'ordine:
    * src/it.unibo.ctxScanningRoom/**MainCtxScanningRoom.kt**
    * src/it.unibo.ctxNavigator/**MainCtxNavigator.kt**
    * src/it.unibo.ctxSprint2/**MainCtxSprint2.kt**
5) Una volta terminata l'esecuzione, facendo refresh, si vedranno i due file di log generati, necessari per il test.
6) Fare "Run as Kotlin JUnit Test" del file **src/Test/testSprint0.kt**

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.
