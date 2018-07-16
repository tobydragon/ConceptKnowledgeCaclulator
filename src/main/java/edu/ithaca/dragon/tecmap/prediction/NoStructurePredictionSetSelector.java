package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoStructurePredictionSetSelector implements PredictionSetSelector {

    public NoStructurePredictionSetSelector() {
    }

    /**
     * Gets the list of learning assessments based on the assessmentToPredict, and the assessments available for the given student
     * @param allAssessments
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getPredictionSetForGivenStudent(List<AssessmentItem> allAssessments, String studentIdToDecideSet, String assessmentToPredict) {
        List<String> learningSet = new ArrayList<>();

        //Only includes assessments that have responses for the given student
        for (AssessmentItem item : allAssessments) {
            for (AssessmentItemResponse response : item.getResponses()) {
                if (response.getUserId().equals(studentIdToDecideSet) && !learningSet.contains(item.getId())) {
                    learningSet.add(item.getId());
                }
            }
        }

        //List of learningSet should always contain the assessmentToPredict (with data or without)
        if (!learningSet.contains(assessmentToPredict)) {
            learningSet.add(assessmentToPredict);
        }
        return learningSet;
    }

    /**
     * Removes the assessment with the lowest response rate from the currentPredictionSet
     * If multiple assessments have the lowest response rate, only the first occurrence will be removed
     * @param allAssessments
     * @param currentPredictionSet
     * @param assessmentToPredict
     * @return
     */
    @Override
    public void removeLowestResponseRateAssessment(List<AssessmentItem> allAssessments, List<String> currentPredictionSet, String assessmentToPredict) {
        int maxResponses = 0;
        double minResponseRate = Double.MAX_VALUE;
        AssessmentItem lowestResponseRateAssessment = null;
        List<AssessmentItem> currentAssessments = new ArrayList<>(); //holds assessments for currentPredictionSet
        for (AssessmentItem assessmentItem : allAssessments) {
            if (currentPredictionSet.contains(assessmentItem.getId())) {
                if (assessmentItem.getResponses().size() > maxResponses) {
                    maxResponses = assessmentItem.getResponses().size(); //Number of
                }
                currentAssessments.add(assessmentItem);
            }
        }
        for (AssessmentItem assessmentItem : currentAssessments) {
            List<AssessmentItemResponse> responses = assessmentItem.getResponses();
            double responseRate = (double) responses.size()/maxResponses;
            if (responseRate < minResponseRate && responseRate != 1.0 && !assessmentItem.getId().equals(assessmentToPredict)) {
                minResponseRate = responseRate;
                lowestResponseRateAssessment = assessmentItem;
            }
        }
        if (lowestResponseRateAssessment != null) {
            currentPredictionSet.remove(lowestResponseRateAssessment.getId());
        }
    }
}
