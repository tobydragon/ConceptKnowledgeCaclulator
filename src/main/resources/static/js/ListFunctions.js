//reads in JSON file and parses it to objectsArray
function readJson(url){
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}