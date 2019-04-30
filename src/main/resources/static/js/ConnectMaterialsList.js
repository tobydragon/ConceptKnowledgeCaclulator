var materials = readJson("/api/connectMaterials/" + courseId);
var resourceRecords = readJson("/api/currentResourceLinks/" + courseId);

$(document).ready(function(){
    createLearningRecordsFromMaterials(materials);
    document.getElementById("recordsList").insertAdjacentHTML("beforebegin", "<h3>CourseID: " + courseId + "</h3>")
    document.getElementById("recordsList").innerHTML = createListOfLearningRecordsString(materials, resourceRecords);
    $("#index").text(loadNavString(materials.length));
});

function createLearningRecordsFromMaterials(materials){
    for (var i = 0; i < materials.length; i++) {
        if (isMaterialLinkedWithoutConceptIDs(materials[i].id, resourceRecords)){
            addResourceToRecords(resourceRecords,materials[i].id, 0);
        }
    }
}


function loadNavString(numOfLearningMaterials){
    var index = "0/";
    return index.concat(numOfLearningMaterials);
}

function createListOfLearningRecordsString(materials, resourceRecords) {

    var typeString = "";

    for (var i = 0; i < materials.length; i++) {
        if (isMaterialLinked(materials[i].id, resourceRecords)) {
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
        if (records[i].conceptIds === undefined || records[i].conceptIds.length == 0){
            return false;
        } else if (materialID === records[i].learningResourceId) {
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