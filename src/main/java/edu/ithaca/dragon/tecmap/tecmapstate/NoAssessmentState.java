package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.TreeConverter;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoAssessmentState {

    protected ConceptGraph graph;

    public NoAssessmentState(String structureFilename) throws IOException {
        graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(structureFilename));
    }

    public ConceptGraphRecord createStructureTree() {
        return TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
    }

    public List<String> createConceptIdListToPrint() {
        Collection<String> ids =  graph.getAllNodeIds();
        return new ArrayList<>(ids);
    }

}
