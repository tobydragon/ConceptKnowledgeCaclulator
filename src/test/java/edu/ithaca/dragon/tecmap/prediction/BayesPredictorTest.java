package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.*;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import io.vavr.Tuple2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.DoubleStream;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;


public class BayesPredictorTest {

    private Predictor bayes;

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

        bayes = new BayesPredictor();
    }


    @Test
    public void toDataFrame() {
        String[] expectedColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        DataFrame testDataframe = BayesPredictor.toDataFrame(learningMatrix, Arrays.asList(expectedColNames));

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

        Assert.assertThat(testDataframe.getColumnNames(), containsInAnyOrder(expectedColNames));
        assertEquals(expectedDataframe.getRowCount(), testDataframe.getRowCount());
        assertEquals(expectedDataframe.getColumnCount(), testDataframe.getColumnCount());
        assertEquals(expectedDataframe.getValueAt(0, expectedDataframe.getColumnId(0, ColumnType.STRING)), testDataframe.getValueAt(0, testDataframe.getColumnId(0, ColumnType.STRING)));
    }

    @Test
    public void discretizeGradeColumn() {
        String[] expectedColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        DataFrame originalDataframe = BayesPredictor.toDataFrame(learningMatrix, Arrays.asList(expectedColNames));

        //Check bad assessment ID
        DataFrame discretizedDataframe = BayesPredictor.discretizeGradeColumn(originalDataframe, "badId");
        assertNull(discretizedDataframe);

        //Check non-double column type
        discretizedDataframe = BayesPredictor.discretizeGradeColumn(originalDataframe, "Students");
        assertNull(discretizedDataframe);

        //Check good discretizedData
        discretizedDataframe = BayesPredictor.discretizeGradeColumn(originalDataframe, "Q5");
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
        String[] expectedColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        DataFrame discretizedDataframe = BayesPredictor.discretizeGradeColumn(BayesPredictor.toDataFrame(learningMatrix, Arrays.asList(expectedColNames)), "Q5");

        Collection<Double> grades = BayesPredictor.getGrades(discretizedDataframe, 0);
        assertEquals(9, grades.size());
    }

    @Test
    public void getRowsToLearn() {
        String[] expectedColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        DataFrame discretizedDataframe = BayesPredictor.discretizeGradeColumn(BayesPredictor.toDataFrame(learningMatrix, Arrays.asList(expectedColNames)), "Q5");

        Map<String, Tuple2<String, Collection<Double>>> rows = BayesPredictor.getRowsToLearn(discretizedDataframe, "Q5");

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
        String[] expectedColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "HW1", "HW2", "HW3", "HW4", "HW5"};

        DataFrame testingDataframe = BayesPredictor.toDataFrame(testingMatrix, Arrays.asList(expectedColNames));

        Map<String, Collection<Double>> rows = BayesPredictor.getRowsToTest(testingDataframe);

        assertNotNull(rows);
        assertTrue(rows.containsKey("s04"));
        assertTrue(rows.containsKey("s05"));

        //Check collection of grades for s04 & s05
        assertEquals(9, rows.get("s04").size());
        assertEquals(9, rows.get("s05").size());
    }

    @Test
    //TESTING BOTH LEARNSET AND CLASSIFY SET SINCE LEARNSET RETURNS VOID
    public void predictions() {
        String[] learningColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        bayes.learnSet(learningMatrix, "Q5", Arrays.asList(learningColNames));

        String[] classifyColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "HW1", "HW2", "HW3", "HW4", "HW5"};

        Map<String, String> classified = bayes.classifySet(testingMatrix, Arrays.asList(classifyColNames));

        //May not be correct just because we test with the learning data as well
        assertEquals("AT-RISK", classified.get("s04"));
        assertEquals("OK", classified.get("s05"));
    }

}
