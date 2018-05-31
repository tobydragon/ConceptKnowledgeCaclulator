package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.TreeConverter;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Tecmap implements TecmapAPI {

    private ConceptGraph graph;

    public Tecmap(String structureFileName) throws IOException {
        graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(structureFileName));
    }

    @Override
    public ConceptGraphRecord createStructureTree() {
        return TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
    }

    @Override
    public List<String> createConceptIdListToPrint() {
        Collection<String> ids =  graph.getAllNodeIds();
        List<String> idsToPrint = new ArrayList<>();
        for (String id : ids){
            idsToPrint.add("\""+id+"\"");
        }
        return idsToPrint;
    }

}
