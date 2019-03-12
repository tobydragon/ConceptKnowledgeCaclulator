package edu.ithaca.dragon.tecmap.learningresource;

import com.github.rcaller.rstuff.RCode;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.RFunctions;

import java.util.ArrayList;
import java.util.List;

public class ContinuousAssessmentMatrix {

    private List<AssessmentItem> columnItems;
    private List<String> assessmentIds;
    private List<String> studentIds;
    private double[][] studentAssessmentGrades;

    public ContinuousAssessmentMatrix(List<AssessmentItem> columnItems) {
        this.columnItems = columnItems;
        this.assessmentIds = getAssessmentIdList(columnItems);
        this.studentIds = getStudentIds(columnItems);
        this.studentAssessmentGrades = createMatrix(columnItems);
    }

    /**
     * Gets a list of strings of all of the assessmentIds from a list of assessment items
     * @param columnItems
     * @return assessmentId list
     */
    static List<String> getAssessmentIdList(List<AssessmentItem> columnItems) {
        List<String> assessmentIds = new ArrayList<>();

        for (AssessmentItem columnItem : columnItems) {
            assessmentIds.add(columnItem.getId());
        }

        return assessmentIds;
    }

    /**
     * Gets a list of strings of all of the studentIds from a list of assessment items' responses in the order
     * that the studentIds appear
     * @param columnItems
     * @return studentId list
     */
    static List<String> getStudentIds(List<AssessmentItem> columnItems) {
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
     * @param columnItems
     * @return 2d array of knowledge estimates
     */
    double[][] createMatrix(List<AssessmentItem> columnItems) {
        double[][] gradeMatrix = new double[this.assessmentIds.size()][this.studentIds.size()];
        for (AssessmentItem columnItem : columnItems) {
            int assessmentIndex = assessmentIds.indexOf(columnItem.getId());
            List<String> studentsWithResponse = new ArrayList<>();
            for (AssessmentItemResponse response : columnItem.getResponses()) {
                String currUserId = response.getUserId();
                int studentIndex = studentIds.indexOf(currUserId);
                gradeMatrix[assessmentIndex][studentIndex] = response.calcKnowledgeEstimate();
                studentsWithResponse.add(currUserId);
            }
            List<String> studentsWithoutResponse = new ArrayList<>(studentIds);
            studentsWithoutResponse.removeAll(studentsWithResponse);
            for (String studentId : studentsWithoutResponse) {
                gradeMatrix[assessmentIndex][studentIds.indexOf(studentId)] = 0.0;
            }
        }

        return gradeMatrix;
    }

    public RCode createRMatrix(double[][] studentKnowledgeEstimates){

        int objLength = columnItems.size();

        //object list into string array

        int i = 0;
        String[] objStr = new String[objLength];
        for(AssessmentItem obj: columnItems){
            objStr[i] = obj.getId();
            i++;
        }


        RCode rMatrix = RFunctions.JavaToR(studentKnowledgeEstimates, objStr);
        return rMatrix;
    }

    public List<AssessmentItem> getColumnItems() {
        return columnItems;
    }

    public double[][] getStudentAssessmentGrades() {
        return studentAssessmentGrades;
    }

    public List<String> getAssessmentIds() {
        return assessmentIds;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }
}
