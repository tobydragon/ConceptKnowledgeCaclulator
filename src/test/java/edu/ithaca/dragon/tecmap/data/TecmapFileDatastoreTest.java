package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapFileDatastoreRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TecmapFileDatastoreTest {

    private TecmapDatastore tecmapDatastore;

    @BeforeEach
    void setup() throws IOException {
        tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
    }

    @Test
    void TecmapFileDatastoreConstructorTest() {
        assertNull(tecmapDatastore.retrieveTecmapForId("BadPaths"));
        assertNull(tecmapDatastore.retrieveTecmapForId("Cs1ExampleBadResource"));

        //Assessment Connected, All Good Files
        assertNotNull(tecmapDatastore.retrieveTecmapForId("Cs1Example"));
        assertEquals(TecmapState.assessmentConnected, tecmapDatastore.retrieveTecmapForId("Cs1Example").getCurrentState());
        //No Assessment, All Good Files
        assertNotNull(tecmapDatastore.retrieveTecmapForId("Cs1ExampleStructure"));
        assertEquals(TecmapState.noAssessment, tecmapDatastore.retrieveTecmapForId("Cs1ExampleStructure").getCurrentState());
        //Assessment Added, All Good Files
        assertNotNull(tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded"));
        assertEquals(TecmapState.assessmentAdded, tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded").getCurrentState());
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
        Map<String, List<TecmapUserAction>> validMap = tecmapDatastore.retrieveValidIdsAndActions();
        assertEquals(3, validMap.size());
        assertEquals(1, validMap.get("Cs1ExampleStructure").size());
        assertThat(validMap.get("Cs1ExampleStructure"), containsInAnyOrder(TecmapUserAction.structureTree));
        assertEquals(2, validMap.get("Cs1ExampleAssessmentAdded").size());
        assertThat(validMap.get("Cs1ExampleAssessmentAdded"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources));
        assertEquals(3, validMap.get("Cs1Example").size());
        assertThat(validMap.get("Cs1Example"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources, TecmapUserAction.cohortTree));
    }

    @Test
    //CREATES AND DELETES THE FILES!!
    void updateTecmapResources() throws Exception {
        TecmapDatastore originalDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);

        String expectedOrigFilename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExampleAssessmentAdded/Cs1ExampleAssessmentAddedResources.json";
        String secondFilename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExampleAssessmentAdded/Cs1ExampleAssessmentAddedResources-backup-0.json";

        String updateFiles = tecmapDatastore.updateTecmapResources("Cs1ExampleAssessmentAdded", tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded").createBlankLearningResourceRecordsFromAssessment());
        assertEquals(expectedOrigFilename, updateFiles);

        //Second call to make sure it adds to the file name so that it doesn't overwrite any files
        updateFiles = tecmapDatastore.updateTecmapResources("Cs1ExampleAssessmentAdded", tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded").createBlankLearningResourceRecordsFromAssessment());
        assertEquals(expectedOrigFilename, updateFiles);

        //And Delete Both all Files Created
        Path path = Paths.get(expectedOrigFilename);
        assertTrue(Files.deleteIfExists(path));

        path = Paths.get(secondFilename);
        assertTrue(Files.deleteIfExists(path));

        path = Paths.get(Settings.DEFAULT_TEST_DATASTORE_PATH + "TecmapDatastore-backup-0.json");
        assertTrue(Files.deleteIfExists(path));

        path = Paths.get(Settings.DEFAULT_TEST_DATASTORE_PATH + "TecmapDatastore-backup-1.json");
        assertTrue(Files.deleteIfExists(path));

        path = Paths.get(Settings.DEFAULT_TEST_DATASTORE_PATH + Settings.DEFAULT_DATASTORE_FILENAME);
        assertTrue(Files.deleteIfExists(path));

        Json.toJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + Settings.DEFAULT_DATASTORE_FILENAME, originalDatastore.createTecmapFileDatastoreRecord());

        //Bad ID
        updateFiles = tecmapDatastore.updateTecmapResources("notAValidID", null);
        assertNull(updateFiles);
        //Good ID, null list
        updateFiles = tecmapDatastore.updateTecmapResources("Cs1ExampleAssessmentAdded", null);
        assertNull(updateFiles);
        //Good ID, empty list
        updateFiles = tecmapDatastore.updateTecmapResources("Cs1ExampleAssessmentAdded", new ArrayList<LearningResourceRecord>());
        assertNull(updateFiles);
    }

    @Test
    void createTecmapFileDatastoreRecord() throws Exception {
        JSONAssert.assertEquals(Json.toJsonString(Json.fromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + Settings.DEFAULT_DATASTORE_FILENAME, TecmapFileDatastoreRecord.class)), Json.toJsonString(tecmapDatastore.createTecmapFileDatastoreRecord()), false);
    }
}