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
    public List<String> getLearningSetForGivenStudent(List<AssessmentItem> allAssessments, String studentIdToDecideSet, String assessmentToPredict) {
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
}
