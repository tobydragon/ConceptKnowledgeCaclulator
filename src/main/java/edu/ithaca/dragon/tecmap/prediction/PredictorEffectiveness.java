package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import io.vavr.Tuple2;

import java.util.List;

public class PredictorEffectiveness {

    private double percentCorrect;
    private List<PredictionResult> results;

    private PredictorEffectiveness(double percentCorrect, List<PredictionResult> results) {
        this.percentCorrect = percentCorrect;
        this.results = results;
    }

    private static Tuple2<KnowledgeEstimateMatrix, KnowledgeEstimateMatrix> splitMatrix(KnowledgeEstimateMatrix originalMatrix, Double ratio) {
        return null;
    }

    public static PredictorEffectiveness testPredictor(Predictor predictor, KnowledgeEstimateMatrix originalMatrix) {
        return null;
    }

}
