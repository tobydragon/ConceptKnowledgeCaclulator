const file = "/api/learningMaterials";
const records = readJson(file);
let newheader;
let newcontent

for (let i = 0; i < records.length; i++) {
    newheader = document.createElement("h1");
    newcontent = document.createTextNode(JSON.stringify(records[i]));
    newheader.appendChild(newcontent);
    document.body.appendChild(newheader);
}

