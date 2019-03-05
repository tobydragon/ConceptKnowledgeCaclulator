const file = "/api/learningMaterials";
const records = readJson(file);
const recordsList = document.getElementById("recordsList");
let listItem;
let content
let tags;

for (let i = 0; i < records.length; i++) {
    listItem = document.createElement("li");
    content = document.createTextNode(records[i].content);
    // tags = document.createTextNode(Object.keys(records[i].tagsMap));
    listItem.appendChild(content);
    // newheader.appendChild(document.createTextNode(" "));
    // newheader.appendChild(tags);
    recordsList.appendChild(listItem);
}

