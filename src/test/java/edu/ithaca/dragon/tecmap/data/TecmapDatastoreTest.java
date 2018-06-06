package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.TecmapAction;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TecmapDatastoreTest {

    private TecmapDatastore tecmapDatastore;

    @BeforeEach
    void setup() throws IOException {
        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);
    }

    @Test
    void retrieveTecmapForId() throws IOException {
        //check invalid options
        assertNull(tecmapDatastore.retrieveTecmapForId("noSuchId"));
        assertNull(tecmapDatastore.retrieveTecmapForId("BadPaths"));

        //check a valid TecmapAPI
        TecmapAPI cs1ExampleMap = tecmapDatastore.retrieveTecmapForId("Cs1Example");

        assertEquals(10, cs1ExampleMap.createBlankLearningResourceRecordsFromAssessment().size());

        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, cs1ExampleMap.createStructureTree().toJsonString());
        Collection<String> twoAssessmentsConnectedConcepts = cs1ExampleMap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
        List<ConceptGraphRecord> records =  cs1ExampleMap.createCohortTree().getGraphRecords();
        assertEquals(4, records.size());
        for (ConceptGraphRecord record : records){
            //Bart
            if (record.getName().equals("s02")){
                //System.out.println(Json.toJsonString(record));
                assertEquals(Cs1ExampleJsonStrings.bartDataTree, Json.toJsonString(record));
            }
        }

    }

    @Test
    void retrieveTecmapForIdTestForDifferentStates() {
        TecmapAPI cs1ExampleMap = tecmapDatastore.retrieveTecmapForId("Cs1Example");
        assertEquals(TecmapState.assessmentConnected, cs1ExampleMap.getCurrentState());
        TecmapAPI cs1ExampleMapNoResources = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");
        assertEquals(TecmapState.assessmentAdded, cs1ExampleMapNoResources.getCurrentState());
        TecmapAPI cs1ExampleMapNoAssessment = tecmapDatastore.retrieveTecmapForId("Cs1ExampleStructure");
        assertEquals(TecmapState.noAssessment, cs1ExampleMapNoAssessment.getCurrentState());
    }

    @Test
    void retrieveTecmapForIdExtraParameter(){
        TecmapAPI noAssessmentModeMap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.noAssessment);
        TecmapAPI assessmentAddedModeMap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.assessmentAdded);
        TecmapAPI assessmentConnectedModeMap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.assessmentConnected);

        assertEquals(TecmapState.noAssessment, noAssessmentModeMap.getCurrentState());
        assertNotNull(noAssessmentModeMap.createStructureTree());
        assertEquals(0, noAssessmentModeMap.createBlankLearningResourceRecordsFromAssessment().size());
        assertNull( noAssessmentModeMap.createCohortTree());

        assertEquals(TecmapState.assessmentAdded, assessmentAddedModeMap.getCurrentState());
        assertNotNull(assessmentAddedModeMap.createStructureTree());
        assertEquals(10, assessmentAddedModeMap.createBlankLearningResourceRecordsFromAssessment().size());
        assertNull(assessmentAddedModeMap.createCohortTree());

        assertEquals(TecmapState.assessmentConnected, assessmentConnectedModeMap.getCurrentState());
        assertNotNull(assessmentConnectedModeMap.createStructureTree());
        assertEquals(10, assessmentConnectedModeMap.createBlankLearningResourceRecordsFromAssessment().size());
        assertNotNull(assessmentConnectedModeMap.createCohortTree());
    }

    @Test
    void retrieveValidIdsAndActions(){
        Map<String, List<TecmapAction>> validMap = tecmapDatastore.retrieveValidIdsAndActions();
        assertEquals(4, validMap.size());
        assertEquals(1, validMap.get("Cs1ExampleStructure").size());
        assertThat(validMap.get("Cs1ExampleStructure"), containsInAnyOrder(TecmapAction.structureTree));
        assertEquals(1, validMap.get("Cs1ExampleAssessmentAdded").size());
        assertThat(validMap.get("Cs1ExampleAssessmentAdded"), containsInAnyOrder(TecmapAction.structureTree));
        assertEquals(2, validMap.get("Cs1Example").size());
        assertThat(validMap.get("Cs1Example"), containsInAnyOrder(TecmapAction.structureTree, TecmapAction.cohortTree));
    }
}