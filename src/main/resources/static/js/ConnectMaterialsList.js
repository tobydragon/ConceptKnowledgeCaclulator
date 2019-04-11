var numOfLearningMaterials = learningMaterials.length;

var recordsList = document.getElementById("recordsList");
var nav = $("#index");
var index = "0/";
var listItem;
var anchor;

index = index.concat(numOfLearningMaterials);

nav.text(index);

for (var i = 0; i < numOfLearningMaterials; i++) {
    listItem = document.createElement("li");
    anchor = document.createElement("a");
    anchor.appendChild(document.createTextNode(learningMaterials[i].id));
    listItem.appendChild(anchor);
    recordsList.appendChild(listItem);
}