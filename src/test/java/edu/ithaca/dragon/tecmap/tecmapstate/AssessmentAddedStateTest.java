package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.reader.ReaderTools;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentAddedStateTest {

    @Test
    void createBlankLearningResourceRecordsFromAssessment1File() throws IOException {
        List<String> assessmentFiles = new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv"));
        AssessmentAddedState state = new AssessmentAddedState(
                new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json")),
                //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
                ReaderTools.learningObjectsFromCSVList(2, assessmentFiles),
                AssessmentItemResponse.createAssessmentItemResponses(assessmentFiles)

        );
        assertEquals(Cs1ExampleJsonStrings.assessment1Str, Json.toJsonString(state.createBlankLearningResourceRecordsFromAssessment()));
    }

    @Test
    void createBlankLearningResourceRecordsFromAssessment2Files() throws IOException {
        List<String> assessmentFiles = new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv"));

        AssessmentAddedState state = new AssessmentAddedState(
                new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json")),
                //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
                ReaderTools.learningObjectsFromCSVList(2, assessmentFiles),
                AssessmentItemResponse.createAssessmentItemResponses(assessmentFiles)

        );

        //System.out.println(Json.toJsonString(state.currentLearningResourceRecords()));
        assertEquals(Cs1ExampleJsonStrings.assessment1And2Str, Json.toJsonString(state.createBlankLearningResourceRecordsFromAssessment()));
    }

}