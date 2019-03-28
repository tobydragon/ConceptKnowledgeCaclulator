
var courseId, //defined in html
    resourceRecords = readJson("/api/currentResourceLinks/" + courseId),
    conceptList = readJson("/api/conceptList/" + courseId);

//linked to the submit button in the html page
function submit() {
    submitToAPI("/api/connectResources/" + courseId, resourceRecords);
}

//linked to the home button in the html page
function goHome() {
    document.location.replace("/view");
}

//linked to the addResource button in the html page
function addResource(newResourceId, maxKnowledgeEstimate) {
    if (newResourceId === "" || newResourceId === "New Resource ID") {
        window.alert("No Resource ID to Add");
    } else {
        //defined in CheckboxMatrixFunctions.js with other resourceRecords functions
        addResourceToRecords(resourceRecords, newResourceId, maxKnowledgeEstimate);
        //defined in Comm.js
        submitToAPI("/api/connectResources/" + courseId, resourceRecords);
    }
}

document.getElementById("authoringTable").innerHTML += buildTableHtmlString(conceptList, resourceRecords);
