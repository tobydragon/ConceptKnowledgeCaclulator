var courseId,
    materials = readJson("/api/connectMaterials/" + courseId),
    concepts = readJson("/api/conceptList/" + courseId),
    resourceRecords = readJson("/api/currentResourceLinks/" + courseId),
    resourceRecord; //needs to be defined in the global scope of this file so that the html has access to it, otherwise it will live inside the scope of the function


//index gets set to path variable indicating which learning material to load
var index = materialIndex;

$(document).ready(function() {

    updateMaterial(index);

});

function updateMaterial(index){
     resourceRecord = updateResourceRecordFromMaterial(resourceRecords, materials[index].id);
    if (materials[index].url !== ""){
        document.getElementById("learningMaterialInfo").innerHTML = updateMaterialsWithURLString(materials[index].id, materials[index].content, materials[index].tagsMap, materials[index].url);
    } else {
        document.getElementById("learningMaterialInfo").innerHTML = updateMaterialsString(materials[index].id, materials[index].content, materials[index].tagsMap);
    }

    document.getElementById("conceptList").innerHTML = updateConceptsString(concepts, resourceRecord);
    $('#index').text(updateNavString(index + 1, materials.length));
}


function nextMaterial(increment){
    index = updateIndex(increment, index, materials.length - 1);
    updateMaterial(index);
}

//Gets the current resource record for the LearningMaterial
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
            typeString += "</td><td><input type='checkbox' checked='true' ";
            typeString += "onclick='updateConceptId(resourceRecords, \"" + resourceRecord.learningResourceId + "\", \"" + concepts[i] + "\")\'></td></tr>";
        } else {
            typeString += "</td><td><input type='checkbox' ";
            typeString += "onclick='updateConceptId(resourceRecords, \"" + resourceRecord.learningResourceId + "\", \"" + concepts[i] + "\")\'></td></tr>";
        }

    }

    return typeString;
}

function suggestWithMaterialTags(){
    document.getElementById("conceptList").innerHTML = updateConceptsStringWithSuggestedTags(concepts, resourceRecord);
}

function updateConceptsStringWithSuggestedTags(concepts, resourceRecord){

    var typeString = "";

    for (var i = 0; i < concepts.length; i++){
        var tags = Object.keys(materials[index].tagsMap);
        typeString += "<tr><td>";
        typeString += concepts[i];
        if (createResourceCheckedForConcept(concepts[i], resourceRecord) || createResourceCheckedFromMaterials(concepts[i], tags)){
            updateConceptId(resourceRecords, resourceRecord.learningResourceId, concepts[i]);
            typeString += "</td><td><input type='checkbox' checked='true' ";
            typeString += "onclick='updateConceptId(resourceRecords, \"" + resourceRecord.learningResourceId + "\", \"" + concepts[i] + "\")\'></td></tr>";
        } else {
            typeString += "</td><td><input type='checkbox' ";
            typeString += "onclick='updateConceptId(resourceRecords, \"" + resourceRecord.learningResourceId + "\", \"" + concepts[i] + "\")\'></td></tr>";
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

function createResourceCheckedFromMaterials(concept, tags){
    for (var i = 0; i < tags.length; i++){
        if (tags[i].toLowerCase().includes(concept.toLowerCase())){
            return true;
        }
    }

    return false;
}

function submit() {
    if (submitToAPINoAlert("/api/connectResources/" + courseId, resourceRecords)){
        window.alert("Saved successfully");
    } else {
        window.alert("There was an error saving the file");
    }

}

function goBackToList() {
    document.location.replace("/view/connectMaterialsList/" + courseId);
}



