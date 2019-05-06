package edu.ithaca.dragon.tecmap.suggester;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.BucketSuggester;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Group;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Suggester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class TecmapSuggesterAPITest {

    private TecmapSuggesterAPI cs1ExampleSuggester;
    private TecmapSuggesterAPI notConnectedExampleSuggester;

    @BeforeEach
    void setup() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
        cs1ExampleSuggester = new TecmapSuggester(tecmapDatastore.retrieveTecmapForId("Cs1Example"));
        notConnectedExampleSuggester = new TecmapSuggester(tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded"));
    }

    @Test
    void suggestConceptsForUser() {
        assertEquals(0, cs1ExampleSuggester.suggestConceptsForUser("s02").size());
        List<String> s03Suggestions = cs1ExampleSuggester.suggestConceptsForUser("s03");
        assertThat(s03Suggestions, containsInAnyOrder("While Loops"));
        assertEquals(1, s03Suggestions.size());

        List<String> s01Suggestions = cs1ExampleSuggester.suggestConceptsForUser("s01");
        assertThat(s01Suggestions, containsInAnyOrder( "For Loops", "If Statements", "While Loops"));
        assertEquals(3, s01Suggestions.size());
        //TODO this should suggest below, but doesn't because ConceptGraphSuggesterLibrary.suggestConcepts doesn't suggest things with 0 estimate
//        assertThat(s01Suggestions, containsInAnyOrder( "For Loops", "Boolean Expressions"));


        assertNull(cs1ExampleSuggester.suggestConceptsForUser("badID"));
        assertNull(notConnectedExampleSuggester.suggestConceptsForUser("s01"));
    }

    //    @Test
    void suggestResourcesForUser() throws JsonProcessingException {
        System.out.println(Json.toJsonString(cs1ExampleSuggester.suggestResourcesForUser("s01")));
    }

    //    @Test
    void suggestResourcesForSpecificConceptForUser() throws JsonProcessingException {
        System.out.println(Json.toJsonString(cs1ExampleSuggester.suggestResourcesForSpecificConceptForUser("s01", "Loops")));
    }

    @Test
    void suggestGroups() throws Exception {
        //setup group buckets
        List<List<Integer>> ranges = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        temp.add(0);
        temp.add(50);
        List<Integer> temp2 = new ArrayList<>();
        temp2.add(50);
        temp2.add(80);
        List<Integer> temp3 = new ArrayList<>();
        temp3.add(80);
        temp3.add(100);
        ranges.add(temp);
        ranges.add(temp2);
        ranges.add(temp3);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BucketSuggester(ranges));

        List<Group> groupings = null;
        groupings = cs1ExampleSuggester.suggestGroups(suggesterList, 2);

        assertEquals(groupings.get(0).getSize(), 1);
        assertEquals(groupings.get(0).getStudentNames().get(0), "s01");

        assertEquals(groupings.get(1).getSize(), 0);

        assertEquals(groupings.get(2).getSize(), 2);
        assertTrue(groupings.get(2).getStudentNames().contains("s02"));
        assertTrue(groupings.get(2).getStudentNames().contains("s03"));
    }
}
