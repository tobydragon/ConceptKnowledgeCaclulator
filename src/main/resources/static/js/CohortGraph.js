// variable that holds the json of the CohortGraphRecord, defined in CohortGraph.html
var courseId;
var cohortGraphs = readJson("/api/cohortTree/" + courseId);
var resourceSuggestions;
var groupSuggestions;

//Currently displays just the concepts, will need to be changed into two tables
function displaySuggestions(name) {
    if (name !== "Average Graph") {
        resourceSuggestions = readJson("/api/suggestResources/" + courseId + "/" + name);
        if (resourceSuggestions.incompleteList.length > 0 || resourceSuggestions.wrongList.length > 0) {
            var suggestionName = "Suggestions for " + name;
            document.getElementById("suggestionName").innerHTML = suggestionName;
            var tableHTML = "<table align='center' border='2' style='text-align: center'><thead>";
            tableHTML += "<tr><th> Concept </th><th> Resource </th></tr>";
            tableHTML += "</thead>";
            tableHTML += "<tbody>"; //reset for table body
            for (var i = 0; i < resourceSuggestions.wrongList.length; i++) {
                var currentSuggestion = resourceSuggestions.wrongList[i];
                tableHTML += "<tr><td>" + currentSuggestion.reasoning + "</td><td>" + currentSuggestion.id + "</td></tr>"
            }
            tableHTML += "</tbody></table>";
            document.getElementById("wrongTable").innerHTML = tableHTML;
            tableHTML = "<table align='center' border='2' style='text-align: center'><thead>";
            tableHTML += "<tr><th> Concept </th><th> Resource </th></tr>";
            tableHTML += "</thead>";
            tableHTML += "<tbody>"; //reset for table body
            for (var i = 0; i < resourceSuggestions.incompleteList.length; i++) {
                var currentSuggestion = resourceSuggestions.incompleteList[i];
                tableHTML += "<tr><td>" + currentSuggestion.reasoning + "</td><td>" + currentSuggestion.id + "</td></tr>"
            }
            tableHTML += "</tbody></table>";
            document.getElementById("incompleteTable").innerHTML = tableHTML;
        } else {
            document.getElementById("wrongTable").innerHTML = "";
            document.getElementById("incompleteTable").innerHTML = "";
            document.getElementById("suggestionName").innerHTML = " Suggestions for " + name;
        }
    } else {
        document.getElementById("wrongTable").innerHTML = "";
        document.getElementById("incompleteTable").innerHTML = "";
        document.getElementById("suggestionName").innerHTML = " Suggestions ";
    }
}

function getGroups(size) {
    document.getElementById("groupTableDiv").innerHTML = "";
    if (size > 0) {
        groupSuggestions = readJson("/api/suggestGroups/" + courseId + "/all/" + size);
        if (groupSuggestions.length > 0) {
            var tableHTML = "<table align='center' border='2' id='groupTable'>";
            tableHTML += "<tr><th> Group # </th><th> Group Members </th> <th> Rationale </th></tr>";
            for (var i = 0; i < groupSuggestions.length; i++) {
                var groupNames = groupSuggestions[i].studentNames;
                tableHTML += "<tr><td> Group " + i + " </td><td>";
                for (var j = 0; j < groupNames.length; j++) {
                    tableHTML += groupNames[j] + " ";
                }
                tableHTML += "</td><td>" + groupSuggestions[i].rationale + "</td></tr>"
            }
            tableHTML += "</table>";
            tableHTML += "<br>";
            document.getElementById("groupTableDiv").innerHTML += tableHTML;
        }
    } else {
        window.alert("Group Size Must Be Greater Than 0");
        document.getElementById("groupSize").value = null;
    }
}

function findAndMakeChart(name, type){
    var currentGraph = null;
    //iterates through the master array of objects and assigns the matching student object to dataObject
    for(var i = 0; i < cohortGraphs.graphRecords.length; i++){
        if(String(cohortGraphs.graphRecords[i].name) == name){
            console.log("FOUND: "+cohortGraphs.graphRecords[i].name);
            currentGraph = cohortGraphs.graphRecords[i];
        }
    }
    if (currentGraph != null){
        makeChart(currentGraph, type);
        displaySuggestions(name);
    }
}

//creates the student buttons
function writeMenu(){
    var names = []; //list of names of the students

    //creates the list of student names from the master objectsArray
    for(var i = 0; i < cohortGraphs.graphRecords.length; i++){
        names.push(cohortGraphs.graphRecords[i].name);
    }
    //defines a line of HTML to inject into the DOM
    //creates a list of button objects. The button click calls makeChart and passes the argument of the student's name
    //and a term "reg" or "avg" to tell whether the graph should display actualcomp or distfromavg
    var newCode = "<button class='accordion'>Section 1</button><div class='panel'><ul style='list-style: none;'>";

    newCode += "<li><button type='button' onclick='findAndMakeChart(&quot;" + names[0] + "&quot;,&quot;reg&quot;)'>" + names[0] + "</button>";
    for(var i = 1; i < names.length; i++){
        newCode += "<li style='float: left'><button type='button' onclick='findAndMakeChart(&quot;" + names[i] + "&quot;,&quot;reg&quot;)'>" + names[i] + "</button>";
        newCode +="<button type='button' onclick='findAndMakeChart(&quot;" + names[i] + "&quot;,&quot;avg&quot;)'>" + "DistAvg" + "</button></li>";
    }
    newCode += "</ul></div>";
    //insert the HTML code into the div with the ID "menu"
    document.getElementById("menu").innerHTML = newCode;
    //make the accordion open when clicked (only one now, loops for each future possible section)
    var acc = document.getElementsByClassName("accordion");
    for (var i = 0; i < acc.length; i++) {
        acc[i].onclick = function(){
            this.classList.toggle("active");
            this.nextElementSibling.classList.toggle("show");
        }
    }
}

//   START OF EXECUTION     //
if (cohortGraphs === undefined || cohortGraphs === null) {
    window.location.replace("/error")
}
document.getElementById("groupSize")
    .addEventListener("keyup", function(event) {
        event.preventDefault();
        if (event.key === "Enter") {
            document.getElementById("groupButton").click();
        }
    });
writeMenu();

