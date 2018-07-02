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

    /**
     * Gets graph from the main datastore assuming:
     * assessment files are all in a private subfolder,
     * resources files are all in the main folder and contain "Resources" in their name,
     * there is one graph file and contains "Graph" in its name
     * @param courseName the name of the directory in the datastore for the course
     * @return ConceptGraph built from the graph file, the assessment files and resource files
     */
    private static ConceptGraph getConceptGraph(String courseName) throws IOException {
        //Get assessment filenames
        File directory = new File(Settings.DEFAULT_MAIN_DATASTORE_PATH + courseName + "/private/");

        List<File> files = Arrays.asList(directory.listFiles());
        List<String> assessmentFilenames = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                if (!file.getName().equals(".gitignore")) {
                    assessmentFilenames.add(Settings.DEFAULT_MAIN_DATASTORE_PATH + courseName + "/private/" + file.getName());
                }
            }
        }

        List<String> resourceFilenames = new ArrayList<>();
        String graphFilename = null;
        //Get resources
        directory = new File(Settings.DEFAULT_MAIN_DATASTORE_PATH + courseName);
        files = Arrays.asList(directory.listFiles());
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().contains("Resources")) {
                    resourceFilenames.add(Settings.DEFAULT_MAIN_DATASTORE_PATH + courseName + "/" + file.getName());
                } else if (file.getName().contains("Graph")) {
                    graphFilename = Settings.DEFAULT_MAIN_DATASTORE_PATH + courseName + "/" + file.getName();
                }
            }
        }

        List<LearningResourceRecord> loRecords = new ArrayList<>();
        for (String filename : resourceFilenames) {
            loRecords.addAll(LearningResourceRecord.buildListFromJson(filename));
        }

        ConceptGraph courseGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(graphFilename),
                loRecords,
                AssessmentItemResponse.createAssessmentItemResponses(assessmentFilenames));

        return courseGraph;
    }

    public static void main(String[] args) throws IOException {
        //Test the Bayes Predictor Effectiveness on COMP220 Data
        Predictor bayes = new BayesPredictor();

        ConceptGraph conceptGraph220 = getConceptGraph("comp220Dragon");

        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();
        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        //Will get a null pointer exception when trying to predict an assessment not connected to a resource
        for (double ratio : learningSizeRatios) {
            PredictorEffectiveness comp220BaseEffectiveness = PredictorEffectiveness.testPredictor(bayes, baseLearningSetSelector,"7-HG", conceptGraph220, ratio);
            PredictorEffectiveness comp220GraphEffectiveness = PredictorEffectiveness.testPredictor(bayes, graphLearningSetSelector,"7-HG", conceptGraph220, ratio);
            System.out.println("Learning Set Size: " + ratio + "\t Base Percent Correct: " + comp220BaseEffectiveness.getPercentCorrect() + "\t Graph Percent Correct: " + comp220GraphEffectiveness.getPercentCorrect());
        }
    }

}
