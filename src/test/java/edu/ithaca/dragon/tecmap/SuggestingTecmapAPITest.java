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

    @BeforeEach
    void setup() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);
        cs1Example = tecmapDatastore.retrieveTecmapForId("Cs1Example");
    }

    @Test
    void suggestConceptsForUser() {
        assertEquals(0, cs1Example.suggestConceptsForUser("s02").size());
        List<String> s03Suggestions = cs1Example.suggestConceptsForUser("s03");
        assertEquals(1, s03Suggestions.size());
        assertThat(s03Suggestions, containsInAnyOrder("While Loops"));
        List<String> s02Suggestions = cs1Example.suggestConceptsForUser("s02");
        assertEquals(2, s02Suggestions.size());
        assertThat(s02Suggestions, containsInAnyOrder( "For Loops", "Boolean Expressions"));
    }

//    @Test
//    void suggestResourcesForUser() {
//    }
//
//    @Test
//    void suggestResourcesForSpecificConceptForUser() {
//    }
}