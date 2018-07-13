package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
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
    private int totalTested;
    private List<PredictionResult> allResults; //Redundant information
    private List<PredictionResult> truePositive;
    private List<PredictionResult> trueNegative;
    private List<PredictionResult> falsePositive;
    private List<PredictionResult> falseNegative;

    private PredictorEffectiveness(double percentCorrect, List<PredictionResult> truePositive, List<PredictionResult> trueNegative, List<PredictionResult> falsePositive, List<PredictionResult> falseNegative) {
        this.percentCorrect = percentCorrect;
        this.truePositive = truePositive;
        this.trueNegative = trueNegative;
        this.falsePositive = falsePositive;
        this.falseNegative = falseNegative;
        this.allResults = new ArrayList<>();
        this.allResults.addAll(truePositive);
        this.allResults.addAll(trueNegative);
        this.allResults.addAll(falsePositive);
        this.allResults.addAll(falseNegative);
        this.totalTested = allResults.size();
    }

    public double getPercentCorrect() {
        return percentCorrect;
    }

    public List<PredictionResult> getAllResults() {
        return allResults;
    }

    public int getTotalTested() {
        return totalTested;
    }

    public List<PredictionResult> getTruePositive() {
        return truePositive;
    }

    public List<PredictionResult> getTrueNegative() {
        return trueNegative;
    }

    public List<PredictionResult> getFalsePositive() {
        return falsePositive;
    }

    public List<PredictionResult> getFalseNegative() {
        return falseNegative;
    }

    /**
     * Splits the given matrix into two matrices, first has the given ratio size, second has (1-ratio) size
     * Assumes that all students have all assessments with responses
     * @param originalMatrix
     * @param ratio
     * @return Tuple2 following the given first(_1) has the given ratio size, second(_2) has (1-ratio) size
     */
    static Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix(ContinuousAssessmentMatrix originalMatrix, Double ratio) {
        int ratioSize = (int) Math.ceil(originalMatrix.getStudentIds().size()*ratio);

        List<String> ratioUserIds = new ArrayList<>();
        for (int i = 0; i < originalMatrix.getStudentIds().size(); i++) {
            if (i < ratioSize) {
                ratioUserIds.add(originalMatrix.getStudentIds().get(i));
            }
        }

        //For ratioMatrix
        List<AssessmentItem> ratioAssessments = new ArrayList<>();
        //For nonRatioMatrix
        List<AssessmentItem> nonRatioAssessments = new ArrayList<>();

        List<AssessmentItem> allAssessments = originalMatrix.getAssessmentItems();
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
        ContinuousAssessmentMatrix ratioMatrix = new ContinuousAssessmentMatrix(ratioAssessments);
        ContinuousAssessmentMatrix nonRatioMatrix = new ContinuousAssessmentMatrix(nonRatioAssessments);

        return new Tuple2<>(ratioMatrix, nonRatioMatrix);
    }

    static List<PredictionResult> getPredictionResults(ContinuousAssessmentMatrix testingMatrix, String assessmentToLearn, GradeDiscreteGroupings atRiskGroupings, Map<String, String> predictions) {
        List<PredictionResult> predictionResults = new ArrayList<>();
        List<String> groups = atRiskGroupings.getGroups();
        List<Integer> pointBreaks = atRiskGroupings.getPointBreaks();

        //Get the expected without having a dataframe
        Map<String, String> expectedResults = new HashMap<>();

        //Correct way of getting expected, because not everyone will have an AssessmentItemResponse, but the Matrix will have the data
        List<AssessmentItem> assessments = testingMatrix.getAssessmentItems();
        for (int i = 0; i < assessments.size(); i++) {
            AssessmentItem currAssessment = assessments.get(i);
            if (currAssessment.getId().equals(assessmentToLearn)) {
                double[] assessmentKnowledgeEstimates = testingMatrix.getStudentAssessmentGrades()[i];
                for (int j = 0; j < assessmentKnowledgeEstimates.length; j++) {
                    String expected = null;
                    for (int k = 0; k < pointBreaks.size(); k++) {
                        if (assessmentKnowledgeEstimates[j]*100 >= pointBreaks.get(k) && expected == null) {
                            expected = groups.get(k);
                        }
                        if (expected == null) {
                            expected = groups.get(pointBreaks.size());
                        }
                    }
                    expectedResults.put(testingMatrix.getStudentIds().get(j), expected);
                }
            }
        }

        for (String studentId : predictions.keySet()) {
            PredictionResult result = new PredictionResult(studentId, expectedResults.get(studentId), predictions.get(studentId));
            predictionResults.add(result);
        }

        return predictionResults;
    }

    static PredictorEffectiveness getPredictorEffectivenessFromResults(List<PredictionResult> predictionResults) {
        List<PredictionResult> truePositive = new ArrayList<>();
        List<PredictionResult> trueNegative = new ArrayList<>();
        List<PredictionResult> falsePositive = new ArrayList<>();
        List<PredictionResult> falseNegative = new ArrayList<>();
        //Calculate the percent correct
        double numCorrect = 0;
        for (PredictionResult result : predictionResults) {
            Result currResult = result.getResult();
            if (currResult == Result.TRUE_POSITIVE) {
                numCorrect++;
                truePositive.add(result);
            } else if (currResult == Result.TRUE_NEGATIVE) {
                numCorrect++;
                trueNegative.add(result);
            } else if (currResult == Result.FALSE_POSITIVE) {
                falsePositive.add(result);
            } else {
                falseNegative.add(result);
            }
        }

        double percentCorrect = numCorrect/predictionResults.size();

        return new PredictorEffectiveness(percentCorrect, truePositive, trueNegative, falsePositive, falseNegative);
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
    public static PredictorEffectiveness testLearningPredictor(LearningPredictor predictor, LearningSetSelector learningSetSelector, String assessmentToLearn, ConceptGraph conceptGraph, GradeDiscreteGroupings atriskGroupings, double ratio) throws IOException{
        ContinuousAssessmentMatrix originalMatrix = new ContinuousAssessmentMatrix(new ArrayList<>(conceptGraph.getAssessmentItemMap().values()));

        //Split the matrix
        Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix = splitMatrix(originalMatrix, ratio);

        //Learn on the ratioSized matrix
        ContinuousAssessmentMatrix learningMatrix = splitMatrix._1;
        //Test on the other sized matrix
        ContinuousAssessmentMatrix testingMatrix = splitMatrix._2;

        //List of learningAssessments based on the first student's assessments
        List<String> learningAssessments = learningSetSelector.getLearningSet(conceptGraph, learningMatrix.getStudentIds().get(0), assessmentToLearn);

        //Learn the category with the given assessments
        predictor.learnSet(learningMatrix, assessmentToLearn, learningAssessments);

        //Test on the learning assessments without the learned category
        List<String> testingAssessments = new ArrayList<>();
        testingAssessments.addAll(learningAssessments);
        testingAssessments.remove(assessmentToLearn);

        Map<String, String> predictions = predictor.classifySet(testingMatrix, testingAssessments);

        List<PredictionResult> predictionResults = getPredictionResults(testingMatrix, assessmentToLearn, atriskGroupings, predictions);

        return getPredictorEffectivenessFromResults(predictionResults);
    }

    public static PredictorEffectiveness testPredictor(Predictor simplePredictor, LearningSetSelector learningSetSelector, String assessmentToLearn, ConceptGraph conceptGraph, GradeDiscreteGroupings atriskGroupings,double ratio) throws IOException {
        ContinuousAssessmentMatrix originalMatrix = new ContinuousAssessmentMatrix(new ArrayList<>(conceptGraph.getAssessmentItemMap().values()));

        //Split the matrix
        Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix = splitMatrix(originalMatrix, ratio);

        //Ignore learningMatrix since simple predictors do not learn
        ContinuousAssessmentMatrix learningMatrix = splitMatrix._1;
        //Test on the other sized matrix
        ContinuousAssessmentMatrix testingMatrix = splitMatrix._2;

        //Get learning set and remove the assessmentToLearn from list
        List<String> testingAssessments = learningSetSelector.getLearningSet(conceptGraph, learningMatrix.getStudentIds().get(0), assessmentToLearn);
        testingAssessments.remove(assessmentToLearn);

        Map<String, String> predictions = simplePredictor.classifySet(testingMatrix, testingAssessments);

        List<PredictionResult> predictionResults = getPredictionResults(testingMatrix, assessmentToLearn, atriskGroupings, predictions);

        return getPredictorEffectivenessFromResults(predictionResults);
    }

}
