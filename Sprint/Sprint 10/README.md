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
- **Sprint 9:** implementazione "stop" e "reactivate" 
- <ins>**Sprint 10:** implementazione "avoid"</ins>
- **Sprint 11:** revisione dell'intero progetto

## Note importanti

Dall'analisi dei requisiti si deduce che il robot deve evitare ostacoli mobili, come umani o animali presenti nella stanza.
In seguito ad un chiarimento con il product owner è emerso che il maître è un ostacolo sempre presente fisicamente nella stanza, ma finché la festa non è iniziata, non è di intralcio e non ci sono altri ostacoli mobili.
Non appena l'evento ha inizio entrano in gioco gli ospiti che rappresentano per il robot ostacoli mobili da evitare.

Nell'analisi del problema, escludendo la fase di esplorazione della stanza nel caso in cui non sia nota, appare evidente che gli ostacoli possono essere incontrati se e solo se il robot sta compiendo un passo in avanti.

La strategia adottata per evitare gli ostacoli mobili in fase di progettazione si delinea come segue: se il robot va verso un ostacolo, quando è troppo vicino ad esso e ha superato una certa soglia di distanza, si ferma e aspetta. Dopo aver aspettato un po’, ci riprova perché nel frattempo l’ostacolo potrebbe essersi spostato. Quest'ultima azione è ripetuta a intervalli regolari.

Siccome nell'ambiente virtuale l'unico modo per gestire tale soglia è quando questa è così piccola da coincidere con la collisione, la strategia è implementata seguendo la stessa logica ma il meccanismo ciclico di attesa e tentativo di avanzamento scatta solo in seguito all'urto del robot contro l'ostacolo che ha incontrato sul suo cammino.   

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.
