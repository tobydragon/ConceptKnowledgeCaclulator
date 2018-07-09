package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContinuousAssessmentMatrixTest {

    private List<AssessmentItem> assessmentItems;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(testFile);
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

}
