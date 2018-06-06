var graph;

if (graph === undefined || graph === null) {
    window.location.replace("/error")
}
makeChart(graph, "reg");
