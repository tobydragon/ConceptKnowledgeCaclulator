package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.writer.Json;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentItemsAddedStateTest {

    @Test
    void createBlankLearningResourceRecordsFromAssessment1File() throws IOException {
        AssessmentItemsAddedState state = new AssessmentItemsAddedState(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv"))
        );
        assertEquals(Cs1ExampleJsonStrings.assessment1Str, Json.toJsonString(state.createBlankLearningResourceRecordsFromAssessment()));
    }

    @Test
    void createBlankLearningResourceRecordsFromAssessment2Files() throws IOException {
        AssessmentItemsAddedState state = new AssessmentItemsAddedState(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
                )
        );
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(state.createBlankLearningResourceRecordsFromAssessment()));
    }

}