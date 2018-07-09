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
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import io.vavr.Tuple2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PredictorEffectivenessTest {

    private ContinuousAssessmentMatrix continuousAssessmentMatrix;
    private ConceptGraph conceptGraph;
    private GradeDiscreteGroupings defaultGroupings;
    private GradeDiscreteGroupings atriskGroupings;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(testFile);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();

        continuousAssessmentMatrix = new ContinuousAssessmentMatrix(assessments);

        conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                LearningResourceRecord.buildListFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json"),
                AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(new String[] {Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv"})));

        defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "discreteGroupings.json");
        atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "atriskGroupings.json");
    }

    @Test
    public void splitMatrix() {
        double ratio = 0.5;
        Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix = PredictorEffectiveness.splitMatrix(continuousAssessmentMatrix, ratio);

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
    public void getPredictionResults() {
        //Hand-Written Predictions Based on continuousAssessmentMatrix
        Map<String, String> predicted = new HashMap<>();
        predicted.put("s04", "AT-RISK");
        predicted.put("s05", "OK");
        predicted.put("s06", "AT-RISK");
        List<PredictionResult> predictionResults = PredictorEffectiveness.getPredictionResults(continuousAssessmentMatrix, "Q5", atriskGroupings, predicted);
        for (PredictionResult predictionResult : predictionResults) {
            if (predictionResult.getStudentId().equals("s04")) {
                assertEquals(Result.TRUE_POSITIVE, predictionResult.getResult());
            } else if (predictionResult.getStudentId().equals("s05")) {
                assertEquals(Result.TRUE_NEGATIVE, predictionResult.getResult());
            } else if (predictionResult.getStudentId().equals("s06")) {
                assertEquals(Result.FALSE_POSITIVE, predictionResult.getResult());
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void getPredictorEffectivenessFromResults() {
        //Hand-Written Predictions Based on continuousAssessmentMatrix
        Map<String, String> predicted = new HashMap<>();
        predicted.put("s04", "AT-RISK");
        predicted.put("s05", "OK");
        predicted.put("s06", "AT-RISK");
        List<PredictionResult> predictionResults = PredictorEffectiveness.getPredictionResults(continuousAssessmentMatrix, "Q5", atriskGroupings, predicted);
        PredictorEffectiveness testPredictor = PredictorEffectiveness.getPredictorEffectivenessFromResults(predictionResults);
        assertEquals(3, testPredictor.getTotalTested());
        assertEquals(1, testPredictor.getTruePositive().size());
        assertEquals(1, testPredictor.getTrueNegative().size());
        assertEquals(1, testPredictor.getFalsePositive().size());
    }

    @Test
    public void testLearningPredictor() throws IOException {
        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();

        PredictorEffectiveness testPredictor = PredictorEffectiveness.testLearningPredictor(new BayesPredictor(defaultGroupings, atriskGroupings), baseLearningSetSelector, "Q5" , conceptGraph, atriskGroupings, 0.5);

        assertEquals((double) 2/3, testPredictor.getPercentCorrect());

        List<PredictionResult> results = testPredictor.getAllResults();
        assertEquals(3, results.size());
        PredictionResult studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        assertEquals(1, testPredictor.getTruePositive().size());
        assertEquals(1, testPredictor.getTrueNegative().size());
        assertEquals(1, testPredictor.getFalsePositive().size());

        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        testPredictor = PredictorEffectiveness.testLearningPredictor(new BayesPredictor(defaultGroupings, atriskGroupings), graphLearningSetSelector, "Q5", conceptGraph, atriskGroupings, 0.5);

        results = testPredictor.getAllResults();
        assertEquals(3, results.size());
        studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        assertEquals(1, testPredictor.getTruePositive().size());
        assertEquals(1, testPredictor.getTrueNegative().size());
        assertEquals(1, testPredictor.getFalsePositive().size());
    }

    @Test
    public void testPredictor() throws IOException {
        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();

        PredictorEffectiveness testPredictor = PredictorEffectiveness.testPredictor(new SimplePredictor(atriskGroupings), baseLearningSetSelector, "Q5", conceptGraph, atriskGroupings,0.5);

        List<PredictionResult> results = testPredictor.getAllResults();
        assertEquals(3, results.size());
        PredictionResult studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        assertEquals(1, testPredictor.getTruePositive().size());
        assertEquals(1, testPredictor.getTrueNegative().size());
        assertEquals(1, testPredictor.getFalsePositive().size());

        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        testPredictor = PredictorEffectiveness.testPredictor(new SimplePredictor(atriskGroupings), graphLearningSetSelector, "Q5", conceptGraph, atriskGroupings, 0.5);

        results = testPredictor.getAllResults();
        assertEquals(3, results.size());
        studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        assertEquals(1, testPredictor.getTruePositive().size());
        assertEquals(1, testPredictor.getTrueNegative().size());
        assertEquals(1, testPredictor.getFalsePositive().size());
    }

}
