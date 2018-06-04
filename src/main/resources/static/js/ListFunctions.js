var conceptList;
var blankRecords;

//reads in JSON file and parses it to objectsArray
function readJson(url){
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

function displayConcepts() {
    var conceptTableCode = "";

    for (var i = 0; i < conceptList.length; i++) {
        conceptTableCode += "<tr><td>" + conceptList[i] + "</td></tr>";
    }
    //insert table into html section for the concept list
    document.getElementById("conceptTable").innerHTML += conceptTableCode;
}

function displayBlankLRRecords() {
    for (var i = 0; i < blankRecords.length; i++) {
        document.getElementById("json").innerHTML += JSON.stringify(blankRecords[i], null, 2);
    }
}

conceptList = readJson("/api/conceptList");
blankRecords = readJson("/api/blankLRRecords");
console.log(blankRecords);
displayConcepts();
displayBlankLRRecords();
