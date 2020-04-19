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
- <ins> **Sprint 8:** implementazione definitiva del frontend </ins>
- **Sprint 9:** implementazione "stop" e "reactivate"
- **Sprint 10:** implementazione "avoid"
- **Sprint 11:** revisione dell'intero progetto

## Estensioni future

Oltre ad alcune estensioni legate al codice e alle feature dell'applicazione, ad ora il progetto è realizzato esclusivamente nell'ambiente virtuale, ma abbiamo già predisposto il nostro **robot fisico equipaggiato di raspberry.**
Potrebbe essere perciò necessario avere altri sprint il cui sprint goal sarebbe appunto quello di implementare su robot fisico l'intero progetto.

### Estensioni possibili Sprint8
Noi abbiamo previsto una funzione di controllo della direzione statica basata sulle celle che sappiamo essere quelle attorno al tavolo. Lo sappiamo perché conosciamo le dimensioni della stanza e la posizione del tavolo.
Eventualmente una estensione dinamica potrebbe operare come segue:
1) con una solveTable(L) ottengo la lista delle celle accessibili.
2) ordino le celle in base alla coordinata X e a parità di coord. X, in base alla coord. Y
3) divido le celle in 4 gruppi:
    * un gruppo avrà la stessa x1
    * un gruppo avrà la stessa x2 ma diversa dalla x1
    * un gruppo avrà la stessa y1
    * un gruppo avrà la stessa y2 ma diversa dalla y1
4) a questo punto assegno la direzione:
    * le celle con stessa x1 < x2 avranno direzione destra
    * le celle con stessa x2 > x1 avranno direzione sinistra
    * le celle con stessa y1 < y2 avranno direzione giù
    * le celle con stessa y2 > y1 avranno direzione su

Es. Celle: 41, 51, 62, 63, 44, 54, 32, 33
Gruppo 1: 32, 33 &rarr; stessa x1, direzione destra
Gruppo 2: 62, 63 &rarr; stessa x2, direzione sinistra
Gruppo 3: 41, 51 &rarr; stessa y1, direzione su
Gruppo 4: 44, 54 &rarr; stessa y2, direzione giù

---

Abbiamo inserito un attore “greedy” che mangia un cibo per volta ogni 10 secondi, simulando il vero e proprio buffet. Attualmente greedy dice alla kb che vuole mangiare un determinato cibo: se la kb riesce a fare la solve, lo toglie dal tavolo, altrimenti non succede nulla. Una possibile estensione potrebbe essere la seguente:
+ Greedy dice alla kb che vuole mangiare un determinato cibo
+ Se il cibo c’è, si procede come prima.
+ Se il cibo non c’è, si fa una richiesta al master che appare sul frontend con scritto “Puoi aggiungere questo cibo nel buffet?”, con un popup o qualcosa del genere.
    * Se il maitre non accetta, non succede niente.
    * Se il maitre accetta, si scatena automaticamente l’addfood senza che il maitre debba scrivere il codice nell’input text e prema addFood: basta che prema OK nel popup.
