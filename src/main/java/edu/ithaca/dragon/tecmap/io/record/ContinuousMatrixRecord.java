package edu.ithaca.dragon.tecmap.io.record;

import com.github.rcaller.rstuff.RCode;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.RFunctions;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 9/18/2018.
 * This class represents all information that can be held as a matrix of doubles
 * Student(row) Assessment(column) grades(matrix) or Factor(column) Assessment(row) Link Strength(matrix)
 */
public class ContinuousMatrixRecord {

    private List<AssessmentItem> assessmentItems;
    private List<String> assessmentIds;
    private List<String> rowIds;
    private double[][] dataMatrix;

    public ContinuousMatrixRecord(List<AssessmentItem> assessmentItems) {
        this.assessmentItems = assessmentItems;
        this.assessmentIds = getAssessmentIds(assessmentItems);
        this.rowIds = getRowIds(assessmentItems);
        this.dataMatrix = createMatrix(assessmentItems);
    }


    public ContinuousMatrixRecord(double[][] dataMatrix, List<String> factorList, List<AssessmentItem> assessmentItems){
        this.assessmentItems = assessmentItems;
        this.assessmentIds = factorList;
        this.rowIds = getAssessmentIds(assessmentItems);
        this.dataMatrix = dataMatrix;

    }

    /**
     * Gets a list of strings of all of the assessmentIds from a list of assessment items
     * @param columnItems
     * @return assessmentId list
     */
    static List<String> getAssessmentIds(List<AssessmentItem> columnItems) {
        List<String> assessmentIds = new ArrayList<>();

        for (AssessmentItem columnItem : columnItems) {
            assessmentIds.add(columnItem.getId());
        }

        return assessmentIds;
    }

    /**
     * Gets a list of strings of all of the assessmentIds from a list of assessment items' responses in the order
     * that the assessmentIds appear
     * @param columnItems
     * @return rowId list
     */
    static List<String> getRowIds(List<AssessmentItem> columnItems) {
        List<String> userIds = new ArrayList<>();

        for (AssessmentItem columnItem : columnItems) {
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
     * @param assessmentItems
     * @return 2d array of knowledge estimates
     */
    double[][] createMatrix(List<AssessmentItem> assessmentItems) {
        double[][] gradeMatrix = new double[this.rowIds.size()][this.assessmentIds.size()];
        for (AssessmentItem columnItem : assessmentItems) {
            int assessmentIndex = assessmentIds.indexOf(columnItem.getId());
            List<String> studentsWithResponse = new ArrayList<>();
            for (AssessmentItemResponse response : columnItem.getResponses()) {
                String currUserId = response.getUserId();
                int studentIndex = rowIds.indexOf(currUserId);
                gradeMatrix[studentIndex][assessmentIndex] = response.calcKnowledgeEstimate();
                studentsWithResponse.add(currUserId);
            }
            List<String> studentsWithoutResponse = new ArrayList<>(rowIds);
            studentsWithoutResponse.removeAll(studentsWithResponse);
            for (String studentId : studentsWithoutResponse) {
                gradeMatrix[rowIds.indexOf(studentId)][assessmentIndex] = 0.0;
            }
        }

        return gradeMatrix;
    }

    public RCode createRMatrix(double[][] studentKnowledgeEstimates){

        int objLength = assessmentItems.size();

        //object list into string array

        int i = 0;
        String[] objStr = new String[objLength];
        for(AssessmentItem obj: assessmentItems){
            objStr[i] = obj.getId();
            i++;
        }


        RCode rMatrix = RFunctions.JavaToR(studentKnowledgeEstimates, objStr);
        return rMatrix;
    }

    public List<AssessmentItem> getAssessmentItems() {
        return assessmentItems;
    }

    public double[][] getDataMatrix() {
        return dataMatrix;
    }

    public List<String> getAssessmentIds() {
        return assessmentIds;
    }

    public List<String> getRowIds() {
        return rowIds;
    }
}