package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
public class TecmapServiceTest {

    private TecmapService onlyStructureTecmapService;
    private TecmapService twoAssessmentsAddedTecmapService;
    private TecmapService twoAssessmentsConnectedTecmapService;

    @Before
    public void setup() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);

        onlyStructureTecmapService = new TecmapService(tecmapDatastore);
        twoAssessmentsAddedTecmapService = new TecmapService(tecmapDatastore);
        twoAssessmentsConnectedTecmapService = new TecmapService(tecmapDatastore);
    }

    @Test
    public void retreiveTecmapAPI() {
        TecmapAPI structureTecmap = onlyStructureTecmapService.retrieveTecmapAPI("Cs1ExampleStructure");
        TecmapAPI addedTecmap = onlyStructureTecmapService.retrieveTecmapAPI("Cs1ExampleAssessmentAdded");
        TecmapAPI connectedTecmap = twoAssessmentsConnectedTecmapService.retrieveTecmapAPI("Cs1Example");
        assertNotNull(structureTecmap);
        assertNotNull(addedTecmap);
        assertNotNull(connectedTecmap);

        TecmapAPI nullTecmap = twoAssessmentsConnectedTecmapService.retrieveTecmapAPI("NoPath");
        assertNull(nullTecmap);
    }

    @Test
    public void retrieveStructureTreeWithIds() throws JsonProcessingException{
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, onlyStructureTecmapService.retrieveStructureTree("Cs1ExampleStructure").toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, twoAssessmentsAddedTecmapService.retrieveStructureTree("Cs1ExampleAssessmentAdded").toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, twoAssessmentsConnectedTecmapService.retrieveStructureTree("Cs1Example").toJsonString());
    }

    @Test
    public void retrieveConceptIdListWithIds() {
        List<String> onlyStructureConcepts = onlyStructureTecmapService.retrieveConceptIdList("Cs1ExampleStructure");
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, onlyStructureConcepts.toString());
        List<String> twoAssessmentsAddedConcepts = twoAssessmentsAddedTecmapService.retrieveConceptIdList("Cs1ExampleAssessmentAdded");
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsAddedConcepts.toString());
        List<String> twoAssessmentsConnectedConcepts = twoAssessmentsConnectedTecmapService.retrieveConceptIdList("Cs1Example");
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
    }

    @Test
    public void retrieveBlankLearningResourceRecordsFromAssessmentWithIds() throws JsonProcessingException{
        assertEquals(0, onlyStructureTecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1ExampleStructure").size());
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsAddedTecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1ExampleAssessmentAdded")));
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsConnectedTecmapService.retrieveBlankLearningResourceRecordsFromAssessment("Cs1Example")));
    }

    @Test
    public void retrieveCohortTreeWithIds() throws JsonProcessingException{
        assertNull(onlyStructureTecmapService.retrieveCohortTree("Cs1ExampleStructure"));
        assertNull(twoAssessmentsAddedTecmapService.retrieveCohortTree("Cs1ExampleAssessmentAdded"));

        CohortConceptGraphsRecord cohortRecord = twoAssessmentsConnectedTecmapService.retrieveCohortTree("Cs1Example");
        List<ConceptGraphRecord> records = cohortRecord.getGraphRecords();
        assertEquals(4, records.size());
        for (ConceptGraphRecord record : records ) {
            if (record.getName().equals("s02")){
                assertEquals(Cs1ExampleJsonStrings.bartDataTree, Json.toJsonString(record));
            }
        }
    }

    @Test
    public void retrieveValidIdsAndActions() {
        //Will be the same for all of the Services, because they all use the same datastore
        Map<String, List<TecmapUserAction>> validMap = onlyStructureTecmapService.retrieveValidIdsAndActions();
        assertEquals(4, validMap.size());
        assertEquals(1, validMap.get("Cs1ExampleStructure").size());
        assertThat(validMap.get("Cs1ExampleStructure"), containsInAnyOrder(TecmapUserAction.structureTree));
        assertEquals(2, validMap.get("Cs1ExampleAssessmentAdded").size());
        assertThat(validMap.get("Cs1ExampleAssessmentAdded"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources));
        assertEquals(3, validMap.get("Cs1Example").size());
        assertThat(validMap.get("Cs1Example"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources, TecmapUserAction.cohortTree));
    }
}