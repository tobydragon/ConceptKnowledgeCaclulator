package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class LearningSetSelectorTest {

    private static final String assessmentToPredict = "Q5";
    private ConceptGraph conceptGraph;
    private KnowledgeEstimateMatrix matrix;

    @Before
    public void setup() throws IOException {
        String filename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(filename);
        List<AssessmentItem> assessmentItems = data.getManualGradedLearningObjects();

        matrix = new KnowledgeEstimateMatrix(assessmentItems);

        conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                LearningResourceRecord.buildListFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json"),
                AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(new String[] {Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv"})));
    }

    @Test
    public void getBaseLearningSet() {
        List<String> learningSet = LearningSetSelector.getBaseLearningSet(matrix, matrix.getUserIdList().get(0), "Q5");

        assertEquals(10, learningSet.size());
        assertTrue(learningSet.contains(assessmentToPredict));

        learningSet = LearningSetSelector.getBaseLearningSet(matrix, matrix.getUserIdList().get(5), assessmentToPredict);
        assertEquals(9, learningSet.size());
        assertTrue(learningSet.contains(assessmentToPredict));
        assertFalse(learningSet.contains("HW5"));
    }

    @Test
    public void getGraphLearningSet() throws IOException {
        String studentId = matrix.getUserIdList().get(0);
        //Checks with student with all grades
        //Check the entire graph
        List<String> learningSet = LearningSetSelector.getGraphLearningSet(conceptGraph, studentId, assessmentToPredict);

        assertEquals(10, learningSet.size());
        Assert.assertThat(learningSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4", "Q1", "HW3", "Q5"));

        //Check with something further down the graph
        learningSet = LearningSetSelector.getGraphLearningSet(conceptGraph, studentId,"Q4");

        assertEquals(7, learningSet.size());
        Assert.assertThat(learningSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        assertFalse(learningSet.contains("Q1"));
        assertFalse(learningSet.contains("HW3"));
        assertFalse(learningSet.contains("Q5"));

        //Checks with student missing HW5
        studentId = matrix.getUserIdList().get(5);
        learningSet = LearningSetSelector.getGraphLearningSet(conceptGraph, studentId, assessmentToPredict);
        assertEquals(9, learningSet.size());
        assertFalse(learningSet.contains("HW5"));
    }

}
