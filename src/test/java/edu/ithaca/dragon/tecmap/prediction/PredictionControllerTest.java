package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.NoStructurePredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictor.SimplePredictor;
import io.vavr.Tuple2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PredictionControllerTest {

    private ContinuousAssessmentMatrix continuousAssessmentMatrix;
    private ConceptGraph conceptGraph;
    private GradeDiscreteGroupings atriskGroupings;
    private PredictionController testController;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        TecmapCSVReader data = new SakaiReader(testFile);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();

        continuousAssessmentMatrix = new ContinuousAssessmentMatrix(assessments);

        conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json"),
                AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(new String[]{Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv"})));

        atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "atriskGroupings.json");

        testController = new PredictionController(new SimplePredictor(atriskGroupings), new NoStructurePredictionSetSelector());
    }


    @Test
    public void splitMatrix() {
        double ratio = 0.5;
        Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix = PredictionController.splitMatrix(continuousAssessmentMatrix, ratio);

        int ratioSize = (int) Math.ceil(continuousAssessmentMatrix.getStudentIds().size()*ratio);

        assertNotNull(splitMatrix);
        //Check that it splits the number of users correctly
        assertEquals(ratioSize, splitMatrix._1.getStudentIds().size());
        assertEquals(continuousAssessmentMatrix.getStudentIds().size()-ratioSize, splitMatrix._2.getStudentIds().size());
        //Check that the ratio contains the first 3 users
        Assert.assertThat(splitMatrix._1.getStudentIds(), containsInAnyOrder(new String[] {"s01", "s02", "s03"}));
        //Check that the number of assessments stays constant
        assertEquals(10, splitMatrix._1.getAssessmentIds().size());
        assertEquals(10, splitMatrix._2.getAssessmentIds().size());
    }

    @Test
    public void getAssessmentsForStudentsWithAllResponses() {
        List<AssessmentItem> columnItems = continuousAssessmentMatrix.getColumnItems();
        List<String> assessmentsToInclude = new ArrayList<>();

        List<AssessmentItem> studentsWithResponses = PredictionController.getAssessmentsForStudentsWithAllResponses(columnItems, assessmentsToInclude);

        assertEquals(0, studentsWithResponses.size());

        //Check with an assessment that everyone has
        assessmentsToInclude.add("HW4");
        studentsWithResponses = PredictionController.getAssessmentsForStudentsWithAllResponses(columnItems, assessmentsToInclude);
        assertEquals(1, studentsWithResponses.size());
        assertEquals(1, studentsWithResponses.get(0).getMaxPossibleKnowledgeEstimate());
        assertEquals(6, studentsWithResponses.get(0).getResponses().size());
        assessmentsToInclude.remove("HW4");

        //Check with an assessment that s06 is missing
        assessmentsToInclude.add("Q2");
        studentsWithResponses = PredictionController.getAssessmentsForStudentsWithAllResponses(columnItems, assessmentsToInclude);
        //Should have omitted s06 since has no HW5
        assertEquals(1, studentsWithResponses.size());
        assertEquals(5, studentsWithResponses.get(0).getMaxPossibleKnowledgeEstimate());
        assertEquals(5, studentsWithResponses.get(0).getResponses().size());

        //Check with a second assessment that s06 is missing
        assessmentsToInclude.add("HW5");
        studentsWithResponses = PredictionController.getAssessmentsForStudentsWithAllResponses(columnItems, assessmentsToInclude);
        //Should have omitted s06 since has no HW5
        assertEquals(2, studentsWithResponses.size());
        assertEquals(1, studentsWithResponses.get(1).getMaxPossibleKnowledgeEstimate());
        assertEquals(5, studentsWithResponses.get(1).getResponses().size());

        assessmentsToInclude.add("HW3");
        studentsWithResponses = PredictionController.getAssessmentsForStudentsWithAllResponses(columnItems, assessmentsToInclude);
        //Should have omitted s06 since no HW5 & no Q2
        assertEquals(3, studentsWithResponses.size());
        assertEquals(1, studentsWithResponses.get(2).getMaxPossibleKnowledgeEstimate());
        assertEquals(5, studentsWithResponses.get(2).getResponses().size());
        assertEquals(2, studentsWithResponses.get(1).getMaxPossibleKnowledgeEstimate());
        assertEquals(5, studentsWithResponses.get(1).getResponses().size());
    }

    @Test
    public void getMatrix() {
        List<AssessmentItem> allAssessments = continuousAssessmentMatrix.getColumnItems();
        List<String> predictionSet = new ArrayList<>();
        predictionSet.add("Q4");
        ContinuousAssessmentMatrix predictionMatrix = testController.getMatrix(allAssessments, predictionSet);
        assertEquals(1, predictionMatrix.getStudentAssessmentGrades().length);
        assertEquals(6, predictionMatrix.getStudentAssessmentGrades()[0].length);
        predictionSet.add("Q2");
        predictionMatrix = testController.getMatrix(allAssessments, predictionSet);
        assertEquals(2, predictionMatrix.getStudentAssessmentGrades().length);
        assertEquals(5, predictionMatrix.getStudentAssessmentGrades()[0].length);
        assertEquals(5, predictionMatrix.getStudentAssessmentGrades()[1].length);
    }

    @Test
    public void getPredictionSet() {
        List<AssessmentItem> allAssessments = new ArrayList<>(conceptGraph.getAssessmentItemMap().values());
        String studentId = allAssessments.get(0).getResponses().get(0).getUserId();
        String assessmentToPredict = "Q5";
        List<String> predictionSet = testController.getPredictionSet(allAssessments, studentId, assessmentToPredict);

        assertEquals(10, predictionSet.size());
        assessmentToPredict = "Q4";
        predictionSet = testController.getPredictionSet(allAssessments, studentId, assessmentToPredict);
        assertEquals(10, predictionSet.size());
    }

    @Test
    public void predict() {
        List<AssessmentItem> allAssessments = new ArrayList<>(conceptGraph.getAssessmentItemMap().values());
        String studentId = allAssessments.get(0).getResponses().get(0).getUserId();
        String assessmentToPredict = "Q5";
        List<String> predictionSet = testController.getPredictionSet(allAssessments, studentId, assessmentToPredict);
        predictionSet.remove("Q5");

        Map<String, String> predictions = testController.predict(continuousAssessmentMatrix, predictionSet);
        assertEquals(6, predictions.keySet().size());
        assertEquals("AT-RISK", predictions.get("s01"));
        assertEquals("OK", predictions.get("s02"));
        assertEquals("AT-RISK", predictions.get("s03"));
        assertEquals("AT-RISK", predictions.get("s04"));
        assertEquals("OK", predictions.get("s05"));
        assertEquals("AT-RISK", predictions.get("s06"));
    }

}
