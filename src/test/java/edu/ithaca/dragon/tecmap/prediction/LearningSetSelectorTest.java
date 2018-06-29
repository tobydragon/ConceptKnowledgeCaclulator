package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class LearningSetSelectorTest {

    private static final String assessmentToPredict = "Q5";
    private ConceptGraph conceptGraph;
    private KnowledgeEstimateMatrix matrix;

    @Before
    public void setup() throws IOException {
        String filename = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(filename);
        List<AssessmentItem> assessmentItems = data.getManualGradedLearningObjects();

        matrix = new KnowledgeEstimateMatrix(assessmentItems);

        SuggestingTecmapAPI tecmap = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH).retrieveTecmapForId("Cs1Example");

        conceptGraph = new ConceptGraph(tecmap.createStructureTree());
    }

    @Test
    public void getBaseLearningSet() {
        List<String> learningSet = LearningSetSelector.getBaseLearningSet(matrix, matrix.getUserIdList().get(0), "Q5");

        assertEquals(10, learningSet.size());
        assertTrue(learningSet.contains(assessmentToPredict));

        learningSet = LearningSetSelector.getBaseLearningSet(matrix, matrix.getUserIdList().get(5), assessmentToPredict);
        assertEquals(9, learningSet.size());
        assertTrue(learningSet.contains(assessmentToPredict));
        assertFalse(learningSet.contains("HW5"));
    }

    @Test
    public void getGraphLearningSet() throws IOException {
        List<String> learningSet = LearningSetSelector.getGraphLearningSet(conceptGraph, matrix.getUserIdList().get(0), assessmentToPredict);

        assertEquals(7, learningSet.size());
        Assert.assertThat(learningSet, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5", "Q4"));
        assertFalse(learningSet.contains("Q1"));
        assertFalse(learningSet.contains("HW3"));
        assertFalse(learningSet.contains("Q5"));
    }

}
