var courseId = "Cs1Example";
var resourceRecords = readJson("../../resources/datastore/Cs1Example/Cs1ExampleMaterials.json");
var conceptList = ["While Loops","For Loops","Boolean Expressions","Intro CS", "Loops", "If Statements"];

const recordsFile = "../../resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json";

const records = readJson(recordsFile);
const concepts = conceptList;

const recordsList = document.getElementById("recordsList");
const tagsList = document.getElementById("tagsList");
var listItem;
var tagItem;
var content;
var tags;
var color = '%c';

console.log(color.concat(JSON.stringify(concepts)), 'color: blue; font-weight: bold;');

for (var i = 0; i < records.length; i++) {
    listItem = document.createElement("li");
    tagItem = document.createElement("li");

    content = document.createTextNode(records[i].content);
    tags = document.createTextNode(Object.keys(records[i].tagsMap));

    tagItem.appendChild(tags);
    listItem.appendChild(content);

    tagsList.appendChild(tagItem);
    recordsList.appendChild(listItem);

    console.log(color.concat(content.wholeText), 'color: red; font-weight: bold;')
}

console.log(color.concat(JSON.stringify(resourceRecords)), 'color: green; font-weight: bold;');