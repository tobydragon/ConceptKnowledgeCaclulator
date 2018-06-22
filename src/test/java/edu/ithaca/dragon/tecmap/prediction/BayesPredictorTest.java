package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BayesPredictorTest {

    BayesPredictor bayesPredictor;
    KnowledgeEstimateMatrix expectedMatrix;

    @BeforeEach
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

        System.out.println(expectedMatrix.getUserIdList());
    }


    @Test
    //TODO
    public void toDataFrame() {

        assertEquals(null, null);
        assertNull(null);
    }

    @Test
    public void learnSet() {

    }

    @Test
    public void classifySet() {

    }

}
