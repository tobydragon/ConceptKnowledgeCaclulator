package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;

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

        List<LearningResourceRecord> loRecords = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(resourceFilenames);

        ConceptGraph courseGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(graphFilename),
                loRecords,
                AssessmentItemResponse.createAssessmentItemResponses(assessmentFilenames));

        return courseGraph;
    }

    public static void main(String[] args) throws IOException {
        GradeDiscreteGroupings defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "discreteGroupings.json");
        GradeDiscreteGroupings atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "atriskGroupings.json");
        //For testing the Bayes Predictor Effectiveness
        LearningPredictor bayes = new BayesPredictor(defaultGroupings, atriskGroupings);

        //For testing the Simple Predictor Effectiveness
        Predictor simple = new SimplePredictor(atriskGroupings);

        //Testing for COMP220 Data
        ConceptGraph conceptGraph220 = getConceptGraph("comp220Dragon");

        //Assessment to learn
        String assessmentToLearn = "Lab 4: Recursion";

        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();
        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        System.out.println("Learning Set Size: \t Base Bayes %Correct: \t Graph Bayes %Correct: \t Base Simple %Correct: \t Graph Simple %Correct:");
        //Will get a null pointer exception when trying to predict an assessment not connected to a resource
        for (double ratio : learningSizeRatios) {
            PredictorEffectiveness comp220BaseBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, baseLearningSetSelector,assessmentToLearn, conceptGraph220, atriskGroupings,ratio);
            bayes.reset();
            PredictorEffectiveness comp220GraphBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, graphLearningSetSelector,assessmentToLearn, conceptGraph220, atriskGroupings,ratio);
            bayes.reset();
            PredictorEffectiveness comp220BaseSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, baseLearningSetSelector, assessmentToLearn, conceptGraph220, atriskGroupings, ratio);
            PredictorEffectiveness comp220GraphSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, graphLearningSetSelector, assessmentToLearn, conceptGraph220, atriskGroupings, ratio);
            System.out.println("\t\t" + ratio +
                    "\t\t\t  " + comp220BaseBayesEffectiveness.getPercentCorrect() +
                    "\t\t" + comp220GraphBayesEffectiveness.getPercentCorrect() +
                    "\t\t" + comp220BaseSimpleEffectiveness.getPercentCorrect() +
                    "\t\t" + comp220GraphSimpleEffectiveness.getPercentCorrect());
        }


    }

}
