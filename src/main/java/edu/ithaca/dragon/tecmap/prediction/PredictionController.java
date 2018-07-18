package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.PredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictor.Predictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionController {

    private Predictor predictor;
    private PredictionSetSelector predictionSetSelector;

    public PredictionController(Predictor predictor, PredictionSetSelector predictionSetSelector) {
        this.predictor = predictor;
        this.predictionSetSelector = predictionSetSelector;
    }

    /**
     * Finds the students that have responses to all of the assessments to be included
     * @param allAssessments
     * @param assessmentsToInclude
     * @return all of the assessmentItemResponses for the students that are to be included
     */
    static List<AssessmentItem> getAssessmentsForStudentsWithAllResponses(List<AssessmentItem> allAssessments, List<String> assessmentsToInclude) {
        Map<String, List<AssessmentItemResponse>> studentResponses = new HashMap<>();
        Map<String, Double> originalMaxKnowledgeEstimates = new HashMap<>();
        for (AssessmentItem assessmentItem : allAssessments) {
            if (assessmentsToInclude.contains(assessmentItem.getId())) {
                originalMaxKnowledgeEstimates.put(assessmentItem.getId(), assessmentItem.getMaxPossibleKnowledgeEstimate());
                for (AssessmentItemResponse response : assessmentItem.getResponses()) {
                    if (studentResponses.containsKey(response.getUserId())) {
                        studentResponses.get(response.getUserId()).add(response);
                    } else {
                        List<AssessmentItemResponse> responseList = new ArrayList<>();
                        responseList.add(response);
                        studentResponses.put(response.getUserId(), responseList);
                    }
                }
            }
        }
        List<AssessmentItemResponse> studentsWithAllAssessmentsToInclude = new ArrayList<>();
        for (Map.Entry<String, List<AssessmentItemResponse>> entry : studentResponses.entrySet()) {
            if (entry.getValue().size() == assessmentsToInclude.size()) {
                studentsWithAllAssessmentsToInclude.addAll(entry.getValue());
            }
        }
        return AssessmentItem.buildListFromAssessmentItemResponses(studentsWithAllAssessmentsToInclude, originalMaxKnowledgeEstimates);
    }

    /**
     * Gets the a matrix to use for prediction by removing students that don't have responses to the assessments in the prediction set
     * @param allAssessments
     * @param predictionSet
     * @return
     */
    public static ContinuousAssessmentMatrix getMatrix(List<AssessmentItem> allAssessments, List<String> predictionSet) {
        List<AssessmentItem> assessmentItemsWithValidStudents = getAssessmentsForStudentsWithAllResponses(allAssessments, predictionSet);
        return new ContinuousAssessmentMatrix(assessmentItemsWithValidStudents);
    }

    /**
     * Gets the predictionSet of assessment ids for future prediction
     * @param allAssessments
     * @param studentId
     * @param assessmentToPredict
     * @return
     */
    public List<String> getPredictionSet(List<AssessmentItem> allAssessments, String studentId, String assessmentToPredict) {
        return predictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, assessmentToPredict);
    }

    /**
     * Gets the classifications given a testing matrix and a set to predict
     * @return
     */
    public Map<String, String> predict(ContinuousAssessmentMatrix testingMatrix, List<String> testingAssessments) {
        return predictor.classifySet(testingMatrix, testingAssessments);
    }

}
