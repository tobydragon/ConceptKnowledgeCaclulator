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
import io.vavr.Tuple2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PredictorEffectivenessTest {

    private KnowledgeEstimateMatrix knowledgeMatrix;
    private ConceptGraph conceptGraph;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(testFile);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();

        knowledgeMatrix = new KnowledgeEstimateMatrix(assessments);

        conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                LearningResourceRecord.buildListFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json"),
                AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(new String[] {Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv"})));
    }

    @Test
    public void splitMatrix() {
        double ratio = 0.5;
        Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix = PredictorEffectiveness.splitMatrix(knowledgeMatrix, ratio);

        int ratioSize = (int) Math.ceil(knowledgeMatrix.getUserIdList().size()*ratio);

        assertNotNull(splitMatrix);
        //Check that it splits the number of users correctly
        assertEquals(ratioSize, splitMatrix._1.getUserIdList().size());
        assertEquals(knowledgeMatrix.getUserIdList().size()-ratioSize, splitMatrix._2.getUserIdList().size());
        //Check that the ratio contains the first 3 users
        Assert.assertThat(splitMatrix._1.getUserIdList(), containsInAnyOrder(new String[] {"s01", "s02", "s03"}));
        //Check that the number of assessments stays constant
        assertEquals(10, splitMatrix._1.getAssessmentIdList().size());
        assertEquals(10, splitMatrix._2.getAssessmentIdList().size());

    }

    @Test
    public void testLearningPredictor() throws IOException {
        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();

        PredictorEffectiveness testPredictor = PredictorEffectiveness.testLearningPredictor(new BayesPredictor(), baseLearningSetSelector, "Q5" , conceptGraph, 0.5);

        assertEquals((double) 2/3, testPredictor.getPercentCorrect());

        List<PredictionResult> results = testPredictor.getResults();
        assertEquals(3, results.size());
        PredictionResult studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());

        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        testPredictor = PredictorEffectiveness.testLearningPredictor(new BayesPredictor(), graphLearningSetSelector, "Q5", conceptGraph, 0.5);

        results = testPredictor.getResults();
        assertEquals(3, results.size());
        studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
    }

    @Test
    public void testPredictor() throws IOException {
        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();

        PredictorEffectiveness testPredictor = PredictorEffectiveness.testPredictor(new SimplePredictor(), baseLearningSetSelector, "Q5", conceptGraph, 0.5);

        List<PredictionResult> results = testPredictor.getResults();
        assertEquals(3, results.size());
        PredictionResult studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());

        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        testPredictor = PredictorEffectiveness.testPredictor(new SimplePredictor(), graphLearningSetSelector, "Q5", conceptGraph, 0.5);

        results = testPredictor.getResults();
        assertEquals(3, results.size());
        studentResult = results.get(0);
        assertEquals("s04", studentResult.getStudentId());
        assertEquals("AT-RISK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
        studentResult = results.get(2);
        assertEquals("s05", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("OK", studentResult.getPredictedResult());
        studentResult = results.get(1);
        assertEquals("s06", studentResult.getStudentId());
        assertEquals("OK", studentResult.getExpectedResult());
        assertEquals("AT-RISK", studentResult.getPredictedResult());
    }

}
