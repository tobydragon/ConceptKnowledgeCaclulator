package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TecmapService {

    private TecmapAPI tecmap;
    private TecmapDatastore tecmapDatastore;

    public TecmapService() throws IOException{

        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_MAIN_DATASTORE_FILE);
//        tecmapDatastore.retrieveTecmapForId("comp171Dragon", TecmapState.assessmentConnected);

    }

    public TecmapService(TecmapDatastore tecmapDatastore) {
        this.tecmapDatastore = tecmapDatastore;
    }

    public TecmapAPI retrieveTecmapAPI(String id) {
        return tecmapDatastore.retrieveTecmapForId(id);
    }

//
//    public ConceptGraphRecord retrieveStructureTree() {
//        if (tecmap != null) {
//            return tecmap.createStructureTree();
//        }
//        return null;
//    }

    public ConceptGraphRecord retrieveStructureTree(String id) {
        tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createStructureTree();
        }
        return null;
    }

//    public List<String> retrieveConceptIdList() {
//        if (tecmap != null) {
//            return tecmap.createConceptIdListToPrint();
//        }
//        return null;
//    }

    public List<String> retrieveConceptIdList(String id) {
        tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createConceptIdListToPrint();
        }
        return null;
    }

//    public List<LearningResourceRecord> retrieveBlankLearningResourceRecordsFromAssessment() {
//        if (tecmap != null) {
//            return tecmap.createBlankLearningResourceRecordsFromAssessment();
//        }
//        return null;
//    }

    public List<LearningResourceRecord> retrieveBlankLearningResourceRecordsFromAssessment(String id) {
        tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createBlankLearningResourceRecordsFromAssessment();
        }
        return null;
    }

//    //TODO add another null check
//    public CohortConceptGraphsRecord retrieveCohortTree() {
//        if (tecmap != null) {
//            return tecmap.createCohortTree();
//        }
//        return null;
//    }

    //TODO add another null check
    public CohortConceptGraphsRecord retrieveCohortTree(String id) {
        tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createCohortTree();
        }
        return null;
    }
}
