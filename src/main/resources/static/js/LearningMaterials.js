const file = "/api/learningMaterials";
const records = readJson(file);
const recordsList = document.getElementById("recordsList");
const tagsList = document.getElementById("tagsList");
let listItem;
let tagItem;
let content
let tags;

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

