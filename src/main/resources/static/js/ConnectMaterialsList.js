var numOfLearningMaterials = learningMaterials.length;

const recordsList = document.getElementById("recordsList");
const nav = $("#index");
var index = "0/";
index = index.concat(numOfLearningMaterials);

nav.text(index);


for (var i = 0; i < numOfLearningMaterials; i++) {
    listItem = document.createElement("li");
    content = document.createTextNode(learningMaterials[i].id);
    listItem.appendChild(content);
    recordsList.appendChild(listItem);
}