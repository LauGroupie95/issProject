# Sprint

L'organizzazione agile dettata dal metodo SCRUM è visibile nel Product Backlog presente in "/Documenti/**Sprint e Product Backlog.xlsx"**
che consigliamo di visionare in quanto contiene dettagliatamente quanto è stato fatto durante i vari sprint.

Di seguito riassumiamo per ogni sprint il relativo Sprint Goal:
- **Sprint preliminare:** analisi dei requisiti
- **Sprint 0:** muovere il robot in una determinata cella senza l'uso del planner &rarr; contiene un JUnit Test
- **Sprint 1:** scansionare la stanza e individuare il tavolo utilizzando il planner
- **Sprint 2:** implementazione di prepare &rarr; contiene un JUnit Test
- **Sprint 3:** implementazione di clear
- **Sprint 4:** frigo intelligente
- **Sprint 5:** implementazione "add food"
- **Sprint 6:** implementazione "consult"
- **Sprint 7:** implementazione parziale del frontend 
- **Sprint 8:** implementazione definitiva del frontend 
- <ins> **Sprint 9:** implementazione "stop" e "reactivate" </ins>
- **Sprint 10:** implementazione "avoid"
- **Sprint 11:** revisione dell'intero progetto

## Note importanti

Dall'analisi dei requisiti, risulta che in ogni momento il maître può fermare e riattivare un task. RBR una volta fatto “reactivate” deve ricordarsi quello che stava facendo: memorizzare il suo goal corrente, il suo task e/o la sua destinazione. (Es. stava andando al tavolo per cosa? Per portare cibo? Per sparecchiare?)

Dall'analisi del problema si evince che quanto dichiarato nella frase precedente potrebbe non essere necessario se si fa uso del planner intelligente fornitoci dal customer, infatti durante la progettazione non c'è stato bisogno di prevedere un'ulteriore estensione della base di conoscenza del robot per implementare la soluzione al requisito richiesto.

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.
