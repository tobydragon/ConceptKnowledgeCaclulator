package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class BayesPredictorTest {

    BayesPredictor bayesPredictor;
    KnowledgeEstimateMatrix expectedMatrix;

    @Before
    public void setup() throws IOException {
        //Set up the classifier
        bayesPredictor = new BayesPredictor();

        //Set up the matrix to test against
        String file1 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment1.csv";
        String file2 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment2.csv";

        CSVReader data = new SakaiReader(file1);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();
        data = new SakaiReader(file2);
        assessments.addAll(data.getManualGradedLearningObjects());

        expectedMatrix = new KnowledgeEstimateMatrix(assessments);
    }


    @Test
    //TODO
    public void toDataFrame() {
        System.out.println(expectedMatrix.getUserIdList());
    }

    @Test
    public void learnSet() {

    }

    @Test
    public void classifySet() {

    }

}
