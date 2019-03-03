const file = "/api/learningMaterials";
const records = readJson(file);
const recordsStrings = JSON.stringify(records);
document.getElementById("records").innerHTML = recordsStrings;
