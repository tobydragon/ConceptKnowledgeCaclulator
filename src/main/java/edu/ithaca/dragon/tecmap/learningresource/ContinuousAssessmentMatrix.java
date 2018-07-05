package edu.ithaca.dragon.tecmap.learningresource;

import java.util.ArrayList;
import java.util.List;

public class ContinuousAssessmentMatrix {

    private List<String> assessmentIds;
    private List<String> studentIds;
    private double[][] studentAssessmentGrades;

    public ContinuousAssessmentMatrix(List<AssessmentItem> assessmentItems) {
        this.assessmentIds = getAssessmentIdList(assessmentItems);
        this.studentIds = getStudentIds(assessmentItems);
        this.studentAssessmentGrades = createMatrix(assessmentItems);
    }

    static List<String> getAssessmentIdList(List<AssessmentItem> assessmentItems) {
        List<String> assessmentIds = new ArrayList<>();

        for (AssessmentItem assessmentItem : assessmentItems) {
            assessmentIds.add(assessmentItem.getId());
        }

        return assessmentIds;
    }

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

    double[][] getStudentAssessmentGrades() {
        return studentAssessmentGrades;
    }
}
