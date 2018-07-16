package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.IOException;
import java.util.List;

public interface LearningSetSelector {

    /**
     * Gets the list of assessmentIds based on the given student, and all of the Assessments
     * @param allAssessments
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return
     * @throws IOException
     */
    List<String> getLearningSetForGivenStudent(List<AssessmentItem> allAssessments, String studentIdToDecideSet, String assessmentToPredict) throws IOException;

}
