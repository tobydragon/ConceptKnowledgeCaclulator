package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictorEffectiveness {

    private double percentCorrect; //between 0 & 1
    private List<PredictionResult> results;

    private PredictorEffectiveness(double percentCorrect, List<PredictionResult> results) {
        this.percentCorrect = percentCorrect;
        this.results = results;
    }

    public double getPercentCorrect() {
        return percentCorrect;
    }

    public List<PredictionResult> getResults() {
        return results;
    }

    /**
     * Splits the given matrix into two matrices, first has the given ratio size, second has (1-ratio) size
     * Assumes that all students have all assessments with responses
     * @param originalMatrix
     * @param ratio
     * @return Tuple2 following the given first(_1) has the given ratio size, second(_2) has (1-ratio) size
     */
    static Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix(KnowledgeEstimateMatrix originalMatrix, Double ratio) {
        int ratioSize = (int) Math.ceil(originalMatrix.getUserIdList().size()*ratio);

        List<String> ratioUserIds = new ArrayList<>();
        for (int i = 0; i < originalMatrix.getUserIdList().size(); i++) {
            if (i < ratioSize) {
                ratioUserIds.add(originalMatrix.getUserIdList().get(i));
            }
        }

        //For ratioMatrix
        List<AssessmentItem> ratioAssessments = new ArrayList<>();
        //For nonRatioMatrix
        List<AssessmentItem> nonRatioAssessments = new ArrayList<>();

        List<AssessmentItem> allAssessments = originalMatrix.getObjList();
        for (AssessmentItem item : allAssessments) {
            AssessmentItem ratioItem = new AssessmentItem(item.getId(), item.getMaxPossibleKnowledgeEstimate());
            AssessmentItem nonRatioItem = new AssessmentItem(item.getId(), item.getMaxPossibleKnowledgeEstimate());
            for (int i = 0; i < item.getResponses().size(); i++) {
                AssessmentItemResponse currResponse = item.getResponses().get(i);
                //Keeps the users between the two matrices constant instead of by index
                if (ratioUserIds.contains(currResponse.getUserId())) {
                    ratioItem.addResponse(currResponse);
                } else {
                    nonRatioItem.addResponse(currResponse);
                }
            }
            ratioAssessments.add(ratioItem);
            nonRatioAssessments.add(nonRatioItem);
        }

        KnowledgeEstimateMatrix ratioMatrix = new KnowledgeEstimateMatrix(ratioAssessments);
        KnowledgeEstimateMatrix nonRatioMatrix = new KnowledgeEstimateMatrix(nonRatioAssessments);

        return new Tuple2<>(ratioMatrix, nonRatioMatrix);
    }

    public static PredictorEffectiveness testPredictor(Predictor predictor, String categoryToLearn, List<String> learningAssessments, KnowledgeEstimateMatrix originalMatrix, double ratio) {
        //Split the matrix
        Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix = splitMatrix(originalMatrix, ratio);

        //Learn on the ratioSized matrix
        KnowledgeEstimateMatrix learningMatrix = splitMatrix._1;
        //Test on the other sized matrix
        KnowledgeEstimateMatrix testingMatrix = splitMatrix._2;

        //Learn the category with the given assessments
        predictor.learnSet(learningMatrix, categoryToLearn, learningAssessments);

        //Test on the learning assessments without the learned category
        List<String> testingAssessments = learningAssessments;
        testingAssessments.remove(categoryToLearn);

        Map<String, String> predictions = predictor.classifySet(testingMatrix, testingAssessments);

        List<PredictionResult> predictionResults = new ArrayList<>();

        //Get the expected without having a dataframe
        Map<String, String> expectedResults = new HashMap<>();
        for (AssessmentItem item : testingMatrix.getObjList()) {
            if (item.getId().equals(categoryToLearn)) {
                for (AssessmentItemResponse response : item.getResponses()) {
                    String expected = "OK";
                    if (response.calcKnowledgeEstimate() < Predictor.ESTIMATE_THRESHOLD) {
                        expected = "AT-RISK";
                    }
                    expectedResults.put(response.getUserId(), expected);
                }
            }
        }

        for (String studentId : predictions.keySet()) {
            PredictionResult result = new PredictionResult(studentId, expectedResults.get(studentId), predictions.get(studentId));
            predictionResults.add(result);
        }

        double numCorrect = 0;
        for (PredictionResult result : predictionResults) {
            if (result.isCorrect()) {
                numCorrect++;
            }
        }

        double percentCorrect = numCorrect/predictionResults.size();

        return new PredictorEffectiveness(percentCorrect, predictionResults);
    }

}
