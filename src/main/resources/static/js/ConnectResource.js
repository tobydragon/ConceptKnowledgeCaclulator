//These 2 vars defined in ConnectResources.html
var courseId;
var conceptList = readJson("/api/conceptList/" + courseId);
var resourceRecords = readJson("/api/blankLRRecords/" + courseId);

var resourceNames; //List of Resource Names for

function getResourceNames() {
    resourceNames = [];
    for (var i=0 ; i<resourceRecords.length; i++) {
        resourceNames.push(resourceRecords[i].learningResourceId)
    }
}

//CONCEPT CANNOT INCLUDE THE QUOTES, FORCED TO BE TRUE IN ID BUILT IN buildTable()
function writeToJson(event) {
    var id = event.id;
    var concept = id.split("_")[0];
    var resource = id.split("_")[1];
    var toChange;
    for (var i = 0; i < resourceRecords.length; i++) {
        if (resourceRecords[i].learningResourceId === resource) {
            toChange = resourceRecords[i].conceptIds;
            if (toChange.includes(concept)) {
                toChange.pop(concept);
            } else {
                toChange.push(concept);
            }
        }
    }
}

function submit() {
    submitToAPI("/api/connectResources/" + courseId);
}

function submitToAPI(url) {
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(resourceRecords));
    request.onreadystatechange = function() {
        if (request.status === 200) {
            window.alert("Submission Successful!");
            window.location.replace("/view");
        } else {
            window.location.replace("/error");
        }
    };
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
            var concept = conceptList[r].split("\"");
            console.log(concept);
            tableHTML += "<td class='text-center'><input id='" + concept[1] + "_" + resourceNames[c] + "' type='checkbox' onclick='writeToJson(this)'></td>";
        }
        tableHTML += "</tr>";
    }
    tableHTML += "</tbody>";
    document.getElementById("authoringTable").innerHTML += tableHTML;
}

if (conceptList === undefined || conceptList === null || resourceRecords === undefined || resourceRecords === null) {
    window.location.replace("/error");
}
console.log(courseId);
getResourceNames();
console.log(resourceRecords);
console.log(resourceNames);
console.log(conceptList);
buildTable();