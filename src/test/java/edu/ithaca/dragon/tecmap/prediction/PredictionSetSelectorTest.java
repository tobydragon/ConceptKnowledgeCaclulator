package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class PredictionSetSelectorTest {

    private static final String assessmentToPredict = "Q5";
    private ContinuousAssessmentMatrix matrix;
    private ConceptGraph conceptGraph;

    @Before
    public void setup() throws IOException {
        String filename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(filename);
        List<AssessmentItem> assessmentItems = data.getManualGradedLearningObjects();

        matrix = new ContinuousAssessmentMatrix(assessmentItems);

        conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json"),
                AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv")));
    }

    @Test
    public void getLearningSetWithBaseSelector() {
        List<AssessmentItem> allAssessments = matrix.getAssessmentItems();
        PredictionSetSelector basePredictionSetSelector = new NoStructurePredictionSetSelector();
        List<String> learningSet = basePredictionSetSelector.getLearningSetForGivenStudent(allAssessments, matrix.getStudentIds().get(0), "Q5");

        assertEquals(10, learningSet.size());
        assertTrue(learningSet.contains(assessmentToPredict));

        learningSet = basePredictionSetSelector.getLearningSetForGivenStudent(allAssessments, matrix.getStudentIds().get(5), assessmentToPredict);
        assertEquals(8, learningSet.size());
        assertTrue(learningSet.contains(assessmentToPredict));
        assertFalse(learningSet.contains("HW5"));
    }

    @Test
    public void getLearningSetWithGraphSelector() {
        PredictionSetSelector graphPredictionSetSelector = new GraphPredictionSetSelector(conceptGraph);
        String studentId = matrix.getStudentIds().get(0);
        List<AssessmentItem> allAssessments = matrix.getAssessmentItems();
        //Checks with student with all grades
        //Check the entire graph
        List<String> learningSet = graphPredictionSetSelector.getLearningSetForGivenStudent(allAssessments, studentId, assessmentToPredict);

        assertEquals(10, learningSet.size());
        Assert.assertThat(learningSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4", "Q1", "HW3", "Q5"));

        //Check with something further down the graph
        learningSet = graphPredictionSetSelector.getLearningSetForGivenStudent(allAssessments, studentId,"Q4");

        assertEquals(7, learningSet.size());
        Assert.assertThat(learningSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        assertFalse(learningSet.contains("Q1"));
        assertFalse(learningSet.contains("HW3"));
        assertFalse(learningSet.contains("Q5"));

        //Checks with student missing HW5
        studentId = matrix.getStudentIds().get(5);
        learningSet = graphPredictionSetSelector.getLearningSetForGivenStudent(allAssessments, studentId, assessmentToPredict);
        assertEquals(8, learningSet.size());
        assertFalse(learningSet.contains("HW5"));
    }

    @Test
    public void getLearningSetWithGraphSelectorNoAssessmentsGiven() {
        GraphPredictionSetSelector graphLearningSetSelector = new GraphPredictionSetSelector(conceptGraph);
        String studentId = matrix.getStudentIds().get(0);

        List<String> learningSet = graphLearningSetSelector.getLearningSetForGivenStudent(studentId, "Q4");
        assertEquals(7, learningSet.size());
        Assert.assertThat(learningSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        assertFalse(learningSet.contains("Q1"));
        assertFalse(learningSet.contains("HW3"));
        assertFalse(learningSet.contains("Q5"));

        //Checks with student missing HW5
        studentId = matrix.getStudentIds().get(5);
        learningSet = graphLearningSetSelector.getLearningSetForGivenStudent(studentId, assessmentToPredict);
        assertEquals(8, learningSet.size());
        assertFalse(learningSet.contains("HW5"));
    }

}
