package edu.ithaca.dragon.tecmap.tecmapstate;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class StructureStateTest {

    BaseState baseState;

    @BeforeEach
    void setup() throws IOException {
        baseState = new BaseState(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json");
    }

    @Test
    void createStructureTree() throws JsonProcessingException {
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, baseState.createStructureTree().toJsonString());
    }

    @Test
    void createConceptIdListToPrint() {
        Collection<String> concepts = baseState.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, concepts.toString());
    }

}