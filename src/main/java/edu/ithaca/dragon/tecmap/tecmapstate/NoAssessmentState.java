package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.TreeConverter;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoAssessmentState {

    protected ConceptGraph structureGraph;

    public NoAssessmentState(ConceptGraph structureGraph){
        this.structureGraph = structureGraph;
    }

    public NoAssessmentState(String structureFilename) throws IOException {
        structureGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(structureFilename));
    }

    public ConceptGraphRecord createStructureTree() {
        return TreeConverter.makeTreeCopy(structureGraph).buildConceptGraphRecord();
    }

    public List<String> createConceptIdListToPrint() {
        Collection<String> ids =  structureGraph.getAllNodeIds();
        return new ArrayList<>(ids);
    }

}
