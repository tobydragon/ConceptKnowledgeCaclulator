package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredictorEffectivenessMain {

    private static final Double[] learningSizeRatios = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    public static void main(String[] args) throws IOException {
        //Test the Bayes Predictor Effectiveness on COMP220 Data
        Predictor bayes = new BayesPredictor();

        File directory = new File(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon/private/");

        List<File> files = Arrays.asList(directory.listFiles());
        List<String> filenames = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                if (!file.getName().equals(".gitignore")) {
                    filenames.add(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon/private/" + file.getName());
                }
            }
        }

        List<AssessmentItem> assessmentItems = new ArrayList<>();

        for (String filename : filenames) {
            CSVReader data = new SakaiReader(filename);
            assessmentItems.addAll(data.getManualGradedLearningObjects());
        }

        KnowledgeEstimateMatrix classMatrix = new KnowledgeEstimateMatrix(assessmentItems);

        for (double ratio : learningSizeRatios) {
            PredictorEffectiveness comp220Effectiveness = PredictorEffectiveness.testPredictor(bayes, "Course Grade", classMatrix.getAssessmentIdList(), classMatrix, ratio);
            System.out.println("Learning Set Size: " + ratio + "\tPercent Correct: " + comp220Effectiveness.getPercentCorrect());
        }
    }

}
