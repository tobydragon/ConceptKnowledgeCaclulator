package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.GraphPredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.NoStructurePredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.PredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictor.BayesPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.LearningPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.Predictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.SimplePredictor;
import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictorEffectivenessMain {

    private static final Double[] learningSizeRatios = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    private static void singleAssessmentTwoSetSelectorTwoPredictorTest(ConceptGraph courseGraph, String assessmentToLearn, LearningPredictor bayes, Predictor simple, PredictionSetSelector basePredictionSetSelector, PredictionSetSelector graphPredictionSetSelector, GradeDiscreteGroupings atriskGroupings) throws IOException {
        System.out.println("Learning Set Size: \t Base Bayes %Correct: \t Graph Bayes %Correct: \t Base Simple %Correct: \t Graph Simple %Correct:");
        //Will get a null pointer exception when trying to predict an assessment not connected to a resource
        double ratio;
//        for (ratio : learningSizeRatios) {
//            PredictorEffectiveness courseBaseBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, basePredictionSetSelector,assessmentToLearn, courseGraph, atriskGroupings,ratio);
//            bayes.reset();
//            PredictorEffectiveness courseGraphBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, graphPredictionSetSelector,assessmentToLearn, courseGraph, atriskGroupings,ratio);
//            bayes.reset();
//            PredictorEffectiveness courseBaseSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, basePredictionSetSelector, assessmentToLearn, courseGraph, atriskGroupings, ratio);
//            PredictorEffectiveness courseGraphSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, graphPredictionSetSelector, assessmentToLearn, courseGraph, atriskGroupings, ratio);
//            System.out.println("\t\t" + ratio +
//                    "\t\t\t  " + courseBaseBayesEffectiveness.getPercentCorrect() + "(" + courseBaseBayesEffectiveness.getTotalTested() + ")" +
//                    "\t\t" + courseGraphBayesEffectiveness.getPercentCorrect() + "(" + courseGraphBayesEffectiveness.getTotalTested() + ")" +
//                    "\t\t" + courseBaseSimpleEffectiveness.getPercentCorrect() + "(" + courseBaseSimpleEffectiveness.getTotalTested() + ")" +
//                    "\t\t" + courseGraphSimpleEffectiveness.getPercentCorrect() + "(" + courseGraphSimpleEffectiveness.getTotalTested() + ")");
//        }
        ratio = 0.5;
        PredictorEffectiveness courseBaseBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, basePredictionSetSelector,assessmentToLearn, courseGraph, atriskGroupings,ratio);
        bayes.reset();
        PredictorEffectiveness courseGraphBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, graphPredictionSetSelector,assessmentToLearn, courseGraph, atriskGroupings,ratio);
        bayes.reset();
        PredictorEffectiveness courseBaseSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, basePredictionSetSelector, assessmentToLearn, courseGraph, atriskGroupings, ratio);
        PredictorEffectiveness courseGraphSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, graphPredictionSetSelector, assessmentToLearn, courseGraph, atriskGroupings, ratio);
        System.out.println("\t\t" + ratio +
                "\t\t\t  " + courseBaseBayesEffectiveness.getPercentCorrect() + "(" + courseBaseBayesEffectiveness.getTotalTested() + ")" +
                "\t\t" + courseGraphBayesEffectiveness.getPercentCorrect() + "(" + courseGraphBayesEffectiveness.getTotalTested() + ")" +
                "\t\t" + courseBaseSimpleEffectiveness.getPercentCorrect() + "(" + courseBaseSimpleEffectiveness.getTotalTested() + ")" +
                "\t\t" + courseGraphSimpleEffectiveness.getPercentCorrect() + "(" + courseGraphSimpleEffectiveness.getTotalTested() + ")");
    }

    private static void allAssessmentTwoSelectorTwoPredictorTest(ConceptGraph courseGraph, LearningPredictor learning, Predictor simple, PredictionSetSelector basePredictionSetSelector, PredictionSetSelector graphPredictionSetSelector, GradeDiscreteGroupings atriskGroupings, double ratio) throws IOException {
        //Get all assessments from courseGraph
        List<AssessmentItem> allAssessments = new ArrayList<>(courseGraph.getAssessmentItemMap().values());
        Map<String, Double> courseBaseBayes = new HashMap<>();
        Map<String, Double> courseGraphBayes = new HashMap<>();
        Map<String, Double> courseBaseSimple = new HashMap<>();
        Map<String, Double> courseGraphSimple = new HashMap<>();
        //Get results for each assessment
        for (AssessmentItem item : allAssessments) {
            String assessment = item.getId();
            courseBaseBayes.put(assessment, PredictorEffectiveness.testLearningPredictor(learning, basePredictionSetSelector, assessment, courseGraph, atriskGroupings,ratio).getPercentCorrect());
            learning.reset();
            courseGraphBayes.put(assessment, PredictorEffectiveness.testLearningPredictor(learning, graphPredictionSetSelector, assessment, courseGraph, atriskGroupings,ratio).getPercentCorrect());
            learning.reset();
            courseBaseSimple.put(assessment, PredictorEffectiveness.testPredictor(simple, basePredictionSetSelector, assessment, courseGraph, atriskGroupings, ratio).getPercentCorrect());
            courseGraphSimple.put(assessment, PredictorEffectiveness.testPredictor(simple, graphPredictionSetSelector, assessment, courseGraph, atriskGroupings, ratio).getPercentCorrect());
        }

        //Sort the results
        Object[] courseBaseBayesSorted = courseBaseBayes.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();
        Object[] courseGraphBayesSorted = courseGraphBayes.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();
        Object[] courseBaseSimpleSorted = courseBaseSimple.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();
        Object[] courseGraphSimpleSorted = courseGraphSimple.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();

        //Output the sorted results
        //Rank lowest % correct to highest
        System.out.println("\tBase Bayes:\t Graph Bayes: \t Base Simple: \t Graph Simple:");
        for (int i = 0; i < courseBaseBayesSorted.length; i++) {
            String baseBayes = courseBaseBayesSorted[i].toString().split("=")[0] + "(" + DataUtil.format(Double.parseDouble(courseBaseBayesSorted[i].toString().split("=")[1])) + ")";
            String graphBayes = courseGraphBayesSorted[i].toString().split("=")[0] + "(" + DataUtil.format(Double.parseDouble(courseGraphBayesSorted[i].toString().split("=")[1])) + ")";
            String baseSimple = courseBaseSimpleSorted[i].toString().split("=")[0] + "(" + DataUtil.format(Double.parseDouble(courseBaseSimpleSorted[i].toString().split("=")[1])) + ")";
            String graphSimple = courseGraphSimpleSorted[i].toString().split("=")[0] + "(" + DataUtil.format(Double.parseDouble(courseGraphSimpleSorted[i].toString().split("=")[1])) + ")";
            System.out.println(i + ")" + baseBayes + "\t" + graphBayes + "\t" + baseSimple + "\t" + graphSimple);
        }
    }

    public static void main(String[] args) throws IOException {
        //Testing for COMP220 Data
        ConceptGraph conceptGraph220 = PredictionController.getConceptGraph("comp220Dragon-2017", Settings.DEFAULT_MAIN_DATASTORE_PATH);

        GradeDiscreteGroupings defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "discreteGroupings.json");
        GradeDiscreteGroupings atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "atriskGroupings.json");
        //For testing the Bayes Predictor Effectiveness
        LearningPredictor bayes = new BayesPredictor(defaultGroupings, atriskGroupings);

        //For testing the Simple Predictor Effectiveness
        Predictor simple = new SimplePredictor(atriskGroupings);

        //Two different ways to select learning sets
        PredictionSetSelector basePredictionSetSelector = new NoStructurePredictionSetSelector();
        PredictionSetSelector graphPredictionSetSelector = new GraphPredictionSetSelector(conceptGraph220);

        //Single Assessment Two Selector Two Predictor Test
        String assessmentToLearn = "e2-CA-1";
        singleAssessmentTwoSetSelectorTwoPredictorTest(conceptGraph220, assessmentToLearn, bayes, simple, basePredictionSetSelector, graphPredictionSetSelector, atriskGroupings);

//        All Assessment Two Selector Two Predictor Test
//        allAssessmentTwoSelectorTwoPredictorTest(conceptGraph220, bayes, simple, basePredictionSetSelector, graphPredictionSetSelector, atriskGroupings, 0.5);
    }

}
