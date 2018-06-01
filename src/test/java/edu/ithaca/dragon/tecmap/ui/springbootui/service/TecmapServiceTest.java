package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.Tecmap;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.writer.Json;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
public class TecmapServiceTest {

    private TecmapService onlyStructureTecmapService;
    private TecmapService twoAssessmentsAddedTecmapService;
    private TecmapService twoAssessmentsConnectedTecmapService;

    @Before
    public void setup() throws IOException {
        Tecmap onlyStructureTecmap = new Tecmap(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json");
        Tecmap twoAssessmentsAddedTecmap = new Tecmap(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
                )
        );

        Tecmap twoAssessmentsConnectedTecmap = new Tecmap(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleResources.json")),
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
                )
        );
        onlyStructureTecmapService = new TecmapService(onlyStructureTecmap);
        twoAssessmentsAddedTecmapService = new TecmapService(twoAssessmentsAddedTecmap);
        twoAssessmentsConnectedTecmapService = new TecmapService(twoAssessmentsConnectedTecmap);
    }

    @Test
    public void retrieveStructureTree() throws JsonProcessingException{
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, onlyStructureTecmapService.retrieveStructureTree().toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, twoAssessmentsAddedTecmapService.retrieveStructureTree().toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, twoAssessmentsConnectedTecmapService.retrieveStructureTree().toJsonString());
    }

    @Test
    public void retrieveConceptIdList() {
        List<String> onlyStructureConcepts = onlyStructureTecmapService.retrieveConceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, onlyStructureConcepts.toString());
        List<String> twoAssessmentsAddedConcepts = twoAssessmentsAddedTecmapService.retrieveConceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsAddedConcepts.toString());
        List<String> twoAssessmentsConnectedConcepts = twoAssessmentsConnectedTecmapService.retrieveConceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
    }

    @Test
    public void retrieveBlankLearningResourceRecordsFromAssessment() throws JsonProcessingException{
        assertEquals(0, onlyStructureTecmapService.retrieveBlankLearningResourceRecordsFromAssessment().size());
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsAddedTecmapService.retrieveBlankLearningResourceRecordsFromAssessment()));
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsConnectedTecmapService.retrieveBlankLearningResourceRecordsFromAssessment()));
    }

    @Test
    public void retrieveCohortTree() throws JsonProcessingException{
        assertNull(onlyStructureTecmapService.retrieveCohortTree());
        assertNull(twoAssessmentsAddedTecmapService.retrieveCohortTree());

        CohortConceptGraphsRecord cohortRecord = twoAssessmentsConnectedTecmapService.retrieveCohortTree();
        List<ConceptGraphRecord> records = cohortRecord.getGraphRecords();
        assertEquals(4, records.size());
        for (ConceptGraphRecord record : records ) {
            if (record.getName().equals("s02")){
                assertEquals(Cs1ExampleJsonStrings.bartDataTree, Json.toJsonString(record));
            }
        }
    }
}