package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.*;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TecmapService {

    private TecmapDatastore tecmapDatastore;

    public TecmapService() throws IOException{
        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_MAIN_DATASTORE_PATH);
//        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
    }

    //For Tests
    public TecmapService(TecmapDatastore tecmapDatastore) {
        this.tecmapDatastore = tecmapDatastore;
    }

    /**
     * Gets a tecmap from the datastore with the given id
     * @param id from datastore
     * @return SuggestingTecmap that corresponds to a given id in the Datastore, null if not found
     */
    public SuggestingTecmapAPI retrieveSuggestingTecmapAPI(String id) {
        return tecmapDatastore.retrieveTecmapForId(id);
    }

    /**
     * Gets structure tree related to tecmap with id
     * @param id
     * @return ConceptGraphRecord used for building trees, null if id not found
     */
    public ConceptGraphRecord retrieveStructureTree(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.createStructureTree();
        }
        return null;
    }

    /**
     * Gets list of all concepts related to the given tecmap with id
     * @param id
     * @return List of concepts as strings (with quotes), null if id not found
     */
    public List<String> retrieveConceptIdList(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.conceptIdList();
        }
        return null;
    }

    /**
     * @param id
     * @return List of LearningResourceRecords with no concepts, null if not found
     */
    public List<LearningResourceRecord> currentLearningResourceRecords(String id) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(id);
        if (tecmap != null) {
            return tecmap.currentLearningResourceRecords();
        }
        return null;
    }

    /**
     * Updates resource file for given tecmap with id and connected LRRecords w/ concepts
     * @param id
     * @param resourceRecords
     * @return String of path name to newly written file, null if not written or tecmap not found
     */
    public String postConnectedResources(String id, List<LearningResourceRecord> resourceRecords) {
        return tecmapDatastore.updateTecmapResources(id, resourceRecords);
    }

    /**
     * Gets all of the Connected Concept Graphs, for the cohort graphs w/ id
     * @param id
     * @return CohortConceptGraphsRecord of all students, null if not found
     */
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

    /**
     * Gets all of the concepts a student needs to work on given a courseId, and a userId
     * @param courseId
     * @param userId
     * @return List of suggested conceptIds, null if not found
     */
    public List<String> retrieveConceptSuggestionsForUser(String courseId, String userId) {
        SuggestingTecmapAPI tecmap = retrieveSuggestingTecmapAPI(courseId);
        if (tecmap != null) {
            return tecmap.suggestConceptsForUser(userId);
        }
        return null;
    }

    /**
     * Gets all of the resources a student needs to work on given a courseId, and a userId
     * @param courseId
     * @param userId
     * @return List of suggested resources (with wrong and incomplete lists), null if not found
     */
    //TODO: UNTESTED
    public OrganizedLearningResourceSuggestions retrieveResourceSuggestionsForUser(String courseId, String userId) {
        SuggestingTecmapAPI tecmap = tecmapDatastore.retrieveTecmapForId(courseId);
        if (tecmap != null) {
            return tecmap.suggestResourcesForUser(userId);
        }
        return null;
    }

    /**
     * Gets all of the resources a student needs to work on for a specific concept given a courseId,
     * a userId, and a conceptId
     * @param courseId
     * @param userId
     * @param conceptId
     * @return List of suggest resources (with wrong and incomplete lists), null if not found
     */
    //TODO: UNTESTED
    public OrganizedLearningResourceSuggestions retrieveResourceSuggestionsForSpecificConceptForUser(String courseId, String userId, String conceptId) {
        SuggestingTecmapAPI tecmap = tecmapDatastore.retrieveTecmapForId(courseId);
        if (tecmap != null) {
            return tecmap.suggestResourcesForSpecificConceptForUser(userId, conceptId);
        }
        return null;
    }

    /**
     * Gets student group suggestions given a courseId, a way to sort, and a group size
     * @param courseId
     * @param sortType : Can be bucket, concept, size, all (default), all others return null
     * @param size
     * @return list of groups, null if not found
     * @throws Exception
     */
    public List<Group> retrieveGroupSuggestions(String courseId, String sortType, int size) throws Exception {
        SuggestingTecmapAPI tecmap = tecmapDatastore.retrieveTecmapForId(courseId);
        if (tecmap != null) {
            //setup suggesters

            //setup group buckets
            List<List<Integer>> ranges = new ArrayList<>();
            List<Integer> temp = new ArrayList<>();
            temp.add(0);
            temp.add(50);
            List<Integer> temp2 = new ArrayList<>();
            temp2.add(50);
            temp2.add(80);
            List<Integer> temp3 = new ArrayList<>();
            temp3.add(80);
            temp3.add(100);
            ranges.add(temp);
            ranges.add(temp2);
            ranges.add(temp3);

            BucketSuggester bucketSuggester = new BucketSuggester(ranges);
            ConceptSuggester conceptSuggester = new ConceptSuggester();
            BySizeSuggester bySizeSuggester = new BySizeSuggester(size, true);
//            ComplementaryKnowledgeSuggester complementaryKnowledgeSuggester = new ComplementaryKnowledgeSuggester();

            //Empty Suggester list
            List<Suggester> suggesterList = new ArrayList<>();
            if (sortType.equals("bucket")) {
                suggesterList.add(bucketSuggester);
            } else if (sortType.equals("concept")) {
                suggesterList.add(conceptSuggester);
            } else if (sortType.equals("size")) {
                suggesterList.add(bySizeSuggester);
            } else if (sortType.equals("all") || sortType.equals("")) {
                suggesterList.addAll(Arrays.asList(bucketSuggester, conceptSuggester, bySizeSuggester));
            } else {
                return null;
            }
            return tecmap.suggestGroups(suggesterList, size);
        }
        return null;
    }

    /**
     * Gets all of the available Tecmap Ids and List of all actions that you can do with each
     * @return Map of ids to list of actions
     */
    public Map<String, List<TecmapUserAction>> retrieveValidIdsAndActions() {
        return tecmapDatastore.retrieveValidIdsAndActions();
    }
}
