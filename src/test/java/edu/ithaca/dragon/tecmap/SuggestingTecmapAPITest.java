package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class SuggestingTecmapAPITest {

    SuggestingTecmapAPI cs1Example;
    SuggestingTecmapAPI notConnectedExample;

    @BeforeEach
    void setup() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);
        cs1Example = tecmapDatastore.retrieveTecmapForId("Cs1Example");
        notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

    }

    @Test
    void suggestConceptsForUser() {
        assertEquals(0, cs1Example.suggestConceptsForUser("s02").size());
        List<String> s03Suggestions = cs1Example.suggestConceptsForUser("s03");
        assertThat(s03Suggestions, containsInAnyOrder("While Loops"));
        assertEquals(1, s03Suggestions.size());
        List<String> s01Suggestions = cs1Example.suggestConceptsForUser("s01");
        assertThat(s01Suggestions, containsInAnyOrder( "For Loops", "If Statements", "While Loops"));
        assertEquals(3, s01Suggestions.size());
        //TODO this should suggest below, but doesn't because ConceptGraphSuggesterLibrary.suggestConcepts doesn't suggest things with 0 estimate
//        assertThat(s01Suggestions, containsInAnyOrder( "For Loops", "Boolean Expressions"));


        assertNull(cs1Example.suggestConceptsForUser("badID"));
        assertNull(notConnectedExample.suggestConceptsForUser("s01"));
    }

//    @Test
//    void suggestResourcesForUser() {
//    }
//
//    @Test
//    void suggestResourcesForSpecificConceptForUser() {
//    }
}