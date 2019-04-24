var courseId, //defined in html, concepts should not be hardcoded
    materials = readJson("/api/connectMaterials/" + courseId),
    concepts = readJson("/api/conceptList/" + courseId),
    resourceRecords = readJson("/api/currentResourceLinks/" + courseId);


//index gets set to path variable indicating which learning material to load
var index = materialIndex;

$(document).ready(function() {

    updateMaterial(index);

});

function updateMaterial(index){
    if (materials[index].url !== ""){
        document.getElementById("learningMaterialInfo").innerHTML = updateMaterialsWithURLString(materials[index].id, materials[index].content, materials[index].tagsMap, materials[index].url);
    } else {
        document.getElementById("learningMaterialInfo").innerHTML = updateMaterialsString(materials[index].id, materials[index].content, materials[index].tagsMap);
    }

    document.getElementById("conceptList").innerHTML = updateConceptsString(concepts, updateResourceRecordFromMaterial(resourceRecords, materials[index].id));
    $('#index').text(updateNavString(index + 1, materials.length));
}


function nextMaterial(increment){
    index = updateIndex(increment, index, materials.length - 1);
    updateMaterial(index);
}

function updateResourceRecordFromMaterial(records, materialID){

    for (var i = 0; i < records.length; i++){
        if (records[i].learningResourceId === materialID){
            return records[i];
        }
    }
}

function updateConceptsString(concepts, resourceRecord){

    var typeString = "";

    for (var i = 0; i < concepts.length; i++){
        typeString += "<tr><td>";
        typeString += concepts[i];
        if (createResourceCheckedForConcept(concepts[i], resourceRecord)){
            typeString += "</td><td><input type='checkbox' checked='true'></td></tr>";
        } else {
            typeString += "</td><td><input type='checkbox'></td></tr>";
        }

    }


    return typeString;

}

function updateIndex(increment, index, numberOfMaterials){

    if (increment){
        index++;
    } else {
        index--;
    }

    if (index < 0){
        return 0;
    }

    if (index >= numberOfMaterials){
        return numberOfMaterials;
    }

    return index;

}

function updateMaterialsString(id, content, tags){

    var typeString = "<p class=\"learningMaterialID\">";
    typeString += id;
    typeString += "</p><p class=\"learningMaterialContent\">";
    typeString += content;
    typeString += "</p><p class=\"suggestedTags\">Suggested Tags:</p><ul class=\"suggestedTags\">";

    tags = Object.keys(tags);

    for (var i = 0; i < tags.length; i++){
        typeString += "<li>";
        typeString += tags[i];
        typeString += "</li>";
    }

    return typeString + "</ul>";
}

function updateMaterialsWithURLString(id, content, tags, url){

    var typeString = "<p class=\"learningMaterialID\">";
    typeString += id;
    typeString += "</p><a href=\"";
    typeString += url;
    typeString += "\" target=\"_blank\" class=\"learningMaterialContent\">";
    typeString += content;
    typeString += "</a><p class=\"suggestedTags\">Suggested Tags:</p><ul class=\"suggestedTags\">";

    tags = Object.keys(tags);

    for (var i = 0; i < tags.length; i++){
        typeString += "<li>";
        typeString += tags[i];
        typeString += "</li>";
    }
    return typeString + "</ul>";
}


function updateNavString(index, numberOfMaterials){
    var navText = (index).toString();
    navText = navText.concat("/");
    navText = navText.concat(numberOfMaterials);
    return navText;
}



