// variable that holds the json of the CohortGraphRecord
var cohortGraphs;

//node object made to hold the root node information.
//used in findRoots
function RootNode(idIn, knowledgeEstimateIn, knowledgeDistFromAvgIn){
    this.id = idIn;
    this.knowledgeEstimate = knowledgeEstimateIn;
    this.knowledgeDistFromAvg = knowledgeDistFromAvgIn;
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
        visualizationList.push(row1);//push row to the list
    }
    
    //iterate through all of the links and add a row for each one
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
            }
        }
        row.push(p); //add parent
        row.push(s); //add score
        visualizationList.push(row); //push row to the list
    }
    
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
        roots.push(new RootNode(graphToCheck.concepts[i].id, graphToCheck.concepts[i].knowledgeEstimate, graphToCheck.concepts[i].knowledgeDistFromAvg));
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
function readJson(fileName){
    var request = new XMLHttpRequest();
    request.open("GET", fileName, false);
    request.send(null)

    return JSON.parse(request.responseText);
}

//Examples of how to make a single Graph
//graph = readJson("../test/testresources/superComplexStructureGraph.json")
//graph = readJson("json/exampleMediumStructureGraph.json");
//graph = readJson("../test/testresources/practicalExamples/basicRealisticExample.json");
//graph = readJson("../test/testresources/practicalExamples/advancedRealisticExample.json");
//graph = readJson("../test/testresources/practicalExamples/blankRealisticExample.json");
//graph = readJson("../test/testresources/practicalExamples/singleStudentRealisticExample.json");

//graph = readJson("../test/testresources/io/simple.json")
//makeChart(graph, "reg")


function findAndMakeChart(name, type){
    var currentGraph = null;
    //iterates through the master array of objects and assigns the matching student object to dataObject
    for(var i = 0; i < cohortGraphs.graphRecords.length; i++){
        if(String(cohortGraphs.graphRecords[i].name) == name){
            console.log("FOUND: "+cohortGraphs.graphRecords[i].name);
            currentGraph = cohortGraphs.graphRecords[i];
        }
    }
    if (currentGraph != null){
        makeChart(currentGraph, type);
    }
}

//creates the student buttons
function writeMenu(){
    var names = []; //list of names of the students

    //creates the list of student names from the master objectsArray
    for(var i = 0; i < cohortGraphs.graphRecords.length; i++){
        names.push(cohortGraphs.graphRecords[i].name);
    }
    //defines a line of HTML to inject into the DOM
    //creates a list of button objects. The button click calls makeChart and passes the argument of the student's name
    //and a term "reg" or "avg" to tell whether the graph should display actualcomp or distfromavg
    var newCode = "<button class='accordion'>Section 1</button><div class='panel'><ul style='list-style: none;'>";
    newCode += "<li><button type='button' onclick='findAndMakeChart(&quot;" + names[0] + "&quot;,&quot;reg&quot;)'>" + names[0] + "</button>";
    for(var i = 1; i < names.length; i++){
        newCode += "<li><button type='button' onclick='findAndMakeChart(&quot;" + names[i] + "&quot;,&quot;reg&quot;)'>" + names[i] + "</button>";
        newCode +="<button type='button' onclick='findAndMakeChart(&quot;" + names[i] + "&quot;,&quot;avg&quot;)'>" + "DistAvg" + "</button></li>";
    }
    newCode += "</ul></div>";
    //insert the HTML code into the div with the ID "menu"
    document.getElementById("menu").innerHTML = newCode;
}

cohortGraphs = readJson("../out/ckcCurrent.json")
console.log(cohortGraphs)
writeMenu()