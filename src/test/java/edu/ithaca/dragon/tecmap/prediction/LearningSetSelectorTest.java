package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LearningSetSelectorTest {

    private ConceptGraph conceptGraph;

    private KnowledgeEstimateMatrix matrix;

    @Before
    public void setup() throws IOException {
        String filename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(filename);
        List<AssessmentItem> assessmentItems = data.getManualGradedLearningObjects();

        matrix = new KnowledgeEstimateMatrix(assessmentItems);
    }

    @Test
    public void getBaseLearningSet() {
        List<String> learningSet = LearningSetSelector.getBaseLearningSet(matrix, matrix.getUserIdList().get(0), "Q5");

        assertEquals(10, learningSet.size());
        assertTrue(learningSet.contains("Q5"));

        learningSet = LearningSetSelector.getBaseLearningSet(matrix, matrix.getUserIdList().get(5), "Q5");
        assertEquals(9, learningSet.size());
        assertTrue(learningSet.contains("Q5"));
        assertFalse(learningSet.contains("HW5"));
    }

    @Test
    public void getGraphLearningSet() {
//        List<String> learningSet = LearningSetSelector.getGraphLearningSet();
    }

}
