package edu.ithaca.dragon.tecmap.tecmapstate;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class NoAssessmentStateTest {

    NoAssessmentState noAssessmentState;

    @BeforeEach
    void setup() throws IOException {
        noAssessmentState = new NoAssessmentState(new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json")));
    }

    @Test
    void createStructureTree() throws JsonProcessingException {
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, noAssessmentState.createStructureTree().toJsonString());
    }

    @Test
    void createConceptIdListToPrint() {
        Collection<String> concepts = noAssessmentState.conceptIdList();
        //System.out.println(concepts.toString());
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, concepts.toString());
    }

}