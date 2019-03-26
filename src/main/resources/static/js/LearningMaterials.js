const recordsFile = "/api/learningMaterials/Cs1Example";
const conceptsFile = "/api/conceptList/Cs1Example";

const records = readJson(recordsFile);
const concepts = readJson(conceptsFile);

const recordsList = document.getElementById("recordsList");
const tagsList = document.getElementById("tagsList");
let listItem;
let tagItem;
let content
let tags;
let color = '%c';

console.log(color.concat(JSON.stringify(concepts)), 'color: red; font-weight: bold;');

for (let i = 0; i < records.length; i++) {
    listItem = document.createElement("li");
    tagItem = document.createElement("li");

    content = document.createTextNode(records[i].content);
    tags = document.createTextNode(Object.keys(records[i].tagsMap));

    tagItem.appendChild(tags);
    listItem.appendChild(content);

    tagsList.appendChild(tagItem);
    recordsList.appendChild(listItem);
}

