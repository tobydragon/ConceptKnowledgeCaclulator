var materials = readJson("/api/connectMaterials/" + courseId);
var resourceRecords = readJson("/api/currentResourceLinks/" + courseId);

$(document).ready(function(){
    document.getElementById("recordsList").insertAdjacentHTML("beforebegin", "<h3>CourseID: " + courseId + "</h3>")
    document.getElementById("recordsList").innerHTML = createListOfLearningRecordsString(materials, resourceRecords);
    $("#index").text(loadNavString(materials.length));
});


function loadNavString(numOfLearningMaterials){
    var index = "0/";
    return index.concat(numOfLearningMaterials);
}

function createListOfLearningRecordsString(materials, resourceRecords) {

    var typeString = "";

    for (var i = 0; i < materials.length; i++) {
        if (isMaterialLinked(materials[i].id, resourceRecords)) {
            typeString += "<li><a class=\"linked\" href=\"/view/connectMaterials/";
        } else {
            typeString += "<li><a href=\"/view/connectMaterials/";
        }

        typeString += courseId;
        typeString += "/" + i + "\">";
        typeString += materials[i].id;
        typeString += " - Linked</a></li>";
    }

    return typeString;
}

function isMaterialLinked(materialID, records) {
    for (var i = 0; i < records.length; i++){
        if (materialID === records[i].learningResourceId){
            return true;
        }
    }

    return false;
}