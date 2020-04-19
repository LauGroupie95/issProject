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
- <ins>**Sprint 5:** implementazione "add food"</ins> 
- **Sprint 6:** implementazione "consult"
- **Sprint 7:** implementazione parziale del frontend
- **Sprint 8:** implementazione definitiva del frontend
- **Sprint 9:** implementazione "stop" e "reactivate"
- **Sprint 10:** implementazione "avoid"
- **Sprint 11:** revisione dell'intero progetto

## Note importanti
Da requisiti, il maître è dentro la stanza e può dare al robot tre comandi: uno di questi è **ADD FOOD**. A seguito di questo comando, parte il relativo task in cui il robot deve aggiungere un cibo al tavolo, in base al codice fornito. 

Nello scenario generale:
-	Al tempo T0, il maître presispone gli item prefissati.
-	Il maître invia PREPARE e attende la fine del task.
- Durante la festa aggiunge cibo se è disponibile nel frigo.
-	Il maître invia CLEAR e aspetta la fine del task. Poi RBR torna a casa.

Con il comando “add food”:
- Caso 1: il cibo c’è, allora il robot va al frigo, preleva il cibo in base al suo codice e lo mette in tavola
- Caso 2: il cibo non c’è, allora il robot invia un warning al maître
    * Motivo della mancanza: il cibo è finito
    * Motivo della mancanza: il cibo non era previsto nel set di item prefissato

Il maître sa in ogni momento quali cibi sono presenti all'interno del frigo, ma nonostante questo può inviare un comando al robot per delegare a lui la fase di controllo.

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.
