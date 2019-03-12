package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapDataFilesRecord;
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
import java.util.*;

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

    //Test that makes sure that a datastore with bad paths creates the correct tecmaps using
    //only the files that exist
    @Test
    void TecmapFileDatastoreWithBadPathsConstructorTest() throws IOException {
        TecmapFileDatastoreRecord tecmapFileDatastoreRecord = tecmapDatastore.createTecmapFileDatastoreRecord();

        List<TecmapDataFilesRecord> tecmapDataFilesRecords = tecmapFileDatastoreRecord.getAllRecords();

        tecmapDataFilesRecords.add(new TecmapDataFilesRecord("BadPaths", "BadPaths", Arrays.asList("BadPaths"), Arrays.asList("BadPaths")));
        tecmapDataFilesRecords.add(new TecmapDataFilesRecord("Cs1ExampleBadResources", "src/test/resources/datastore/Cs1Example/Cs1ExampleGraph.json",
                Arrays.asList("BadResourceFile"),
                Arrays.asList("src/test/resources/datastore/Cs1Example/Cs1ExampleAssessment1.csv", "src/test/resources/author/tecmapExamples/Cs1ExampleAssessment2.csv")));

        tecmapDataFilesRecords.add(new TecmapDataFilesRecord("Cs1ExampleBadAssessment", "src/test/resources/datastore/Cs1Example/Cs1ExampleGraph.json",
                Arrays.asList("src/test/resources/datastore/Cs1Example/Cs1ExampleResources.json"),
                Arrays.asList("BadAssessmentFile")));

        tecmapFileDatastoreRecord.setAllRecords(tecmapDataFilesRecords);

        TecmapFileDatastore badPathsDatastore = new TecmapFileDatastore(tecmapFileDatastoreRecord, "anyPath");

        assertNull(badPathsDatastore.retrieveTecmapForId("BadPaths"));

        //Assessment Connected, All Good Files
        assertNotNull(badPathsDatastore.retrieveTecmapForId("Cs1Example"));
        assertEquals(TecmapState.assessmentConnected, badPathsDatastore.retrieveTecmapForId("Cs1Example").getCurrentState());
        //No Assessment, All Good Files
        assertNotNull(badPathsDatastore.retrieveTecmapForId("Cs1ExampleStructure"));
        assertEquals(TecmapState.noAssessment, badPathsDatastore.retrieveTecmapForId("Cs1ExampleStructure").getCurrentState());
        //Assessment Added, All Good Files
        assertNotNull(badPathsDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded"));
        assertEquals(TecmapState.assessmentAdded, badPathsDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded").getCurrentState());
        //Assessment Added, Bad Resource Files
        assertNotNull(badPathsDatastore.retrieveTecmapForId("Cs1ExampleBadResources"));
        assertEquals(TecmapState.assessmentAdded, badPathsDatastore.retrieveTecmapForId("Cs1ExampleBadResources").getCurrentState());
        //NoAssessment, Bad Assessment Files
        assertNotNull(badPathsDatastore.retrieveTecmapForId("Cs1ExampleBadAssessment"));
        assertEquals(TecmapState.resourcesNoAssessment, badPathsDatastore.retrieveTecmapForId("Cs1ExampleBadAssessment").getCurrentState());
    }

    @Test
    void retrieveTecmapForId() throws IOException {
        //check invalid options
        assertNull(tecmapDatastore.retrieveTecmapForId("noSuchId"));
        assertNull(tecmapDatastore.retrieveTecmapForId("BadPaths"));

        //check a valid TecmapAPI
        TecmapAPI cs1ExampleMap = tecmapDatastore.retrieveTecmapForId("Cs1Example");

        assertEquals(10, cs1ExampleMap.currentLearningResourceRecords().size());

        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, cs1ExampleMap.createStructureTree().toJsonString());
        Collection<String> twoAssessmentsConnectedConcepts = cs1ExampleMap.conceptIdList();
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
        assertEquals(0, noAssessmentModeMap.currentLearningResourceRecords().size());
        assertNull( noAssessmentModeMap.createCohortTree());

        assertEquals(TecmapState.assessmentAdded, assessmentAddedModeMap.getCurrentState());
        assertNotNull(assessmentAddedModeMap.createStructureTree());
        assertEquals(10, assessmentAddedModeMap.currentLearningResourceRecords().size());
        assertNull(assessmentAddedModeMap.createCohortTree());

        assertEquals(TecmapState.assessmentConnected, assessmentConnectedModeMap.getCurrentState());
        assertNotNull(assessmentConnectedModeMap.createStructureTree());
        assertEquals(10, assessmentConnectedModeMap.currentLearningResourceRecords().size());
        assertNotNull(assessmentConnectedModeMap.createCohortTree());
    }

    @Test
    void retrieveValidIdsAndActions(){
        Map<String, List<TecmapUserAction>> validMap = tecmapDatastore.retrieveValidIdsAndActions();
        assertEquals(1, validMap.get("Cs1ExampleStructure").size());
        assertThat(validMap.get("Cs1ExampleStructure"), containsInAnyOrder(TecmapUserAction.structureTree));
        assertEquals(2, validMap.get("Cs1ExampleAssessmentAdded").size());
        assertThat(validMap.get("Cs1ExampleAssessmentAdded"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources));
        assertEquals(3, validMap.get("Cs1Example").size());
        assertThat(validMap.get("Cs1Example"), containsInAnyOrder(TecmapUserAction.structureTree, TecmapUserAction.connectResources, TecmapUserAction.cohortTree));
    }

    @Test
    //CREATES AND DELETES THE FILES!!
    void updateTecmapResourcesTest() throws Exception {
        TecmapDatastore originalDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);

        String expectedOrigFilename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExampleAssessmentAdded/Cs1ExampleAssessmentAddedResources.json";
        String secondFilename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExampleAssessmentAdded/Cs1ExampleAssessmentAddedResources-backup-0.json";

        String updateFiles = tecmapDatastore.updateTecmapResources("Cs1ExampleAssessmentAdded", tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded").currentLearningResourceRecords());
        assertEquals(expectedOrigFilename, updateFiles);

        //Second call to make sure it adds to the file name so that it doesn't overwrite any files
        updateFiles = tecmapDatastore.updateTecmapResources("Cs1ExampleAssessmentAdded", tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded").currentLearningResourceRecords());
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