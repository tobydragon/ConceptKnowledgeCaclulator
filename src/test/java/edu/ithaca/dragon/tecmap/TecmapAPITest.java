package edu.ithaca.dragon.tecmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
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
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);

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
        Collection<String> onlyStructureConcepts = onlyStructureTecmap.conceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, onlyStructureConcepts.toString());
        Collection<String> twoAssessmentsAddedConcepts = twoAssessmentsAddedTecmap.conceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsAddedConcepts.toString());
        Collection<String> twoAssessmentsConnectedConcepts = twoAssessmentsConnectedTecmap.conceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
    }

    @Test
    void currentLearningResourceRecords() throws IOException {
        assertEquals(0, onlyStructureTecmap.currentLearningResourceRecords().size());
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsAddedTecmap.currentLearningResourceRecords()));
        assertEquals(Cs1ExampleJsonStrings.resourcesConnectedString, Json.toJsonString(twoAssessmentsConnectedTecmap.currentLearningResourceRecords()));
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

    @Test
    void getAverageConceptGraph() {
        assertNull(onlyStructureTecmap.getAverageConceptGraph());
        assertNull(twoAssessmentsAddedTecmap.getAverageConceptGraph());

        ConceptGraph avgGraph = twoAssessmentsConnectedTecmap.getAverageConceptGraph();
        //Assert that it has data
        assertEquals(10, avgGraph.getAssessmentItemMap().values().size());
    }


}