# Sprint

L'organizzazione agile dettata dal metodo SCRUM è visibile nel Product Backlog presente in "/Documenti/**Sprint e Product Backlog.xlsx"**
che consigliamo di visionare in quanto contiene dettagliatamente quanto è stato fatto durante i vari sprint.

Di seguito riassumiamo per ogni sprint il relativo Sprint Goal:
- **Sprint preliminare:** analisi dei requisiti
- **Sprint 0:** muovere il robot in una determinata cella senza l'uso del planner  &rarr; contiene un JUnit Test
- **Sprint 1:** scansionare la stanza e individuare il tavolo utilizzando il planner
- **Sprint 2:** implementazione di prepare  &rarr; contiene un JUnit Test
- <ins>**Sprint 3:** implementazione di clear </ins> 
- **Sprint 4:** frigo intelligente
- **Sprint 5:** implementazione "add food"
- **Sprint 6:** implementazione "consult"
- **Sprint 7:** implementazione parziale del frontend
- **Sprint 8:** implementazione definitiva del frontend
- **Sprint 9:** implementazione "stop" e "reactivate"
- **Sprint 10:** implementazione "avoid"
- **Sprint 11:** revisione dell'intero progetto

## Note importanti
Da requisiti, il maître è dentro la stanza e può dare al robot tre comandi: uno di questi è **CLEAR**. A seguito di questo comando, parte il relativo task in cui il robot deve rimettere il cibo non mangiato in frigo e i piatti in lavastoviglie. 

Nello scenario generale:
-	al tempo T0, il maître presispone gli item prefissati.
-	Il maître invia PREPARE e attende la fine del task.
-	Il maître invia CLEAR e aspetta la fine del task. Poi RBR torna a casa.

Con il comando “clear”:
* rbr prenderà tutto il cibo rimasto nel table per rimetterlo in fridge.
* metterà tutti gli oggetti nel dishwasher. Al termine, rbr tornerà in RH.

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.

