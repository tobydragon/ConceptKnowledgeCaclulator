const materialsFile = "/api/connectMaterials/Cs1Example";
const conceptsFile = "/api/conceptList/Cs1Example";

const materials = readJson(materialsFile);
const concepts = readJson(conceptsFile);

var tagItem;
var content;

var row;
var conceptName;
var box;

var index = 0;
var navText;

$(document).ready(function() {
    updateIndexAndMaterials();
});



for (var i = 0; i < concepts.length; i++){
    row = document.createElement("tr");

    conceptName = document.createElement("td");
    box = document.createElement("td");

    conceptName.appendChild(document.createTextNode(concepts[i]));
    box.insertAdjacentHTML('afterbegin',"<input type='checkbox'>");

    row.appendChild(conceptName);
    row.appendChild(box);
    $('table').append(row);
}

function updateIndexAndMaterials(increment){
    if (increment){
        index++;
    } else {
        index--;
    }

    if (index < 0){
        index = 0;
    }

    if (index >= materials.length){
        index = materials.length - 1;
    }

    updateMaterials();
    updateNav();

}

function updateMaterials(){

    $('.learningMaterialID').text(materials[index].id);
    $('.learningMaterialContent').text(materials[index].content);
    $('ul.suggestedTags').empty();

    var tags = Object.keys(materials[index].tagsMap);

    for (var i = 0; i < tags.length; i++){
        tagItem = document.createElement("li");
        tagItem.appendChild(document.createTextNode(tags[i]));
        $('ul.suggestedTags').append(tagItem);
    }
}

function updateNav(){
    navText = (index+1).toString();
    navText = navText.concat("/");
    navText = navText.concat(materials.length);
    $('#index').text(navText);
}



