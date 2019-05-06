package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
import edu.ithaca.dragon.tecmap.io.record.ContinuousMatrixRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContinuousAssessmentMatrixTest {

    private List<AssessmentItem> assessmentItems;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        TecmapCSVReader data = new SakaiReader(testFile);
        assessmentItems = data.getManualGradedLearningObjects();
    }

    @Test
    public void getAssessmentIdList() {
        List<String> assessmentIds = ContinuousAssessmentMatrix.getAssessmentIdList(assessmentItems);

        String[] expected = {"Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        assertEquals(10, assessmentIds.size());
        Assert.assertThat(assessmentIds, containsInAnyOrder(expected));
    }

    @Test
    public void getUserIds() {
        List<String> userIds = ContinuousAssessmentMatrix.getStudentIds(assessmentItems);

        String[] expected = {"s01", "s02", "s03", "s04", "s05", "s06"};

        assertEquals(6, userIds.size());
        Assert.assertThat(userIds, containsInAnyOrder(expected));
    }

    @Test
    public void createMatrix() {
        ContinuousAssessmentMatrix matrix = new ContinuousAssessmentMatrix(assessmentItems);

        double[][] assessmentGrades = matrix.getStudentAssessmentGrades();


        assertEquals(10, assessmentGrades.length);
        assertEquals(6, assessmentGrades[0].length);
        //Top left corner should be s01's Q1 grade (which is 0.5)
        assertEquals(0.5, assessmentGrades[0][0]);
        //s06 is missing the HW5 grade (bottom right corner), so should be 0
        assertEquals(0, assessmentGrades[9][5]);
    }

    /**This test should make sure that even if the AssessmentItems are not in the same order as it is
     * read in from the CSV, the AssessmentItems still match up with the dataMatrix
     */
    @Test
    public void noRelianceOnIndicesTest(){
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
            ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);
            double[][] dataMatrix = assessmentMatrix.getDataMatrix();
            List<String> students = assessmentMatrix.getRowIds();
            List<String> originalAssessmentOrder = new ArrayList<>();
            originalAssessmentOrder.add("PINBALL");
            originalAssessmentOrder.add("BILLIARD");
            originalAssessmentOrder.add("GOLF");
            originalAssessmentOrder.add("X.1500M");
            originalAssessmentOrder.add("X.2KROW");
            originalAssessmentOrder.add("X.12MINTR");
            originalAssessmentOrder.add("BENCH");
            originalAssessmentOrder.add("CURL");
            originalAssessmentOrder.add("MAXPUSHU");

            List<String> newAssessmentOrder = new ArrayList<>();
            for(AssessmentItem assessment : assessmentItems){
                newAssessmentOrder.add(assessment.getId());
            }

            //Lists should not be the same order in order to properly test next tests
            if(newAssessmentOrder == originalAssessmentOrder){
                Assert.fail();
            }

            int indexOfPINBALL = newAssessmentOrder.indexOf("PINBALL");
            int indexOfStudent1 = students.indexOf("1");
            assertEquals(dataMatrix[0][5], dataMatrix[indexOfStudent1][indexOfPINBALL]);

            int indexOfBENCH = newAssessmentOrder.indexOf("BENCH");
            int indexOfStudent42 = students.indexOf("42");
            assertEquals(dataMatrix[41][0], dataMatrix[indexOfStudent42][indexOfBENCH]);

        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }

}
