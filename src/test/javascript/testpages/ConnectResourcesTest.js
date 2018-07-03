//defined in html in real page
var courseId = "Cs1Example";
//defined by calling to the api in real page
var resourceRecords = readJson("/api/blankLRRecords/" + courseId);
var conceptList = readJson("/api/conceptList/" + courseId);

document.getElementById("authoringTable").innerHTML += buildTableHtmlString(conceptList, resourceRecords);
