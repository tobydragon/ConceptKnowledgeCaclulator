package edu.ithaca.dragon.tecmap.learningresource;

import java.util.ArrayList;
import java.util.List;

public class ContinuousAssessmentMatrix {

    private List<AssessmentItem> assessmentItems;
    private List<String> assessmentIds;
    private List<String> studentIds;
    private double[][] studentAssessmentGrades;

    public ContinuousAssessmentMatrix(List<AssessmentItem> assessmentItems) {
        this.assessmentItems = assessmentItems;
        this.assessmentIds = getAssessmentIdList(assessmentItems);
        this.studentIds = getStudentIds(assessmentItems);
        this.studentAssessmentGrades = createMatrix(assessmentItems);
    }

    /**
     * Gets a list of strings of all of the assessmentIds from a list of assessment items
     * @param assessmentItems
     * @return assessmentId list
     */
    static List<String> getAssessmentIdList(List<AssessmentItem> assessmentItems) {
        List<String> assessmentIds = new ArrayList<>();

        for (AssessmentItem assessmentItem : assessmentItems) {
            assessmentIds.add(assessmentItem.getId());
        }

        return assessmentIds;
    }

    /**
     * Gets a list of strings of all of the studentIds from a list of assessment items' responses in the order
     * that the studentIds appear
     * @param assessmentItems
     * @return studentId list
     */
    static List<String> getStudentIds(List<AssessmentItem> assessmentItems) {
        List<String> userIds = new ArrayList<>();

        for (AssessmentItem assessmentItem : assessmentItems) {
            for (AssessmentItemResponse response : assessmentItem.getResponses()) {
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
        double[][] gradeMatrix = new double[this.assessmentIds.size()][this.studentIds.size()];
        for (AssessmentItem assessmentItem : assessmentItems) {
            int assessmentIndex = assessmentIds.indexOf(assessmentItem.getId());
            List<String> studentsWithResponse = new ArrayList<>();
            for (AssessmentItemResponse response : assessmentItem.getResponses()) {
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

    public List<AssessmentItem> getAssessmentItems() {
        return assessmentItems;
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
