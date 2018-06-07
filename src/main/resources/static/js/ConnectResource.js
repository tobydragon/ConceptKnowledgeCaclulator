//These 2 vars defined in ConnectResources.html
var conceptList;
var blankRecords;

var resourceNames; //List of Resource Names for

function getResourceNames() {
    resourceNames = [];
    for (var i=0 ; i<blankRecords.length; i++) {
        resourceNames.push(blankRecords[i].learningResourceId)
    }
}

function writeToJson(event) {
    var id = event.id;
    var concept = id.split("_")[0];
    var resource = id.split("_")[1];
    var toChange;
    for (var i = 0; i < blankRecords.length; i++) {
        if (blankRecords[i].learningResourceId === resource) {
            toChange = blankRecords[i].conceptIds;
            if (toChange.includes(concept)) {
                toChange.pop(concept);
            } else {
                toChange.push(concept);
            }
        }
    }
    displayBlankLRRecords();
}

//TODO: Write this tomorrow
function submit() {

}

function buildTable() {
    //Build the top row with Resource Names
    var tableHTML = "<thead><tr><th class='col-xs-1' scope='col'></th>";
    for (var i = 0; i < resourceNames.length; i++) {
        tableHTML += "<th class='col-xs-1' scope='col'> " + resourceNames[i] + " </th>";
    }
    tableHTML += "</tr></thead>";
    document.getElementById("authoringTable").innerHTML += tableHTML;

    //Build the rest of the rows that interact with the Concept Names
    tableHTML = "<tbody>";
    for (var r = 0; r < conceptList.length; r++) {
        tableHTML += "<tr><th scope='row'>" + conceptList[r] + "</th>";
        for (var c = 0; c < resourceNames.length; c++) {
            tableHTML += "<td class='text-center'><input id='" + conceptList[r] + "_" + resourceNames[c] + "' type='checkbox' onclick='writeToJson(this)'></td>";
        }
        tableHTML += "</tr>";
    }
    tableHTML += "</tbody>";
    document.getElementById("authoringTable").innerHTML += tableHTML;

    var submitButton = "<br><br><button class='center-block btn btn-primary' type='submit' onclick='submit()'> Submit </button>";
    document.getElementById("authoring").innerHTML += submitButton;
}

if (conceptList === undefined || conceptList === null || blankRecords === undefined || blankRecords === null) {
    window.location.replace("/error");
}
getResourceNames();
buildTable();