package edu.ithaca.dragon.tecmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TecmapAPITest {

    TecmapAPI tecmap;

    @BeforeEach
    void setup() throws IOException {
        tecmap = new Tecmap(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json");
    }

    @Test
    void createStructureTree() throws JsonProcessingException {
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, tecmap.createStructureTree().toJsonString());
    }

    @Test
    void createConceptIdList() {
        List<String> concepts = tecmap.createConceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, concepts.toString());
    }


}