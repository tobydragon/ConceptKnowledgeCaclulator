package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimplePredictorTest {

    private KnowledgeEstimateMatrix testingMatrix;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(testFile);
        List<AssessmentItem> assessmentItemList = new ArrayList<>();
        assessmentItemList.addAll(data.getManualGradedLearningObjects());

        testingMatrix = new KnowledgeEstimateMatrix(assessmentItemList);
    }

    //Going to Predict Q5
    @Test
    public void classifySet() {
        Predictor simple = new SimplePredictor();

        List<String> classifyingSet = testingMatrix.getAssessmentIdList();

        //Remove the assessment you are trying to predict
        classifyingSet.remove("Q5");

        Map<String, String> classified = simple.classifySet(testingMatrix, classifyingSet);

        assertEquals("AT-RISK", classified.get("s04"));
        assertEquals("OK", classified.get("s05"));
        assertEquals("AT-RISK", classified.get("s06"));
    }

}
