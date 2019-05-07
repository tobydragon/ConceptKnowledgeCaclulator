var materials = readJson("/api/connectMaterials/" + courseId);
var resourceRecords = readJson("/api/currentResourceLinks/" + courseId);

$(document).ready(function(){
    createLearningRecordsFromMaterials(materials);

    if (submitToAPINoAlert("/api/connectResources/" + courseId, resourceRecords)){
        window.alert("Saved successfully");
    } else {
        //window.alert("There was an error saving the file");
    }

    document.getElementById("recordsList").insertAdjacentHTML("beforebegin", "<h3>CourseID: " + courseId + "</h3>")
    document.getElementById("recordsList").innerHTML = createListOfLearningRecordsString(materials, resourceRecords);
    $("#index").text(loadNavString(materials.length));
});

function createLearningRecordsFromMaterials(materials){

    var conceptIDs = [];

    for (var i = 0; i < resourceRecords.length; i++){
        conceptIDs.push(resourceRecords[i].learningResourceId);
    }

    for (i = 0; i < materials.length; i++) {
        if (!conceptIDs.includes(materials[i].id)){
            addResourceToRecords(resourceRecords,materials[i].id, 0);
        }
    }
}


function loadNavString(numOfLearningMaterials){
    var index = "0/";
    return index.concat(numOfLearningMaterials);
}

function createListOfLearningRecordsString(materials, records) {

    var typeString = "";

    for (var i = 0; i < materials.length; i++) {
        if (isMaterialLinked(materials[i].id, records)) {
            typeString += "<li><a class=\"linked\" href=\"/view/connectMaterials/";
            typeString += courseId;
            typeString += "/" + i + "\">";
            typeString += materials[i].id;
            typeString += " - Linked</a></li>";
        } else {
            typeString += "<li><a href=\"/view/connectMaterials/";
            typeString += courseId;
            typeString += "/" + i + "\">";
            typeString += materials[i].id;
            typeString += "</a></li>";
        }

    }

    return typeString;
}

function isMaterialLinked(materialID, records) {
    for (var i = 0; i < records.length; i++){
        if (records[i].conceptIds !== undefined && records[i].conceptIds.length !== 0 && materialID === records[i].learningResourceId) {
            return true;
        }
    }
    return false;
}

function isMaterialLinkedWithoutConceptIDs(materialID, records) {
    for (var i = 0; i < records.length; i++){
         if (materialID === records[i].learningResourceId && (records[i].conceptIds === undefined || records[i].conceptIds.length == 0)) {
            return true;
        }
    }
    return false;
}