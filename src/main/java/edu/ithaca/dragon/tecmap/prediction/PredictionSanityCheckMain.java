package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.GraphPredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.NoStructurePredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.PredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictor.BayesPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.LearningPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.Predictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.SimplePredictor;

import java.io.IOException;

public class PredictionSanityCheckMain {

    private static void bayesTest() throws IOException {
        ConceptGraph conceptGraph = PredictionController.getConceptGraph("BayesTest", Settings.DEFAULT_TEST_DATASTORE_PATH);

        GradeDiscreteGroupings defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "discreteGroupings.json");
        GradeDiscreteGroupings atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "atriskGroupings.json");
        //For testing the Bayes Predictor Effectiveness
        LearningPredictor bayes = new BayesPredictor(defaultGroupings, atriskGroupings);

        //noStructureSetSelector
        PredictionSetSelector noStructurePredictionSetSelector = new NoStructurePredictionSetSelector();
        String assessmentToLearn = "Exam";
        double ratio = 0.19;

        PredictorEffectiveness noStructureBayesTest = PredictorEffectiveness.testLearningPredictor(bayes, noStructurePredictionSetSelector, assessmentToLearn, conceptGraph, atriskGroupings, ratio);
        System.out.println(noStructureBayesTest.getPercentCorrect());
    }

    private static void graphTest() throws IOException {
        ConceptGraph conceptGraph = PredictionController.getConceptGraph("GraphTest", Settings.DEFAULT_TEST_DATASTORE_PATH);

        GradeDiscreteGroupings atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "atriskGroupings.json");
        //For testing the Bayes Predictor Effectiveness
        Predictor simple = new SimplePredictor(atriskGroupings);

        //noStructureSetSelector
        PredictionSetSelector noStructurePredictionSetSelector = new NoStructurePredictionSetSelector();
        PredictionSetSelector graphPredictionSetSelector = new GraphPredictionSetSelector(conceptGraph);
        String assessmentToLearn = "Q1";
        double ratio = 0.0;

        PredictorEffectiveness noStructureTest = PredictorEffectiveness.testPredictor(simple, noStructurePredictionSetSelector, assessmentToLearn, conceptGraph, atriskGroupings, ratio);
        PredictorEffectiveness graphTest = PredictorEffectiveness.testPredictor(simple, graphPredictionSetSelector, assessmentToLearn, conceptGraph, atriskGroupings, ratio);
        System.out.println(noStructureTest.getPercentCorrect());
    }

    public static void main(String[] args) throws IOException {
//        bayesTest();
        graphTest();
    }

}
