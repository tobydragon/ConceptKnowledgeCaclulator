
var courseId, //defined in html
    resourceRecords = readJson("/api/blankLRRecords/" + courseId),
    conceptList = readJson("/api/conceptList/" + courseId);

//linked to the submit button in the html page
function submit() {
    submitToAPI("/api/connectResources/" + courseId, resourceRecords);
}

//linked to the home button in the html page
function goHome() {
    document.location.replace("/view");
}

document.getElementById("authoringTable").innerHTML += buildTableHtmlString(conceptList, resourceRecords);
