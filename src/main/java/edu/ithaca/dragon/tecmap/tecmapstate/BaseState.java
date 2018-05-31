package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.TreeConverter;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseState {

    private ConceptGraph graph;

    public BaseState(String structureFilename) throws IOException {
        graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(structureFilename));
    }

    public ConceptGraphRecord createStructureTree() {
        return TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
    }

    public List<String> createConceptIdListToPrint() {
        Collection<String> ids =  graph.getAllNodeIds();
        //TODO: convert this to functional style
        List<String> idsToPrint = new ArrayList<>();
        for (String id : ids){
            idsToPrint.add("\""+id+"\"");
        }
        return idsToPrint;
    }

}
