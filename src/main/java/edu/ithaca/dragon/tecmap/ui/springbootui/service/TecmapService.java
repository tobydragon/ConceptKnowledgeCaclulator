package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
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
//        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_MAIN_DATASTORE_FILE);
        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);
    }

    //For Tests
    public TecmapService(TecmapDatastore tecmapDatastore) {
        this.tecmapDatastore = tecmapDatastore;
    }

    /**
     * is public for sole reason of testing
     * @param id from datastore
     * @return SuggestingTecmap that corresponds to a given id in the Datastore
     */
    public SuggestingTecmapAPI retrieveSuggestingTecmapAPI(String id) {
        return tecmapDatastore.retrieveTecmapForId(id);
    }

    public ConceptGraphRecord retrieveStructureTree(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createStructureTree();
        }
        return null;
    }

    public List<String> retrieveConceptIdList(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createConceptIdListToPrint();
        }
        return null;
    }

    public List<LearningResourceRecord> retrieveBlankLearningResourceRecordsFromAssessment(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createBlankLearningResourceRecordsFromAssessment();
        }
        return null;
    }

    public String postConnectedResources(String id, List<LearningResourceRecord> resourceRecords) {
        return tecmapDatastore.updateTecmapResources(id, resourceRecords);
    }

    public CohortConceptGraphsRecord retrieveCohortTree(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            CohortConceptGraphsRecord cohortTree = tecmap.createCohortTree();
            if (cohortTree != null) {
                return cohortTree;
            }
        }
        return null;
    }

    public List<String> retrieveConceptSuggestionsForUser(String courseId, String userId) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(courseId);
        if (tecmap != null) {
            return tecmap.suggestConceptsForUser(userId);
        }
        return null;
    }


    //TODO: UNTESTED
    public OrganizedLearningResourceSuggestions retrieveResourceSuggestionsForUser(String courseId, String userId) {
        SuggestingTecmapAPI tecmap = tecmapDatastore.retrieveTecmapForId(courseId);
        if (tecmap != null) {
            return tecmap.suggestResourcesForUser(userId);
        }
        return null;
    }

    //TODO: UNTESTED
    public OrganizedLearningResourceSuggestions retrieveResourceSuggestionsForSpecificConceptForUser(String courseId, String userId, String conceptId) {
        SuggestingTecmapAPI tecmap = tecmapDatastore.retrieveTecmapForId(courseId);
        if (tecmap != null) {
            return tecmap.suggestResourcesForSpecificConceptForUser(userId, conceptId);
        }
        return null;
    }

    public Map<String, List<TecmapUserAction>> retrieveValidIdsAndActions() {
        return tecmapDatastore.retrieveValidIdsAndActions();
    }
}
