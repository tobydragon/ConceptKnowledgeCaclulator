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
    private String datastoreId;
    private TecmapDatastore tecmapDatastore;

    public TecmapService() throws IOException{

        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_MAIN_DATASTORE_FILE);
        tecmap = tecmapDatastore.retrieveTecmapForId("comp171Dragon", TecmapState.assessmentConnected);

    }

    public TecmapService(TecmapDatastore tecmapDatastore, String id, TecmapState tecmapState) {
        this.tecmapDatastore = tecmapDatastore;
        datastoreId = id;
        tecmap = tecmapDatastore.retrieveTecmapForId(datastoreId, tecmapState);
    }

    public ConceptGraphRecord retrieveStructureTree() {
        if (tecmap != null) {
            return tecmap.createStructureTree();
        }
        return null;
    }

    public List<String> retrieveConceptIdList() {
        if (tecmap != null) {
            return tecmap.createConceptIdListToPrint();
        }
        return null;
    }

    public List<LearningResourceRecord> retrieveBlankLearningResourceRecordsFromAssessment() {
        if (tecmap != null) {
            return tecmap.createBlankLearningResourceRecordsFromAssessment();
        }
        return null;
    }

    public CohortConceptGraphsRecord retrieveCohortTree() {
        if (tecmap != null) {
            return tecmap.createCohortTree();
        }
        return null;
    }

    public TecmapAPI chooseTecmap(String id) {
        datastoreId = id;
        tecmap = tecmapDatastore.retrieveTecmapForId(datastoreId);
        return tecmap;
    }

    public TecmapAPI chooseTecmap(String id, TecmapState tecmapState) {
        datastoreId = id;
        tecmap = tecmapDatastore.retrieveTecmapForId(datastoreId, tecmapState);
        return tecmap;
    }
}
