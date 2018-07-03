//defined in html in real page
var courseId = "Cs1Example",
    //defined by calling to the api in real page
    resourceRecords = readJson("../../resources/datastore/Cs1Example/Cs1ExampleResources.json"),
    conceptList = ["While Loops","For Loops","Boolean Expressions","Intro CS", "Loops", "If Statements"],
    tableHtmlString = buildTableHtmlString(conceptList, resourceRecords);

console.log("ConnectResources.tableHtmlString:" + tableHtmlString);
document.getElementById("authoringTable").innerHTML += tableHtmlString;
