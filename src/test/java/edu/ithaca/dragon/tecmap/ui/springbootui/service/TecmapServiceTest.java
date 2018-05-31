package edu.ithaca.dragon.tecmap.ui.springbootui.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class TecmapServiceTest {

    private TecmapService tecmapService;

    @Before
    public void setup() throws IOException {
        tecmapService = new TecmapService();
    }

    @Test
    public void retrieveStructureTree() throws JsonProcessingException{
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, tecmapService.retrieveStructureTree().toJsonString());
    }

    @Test
    public void retrieveConceptIdList() {
        Collection<String> concepts = tecmapService.retrieveConceptIdList();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, concepts.toString());
    }
}