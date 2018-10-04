package edu.ithaca.dragon.tecmap.io.record;

import com.github.rcaller.rstuff.RCode;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.RFunctions;
import edu.ithaca.dragon.tecmap.learningresource.ColumnItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 9/18/2018.
 * This class represents all information that can be held as a matrix of doubles
 * Student(row) Assessment(column) grades(matrix) or Factor(row) Assessment(column) Link Strength(matrix)
 */
public class ContinuousMatrixRecord {

    private List<ColumnItem> columnItems;
    private List<String> columnIds;
    private List<String> rowIds;
    private double[][] dataMatrix;

    public ContinuousMatrixRecord(List<ColumnItem> columnItems) {
        this.columnItems = columnItems;
        this.columnIds = getColumnIdList(columnItems);
        this.rowIds = getRowIds(columnItems);
        this.dataMatrix = createMatrix(columnItems);
    }

    public ContinuousMatrixRecord(double[][] dataMatrix, List<ColumnItem> columnItems, List<String> rowIds){
        this.columnItems = columnItems;
        this.columnIds = getColumnIdList(columnItems);
        this.rowIds = rowIds;
        this.dataMatrix = dataMatrix;

    }

    /**
     * Gets a list of strings of all of the assessmentIds from a list of assessment items
     * @param columnItems
     * @return assessmentId list
     */
    static List<String> getColumnIdList(List<ColumnItem> columnItems) {
        List<String> assessmentIds = new ArrayList<>();

        for (ColumnItem columnItem : columnItems) {
            assessmentIds.add(columnItem.getId());
        }

        return assessmentIds;
    }

    /**
     * Gets a list of strings of all of the studentIds from a list of assessment items' responses in the order
     * that the studentIds appear
     * @param columnItems
     * @return rowId list
     */
    static List<String> getRowIds(List<ColumnItem> columnItems) {
        List<String> userIds = new ArrayList<>();

        for (ColumnItem columnItem : columnItems) {
            for (AssessmentItemResponse response : columnItem.getResponses()) {
                String currId = response.getUserId();
                if (!userIds.contains(currId)) {
                    userIds.add(currId);
                }
            }
        }

        return userIds;
    }

    /**
     * Creates a matrix of the continuous variable knowledge estimates for each assessment and user pair
     * Follows the indices given from the assessmentItem and the studentId list
     * If no response for a given student, defaults to an estimate of 0
     * @param columnItems
     * @return 2d array of knowledge estimates
     */
    double[][] createMatrix(List<ColumnItem> columnItems) {
        double[][] gradeMatrix = new double[this.columnIds.size()][this.rowIds.size()];
        for (ColumnItem columnItem : columnItems) {
            int assessmentIndex = columnIds.indexOf(columnItem.getId());
            List<String> studentsWithResponse = new ArrayList<>();
            for (AssessmentItemResponse response : columnItem.getResponses()) {
                String currUserId = response.getUserId();
                int studentIndex = rowIds.indexOf(currUserId);
                gradeMatrix[assessmentIndex][studentIndex] = response.calcKnowledgeEstimate();
                studentsWithResponse.add(currUserId);
            }
            List<String> studentsWithoutResponse = new ArrayList<>(rowIds);
            studentsWithoutResponse.removeAll(studentsWithResponse);
            for (String studentId : studentsWithoutResponse) {
                gradeMatrix[assessmentIndex][rowIds.indexOf(studentId)] = 0.0;
            }
        }

        return gradeMatrix;
    }

    public RCode createRMatrix(double[][] studentKnowledgeEstimates){

        int objLength = columnItems.size();

        //object list into string array

        int i = 0;
        String[] objStr = new String[objLength];
        for(ColumnItem obj: columnItems){
            objStr[i] = obj.getId();
            i++;
        }


        RCode rMatrix = RFunctions.JavaToR(studentKnowledgeEstimates, objStr);
        return rMatrix;
    }

    public List<ColumnItem> getColumnItems() {
        return columnItems;
    }

    public double[][] getDataMatrix() {
        return dataMatrix;
    }

    public List<String> getAssessmentIds() {
        return columnIds;
    }

    public List<String> getStudentIds() {
        return rowIds;
    }
}