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

public class PredictorEffectivenessTest {

    private KnowledgeEstimateMatrix knowledgeMatrix;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(testFile);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();

        knowledgeMatrix = new KnowledgeEstimateMatrix(assessments);
    }

    @Test
    public void splitMatrix() {

    }

    @Test
    public void testPredictor() {

    }

}
