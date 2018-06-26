package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import io.vavr.Tuple2;

public class PredictorEffectiveness {

    private Predictor predictor;

    PredictorEffectiveness() {
        predictor = null;
    }

    PredictorEffectiveness(Predictor predictorType) {
        this.predictor = predictorType;
    }

    public static Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix(KnowledgeEstimateMatrix originalMatrix, Double ratio) {
        return null;
    }

    public static void main(String[] args) {
        PredictorEffectiveness bayesEffectiveness = new PredictorEffectiveness(new BayesPredictor());


    }

}
