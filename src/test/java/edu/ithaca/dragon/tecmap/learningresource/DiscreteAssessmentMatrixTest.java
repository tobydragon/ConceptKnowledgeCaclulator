package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscreteAssessmentMatrixTest {

    private List<ColumnItem> columnItems;
    private GradeDiscreteGroupings defaultGroupings;
    private GradeDiscreteGroupings atriskGroupings;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        CSVReader data = new SakaiReader(testFile);
        columnItems = data.getManualGradedLearningObjects();

        defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "discreteGroupings.json");
        atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "atriskGroupings.json");
    }

    @Test
    public void discretizeAssessment() {
        DiscreteAssessmentMatrix matrix = new DiscreteAssessmentMatrix(columnItems, defaultGroupings);

        String[][] assessmentGrades = matrix.getStudentAssessmentGrades();
        assertEquals("F", assessmentGrades[0][0]);
        matrix.discretizeAssessment(assessmentGrades, columnItems.get(0), atriskGroupings);
        assertEquals("AT-RISK", assessmentGrades[0][0]);
    }

    @Test
    public void createMatrixWithOneGrouping() throws RuntimeException {
        DiscreteAssessmentMatrix matrix = new DiscreteAssessmentMatrix(columnItems, defaultGroupings);

        String[][] assessmentGrades = matrix.getStudentAssessmentGrades();

        assertEquals(10, assessmentGrades.length);
        assertEquals(6, assessmentGrades[0].length);
        //Top left corner should be s01's Q1 grade (which is 0.5, which belongs in F)
        assertEquals("F", assessmentGrades[0][0]);
        //s02's Q1 grade (which is a 1, which belongs in A)
        assertEquals("A", assessmentGrades[0][1]);
        //s06 is missing the HW5 grade (bottom right corner), so should be 0
        assertEquals("F", assessmentGrades[9][5]);
    }

    @Test
    public void createMatrixWithTwoGroupings() throws RuntimeException {
        DiscreteAssessmentMatrix matrix = new DiscreteAssessmentMatrix(columnItems, defaultGroupings, "HW5", atriskGroupings);

        String[][] assessmentGrades = matrix.getStudentAssessmentGrades();

        assertEquals(10, assessmentGrades.length);
        assertEquals(6, assessmentGrades[0].length);
        //Top left corner should be s01's Q1 grade (which is 0.5, which belongs in F)
        assertEquals("F", assessmentGrades[0][0]);
        //s02's Q1 grade (which is a 1, which belongs in A)
        assertEquals("A", assessmentGrades[0][1]);
        //s06 is missing the HW5 grade (bottom right corner), and is special assessment should be AT-RISK
        assertEquals("AT-RISK", assessmentGrades[9][5]);
    }

}
