package edu.ithaca.dragon.tecmap.prediction.predictionsetselector;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.IOException;
import java.util.List;

public interface PredictionSetSelector {

    /**
     * Gets the list of assessmentIds based on the given student, and all of the Assessments
     * @param allAssessments
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return
     * @throws IOException
     */
    List<String> getPredictionSetForGivenStudent(List<AssessmentItem> allAssessments, String studentIdToDecideSet, String assessmentToPredict);

    /**
     * Removes the assessment with the lowest response rate from the currentPredictionSet
     * @param allAssessments
     * @param currentPredictionSet mutated in the
     * @param assessmentToPredict
     */
    void removeLowestResponseRateAssessment(List<AssessmentItem> allAssessments, List<String> currentPredictionSet, String assessmentToPredict);

}
