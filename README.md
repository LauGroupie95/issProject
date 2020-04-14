# issProject
Progetto per il corso di Ingegneria dei Sistemi Software M tenuto dal Prof. Antonio Natali.

## Team
I componenti del team sono Lorenzo Castagnini, Laura Gruppioni e Giovanni Zanca.

## Descrizione
L'obiettivo del progetto è quello di realizzare un Buffet Service, dove un maître de salle comanda un robot per preparare la sala prima che cominci l'evento, pulirla al termine dell'evento e aggiungere cibo sul tavolo qualora gli venga richiesto durante lo svolgimento dell'evento stesso. 
Per maggiori dettagli consultare /Documenti/**TFBO19ISS.pdf**.

Informazioni utili:
- Il nostro robot usa un AI planner per calcolare dinamicamente una sequenza di mosse che permettono di raggiungere un obiettivo, in termini spaziali.
- Nel progetto è utilizzato Prolog per implementare e mantenere la Knowledge Base o Base di Conoscenza.
- La maggior parte del progetto è sviluppata usando il Linguaggio DSL QAK (external DSL che genera codice Kotlin) sviluppato dal Prof. Antonio Natali.
- Il robot è virtuale, ma una possibile estensione prevede il suo impiego fisico tramite l'equipaggiamento di un raspberry.
- Si utilizzano MQTT e CoAP come protocolli di comunicazione nel progetto, come descritto nei requisiti del problema.
- L'ambiente virtuale su cui vengono simulati gli spostamenti del robot è sviluppato da Pierfrancesco Soffritti.
- Il frontend è sviluppato in NodeJS e utilizza Express, un server http.
- Come metodo organizzativo agile abbiamo utilizzato SCRUM: tutti i nostri sprint sono disponibili in /Sprint. Abbiamo raggiunto il risultato finale attraverso lo sviluppo incrementale.

## Istruzioni per l'ambiente virtuale
Per lanciare correttamente il nostro sistema virtuale occorre:
1. Clonare la repository
2. Aprire in Eclipse i seguenti due progetti:
    * Sprint/SPRINT 11/**it.unibo.sprint11_definitivo**
    * Sprint/SPRINT 11/**it.unibo.eclipse.qak.robotMinds19**
3. Eseguire da linea di comando "gradle build eclipse" per iniettare le dipendenze nei progetti
4. Installare da linea di comando le dipendenze tramite "npm install" nei seguenti punti del progetto:   
    * /Sprint/SPRINT 11/**it.unibo.robots19/node/WEnv/server**
    * /Sprint/SPRINT 11/**it.unibo.robots19/node/WEnv/WebGLScene** 
5. Ripetere lo stesso procedimento del punto 4 per il frontend: 
Sprint/SPRINT 11/**it.unibo.frontend_definitivo/nodeCode/frontend**
6. Lanciare l'ambiente virtuale ed il frontend con gli indirizzi
    * **localhost:8080** per il frontend
    * **localhost:8090** per l'ambiente virtuale
7. Lanciare dal progetto eclipse i seguenti contesti: 
    * Sprint/SPRINT 11/it.unibo.eclipse.qak.robotMinds19/src/it/unibo/ctxRobotMind/**MainCtxRobotMind.kt**
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxScanningRoom/**MainCtxScanningRoom.kt**
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxKb/**MainCtxKb.kt**
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxFridge/**MainCtxFridge.kt**
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxNavigator/**MainCtxNavigator.kt**
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxParty/**MainCtxParty.kt**
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxGuest/**MainCtxGuest.kt** (facoltativo)
    * Sprint/SPRINT 11/it.unibo.sprint11_definitivo/src/it/unibo/ctxRoomButler/**MainCtxRoomButler.kt** 

**Attenzione:**
- per i punti 4 e 5 si può utilizzare il file "installWebApplications.bat"
- per il punto 6 si può utilizzare il file "startWebApplications.bat"
- per il punto 7 si può utilizzare il file "startQakApplication.bat"

N.B. I plugins QAK da mettere nella cartella Dropins di Eclipse XText e le Librerie sono disponibili qui: https://github.com/anatali/iss2019Lab/tree/master/libs

## Tags
PROLOG, AI, ARTIFICIAL INTELLIGENCE, DSL, QAK, GOOGLE, VIRTUAL ENVIRONMENT, NODEJS, JAVASCRIPT, EXPRESS, MQTT, COAP, IOT, SMART DEVICES
