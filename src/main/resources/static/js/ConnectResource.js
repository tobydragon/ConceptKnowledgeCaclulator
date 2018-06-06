//Both vars defined in ConnectResources.html
var conceptList;
var blankRecords;

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

if (conceptList === undefined || conceptList === null || blankRecords === undefined || blankRecords === null) {
    window.location.replace("/error");
}
displayConcepts();
displayBlankLRRecords();
