package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TecmapService {

    private TecmapAPI tecmap;

    public TecmapService() throws IOException{
//        tecmap = new Tecmap(
//                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
//                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleResources.json")),
//                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
//                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
//                )
//        );

        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);
        tecmap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.assessmentConnected);
    }

    public TecmapService(TecmapDatastore tecmapDatastore, String id, TecmapState tecmapState) {
        tecmap = tecmapDatastore.retrieveTecmapForId(id, tecmapState);
    }

    public ConceptGraphRecord retrieveStructureTree() { return tecmap.createStructureTree(); }

    public List<String> retrieveConceptIdList() {
        return tecmap.createConceptIdListToPrint();
    }

    public List<LearningResourceRecord> retrieveBlankLearningResourceRecordsFromAssessment() { return tecmap.createBlankLearningResourceRecordsFromAssessment(); }

    public CohortConceptGraphsRecord retrieveCohortTree() { return tecmap.createCohortTree(); }
}
