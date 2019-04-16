var materials = readJson("/api/connectMaterials/" + courseId);

$(document).ready(function(){

    document.getElementById("recordsList").innerHTML = createListOfLearningRecordsString(materials);
    $("#index").text(loadNavString(materials.length));
});


function loadNavString(numOfLearningMaterials){
    var index = "0/";
    return index.concat(numOfLearningMaterials);
}

function createListOfLearningRecordsString(learningRecords) {

    var typeString = "";

    for (var i = 0; i < learningRecords.length; i++) {
        typeString += "<li><a>";
        typeString += learningRecords[i].id;
        typeString += "</a></li>";

    }

    return typeString;
}