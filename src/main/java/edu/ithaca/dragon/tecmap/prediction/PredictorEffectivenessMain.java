package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    static ConceptGraph getConceptGraph(String courseName, String datastorePath) throws IOException {
        //Get assessment filenames
        File directory = new File(datastorePath + courseName + "/private/");

        List<File> files = Arrays.asList(directory.listFiles());
        List<String> assessmentFilenames = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                if (!file.getName().equals(".gitignore")) {
                    assessmentFilenames.add(datastorePath + courseName + "/private/" + file.getName());
                }
            }
        }

        List<String> resourceFilenames = new ArrayList<>();
        String graphFilename = null;
        //Get resources
        directory = new File(datastorePath + courseName);
        files = Arrays.asList(directory.listFiles());
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().contains("Resources")) {
                    resourceFilenames.add(datastorePath + courseName + "/" + file.getName());
                } else if (file.getName().contains("Graph")) {
                    graphFilename = datastorePath + courseName + "/" + file.getName();
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

    private static void singleAssessmentTwoSetSelectorTwoPredictorTest(ConceptGraph courseGraph, String assessmentToLearn, LearningPredictor bayes, Predictor simple, LearningSetSelector baseLearningSetSelector, LearningSetSelector graphLearningSetSelector, GradeDiscreteGroupings atriskGroupings) throws IOException {
        System.out.println("Learning Set Size: \t Base Bayes %Correct: \t Graph Bayes %Correct: \t Base Simple %Correct: \t Graph Simple %Correct:");
        //Will get a null pointer exception when trying to predict an assessment not connected to a resource
        for (double ratio : learningSizeRatios) {
            PredictorEffectiveness courseBaseBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, baseLearningSetSelector,assessmentToLearn, courseGraph, atriskGroupings,ratio);
            bayes.reset();
            PredictorEffectiveness courseGraphBayesEffectiveness = PredictorEffectiveness.testLearningPredictor(bayes, graphLearningSetSelector,assessmentToLearn, courseGraph, atriskGroupings,ratio);
            bayes.reset();
            PredictorEffectiveness courseBaseSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, baseLearningSetSelector, assessmentToLearn, courseGraph, atriskGroupings, ratio);
            PredictorEffectiveness courseGraphSimpleEffectiveness = PredictorEffectiveness.testPredictor(simple, graphLearningSetSelector, assessmentToLearn, courseGraph, atriskGroupings, ratio);
            System.out.println("\t\t" + ratio +
                    "\t\t\t  " + courseBaseBayesEffectiveness.getPercentCorrect() + "(" + courseBaseBayesEffectiveness.getTotalTested() + ")" +
                    "\t\t" + courseGraphBayesEffectiveness.getPercentCorrect() + "(" + courseGraphBayesEffectiveness.getTotalTested() + ")" +
                    "\t\t" + courseBaseSimpleEffectiveness.getPercentCorrect() + "(" + courseBaseSimpleEffectiveness.getTotalTested() + ")" +
                    "\t\t" + courseGraphSimpleEffectiveness.getPercentCorrect() + "(" + courseGraphSimpleEffectiveness.getTotalTested() + ")");
        }
    }

    private static void allAssessmentTwoSelectorTwoPredictorTest(ConceptGraph courseGraph, LearningPredictor bayes, Predictor simple, LearningSetSelector baseLearningSetSelector, LearningSetSelector graphLearningSetSelector, GradeDiscreteGroupings atriskGroupings, double ratio) throws IOException {
        //Get all assessments from courseGraph
        List<AssessmentItem> allAssessments = new ArrayList<>(courseGraph.getAssessmentItemMap().values());
        Map<String, Double> courseBaseBayes = new HashMap<>();
        Map<String, Double> courseGraphBayes = new HashMap<>();
        Map<String, Double> courseBaseSimple = new HashMap<>();
        Map<String, Double> courseGraphSimple = new HashMap<>();
        //Get results for each assessment
        for (AssessmentItem item : allAssessments) {
            String assessment = item.getId();
            courseBaseBayes.put(assessment, PredictorEffectiveness.testLearningPredictor(bayes, baseLearningSetSelector, assessment, courseGraph, atriskGroupings,ratio).getPercentCorrect());
            bayes.reset();
            courseGraphBayes.put(assessment, PredictorEffectiveness.testLearningPredictor(bayes, graphLearningSetSelector, assessment, courseGraph, atriskGroupings,ratio).getPercentCorrect());
            bayes.reset();
            courseBaseSimple.put(assessment, PredictorEffectiveness.testPredictor(simple, baseLearningSetSelector, assessment, courseGraph, atriskGroupings, ratio).getPercentCorrect());
            courseGraphSimple.put(assessment, PredictorEffectiveness.testPredictor(simple, graphLearningSetSelector, assessment, courseGraph, atriskGroupings, ratio).getPercentCorrect());
        }

        //Sort the results
        Object[] courseBaseBayesSorted = courseBaseBayes.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();
        Object[] courseGraphBayesSorted = courseGraphBayes.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();
        Object[] courseBaseSimpleSorted = courseBaseSimple.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();
        Object[] courseGraphSimpleSorted = courseGraphSimple.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray();

        //Output the sorted results
        //Rank lowest % correct to highest
        System.out.println("\tBase Bayes:\t\t\t Graph Bayes: \t\t\t Base Simple \t\t\t Graph Simple");
        for (int i = 0; i < courseBaseBayesSorted.length; i++) {
            String baseBayes = courseBaseBayesSorted[i].toString().split("=")[0] + "(" + courseBaseBayesSorted[i].toString().split("=")[1].substring(0, 3) + ")";
            String graphBayes = courseGraphBayesSorted[i].toString().split("=")[0] + "(" + courseGraphBayesSorted[i].toString().split("=")[1].substring(0, 3) + ")";
            String baseSimple = courseBaseSimpleSorted[i].toString().split("=")[0] + "(" + courseBaseSimpleSorted[i].toString().split("=")[1].substring(0, 3) + ")";
            String graphSimple = courseGraphSimpleSorted[i].toString().split("=")[0] + "(" + courseGraphSimpleSorted[i].toString().split("=")[1].substring(0, 3) + ")";
            System.out.println(i + ")" + baseBayes + "\t" + graphBayes + "\t" + baseSimple + "\t" + graphSimple);
        }
    }

    public static void main(String[] args) throws IOException {
        GradeDiscreteGroupings defaultGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "discreteGroupings.json");
        GradeDiscreteGroupings atriskGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_MAIN_PREDICTION_PATH + "atriskGroupings.json");
        //For testing the Bayes Predictor Effectiveness
        LearningPredictor bayes = new BayesPredictor(defaultGroupings, atriskGroupings);

        //For testing the Simple Predictor Effectiveness
        Predictor simple = new SimplePredictor(atriskGroupings);

        //Two different ways to select learning sets
        LearningSetSelector baseLearningSetSelector = new BaseLearningSetSelector();
        LearningSetSelector graphLearningSetSelector = new GraphLearningSetSelector();

        //Testing for COMP220 Data
        ConceptGraph conceptGraph220 = getConceptGraph("comp220Dragon", Settings.DEFAULT_MAIN_DATASTORE_PATH);

        //Single Assessment Two Selector Two Predictor Test
        String assessmentToLearn = "Lab 4: Recursion";
//        singleAssessmentTwoSetSelectorTwoPredictorTest(conceptGraph220, assessmentToLearn, bayes, simple, baseLearningSetSelector, graphLearningSetSelector, atriskGroupings);

        //All Assessment Two Selector Two Predictor Test
        allAssessmentTwoSelectorTwoPredictorTest(conceptGraph220, bayes, simple, baseLearningSetSelector, graphLearningSetSelector, atriskGroupings, 0.5);
    }

}
