package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class TecmapService {

    private TecmapDatastore tecmapDatastore;

    public TecmapService() throws IOException{
        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_MAIN_DATASTORE_FILE);
//        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);
    }

    //For Tests
    public TecmapService(TecmapDatastore tecmapDatastore) {
        this.tecmapDatastore = tecmapDatastore;
    }

    /**
     * @param id
     * @return Tecmap that corresponds to a given id in the Datastore
     */
    public TecmapAPI retrieveTecmapAPI(String id) {
        return tecmapDatastore.retrieveTecmapForId(id);
    }

    public ConceptGraphRecord retrieveStructureTree(String id) {
        TecmapAPI tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createStructureTree();
        }
        return null;
    }

    public List<String> retrieveConceptIdList(String id) {
        TecmapAPI tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createConceptIdListToPrint();
        }
        return null;
    }

    public List<LearningResourceRecord> retrieveBlankLearningResourceRecordsFromAssessment(String id) {
        TecmapAPI tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createBlankLearningResourceRecordsFromAssessment();
        }
        return null;
    }

    public CohortConceptGraphsRecord retrieveCohortTree(String id) {
        TecmapAPI tecmap = retrieveTecmapAPI(id);
        if (tecmap != null) {
            CohortConceptGraphsRecord cohortTree = tecmap.createCohortTree();
            if (cohortTree != null) {
                return cohortTree;
            }
        }
        return null;
    }

    public Map<String, List<TecmapUserAction>> retrieveValidIdsAndActions() {
        return tecmapDatastore.retrieveValidIdsAndActions();
    }
}
