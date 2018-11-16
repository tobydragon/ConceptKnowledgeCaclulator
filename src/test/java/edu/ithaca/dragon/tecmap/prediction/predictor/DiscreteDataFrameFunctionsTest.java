package edu.ithaca.dragon.tecmap.prediction.predictor;

import ch.netzwerg.paleo.*;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.DiscreteAssessmentMatrix;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import io.vavr.Tuple2;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DiscreteDataFrameFunctionsTest {

    private DiscreteAssessmentMatrix testingMatrix;
    private GradeDiscreteGroupings defaultGroupings;
    private GradeDiscreteGroupings atriskGroupings;

    @Before
    public void setup() throws IOException {

        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        TecmapCSVReader data = new SakaiReader(testFile);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();

        defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "discreteGroupings.json");
        atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "atriskGroupings.json");

        testingMatrix = new DiscreteAssessmentMatrix(assessments, defaultGroupings, "Q5", atriskGroupings);
    }


    @Test
    public void toDataFrame() {
        List<String> expectedColNames = testingMatrix.getAssessmentIds();

        DataFrame testDataframe = DiscreteDataFrameFunctions.toDataFrame(testingMatrix, expectedColNames);

        StringColumn studentCol = StringColumn.ofAll(ColumnIds.StringColumnId.of("Students"), testingMatrix.getStudentIds());

        List<CategoryColumn> assessmentCols = new ArrayList<>();

        List<String> assessmentIds = testingMatrix.getAssessmentIds();

        String[][] assessmentMatrix = testingMatrix.getStudentAssessmentGrades();
        for (int i = 0; i < assessmentMatrix.length; i++) {
            CategoryColumn assessmentCol = CategoryColumn.ofAll(ColumnIds.CategoryColumnId.of(assessmentIds.get(i)), assessmentMatrix[i]);
            assessmentCols.add(assessmentCol);
        }

        List<Column<?>> allCols = new ArrayList<>();
        allCols.add(studentCol);
        allCols.addAll(assessmentCols);

        DataFrame expectedDataframe = DataFrame.ofAll(allCols);

        assertEquals(expectedDataframe.getRowCount(), testDataframe.getRowCount());
        assertEquals(expectedDataframe.getColumnCount(), testDataframe.getColumnCount());
        assertEquals(expectedDataframe.getValueAt(0, expectedDataframe.getColumnId(0, ColumnType.STRING)), testDataframe.getValueAt(0, testDataframe.getColumnId(0, ColumnType.STRING)));
    }

    @Test
    public void getGrades() {
        List<String> expectedColNames = testingMatrix.getAssessmentIds();

        DataFrame discretizedDataframe = DiscreteDataFrameFunctions.toDataFrame(testingMatrix, expectedColNames);

        Collection<String> grades = DiscreteDataFrameFunctions.getGrades(discretizedDataframe, expectedColNames,0);
        assertEquals(10, grades.size());
    }

    @Test
    public void getRowsToLearn() {
        List<String> expectedColNames = testingMatrix.getAssessmentIds();

        DataFrame discretizedDataframe = DiscreteDataFrameFunctions.toDataFrame(testingMatrix, expectedColNames);

        Map<String, Tuple2<String, Collection<String>>> rows = DiscreteDataFrameFunctions.getRowsToLearn(discretizedDataframe, expectedColNames,"Q5");

        assertNotNull(rows);
        assertTrue(rows.containsKey("s01"));
        assertTrue(rows.containsKey("s02"));
        assertTrue(rows.containsKey("s03"));

        //Check for s01 (at-risk student)
        Tuple2<String, Collection<String>> specificStudentMap = rows.get("s01");
        assertEquals("AT-RISK", specificStudentMap._1);
        assertEquals(9, specificStudentMap._2.size());

        //Check for s02 (ok student)
        specificStudentMap = rows.get("s02");
        assertEquals("OK", specificStudentMap._1);
        assertEquals(9, specificStudentMap._2.size());
    }

    @Test
    public void getRowsToTest() {
        String[] expectedColNames = {"Q1", "Q2", "Q3", "Q4", "HW1", "HW2", "HW3", "HW4", "HW5"};

        DataFrame testingDataframe = DiscreteDataFrameFunctions.toDataFrame(testingMatrix, Arrays.asList(expectedColNames));

        Map<String, Collection<String>> rows = DiscreteDataFrameFunctions.getRowsToTest(testingDataframe, Arrays.asList(expectedColNames));

        assertNotNull(rows);
        assertTrue(rows.containsKey("s04"));
        assertTrue(rows.containsKey("s05"));

        //Check collection of grades for s04 & s05
        assertEquals(9, rows.get("s04").size());
        assertEquals(9, rows.get("s05").size());
    }
}
