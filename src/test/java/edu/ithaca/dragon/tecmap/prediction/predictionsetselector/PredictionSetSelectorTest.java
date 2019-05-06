package edu.ithaca.dragon.tecmap.prediction.predictionsetselector;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
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
        TecmapCSVReader data = new SakaiReader(filename);
        List<AssessmentItem> columnItems = data.getManualGradedLearningObjects();

        matrix = new ContinuousAssessmentMatrix(columnItems);

        conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json"),
                AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv")));
    }

    @Test
    public void getPredictionSetWithBaseSelector() {
        List<AssessmentItem> allAssessments = matrix.getColumnItems();
        PredictionSetSelector basePredictionSetSelector = new NoStructurePredictionSetSelector();
        List<String> predictionSet = basePredictionSetSelector.getPredictionSetForGivenStudent(allAssessments, matrix.getStudentIds().get(0), "Q5");

        assertEquals(10, predictionSet.size());
        assertTrue(predictionSet.contains(assessmentToPredict));

        predictionSet = basePredictionSetSelector.getPredictionSetForGivenStudent(allAssessments, matrix.getStudentIds().get(5), assessmentToPredict);
        assertEquals(8, predictionSet.size());
        assertTrue(predictionSet.contains(assessmentToPredict));
        assertFalse(predictionSet.contains("HW5"));
    }

    @Test
    public void getPredictionSetWithGraphSelector() {
        PredictionSetSelector graphPredictionSetSelector = new GraphPredictionSetSelector(conceptGraph);
        String studentId = matrix.getStudentIds().get(0);
        List<AssessmentItem> allAssessments = matrix.getColumnItems();
        //Checks with student with all grades
        //Check the entire graph
        List<String> predictionSet = graphPredictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, assessmentToPredict);

        assertEquals(10, predictionSet.size());
        Assert.assertThat(predictionSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4", "Q1", "HW3", "Q5"));

        //Check with something further down the graph
        predictionSet = graphPredictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId,"Q4");

        assertEquals(7, predictionSet.size());
        Assert.assertThat(predictionSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        assertFalse(predictionSet.contains("Q1"));
        assertFalse(predictionSet.contains("HW3"));
        assertFalse(predictionSet.contains("Q5"));

        //Checks with student missing HW5
        studentId = matrix.getStudentIds().get(5);
        predictionSet = graphPredictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, assessmentToPredict);
        assertEquals(8, predictionSet.size());
        assertFalse(predictionSet.contains("HW5"));
    }

    @Test
    public void getPredictionSetWithGraphSelectorNoAssessmentsGiven() {
        GraphPredictionSetSelector graphPredictionSetSelector = new GraphPredictionSetSelector(conceptGraph);
        String studentId = matrix.getStudentIds().get(0);

        List<String> predictionSet = graphPredictionSetSelector.getPredictionSetForGivenStudent(studentId, "Q4");
        assertEquals(7, predictionSet.size());
        Assert.assertThat(predictionSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        assertFalse(predictionSet.contains("Q1"));
        assertFalse(predictionSet.contains("HW3"));
        assertFalse(predictionSet.contains("Q5"));

        //Checks with student missing HW5
        studentId = matrix.getStudentIds().get(5);
        predictionSet = graphPredictionSetSelector.getPredictionSetForGivenStudent(studentId, assessmentToPredict);
        assertEquals(8, predictionSet.size());
        assertFalse(predictionSet.contains("HW5"));
    }

    //Test for both NoStructure & Graph PredictionSetSelectors
    @Test
    public void removeLowestResponseRateAssessment() {
        PredictionSetSelector predictionSetSelector = new NoStructurePredictionSetSelector();
        String studentId = matrix.getStudentIds().get(0);
        List<AssessmentItem> allAssessments = matrix.getColumnItems();

        //Test with the NoStructure selector
        List<String> predictionSet = predictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, "Q4");
        assertEquals(10, predictionSet.size());
        predictionSetSelector.removeLowestResponseRateAssessment(allAssessments, predictionSet, "Q4");
        assertEquals(9, predictionSet.size());
        Assert.assertThat(predictionSet, containsInAnyOrder("HW4", "HW1", "HW2", "Q3", "HW5", "Q4", "Q1", "HW3", "Q5"));

        //Test with the Graph selector
        predictionSetSelector = new GraphPredictionSetSelector(conceptGraph);
        predictionSet = predictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, "Q4");
        assertEquals(7, predictionSet.size());
        Assert.assertThat(predictionSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        predictionSetSelector.removeLowestResponseRateAssessment(allAssessments, predictionSet, "Q4");
        assertEquals(6, predictionSet.size());
        Assert.assertThat(predictionSet, containsInAnyOrder("HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));

        //Test that if all assessments have all responses, nothing gets removed
        predictionSet = predictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, "Q1");
        assertEquals(4, predictionSet.size());
        predictionSetSelector.removeLowestResponseRateAssessment(allAssessments, predictionSet, "Q1");
        assertEquals(4, predictionSet.size());
    }

}
