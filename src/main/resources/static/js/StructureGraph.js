//defined in StructureGraph.html
var courseId;
var graph = readJson("/api/structureTree/" + courseId);

if (graph === undefined || graph === null) {
    window.location.replace("/error")
}
makeChart(graph, "reg");
