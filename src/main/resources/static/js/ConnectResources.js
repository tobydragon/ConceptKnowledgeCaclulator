
var courseId, //defined in html
    resourceRecords = readJson("/api/blankLRRecords/" + courseId),
    conceptList = readJson("/api/conceptList/" + courseId);

function submitToAPI(url, objectToSubmit) {
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(objectToSubmit));
    request.onreadystatechange = function() {
        if (request.status === 200) {
            window.alert("Submission Successful!");
            window.location.replace("/view");
        } else {
            window.location.replace("/error");
        }
    };
}

//linked to the submit button in the html page
function submit() {
    submitToAPI("/api/connectResources/" + courseId, resourceRecords);
}

document.getElementById("authoringTable").innerHTML += buildTableHtmlString(conceptList, resourceRecords);
