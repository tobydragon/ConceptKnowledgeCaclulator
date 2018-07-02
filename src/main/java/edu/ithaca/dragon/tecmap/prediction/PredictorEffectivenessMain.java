package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

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

        //Get assessment filenames
        File directory = new File(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon/private/");

        List<File> files = Arrays.asList(directory.listFiles());
        List<String> assessmentFilenames = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                if (!file.getName().equals(".gitignore")) {
                    assessmentFilenames.add(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon/private/" + file.getName());
                }
            }
        }

        //Get resources
        directory = new File(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon");
        files = Arrays.asList(directory.listFiles());
        List<String> resourceFilenames = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().contains("Resources")) {
                    resourceFilenames.add(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon/" + file.getName());
                }
            }
        }

        List<LearningResourceRecord> loRecords = new ArrayList<>();
        for (String filename : resourceFilenames) {
            loRecords.addAll(LearningResourceRecord.buildListFromJson(filename));
        }


        ConceptGraph conceptGraph220 = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_MAIN_DATASTORE_PATH + "comp220Dragon/comp220DragonGraph.json"),
                loRecords,
                AssessmentItemResponse.createAssessmentItemResponses(assessmentFilenames));

        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();

        //Will get a null pointer exception when trying to predict an assessment not connected to a resource
        for (double ratio : learningSizeRatios) {
            PredictorEffectiveness comp220Effectiveness = PredictorEffectiveness.testPredictor(bayes, baseLearningSetSelector,"final-1", conceptGraph220, ratio);
            System.out.println("Learning Set Size: " + ratio + "\tPercent Correct: " + comp220Effectiveness.getPercentCorrect());
        }
    }

}
