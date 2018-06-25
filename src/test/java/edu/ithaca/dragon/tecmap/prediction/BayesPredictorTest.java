package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.*;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;


public class BayesPredictorTest {

    KnowledgeEstimateMatrix expectedMatrix;

    @Before
    public void setup() throws IOException {

        //Set up the matrix to test against
        String file1 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment1.csv";
        String file2 = Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment2.csv";

        CSVReader data = new SakaiReader(file1);
        List<AssessmentItem> assessments = data.getManualGradedLearningObjects();
        data = new SakaiReader(file2);
        assessments.addAll(data.getManualGradedLearningObjects());

        expectedMatrix = new KnowledgeEstimateMatrix(assessments);
    }


    @Test
    public void toDataFrame() {
        DataFrame testDataframe = BayesPredictor.toDataFrame(expectedMatrix);

        StringColumn studentCol = StringColumn.ofAll(ColumnIds.StringColumnId.of("Students"), expectedMatrix.getUserIdList());

        List<DoubleColumn> assessmentCols = new ArrayList<>();

        List<String> assessmentIds = new ArrayList<>();
        for (AssessmentItem assessment : expectedMatrix.getObjList()) {
            assessmentIds.add(assessment.getId());
        }

        double[][] assessmentMatrix = expectedMatrix.getStudentKnowledgeEstimates();
        for (int i = 0; i < assessmentMatrix.length; i++) {
            DoubleColumn assessmentColumn = DoubleColumn.ofAll(ColumnIds.DoubleColumnId.of(assessmentIds.get(i)), DoubleStream.of(assessmentMatrix[i]));
            assessmentCols.add(assessmentColumn);
        }

        List<Column<?>> allCols = new ArrayList<>();
        allCols.add(studentCol);
        allCols.addAll(assessmentCols);

        DataFrame expectedDataframe = DataFrame.ofAll(allCols);

        String[] expectedColNames = {"Students", "Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"};

        Assert.assertThat(testDataframe.getColumnNames(), containsInAnyOrder(expectedColNames));
        assertEquals(expectedDataframe.getRowCount(), testDataframe.getRowCount());
        assertEquals(expectedDataframe.getColumnCount(), testDataframe.getColumnCount());
        assertEquals(expectedDataframe.getValueAt(0, expectedDataframe.getColumnId(0, ColumnType.STRING)), testDataframe.getValueAt(0, testDataframe.getColumnId(0, ColumnType.STRING)));
    }

    @Test
    public void discretizeGradeColumn() {
        DataFrame originalDataframe = BayesPredictor.toDataFrame(expectedMatrix);

        //Check bad assessment ID
        DataFrame discretizedDataframe = BayesPredictor.discretizeGradeColumn(originalDataframe, "badId");
        assertNull(discretizedDataframe);

        //Check non-double column type
        discretizedDataframe = BayesPredictor.discretizeGradeColumn(originalDataframe, "Students");
        assertNull(discretizedDataframe);

        //Check good discretizedData
        discretizedDataframe = BayesPredictor.discretizeGradeColumn(originalDataframe, "Q1");
        assertNotNull(discretizedDataframe);

        ColumnIds.CategoryColumnId discreteColumnId = null;

        ColumnIds.DoubleColumnId badDoubleColumnId = null;

        for (ColumnId columnId : discretizedDataframe.getColumnIds()) {
            if (columnId.getName().equals("Q1")) {
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
        assertEquals(discretizedDataframe.getValueAt(2, discreteColumnId), "OK");
    }

    @Test
    public void getRowsToLearn() {
        DataFrame discretizedDataframe = BayesPredictor.discretizeGradeColumn(BayesPredictor.toDataFrame(expectedMatrix), "Q1");

        Map<String, Collection<Double>> rows = BayesPredictor.getRowsToLearn(discretizedDataframe, "Q1");

        assertNotNull(rows);
        assertTrue(rows.containsKey("OK"));
        assertTrue(rows.containsKey("AT-RISK"));
        assertNotNull(rows.get("OK"));
        assertNotNull(rows.get("AT-RISK"));
    }

    @Test
    public void learnSet() {

    }

    @Test
    public void classifySet() {

    }

}
