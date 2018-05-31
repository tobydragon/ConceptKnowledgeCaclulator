package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.Tecmap;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TecmapService {

    private static Tecmap tecmap = null;

    public TecmapService() throws IOException{
        tecmap = new Tecmap(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json");
    }

    public ConceptGraphRecord retrieveStructureTree() {
        return tecmap.createStructureTree();
    }

    public List<String> retrieveConceptIdList() {
        return tecmap.createConceptIdListToPrint();
    }

}
