package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.NoStructurePredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.PredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictor.BayesPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.LearningPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.Predictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.SimplePredictor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LivePredictionMain {

    /**
     * Example usage of prediction
     * @param courseGraph
     * @param atriskGroupings
     */
    private static void predictorMain(ConceptGraph courseGraph, GradeDiscreteGroupings atriskGroupings) {
        Predictor predictor = new SimplePredictor(atriskGroupings);
        PredictionSetSelector setSelector = new NoStructurePredictionSetSelector();
        PredictionController controller = new PredictionController(predictor, setSelector);
        List<AssessmentItem> allAssessments = new ArrayList<>(courseGraph.getAssessmentItemMap().values());
        controller.getPredictions(allAssessments, "Lab 4: Recursion");
    }

    private static void learningPredictorMain(ConceptGraph courseGraph, GradeDiscreteGroupings defaultGroupings, GradeDiscreteGroupings atriskGroupings) {
        LearningPredictor learningPredictor = new BayesPredictor(defaultGroupings, atriskGroupings);
        PredictionSetSelector setSelector = new NoStructurePredictionSetSelector();
        PredictionController controller = new PredictionController(learningPredictor, setSelector);
        List<AssessmentItem> allAssessments = new ArrayList<>(courseGraph.getAssessmentItemMap().values());
        controller.getPredictions(allAssessments, "Lab 4: Recursion");
    }

    public static void main(String[] args) throws IOException {
        //Will all have to be in the tecmap
        ConceptGraph comp220 = PredictionController.getConceptGraph("comp220Dragon-2017", Settings.DEFAULT_MAIN_DATASTORE_PATH);
        GradeDiscreteGroupings defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "discreteGroupings.json");
        GradeDiscreteGroupings atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "atriskGroupings.json");
        predictorMain(comp220, atriskGroupings);
        learningPredictorMain(comp220, defaultGroupings, atriskGroupings);
    }

}
