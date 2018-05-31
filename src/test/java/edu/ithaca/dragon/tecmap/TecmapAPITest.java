package edu.ithaca.dragon.tecmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.io.writer.Json;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TecmapAPITest {

    private TecmapAPI onlyStructureTecmap;
    private TecmapAPI twoAssessmentsAddedTecmap;

    @BeforeEach
    void setup() throws IOException {
        onlyStructureTecmap = new Tecmap(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json");
        twoAssessmentsAddedTecmap = new Tecmap(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
                )
        );
    }

    @Test
    void createStructureTree() throws JsonProcessingException {
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, onlyStructureTecmap.createStructureTree().toJsonString());
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, twoAssessmentsAddedTecmap.createStructureTree().toJsonString());
    }

    @Test
    void createConceptIdListToPrint() {
        Collection<String> onlyStructureConcepts = onlyStructureTecmap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, onlyStructureConcepts.toString());
        Collection<String> twoAssessmentsAddedConcepts = onlyStructureTecmap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsAddedConcepts.toString());
    }

    @Test
    void createBlankLearningResourceRecordsFromAssessment2Files() throws IOException {
        assertEquals(0, onlyStructureTecmap.createBlankLearningResourceRecordsFromAssessment().size());
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(twoAssessmentsAddedTecmap.createBlankLearningResourceRecordsFromAssessment()));
    }
}