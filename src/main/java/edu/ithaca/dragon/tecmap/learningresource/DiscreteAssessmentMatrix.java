package edu.ithaca.dragon.tecmap.learningresource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscreteAssessmentMatrix {

    private List<String> assessmentIds;
    private List<String> studentIds;
    private String[][] studentAssessmentGrades;

    public DiscreteAssessmentMatrix(List<AssessmentItem> assessmentItems, String groupingsFilename) throws IOException {
        this.assessmentIds = ContinuousAssessmentMatrix.getAssessmentIdList(assessmentItems);
        this.studentIds = ContinuousAssessmentMatrix.getStudentIds(assessmentItems);
        this.studentAssessmentGrades = createMatrix(assessmentItems, groupingsFilename);
    }

    String[][] createMatrix(List<AssessmentItem> assessmentItems, String filename) throws IOException {
        GradeDiscreteGroupings discreteGroupings = GradeDiscreteGroupings.buildFromJson(filename);
        List<String> groupings = discreteGroupings.getGroups();
        List<Integer> gradePointBreaks = discreteGroupings.getPointBreaks();
        if (groupings.size()-1 != gradePointBreaks.size()) {
            throw new IOException("There must be one less grade Point Break than groups in " + filename);
        }
        String defaultLowestGrade = groupings.get(gradePointBreaks.size());

        String[][] discreteGradesMatrix = new String[assessmentIds.size()][studentIds.size()];
        for (AssessmentItem assessmentItem : assessmentItems) {
            int assessmentIndex = assessmentIds.indexOf(assessmentItem.getId());
            List<String> studentsWithResponse = new ArrayList<>();
            for (AssessmentItemResponse response : assessmentItem.getResponses()) {
                String currUserId = response.getUserId();
                int studentIndex = studentIds.indexOf(currUserId);
                studentsWithResponse.add(currUserId);
                String discretizedGrade = null;
                for (int i = 0; i < gradePointBreaks.size(); i++) {
                    if (response.calcKnowledgeEstimate()*100 >= gradePointBreaks.get(i)) {
                        discretizedGrade = groupings.get(i);
                    }
                }
                if (discretizedGrade == null) {
                    discretizedGrade = defaultLowestGrade;
                }
                discreteGradesMatrix[assessmentIndex][studentIndex] = discretizedGrade;
            }
            List<String> studentsWithoutResponses = new ArrayList<>(studentIds);
            studentsWithoutResponses.removeAll(studentsWithResponse);
            for (String studentId : studentsWithoutResponses) {
                discreteGradesMatrix[assessmentIndex][studentIds.indexOf(studentId)] = defaultLowestGrade;
            }
        }

        return discreteGradesMatrix;
    }

    public String[][] getStudentAssessmentGrades() {
        return studentAssessmentGrades;
    }
}
