//reads in JSON file and parses it to objectsArray
function readJson(url){
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);

}

function submitToAPI(url, objectToSubmit) {
    var request = new XMLHttpRequest();
    request.open("POST", url);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(objectToSubmit));
    request.onreadystatechange = function() {
        if (request.status === 200) {
            window.alert("Submission Successful!");
            window.location.reload(true);
        } else {
            window.location.replace("/error");
        }
    };
}