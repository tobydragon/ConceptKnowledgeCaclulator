package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContinuousMatrixRecordTest {

    private List<AssessmentItem> assessmentItems;

    @Before
    public void setup() throws IOException {
        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "AnalysisExample/AnalysisExample1.csv";
        CSVReader data = new SakaiReader(testFile);
        assessmentItems = data.getManualGradedLearningObjects();
    }

    @Test
    public void createMatrix() {
        ContinuousMatrixRecord matrix = new ContinuousMatrixRecord(assessmentItems);

        double[][] assessmentGrades = matrix.getDataMatrix();

        List<String> students = matrix.getRowIds();
        for(String i:students){
            System.out.print(i + " ");
        }

        System.out.println();

        int rows = assessmentGrades.length;
        int cols = assessmentGrades[0].length;
        for(int i = 0; i<rows; i++)
        {
            for(int j = 0; j<cols; j++)
            {
                System.out.print(assessmentGrades[i][j] + "  ");
            }
            System.out.println();
        }



        assertEquals(5, assessmentGrades.length);
        assertEquals(7, assessmentGrades[0].length);
        //Top left corner should be s01's Q1 grade (which is 0.5)
        assertEquals(0.9, assessmentGrades[0][0]);
        assertEquals(0.252, assessmentGrades[3][5]);
        assertEquals(.95, assessmentGrades[4][6]);
    }



}
