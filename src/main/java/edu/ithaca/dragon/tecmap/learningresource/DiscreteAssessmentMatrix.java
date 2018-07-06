package edu.ithaca.dragon.tecmap.learningresource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscreteAssessmentMatrix {

    private List<String> assessmentIds;
    private List<String> studentIds;
    private String[][] studentAssessmentGrades;

    public DiscreteAssessmentMatrix(List<AssessmentItem> assessmentItems, GradeDiscreteGroupings groupings) {
        this.assessmentIds = ContinuousAssessmentMatrix.getAssessmentIdList(assessmentItems);
        this.studentIds = ContinuousAssessmentMatrix.getStudentIds(assessmentItems);
        this.studentAssessmentGrades = createMatrix(assessmentItems, groupings);
    }

    public DiscreteAssessmentMatrix(List<AssessmentItem> assessmentItems, GradeDiscreteGroupings defaultGrouping, String specialDiscreteAssessment, GradeDiscreteGroupings specialGrouping) throws RuntimeException {
        this.assessmentIds = ContinuousAssessmentMatrix.getAssessmentIdList(assessmentItems);
        this.studentIds = ContinuousAssessmentMatrix.getStudentIds(assessmentItems);
        if (!assessmentIds.contains(specialDiscreteAssessment)) {
            throw new RuntimeException("Cannot group assessment not in assessmentItems list");
        }
        this.studentAssessmentGrades = createMatrix(assessmentItems, defaultGrouping, specialDiscreteAssessment, specialGrouping);
    }

    void discretizeAssessment(String[][] discreteGradesMatrix, AssessmentItem assessmentItem, GradeDiscreteGroupings discreteGroupings) throws RuntimeException{
        List<String> groupings = discreteGroupings.getGroups();
        List<Integer> gradePointBreaks = discreteGroupings.getPointBreaks();
        if (groupings.size()-1 != gradePointBreaks.size()) {
            throw new RuntimeException("There must be one less grade Point Break than groups in discreteGroupings");
        }
        String defaultLowestGrade = groupings.get(gradePointBreaks.size());
        int assessmentIndex = assessmentIds.indexOf(assessmentItem.getId());
        List<String> studentsWithResponse = new ArrayList<>();
        for (AssessmentItemResponse response : assessmentItem.getResponses()) {
            String currUserId = response.getUserId();
            int studentIndex = studentIds.indexOf(currUserId);
            studentsWithResponse.add(currUserId);
            String discretizedGrade = null;
            for (int i = 0; i < gradePointBreaks.size(); i++) {
                if (response.calcKnowledgeEstimate()*100 >= gradePointBreaks.get(i) && discretizedGrade == null) {
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

    /**
     * Creates a matrix of the discrete categorical variable from grade groups from a given file for each assessment and user pair
     * Follows the indices given from the assessmentItem and the studentId list
     * If no response for a given student, defaults to the lowest grade group
     * @param assessmentItems
     * @param discreteGroupings groupings object
     * @return 2d array of strings (the discretized grade)
     * @throws IOException
     */
    String[][] createMatrix(List<AssessmentItem> assessmentItems, GradeDiscreteGroupings discreteGroupings) throws RuntimeException {
        String[][] discreteGradesMatrix = new String[assessmentIds.size()][studentIds.size()];
        for (AssessmentItem assessmentItem : assessmentItems) {
            discretizeAssessment(discreteGradesMatrix, assessmentItem, discreteGroupings);
        }
        return discreteGradesMatrix;
    }

    /**
     * Creates a matrix with mostly discrete categorical variables following one grouping method
     * Special Assessment is used to discretize a column using a specific group
     * @param assessmentItems
     * @param defaultGroupings
     * @param specialAssessment
     * @param specialGroup
     * @return
     */
    String[][] createMatrix(List<AssessmentItem> assessmentItems, GradeDiscreteGroupings defaultGroupings, String specialAssessment, GradeDiscreteGroupings specialGroup) throws RuntimeException {
        String[][] discreteGradesMatrix = new String[assessmentIds.size()][studentIds.size()];
        for (AssessmentItem assessmentItem : assessmentItems) {
            if (assessmentItem.getId().equals(specialAssessment)) {
                discretizeAssessment(discreteGradesMatrix, assessmentItem, specialGroup);
            } else {
                discretizeAssessment(discreteGradesMatrix, assessmentItem, defaultGroupings);
            }
        }
        return discreteGradesMatrix;
    }

                        /****** Simple Methods ********/
    public String[][] getStudentAssessmentGrades() {
        return studentAssessmentGrades;
    }

    public List<String> getAssessmentIds() {
        return assessmentIds;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }
}
