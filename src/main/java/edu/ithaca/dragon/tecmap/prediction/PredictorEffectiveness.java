package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import io.vavr.Tuple2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictorEffectiveness {
    private static final Logger logger = LogManager.getLogger(PredictorEffectiveness.class);


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

        try {

            KnowledgeEstimateMatrix ratioMatrix = new KnowledgeEstimateMatrix(ratioAssessments);
            KnowledgeEstimateMatrix nonRatioMatrix = new KnowledgeEstimateMatrix(nonRatioAssessments);

            return new Tuple2<>(ratioMatrix, nonRatioMatrix);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    private static List<PredictionResult> getResults(KnowledgeEstimateMatrix testingMatrix, String assessmentToLearn, Map<String, String> predictions) {
        List<PredictionResult> predictionResults = new ArrayList<>();

        //Get the expected without having a dataframe
        Map<String, String> expectedResults = new HashMap<>();

        //Correct way of getting expected, because not everyone will have an AssessmentItemResponse, but the Matrix will have the data
        List<AssessmentItem> assessments = testingMatrix.getObjList();
        for (int i = 0; i < assessments.size(); i++) {
            AssessmentItem currAssessment = assessments.get(i);
            if (currAssessment.getId().equals(assessmentToLearn)) {
                double[] assessmentKnowledgeEstimates = testingMatrix.getStudentKnowledgeEstimates()[i];
                for (int j = 0; j < assessmentKnowledgeEstimates.length; j++) {
                    String expected = "OK";
                    if (assessmentKnowledgeEstimates[j] < Predictor.ESTIMATE_THRESHOLD) {
                        expected = "AT-RISK";
                    }
                    expectedResults.put(testingMatrix.getUserIdList().get(j), expected);
                }
            }
        }

        for (String studentId : predictions.keySet()) {
            PredictionResult result = new PredictionResult(studentId, expectedResults.get(studentId), predictions.get(studentId));
            predictionResults.add(result);
        }

        return predictionResults;
    }

    /**
     * Test how effective a predictor is, works off of the Predictor interface
     * @param predictor specific type of predictor
     * @param learningSetSelector how the learning set is being chosen
     * @param assessmentToLearn what assessment is being learned
     * @param conceptGraph graph that prediction is working on
     * @param ratio what percent of the originalMatrix should be used for learning
     * @return PredictorEffectiveness object with percent correct and a list of all the results
     */
    public static PredictorEffectiveness testLearningPredictor(LearningPredictor predictor, LearningSetSelector learningSetSelector, String assessmentToLearn, ConceptGraph conceptGraph, double ratio) throws IOException{
        KnowledgeEstimateMatrix originalMatrix = new KnowledgeEstimateMatrix(new ArrayList<>(conceptGraph.getAssessmentItemMap().values()));

        //Split the matrix
        Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix = splitMatrix(originalMatrix, ratio);

        //Learn on the ratioSized matrix
        KnowledgeEstimateMatrix learningMatrix = splitMatrix._1;
        //Test on the other sized matrix
        KnowledgeEstimateMatrix testingMatrix = splitMatrix._2;

        //List of learningAssessments based on the first student's assessments
        List<String> learningAssessments = learningSetSelector.getLearningSet(conceptGraph, learningMatrix.getUserIdList().get(0), assessmentToLearn);

        //Learn the category with the given assessments
        predictor.learnSet(learningMatrix, assessmentToLearn, learningAssessments);

        //Test on the learning assessments without the learned category
        List<String> testingAssessments = new ArrayList<>();
        testingAssessments.addAll(learningAssessments);
        testingAssessments.remove(assessmentToLearn);

        Map<String, String> predictions = predictor.classifySet(testingMatrix, testingAssessments);

        List<PredictionResult> predictionResults = getResults(testingMatrix, assessmentToLearn, predictions);

        //Calculate the percent correct
        double numCorrect = 0;
        for (PredictionResult result : predictionResults) {
            if (result.getResult() == Result.TRUE_NEGATIVE || result.getResult() == Result.TRUE_POSITIVE) {
                numCorrect++;
            }
        }

        double percentCorrect = numCorrect/predictionResults.size();

        return new PredictorEffectiveness(percentCorrect, predictionResults);
    }

    public static PredictorEffectiveness testPredictor(Predictor simplePredictor, LearningSetSelector learningSetSelector, String assessmentToLearn, ConceptGraph conceptGraph, double ratio) throws IOException {
        KnowledgeEstimateMatrix originalMatrix = new KnowledgeEstimateMatrix(new ArrayList<>(conceptGraph.getAssessmentItemMap().values()));

        //Split the matrix
        Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix = splitMatrix(originalMatrix, ratio);

        //Ignore learningMatrix since simple predictors do not learn
        KnowledgeEstimateMatrix learningMatrix = splitMatrix._1;
        //Test on the other sized matrix
        KnowledgeEstimateMatrix testingMatrix = splitMatrix._2;

        //Get learning set and remove the assessmentToLearn from list
        List<String> testingAssessments = learningSetSelector.getLearningSet(conceptGraph, learningMatrix.getUserIdList().get(0), assessmentToLearn);
        testingAssessments.remove(assessmentToLearn);

        Map<String, String> predictions = simplePredictor.classifySet(testingMatrix, testingAssessments);

        List<PredictionResult> predictionResults = getResults(testingMatrix, assessmentToLearn, predictions);

        //Calculate the percent correct
        double numCorrect = 0;
        for (PredictionResult result : predictionResults) {
            if (result.getResult() == Result.TRUE_NEGATIVE || result.getResult() == Result.TRUE_POSITIVE) {
                numCorrect++;
            }
        }

        double percentCorrect = numCorrect/predictionResults.size();

        return new PredictorEffectiveness(percentCorrect, predictionResults);
    }

}
