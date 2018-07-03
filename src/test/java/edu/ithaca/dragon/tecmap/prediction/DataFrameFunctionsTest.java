package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.*;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import io.vavr.Tuple2;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataFrameFunctionsTest {

    private KnowledgeEstimateMatrix learningMatrix;
    private KnowledgeEstimateMatrix testingMatrix;

    @Before
    public void setup() throws IOException {

        //Set up the matrix to test against
        String file1 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment1.csv";
        String file2 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment2.csv";

        CSVReader data = new SakaiReader(file1);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();
        data = new SakaiReader(file2);
        assessments.addAll(data.getManualGradedLearningObjects());

        learningMatrix = new KnowledgeEstimateMatrix(assessments);

        String testFile = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1ExamplePrediction/Cs1ExampleAssessments.csv";
        data = new SakaiReader(testFile);
        assessments = data.getManualGradedLearningObjects();

        testingMatrix = new KnowledgeEstimateMatrix(assessments);
    }


    @Test
    public void toDataFrame() {
        List<String> expectedColNames = learningMatrix.getAssessmentIdList();

        DataFrame testDataframe = DataFrameFunctions.toDataFrame(learningMatrix, expectedColNames);

        StringColumn studentCol = StringColumn.ofAll(ColumnIds.StringColumnId.of("Students"), learningMatrix.getUserIdList());

        List<DoubleColumn> assessmentCols = new ArrayList<>();

        List<String> assessmentIds = new ArrayList<>();
        for (AssessmentItem assessment : learningMatrix.getObjList()) {
            assessmentIds.add(assessment.getId());
        }

        double[][] assessmentMatrix = learningMatrix.getStudentKnowledgeEstimates();
        for (int i = 0; i < assessmentMatrix.length; i++) {
            DoubleColumn assessmentColumn = DoubleColumn.ofAll(ColumnIds.DoubleColumnId.of(assessmentIds.get(i)), DoubleStream.of(assessmentMatrix[i]));
            assessmentCols.add(assessmentColumn);
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
    public void discretizeGradeColumn() {
        List<String> expectedColNames = learningMatrix.getAssessmentIdList();

        DataFrame originalDataframe = DataFrameFunctions.toDataFrame(learningMatrix, expectedColNames);

        //Check bad assessment ID
        DataFrame discretizedDataframe = DataFrameFunctions.discretizeGradeColumn(originalDataframe, "badId");
        assertNull(discretizedDataframe);

        //Check non-double column type
        discretizedDataframe = DataFrameFunctions.discretizeGradeColumn(originalDataframe, "Students");
        assertNull(discretizedDataframe);

        //Check good discretizedData
        discretizedDataframe = DataFrameFunctions.discretizeGradeColumn(originalDataframe, "Q5");
        assertNotNull(discretizedDataframe);

        ColumnIds.CategoryColumnId discreteColumnId = null;

        ColumnIds.DoubleColumnId badDoubleColumnId = null;

        for (ColumnId columnId : discretizedDataframe.getColumnIds()) {
            if (columnId.getName().equals("Q5")) {
                if (columnId.getType() == ColumnType.CATEGORY) {
                    discreteColumnId = (ColumnIds.CategoryColumnId) columnId;
                } else if (columnId.getType() == ColumnType.DOUBLE) {
                    badDoubleColumnId = (ColumnIds.DoubleColumnId) columnId;
                }
            }
        }

        //Check that the original column that was discretized is not in the discretized dataframe
        assertNull(badDoubleColumnId);

        assertEquals(discretizedDataframe.getValueAt(0, discreteColumnId), "AT-RISK");
        assertEquals(discretizedDataframe.getValueAt(1, discreteColumnId), "OK");
        assertEquals(discretizedDataframe.getValueAt(2, discreteColumnId), "AT-RISK");
    }

    @Test
    public void getGrades() {
        List<String> expectedColNames = learningMatrix.getAssessmentIdList();

        DataFrame discretizedDataframe = DataFrameFunctions.discretizeGradeColumn(DataFrameFunctions.toDataFrame(learningMatrix, expectedColNames), "Q5");

        Collection<Double> grades = DataFrameFunctions.getGrades(discretizedDataframe, 0);
        assertEquals(9, grades.size());
    }

    @Test
    public void getRowsToLearn() {
        List<String> expectedColNames = learningMatrix.getAssessmentIdList();

        DataFrame discretizedDataframe = DataFrameFunctions.discretizeGradeColumn(DataFrameFunctions.toDataFrame(learningMatrix, expectedColNames), "Q5");

        Map<String, Tuple2<String, Collection<Double>>> rows = DataFrameFunctions.getRowsToLearn(discretizedDataframe, "Q5");

        assertNotNull(rows);
        assertTrue(rows.containsKey("s01"));
        assertTrue(rows.containsKey("s02"));
        assertTrue(rows.containsKey("s03"));
        assertFalse(rows.containsKey("s04"));

        //Check for s01 (at-risk student)
        Tuple2<String, Collection<Double>> specificStudentMap = rows.get("s01");
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

        DataFrame testingDataframe = DataFrameFunctions.toDataFrame(testingMatrix, Arrays.asList(expectedColNames));

        Map<String, Collection<Double>> rows = DataFrameFunctions.getRowsToTest(testingDataframe);

        assertNotNull(rows);
        assertTrue(rows.containsKey("s04"));
        assertTrue(rows.containsKey("s05"));

        //Check collection of grades for s04 & s05
        assertEquals(9, rows.get("s04").size());
        assertEquals(9, rows.get("s05").size());
    }

}
