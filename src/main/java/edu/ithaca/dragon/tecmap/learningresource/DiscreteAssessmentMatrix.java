package edu.ithaca.dragon.tecmap.learningresource;

import java.io.IOException;
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

        String[][] discreteGrades = null;

        return discreteGrades;
    }

    public String[][] getStudentAssessmentGrades() {
        return studentAssessmentGrades;
    }
}
