package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BayesPredictorTest {

    private KnowledgeEstimateMatrix learningMatrix;
    private KnowledgeEstimateMatrix testingMatrix;

    @Before
    public void setup() throws IOException {

        //Set up the matrix to test against
        String file1 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment1.csv";
        String file2 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment2.csv";

        CSVReader data = new SakaiReader(file1);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();
        data = new SakaiReader(file2);
        assessments.addAll(data.getManualGradedLearningObjects());

        learningMatrix = new KnowledgeEstimateMatrix(assessments);

        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        data = new SakaiReader(testFile);
        assessments = data.getManualGradedLearningObjects();

        testingMatrix = new KnowledgeEstimateMatrix(assessments);
    }

    @Test
    //TESTING BOTH LEARNSET AND CLASSIFY SET SINCE LEARNSET RETURNS VOID
    public void predictions() {
        LearningPredictor bayes = new BayesPredictor();

        List<String> learningColNames = learningMatrix.getAssessmentIdList();

        bayes.learnSet(learningMatrix, "Q5", learningColNames);

        String[] classifyColNames = {"Q1", "Q2", "Q3", "Q4", "HW1", "HW2", "HW3", "HW4", "HW5"};

        Map<String, String> classified = bayes.classifySet(testingMatrix, Arrays.asList(classifyColNames));

        assertEquals("AT-RISK", classified.get("s04"));
        assertEquals("OK", classified.get("s05"));
    }

}
