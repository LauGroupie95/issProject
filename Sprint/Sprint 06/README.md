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
- <ins>**Sprint 6:** implementazione "consult"</ins> 
- **Sprint 7:** implementazione parziale del frontend
- **Sprint 8:** implementazione definitiva del frontend
- **Sprint 9:** implementazione "stop" e "reactivate"
- **Sprint 10:** implementazione "avoid"
- **Sprint 11:** revisione dell'intero progetto

## Note importanti

Questo Sprint lo abbiamo dedicato al tentativo di convogliare risultati eterogenei provenienti da protocolli differenti in un'unica risposta in quanto il frigo espone il suo contenuto tramite CoAP, in contrasto con tutti gli altri attori.

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.

### Estensioni possibili Sprint6
Il RoomButler è stato creato seguendo l'architettura master/slave: il master è una simulazione del frontend che dà gli ordini e lo slave, cioè Room Butler, diventa a sua volta un master che dice agli altri attori cosa fare.
