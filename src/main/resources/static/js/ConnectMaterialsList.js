$(document).ready(function(){
    document.getElementById("recordsList").innerHTML = createListOfLearningRecordsString(learningMaterials);
    $("#index").text(loadNavString(learningMaterials.length));
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