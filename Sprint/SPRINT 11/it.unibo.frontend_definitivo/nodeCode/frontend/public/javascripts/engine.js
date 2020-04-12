var socket = io.connect();

//Attendo che il frontend si sia caricato completamente prima di popolare la select 
$(document).ready(function(){
    //Per aiutare nell'inserimento del codice, estensione futura autocompletamento
    var data = '{"c000": "quiches", "c001": "wurstel", "c002": "cocacola", "c003": "chips", "c004": "pizza", "c005": "fanta", "c006": "beers", "c007": "pasta", "c008": "nuggets", "c009": "snacks", "c010": "redwine", "c011": "sprite", "c012": "eggs", "c013": "coffee", "c014": "nutella", "c015": "tigelle", "c00021": "Error food code format", "c040": "Not present"}';
    //Eventualmente effettuare una lettura da file food.json per caricare i dati

    var jsonArray = JSON.parse(data);
    var select = document.getElementById('foodcode-dropdown');
    var option = document.createElement('option');
    option.value = "resetSelect";
    option.text = "";
    select.add(option);

    Object.keys(jsonArray).forEach(function(key) {
        option = document.createElement('option');
        option.value = key;
        option.text = key + " - " + jsonArray[key];
        select.add(option);
    });
});

//Sostituisce due file css
function changeCSS(cssFile, cssLinkIndex) {
    var oldlink = document.getElementsByTagName("link").item(cssLinkIndex);

    var newlink = document.createElement("link");
    newlink.setAttribute("rel", "stylesheet");
    newlink.setAttribute("type", "text/css");
    newlink.setAttribute("href", cssFile);

    document.getElementsByTagName("head").item(0).replaceChild(newlink, oldlink);
}

//Formatta l'output di pantryState e dishwasherState
function formatterStringObjects(str){
    return str.replace("[", "").replace(/,/g,"\n").replace("]", "");
}

//Formatta l'output di tableState e robotState
function formatterStrings(str){
    //Se "%" è ultimo, lista di oggetti; altrimenti se "%" è primo, lista di cibi; altrimenti lista di oggetti e cibi
    var strFormatted = "";
    if(str.slice(-1) === "%"){
        strFormatted = str.replace("%","").replace(/,/g,"\n");
    } else if (str.charAt(0) === "%") {
        strFormatted = str.replace("%","").replace(/-/g,"\n").replace(/,/g,"\t\t");
    } else {
        if(str.includes("%")){
            var lists = str.split("%");
            var listObjFormatted = lists[0].replace(/,/g,"\n");
            var listFoodFormatted = lists[1].replace(/-/g,"\n").replace(/,/g,"\t\t");
            strFormatted = listObjFormatted + "\n" + listFoodFormatted;
        } else {
            strFormatted = str;
        }
    }
    return strFormatted;
}

//Formatta l'output di fridgeState (ovvero CoAP expose)
function formatterStringExpose(str){
    return str.toString().replace(/-/g,"\n").replace(/,/g,"\t\t");
}

//Controlla il formato del food code
function controlInput(str){
    var patternFoodCode = new RegExp("c[0-9]{3}$");
    if(!patternFoodCode.test(str)){
        console.log("Il codice del cibo deve iniziare con 'c' e contenere 3 numeri");
        document.getElementById('titleModal').innerHTML = "Incorrect food code";
        document.getElementById('bodyModal').innerHTML = "The food code must start with 'c' and must contain 3 numbers";
        $('#modal').modal('show');
        return false;
    }
    return true;
}

//Gestione dell'evento onClick del bottone prepare
function onClickPrepare(){
    document.getElementById('stopbutton').disabled = false;
    document.getElementById('preparebutton').disabled = true;
    document.getElementById('prepareform').submit();
}

//Gestione dell'evento onClick del bottone request
function onClickAnswer(foodCode){
    controlInput(foodCode);
}

//Gestione dell'evento onClick del bottone addfood
function onClickAddFood(foodCode){
    if(controlInput(foodCode)){
        document.getElementById('stopbutton').disabled = false;
        document.getElementById('addfoodbutton').disabled = true;
        document.getElementById('clearbutton').disabled = true;
        document.getElementById('foodcodeform').submit();
    }
    document.getElementById('foodcode-dropdown').value = "";
}
            
//Gestione dell'evento onClick del bottone clear
function onClickClear(){
    document.getElementById('stopbutton').disabled = false;
    document.getElementById('addfoodbutton').disabled = true;
    document.getElementById('clearbutton').disabled = true;
    document.getElementById('clearform').submit();
}

//Gestione dell'evento onClick del bottone stop
function onCliskStop(){
    document.getElementById('reactivatebutton').disabled = false;				
    document.getElementById('stopbutton').disabled = true;
    changeCSS('stylesheets/dualStyle.css', 1);
    $('#title').text("RBR CONSOLE - STOPPED");
    document.getElementById('stopform').submit();
}

//Gestione dell'evento onClick del bottone reactivate
function onClickReactivate(){
    document.getElementById('stopbutton').disabled = false;
    document.getElementById('reactivatebutton').disabled = true;
    changeCSS('stylesheets/style.css', 1);
    $('#title').text("RBR CONSOLE");
    document.getElementById('reactivateform').submit();
}

//Alla ricezione di systemStarted
function systemStartedReceived(){
    document.getElementById('preparebutton').disabled = false;
    document.getElementById('answerbutton').disabled = false;		
}

//Alla ricezione di endPrepare, foodAdded e warning
function onParty(){
    document.getElementById('addfoodbutton').disabled = false;
    document.getElementById('clearbutton').disabled = false;
    document.getElementById('stopbutton').disabled = true;				
}

//Alla ricezione di endClear
function endClearReceived(){
    document.getElementById('stopbutton').disabled = true;				
}

//Canale 'connect'
socket.on('connect', function(){ 
    console.log("socket connected");
});

//Canale 'message'
socket.on('message', function(v){ 
       console.log("RECEIVED " + v);
    if(v.indexOf("robotPosition:") >= 0){
        //robotPosition: pos([${getPosX()},${getPosY()}],${getDirection()}]
        var sep = v.indexOf("(");
        var msgStr = v.substr(sep+1);
        document.getElementById('robotPosition').innerHTML = msgStr;
    }
    else if(v.indexOf("currentTask:") >= 0){
        //currentTask: task(Executing the command PREPARE
        var sep = v.indexOf("(");
        var msgStr = v.substr(sep+1);
        document.getElementById('currentTask').innerHTML = msgStr;
    }
    else if(v.indexOf("goal:") >= 0){
        //goal: goal(PANTRY | (5, 0)
        var sep = v.indexOf("(");
        var msgStr = v.substr(sep+1);
        document.getElementById('currentGoal').innerHTML = msgStr; 
    }
    else if(v.indexOf("taskCompleted") >= 0){
        //systemStarted: taskCompleted(systemStarted)
        var sep = v.indexOf(":");
        var msgStr = v.substr(0, sep);

        if(msgStr === "systemStarted"){ 
            systemStartedReceived();
        } else if(msgStr === "endPrepare" || msgStr === "warning" || msgStr === "foodAdded"){
            if(msgStr === "warning"){
                document.getElementById('titleModal').innerHTML = "WARNING";
                document.getElementById('bodyModal').innerHTML = "The fridge doesn't contain this food code";
                $('#modal').modal('show');
            }
            document.getElementById('foodcode-dropdown').selectedIndex = 0;
            onParty();
        } else if(msgStr === "endClear"){
            endClearReceived();
        }
        
        document.getElementById('currentTask').innerHTML = "Waiting for the next command";
        document.getElementById('currentGoal').innerHTML = "";
    }
});

//Canale 'pantryState'
socket.on('pantryState', function(v){
    document.getElementById('textareaOutputPantry').value = formatterStringObjects(v);
});

//Canale 'fridgeState'
socket.on('fridgeState', function(v){
    document.getElementById('textareaOutputFridge').value = formatterStringExpose(v);
});

//Canale 'dishwasherState'
socket.on('dishwasherState', function(v){
    document.getElementById('textareaOutputDishWasher').value = formatterStringObjects(v);
});

//Canale 'robotState'
socket.on('robotState', function(v){
    document.getElementById('textareaOutputRobot').value = formatterStrings(v); 
});

//Canale 'tableState'
socket.on('tableState', function(v){
    document.getElementById('textareaOutputTable').value = formatterStrings(v);
});

//Canale 'request'
socket.on('request', function(v){
    document.getElementById('titleModal').innerHTML = "Response request";
    document.getElementById('bodyModal').innerHTML = v === "yes" ? "The requested food is present in the fridge" : "The requested food is not present in the fridge" ;
    $('#modal').modal('show');
    document.getElementById('foodcode-dropdown').selectedIndex = 0;
});