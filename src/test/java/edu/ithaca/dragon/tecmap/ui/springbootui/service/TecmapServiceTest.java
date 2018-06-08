package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
public class TecmapServiceTest {

    private TecmapService tecmapService;

    @Before
    public void setup() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);

        tecmapService = new TecmapService(tecmapDatastore);
    }

    @Test
    public void retreiveSuggestingTecmapAPI() {
        SuggestingTecmapAPI structureTecmap = tecmapService.retrieveSuggestingTecmapAPI("Cs1ExampleStructure");
        SuggestingTecmapAPI addedTecmap = tecmapService.retrieveSuggestingTecmapAPI("Cs1ExampleAssessmentAdded");
        SuggestingTecmapAPI connectedTecmap = tecmapService.retrieveSuggestingTecmapAPI("Cs1Example");
        assertNotNull(structureTecmap);
        assertNotNull(addedTecmap);
        assertNotNull(connectedTecmap);

        SuggestingTecmapAPI nullTecmap = tecmapService.retrieveSuggestingTecmapAPI("NoPath");
        assertNull(nullTecmap);
    }

    @Test
    public void retrieveStructureTreeWithIds() throws JsonProcessingException{
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, tecmapService.retrieveStructureTree("Cs1ExampleStructure").toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, tecmapService.retrieveStructureTree("Cs1ExampleAssessmentAdded").toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, tecmapService.retrieveStructureTree("Cs1Example").toJsonString());
    }

    @Test
    public void retrieveConceptIdListWithIds() {
        List<String> onlyStructureConcepts = tecmapService.retrieveConceptIdList("Cs1ExampleStructure");
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, onlyStructureConcepts.toString());
        List<String> twoAssessmentsAddedConcepts = tecmapService.retrieveConceptIdList("Cs1ExampleAssessmentAdded");
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsAddedConcepts.toString());
        List<String> twoAssessmentsConnectedConcepts = tecmapService.retrieveConceptIdList("Cs1Example");
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
    }

    @Test
    public void retrieveBlankLearningResourceRecordsFromAssessmentWithIds() throws JsonProcessingException{
        assertEquals(0, tecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1ExampleStructure").size());
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1ExampleAssessmentAdded")));
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1Example")));
    }

    @Test
    public void postConnectedResources() {
        assertEquals("src/test/resources/datastore/Cs1ExampleAssessmentAdded/Cs1ExampleAssessmentAddedResources.json", tecmapService.postConnectedResources("Cs1ExampleAssessmentAdded", tecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1ExampleAssessmentAdded")));
        assertNull(tecmapService.postConnectedResources("notAValidID", null));
        assertNull(tecmapService.postConnectedResources("Cs1ExampleAssessmentAdded", null));
        assertNull(tecmapService.postConnectedResources("Cs1ExampleAssessmentAdded", new ArrayList<LearningResourceRecord>()));
    }

    @Test
    public void retrieveCohortTreeWithIds() throws JsonProcessingException{
        assertNull(tecmapService.retrieveCohortTree("Cs1ExampleStructure"));
        assertNull(tecmapService.retrieveCohortTree("Cs1ExampleAssessmentAdded"));

        CohortConceptGraphsRecord cohortRecord = tecmapService.retrieveCohortTree("Cs1Example");
        List<ConceptGraphRecord> records = cohortRecord.getGraphRecords();
        assertEquals(4, records.size());
        for (ConceptGraphRecord record : records ) {
            if (record.getName().equals("s02")){
                assertEquals(Cs1ExampleJsonStrings.bartDataTree, Json.toJsonString(record));
            }
        }
    }

    @Test
    public void retrieveConceptSuggestionsForUser() {
        //These 2 can't have suggestions since they don't have resources connected
        assertNull(tecmapService.retrieveConceptSuggestionsForUser("Cs1ExampleStructure", "s01"));
        assertNull(tecmapService.retrieveConceptSuggestionsForUser("Cs1ExampleAssessmentAdded", "s01"));

        assertEquals(0, tecmapService.retrieveConceptSuggestionsForUser("Cs1Example", "s02").size());
        List<String> s03Suggestions = tecmapService.retrieveConceptSuggestionsForUser("Cs1Example", "s03");
        assertThat(s03Suggestions, containsInAnyOrder("While Loops"));
        assertEquals(1, s03Suggestions.size());
        List<String> s01Suggestions = tecmapService.retrieveConceptSuggestionsForUser("Cs1Example", "s01");
        assertThat(s01Suggestions, containsInAnyOrder("For Loops", "If Statements", "While Loops"));
        assertEquals(3, s01Suggestions.size());
        //TODO this should suggest below, but doesn't because ConceptGraphSuggesterLibrary.suggestConcepts doesn't suggest things with 0 estimate
//        assertThat(s01Suggestions, containsInAnyOrder( "For Loops", "Boolean Expressions"));

        assertNull(tecmapService.retrieveConceptSuggestionsForUser("Cs1Example", "badStudentID"));
    }

//    @Test
    //TODO: FIX TESTS ONCE METHOD IS FINALIZED
    public void retrieveResourceSuggestionsForUser() throws JsonProcessingException {
        System.out.println(Json.toJsonString(tecmapService.retrieveResourceSuggestionsForUser("Cs1Example", "s01")));
    }

//    @Test
    //TODO: FIX TESTS ONCE METHOD IS FINALIZED
    public void retrieveResourceSuggestionsForSpecificConceptForUser() throws JsonProcessingException {
        System.out.println(Json.toJsonString(tecmapService.retrieveResourceSuggestionsForSpecificConceptForUser("Cs1Example", "s01", "Loops")));
    }

    @Test
    public void retrieveValidIdsAndActions() {
        //Will be the same for all of the Services, because they all use the same datastore
        Map<String, List<TecmapUserAction>> validMap = tecmapService.retrieveValidIdsAndActions();
        assertEquals(4, validMap.size());
        assertEquals(1, validMap.get("Cs1ExampleStructure").size());
        assertThat(validMap.get("Cs1ExampleStructure"), containsInAnyOrder(TecmapUserAction.structureTree));
        assertEquals(2, validMap.get("Cs1ExampleAssessmentAdded").size());
        assertThat(validMap.get("Cs1ExampleAssessmentAdded"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources));
        assertEquals(3, validMap.get("Cs1Example").size());
        assertThat(validMap.get("Cs1Example"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources, TecmapUserAction.cohortTree));
    }
}