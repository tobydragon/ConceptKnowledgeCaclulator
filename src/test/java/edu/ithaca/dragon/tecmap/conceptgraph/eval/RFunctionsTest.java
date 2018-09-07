package edu.ithaca.dragon.tecmap.conceptgraph.eval;


import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bleblanc2 on 6/19/17.
 */
public class RFunctionsTest {


    //@Test
    public void studentKnowledgeEstAvgTest(){
        String file = Settings.TEST_RESOURCE_DIR + "ManuallyCreated/partialComplexRealitsticAssessment.csv";
        try {
            CSVReader data = new SakaiReader(file);
            List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
            double avg = RFunctions.StudentKnowledgeEstAvg(newMatrix, "stu1");
            Assert.assertEquals(.7238, avg, 0.001);
            avg = RFunctions.StudentKnowledgeEstAvg(newMatrix, "stu2");
            Assert.assertEquals(.861, avg, 0.001);
            avg = RFunctions.StudentKnowledgeEstAvg(newMatrix, "stu3");
            Assert.assertEquals(.995, avg, 0.001);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }



    //These two functions below, findFactorCount and getFactorMatrix, are commented out because they are forced to print through R's methods.
    //These tests should be used if getFactorMatrix in ConsoleUI or ConceptKnowledgeCalculator need debugging

    //@Test
    public void findFactorCountTest(){
        try {
            CSVReader data = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv");
            List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);

            int factorCount = RFunctions.findFactorCount(newMatrix);
            Assert.assertEquals(2, factorCount, 0);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }



    public static void getFactorMatrixTest(){

        try{
            CSVReader data = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv");
            List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
            RFunctions.getFactorMatrix(newMatrix);
            TimeUnit.SECONDS.sleep(5);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }



    //@Test
    public static void returnFactorMatrixTest(){
        try {
            CSVReader data = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv");
            List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);

            double[][] statsMatrix = RFunctions.returnFactorMatrix(newMatrix);

    double[] arr1 = new double[7];
    double[] arr2 = new double[7];

    for (int i = 0; i < 7; i++) {
    arr1[i] = statsMatrix[i][0];
    }
    //System.out.println("Factor 1: "+ Arrays.toString(arr1));
    //System.out.println("----------");
    for (int i = 0; i < 7; i++) {
    arr2[i] = statsMatrix[i][1];
    }
    //System.out.println("Factor 2: "+ Arrays.toString(arr2));

    double[] exArr1 = new double[]{0.6808680, 0.5922147, 0.6842057, 0.8361693, 0.9434352, 0.1443587, 0.2874747};
    double[] exArr2 = new double[]{0.4648499, 0.5747219, 0.5261558, 0.1391935, 0.2398479, 0.7946735, 0.7936599};

    Assert.assertArrayEquals(exArr1, arr1, 0.0001);
    Assert.assertArrayEquals(exArr2, arr2, 0.0001);
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }


    //@Test
    public void modelMakerTest() throws IOException{
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResource.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs ccg = new CohortConceptGraphs(graph, assessmentItemResponses);
        Assert.assertEquals("B -> Q1, Q1ToB, NA \n" +
                "B -> Q2, Q2ToB, NA \n" +
                "C -> Q3, Q3ToC, NA \n" +
                "C -> Q4, Q4ToC, NA \n" +
                "C -> Q5, Q5ToC, NA \n" +
                "C -> Q6, Q6ToC, NA \n", RFunctions.modelMaker(ccg));

        graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/comp220GraphExample.json"));
        assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "/comp220/comp220ExampleDataPortion.csv"));
        links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/comp220Resources.json"));
        graph.addLearningResourcesFromRecords(links);
        ccg = new CohortConceptGraphs(graph, assessmentItemResponses);

        Assert.assertEquals("Recursion -> Lab4Recursion, Lab4RecursionToRecursion, NA \n" +
                "Recursion -> Lab5ComparingSearches, Lab5ComparingSearchesToRecursion, NA \n" +
                "Stack vs Heap -> Lab2ArrayLibrary, Lab2ArrayLibraryToStack vs Heap, NA \n" +
                "Stack vs Heap -> Lab3ComparingArrayLibraryEfficiency, Lab3ComparingArrayLibraryEfficiencyToStack vs Heap, NA \n" +
                "Array -> Lab4Recursion, Lab4RecursionToArray, NA \n" +
                "Array -> Lab8ComparingArraysandLinkedLists, Lab8ComparingArraysandLinkedListsToArray, NA \n" +
                "Array -> Lab2ArrayLibrary, Lab2ArrayLibraryToArray, NA \n" +
                "Array -> Lab6ArrayListandTesting, Lab6ArrayListandTestingToArray, NA \n" +
                "Array -> Lab3ComparingArrayLibraryEfficiency, Lab3ComparingArrayLibraryEfficiencyToArray, NA \n" +
                "Interfaces -> Lab1GuessthePattern, Lab1GuessthePatternToInterfaces, NA \n" +
                "Pointers -> Lab4Recursion, Lab4RecursionToPointers, NA \n" +
                "Pointers -> Lab2ArrayLibrary, Lab2ArrayLibraryToPointers, NA \n" +
                "Pointers -> Lab3ComparingArrayLibraryEfficiency, Lab3ComparingArrayLibraryEfficiencyToPointers, NA \n" +
                "Linked Nodes -> Lab7LinkedList, Lab7LinkedListToLinked Nodes, NA \n" +
                "Sort -> Lab5ComparingSearches, Lab5ComparingSearchesToSort, NA \n" +
                "List -> Lab8ComparingArraysandLinkedLists, Lab8ComparingArraysandLinkedListsToList, NA \n" +
                "List -> Lab6ArrayListandTesting, Lab6ArrayListandTestingToList, NA \n" +
                "List -> Lab7LinkedList, Lab7LinkedListToList, NA \n", RFunctions.modelMaker(ccg));
    }


    public static void confirmatoryGraphTest() throws IOException, InterruptedException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResource.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs ccg = new CohortConceptGraphs(graph, assessmentItemResponses);

        CSVReader data = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv");
        List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        RFunctions.confirmatoryGraph(newMatrix, ccg);
        TimeUnit.SECONDS.sleep(5);
    }

    public static void getConfirmatoryMatrixTest() throws IOException, InterruptedException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResource.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs ccg = new CohortConceptGraphs(graph, assessmentItemResponses);

        CSVReader data = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv");
        List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        RFunctions.returnConfirmatoryMatrix(newMatrix, ccg);
        TimeUnit.SECONDS.sleep(5);
    }



    //@Test
    public void getColumnCountTest(){
        try {
            CSVReader data = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv");
            List<AssessmentItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
            Assert.assertEquals(7, RFunctions.getColumnCount(newMatrix), 0);
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    //@Test
    public void modelToFileTest() throws IOException{
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResource.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs ccg = new CohortConceptGraphs(graph, assessmentItemResponses);

        RFunctions.modelToFile(ccg);
    }

    //@Test
    public static void main(String args[]){
        //getFactorMatrixTest();
        //confirmatoryGraphTest();
        //returnFactorMatrixTest();
        System.out.println("- R prints info on deleting invalid columns \n" +
                "- R prints a matrix of factors with Learning Objects\n" +
                "- R prints other info. Not very useful\n" +
                "- R prints warning messages about the graph and should be disregarded\n" +
                "- R creates a graph displaying the exploratory factor analysis");
        //getFactorMatrixTest();
        System.out.println(
                "- R creates a graph displaying the confirmatory factor analysis");
        try {
            confirmatoryGraphTest();
        }
        catch (Exception e){
            System.out.println("ERROR: \t in confirmatoryGraphTest");
        }
        //returnConfirmatoryMatrixTest();

        //both returns a double[][] and a printout
        System.out.println("- R prints info on deleting invalid columns\n" +
                "- The function returns a matrix of the factors seen from getMatrixTest()");
        //returnFactorMatrixTest();


    }


}
