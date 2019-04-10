const materialsFile = "/api/connectMaterials/Cs1Example";
const conceptsFile = "/api/conceptList/Cs1Example";

const materials = readJson(materialsFile);
const concepts = readJson(conceptsFile);

var tagItem;
var content;
var tags = Object.keys(materials[0].tagsMap);

$('.learningMaterialID').text(materials[0].id);
$('.learningMaterialContent').text(materials[0].content);

for (var i = 0; i < tags.length; i++){
    tagItem = document.createElement("li");
    tagItem.appendChild(document.createTextNode(tags[i]));
    $('ul.suggestedTags').append(tagItem);
}

console.log(tags.length);




