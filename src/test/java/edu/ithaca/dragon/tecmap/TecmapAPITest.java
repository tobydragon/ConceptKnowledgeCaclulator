package edu.ithaca.dragon.tecmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TecmapAPITest {

    private TecmapAPI onlyStructureTecmap;
    private TecmapAPI twoAssessmentsAddedTecmap;
    private TecmapAPI twoAssessmentsConnectedTecmap;

    @BeforeEach
    void setup()throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE, Settings.TEST_ROOT_PATH);

        onlyStructureTecmap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.noAssessment);
        twoAssessmentsAddedTecmap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.assessmentAdded);
        twoAssessmentsConnectedTecmap = tecmapDatastore.retrieveTecmapForId("Cs1Example", TecmapState.assessmentConnected);
    }

    @Test
    void createStructureTree() throws JsonProcessingException {
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, onlyStructureTecmap.createStructureTree().toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, twoAssessmentsAddedTecmap.createStructureTree().toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, twoAssessmentsConnectedTecmap.createStructureTree().toJsonString());
    }

    @Test
    void createConceptIdListToPrint() {
        Collection<String> onlyStructureConcepts = onlyStructureTecmap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, onlyStructureConcepts.toString());
        Collection<String> twoAssessmentsAddedConcepts = twoAssessmentsAddedTecmap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsAddedConcepts.toString());
        Collection<String> twoAssessmentsConnectedConcepts = twoAssessmentsConnectedTecmap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
    }

    @Test
    void createBlankLearningResourceRecordsFromAssessment2Files() throws IOException {
        assertEquals(0, onlyStructureTecmap.createBlankLearningResourceRecordsFromAssessment().size());
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsAddedTecmap.createBlankLearningResourceRecordsFromAssessment()));
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsConnectedTecmap.createBlankLearningResourceRecordsFromAssessment()));
    }

    @Test
    void createCohortTree() throws JsonProcessingException {
        assertNull(onlyStructureTecmap.createCohortTree());
        assertNull(twoAssessmentsAddedTecmap.createCohortTree());

        List<ConceptGraphRecord> records =  twoAssessmentsConnectedTecmap.createCohortTree().getGraphRecords();
        assertEquals(4, records.size());
        for (ConceptGraphRecord record : records){
            //Bart
            if (record.getName().equals("s02")){
                //System.out.println(Json.toJsonString(record));
                assertEquals(Cs1ExampleJsonStrings.bartDataTree, Json.toJsonString(record));
            }
        }
    }
}