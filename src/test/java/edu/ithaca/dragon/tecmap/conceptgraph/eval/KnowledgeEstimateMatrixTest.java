package edu.ithaca.dragon.tecmap.conceptgraph.eval;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by bleblanc2 on 6/13/17.
 */
public class KnowledgeEstimateMatrixTest {

    @Test
    public void createMatrixTest() {

        //String file = Settings.TEST_RESOURCE_DIR + "io/DataCSVExample.csv";

        String file = Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv";

        try {
            TecmapCSVReader data = new SakaiReader(file);
            List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
            double[][] myMatrix = newMatrix.getStudentKnowledgeEstimates();
            double[] arr1 = new double[9];
            double[] arr2 = new double[9];
            double[] arr3 = new double[9];


            for (int i = 0; i < 9; i++) {
                arr1[i] = myMatrix[i][0];
            }

            for (int i = 0; i < 9; i++) {
                arr2[i] = myMatrix[i][1];
            }

            for (int i = 0; i < 9; i++) {
                arr3[i] = myMatrix[i][2];
            }


            double[] exArr1 = new double[]{1, 1, 0.815, 0.7, 0, 0, 1, 1, 1};
            double[] exArr2 = new double[]{1, 1, 0.85, 1, 0, 0.9, 1, 1, 1};
            double[] exArr3 = new double[]{1, 1, 0.98, 1, 0.975, 1, 1, 1, 1};


            //System.out.println(Arrays.toString(exArr2) +"---" + Arrays.toString(arr2));

            //KnowledgeEstimateMatrix Check
            Assert.assertArrayEquals(exArr1, arr1, 0);
            Assert.assertArrayEquals(exArr2, arr2, 0);
            Assert.assertArrayEquals(exArr3, arr3, 0);

            //userIdList Check
            List<String> actualString = new LinkedList<String>();
            actualString.add("stu1");
            actualString.add("stu2");
            actualString.add("stu3");
//            Assert.assertEquals(actualString, newMatrix.getUserIdList());

        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void getAssessmentIdList() throws IOException{
        String file = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        TecmapCSVReader data = new SakaiReader(file);
        List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);

        List<String> assessmentIds = newMatrix.getAssessmentIdList();

        String[] expected = {"Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        assertEquals(10, assessmentIds.size());
        Assert.assertThat(assessmentIds, containsInAnyOrder(expected));
    }

    @Test
    public void createMatrixWithRepeatedAssessmentItems() throws IOException {
        String file = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment1.csv";
        TecmapCSVReader data = new SakaiReader(file);

        List<AssessmentItem> columnItems = data.getManualGradedLearningObjects();
        file = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment2.csv";
        data = new SakaiReader(file);
        columnItems.addAll(data.getManualGradedLearningObjects());

        //Try and add repeated assessments
        file = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        data = new SakaiReader(file);
        columnItems.addAll(data.getManualGradedLearningObjects());


        assertThrows(IOException.class, () -> {
            KnowledgeEstimateMatrix matrix = new KnowledgeEstimateMatrix(columnItems);
        });

    }
}

