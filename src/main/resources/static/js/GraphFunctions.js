//node object made to hold the root node information.
//used in findRoots
function RootNode(idIn, knowledgeEstimateIn, knowledgeDistFromAvgIn, resourceSummariesIn, dataImportanceIn){
    this.id = idIn;
    this.knowledgeEstimate = knowledgeEstimateIn;
    this.knowledgeDistFromAvg = knowledgeDistFromAvgIn;
    this.resourceSummaries = resourceSummariesIn;
    this.dataImportance = dataImportanceIn;
}

//takes the name of the student whose org chart should be drawn
//dataobject is the cg
function makeChart(dataObject,typeGraph){

    //initializes visualizationList
    var visualizationList = []; //the formatted list of data
    //clears the "section" HTML
    document.getElementById("section").innerHTML = "";

    //assigns roots of the tree to an array of RootNode objects
    var roots = findRoot(dataObject);
    //iterate through all of the roots and add them to the visualizationList
    for(var i = 0; i < roots.length; i++){
        var row1 = [];//make an empty row
        var c = roots[i].id;
        if(typeGraph == "reg"){
            var s = roots[i].knowledgeEstimate;
            row1.push({v:c, f:stripTitle(c)+'<div style="color:blue; font-style:italic">Score: '+s+'</div>'});//add the topic
        }else{
            var s = roots[i].knowledgeDistFromAvg;
            console.log(s);
            row1.push({v:c, f:stripTitle(c)+'<div style="color:blue; font-style:italic">Distance: '+s+'</div>'});//add the topic
        }
        row1.push(null);//add the parent, null for roots
        row1.push(s);//add the score
        row1.push(convertToSingleString(roots[i].resourceSummaries));
        row1.push(roots[i].dataImportance);
        visualizationList.push(row1);//push row to the list
    }

    //iterate through all of the links and add a row for each child node in the link (since nothing is the child of two things)
    for(var i = 0; i < dataObject.links.length; i++){
        var row = [];                              //make empty row
        var c = dataObject.links[i].child;         //def Topic
        var p = dataObject.links[i].parent;        //def parents
        //search through the list of concepts to find the node that corresponds with the child in the link
        for(var j = 0; j < dataObject.concepts.length; j++){
            //if the node ID matches the name in the link
            if(dataObject.concepts[j].id == dataObject.links[i].child){
                if(typeGraph == "reg"){
                    var s = dataObject.concepts[j].knowledgeEstimate;
                    row.push({v:c, f:stripTitle(c)+'<div style="color:blue; font-style:italic">Score: '+s+'</div>'});
                }else{
                    var s = dataObject.concepts[j].knowledgeDistFromAvg;
                    row.push({v:c, f:stripTitle(c)+'<div style="color:blue; font-style:italic">Distance: '+s+'</div>'});
                }
                var resourceSummaries = dataObject.concepts[j].resourceSummaries;
                var dataImportance = dataObject.concepts[j].dataImportance;
            }
        }
        row.push(p); //add parent
        row.push(s); //add score
        row.push(convertToSingleString(resourceSummaries));
        row.push(dataImportance);
        visualizationList.push(row); //push row to the list
    }
    //console.log(visualizationList);
    //calls drawOrgChart from org.js, passing the formatted double array of data
    drawOrgChart(visualizationList);
}

//returns an array of the IDs of the roots of the tree
//takes in the dataObject informatin of one org chart
//NOTE: The reason that we iterate through the dataList.concepts list instead of just iterating through roots is because we cannot iterate through it and remove things from it at the same time.
function findRoot(graphToCheck){
    //initialize roots
    var roots = [];
    //add all of the node IDs to the roots array
    for(var i = 0; i < graphToCheck.concepts.length; i++){
        roots.push(new RootNode(graphToCheck.concepts[i].id, graphToCheck.concepts[i].knowledgeEstimate, graphToCheck.concepts[i].knowledgeDistFromAvg, graphToCheck.concepts[i].resourceSummaries, graphToCheck.concepts[i].dataImportance));
    }
    //loop through all of the links
    for(var i = 0; i < graphToCheck.links.length; i++){
        //loop through all of the concepts
        for(var j = 0; j < roots.length; j++){
            //current link's child is equal to the current node
            if(graphToCheck.links[i].child == roots[j].id){
                roots.splice(j, 1);
                j = roots.length+1;
            }
        }
    }
    return roots;
}

//returns a string that is everything before the "-" in a string
//takes a string, expecting format "Boolean-3" or "Boolean_Expressions-12"
function stripTitle(title) {
    for (var i = 0; i < title.length; i++) {
        if (title[i] == "-") {
            title = title.slice(0, i);
            return title;
        }
    }
    return title;
}



//reads in JSON file and parses it to objectsArray
function readJson(url){
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    try {
        return JSON.parse(request.response);
    } catch (e) {
        window.location.replace("/error")
    }

}

function convertToSingleString(listOfStrings){
    if (listOfStrings.length > 0) {
        var singleString = listOfStrings[0];
        for (var i = 1; i < listOfStrings.length; i++) {
            singleString += "<br>" + listOfStrings[i];
        }
        return singleString;
    }
    else {
        return "none";
    }
}