package edu.ithaca.dragonlab.ckc.suggester;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragonlab.ckc.conceptgraph.*;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectLinkRecordFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by home on 5/20/17.
 */
public class LearningObjectSuggesterTest {

    @Test
    public void suggestionResourceSimpleTest(){
        ConceptGraph orig = ExampleConceptGraphFactory.simpleTestGraphTest();

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(orig);
        SuggestionResource res =  new SuggestionResource(orig, concepts);
        List<LearningObjectSuggestion> incomTest = res.incompleteList;
        List<LearningObjectSuggestion> wrongTest = res.wrongList;

        Assert.assertEquals(wrongTest.size(),2);
        Assert.assertEquals(wrongTest.get(0).getId(), "Q4");
        Assert.assertEquals(wrongTest.get(1).getId(), "Q5");

        Assert.assertEquals(incomTest.size(),0);
    }

    @Test
    public void suggestionResourceMediumTest(){
        ConceptGraph orig= ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(orig);
        SuggestionResource res =  new SuggestionResource(orig, concepts);

        List<LearningObjectSuggestion> incomTest = res.incompleteList;
        List<LearningObjectSuggestion> wrongTest = res.wrongList;

        Assert.assertEquals(incomTest.size(),5);
        Assert.assertEquals(incomTest.get(0).getId(),"Q10");
        Assert.assertEquals(incomTest.get(1).getId(),"Q10");
        Assert.assertEquals(incomTest.get(2).getId(),"Q3");
        Assert.assertEquals(incomTest.get(3).getId(),"Q6");
        Assert.assertEquals(incomTest.get(4).getId(),"Q6");

        Assert.assertEquals(wrongTest.size(),4);
        Assert.assertEquals(wrongTest.get(0).getId(), "Q9");
        Assert.assertEquals(wrongTest.get(1).getId(), "Q9");
        Assert.assertEquals(wrongTest.get(2).getId(), "Q1");
        Assert.assertEquals(wrongTest.get(3).getId(), "Q2");

    }

    @Test
    public void conceptsToWorkOnTest(){
        ConceptGraph orig= ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(orig);

        Assert.assertEquals(concepts.size(), 2);
        Assert.assertEquals(concepts.get(0).getID(), "If Statement");
        Assert.assertEquals(concepts.get(1).getID(), "While Loop");

    }

    @Test
    public void RealDataConceptsTOWorkOn() throws IOException {

        CohortConceptGraphs cohortConceptGraphs = null;

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson("resources/comp220/comp220Graph.json");
        List<LearningObjectLinkRecord> linkRecord = LearningObjectLinkRecord.buildListFromJson("resources/comp220/comp220Resources.json");
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        CSVReader csvReader = new CSVReader("localresources/comp220/comp220ExampleDataPortion.csv");
        List<LearningObjectResponse> assessments = csvReader.getManualGradedResponses();

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        ConceptGraph userGraph = cohortConceptGraphs.getUserGraph("s13");

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);

        Assert.assertEquals(concepts.get(0).getID(), "Pointers");

    }


    @Test
    public void RealDataConceptsTOWorkOnZeroSugg() throws IOException {

        CohortConceptGraphs cohortConceptGraphs = null;

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson("resources/comp220/comp220Graph.json");
        List<LearningObjectLinkRecord> linkRecord = LearningObjectLinkRecord.buildListFromJson("resources/comp220/comp220Resources.json");
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        CSVReader csvReader = new CSVReader("localresources/comp220/comp220ExampleDataPortion.csv");
        List<LearningObjectResponse> assessments = csvReader.getManualGradedResponses();

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        ConceptGraph userGraph = cohortConceptGraphs.getUserGraph("s11");

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);

        Assert.assertEquals(concepts.size(), 0);
    }

    @Test
    public void buildSuggestionMapSimpleTest() {
        ConceptGraph orig = ExampleConceptGraphFactory.simpleTestGraphTest();
        //what it is

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(orig);
        HashMap<String, List<LearningObjectSuggestion>> objectSuggestionMap = LearningObjectSuggester.buildSuggestionMap(concepts,1,orig);

        //what it should be
        List<LearningObjectSuggestion> testList3 = new ArrayList<>();
        Assert.assertEquals(1, objectSuggestionMap.size());
        Assert.assertEquals(testList3, objectSuggestionMap.get("C"));

    }

    @Test
    public void buildSuggestionMapWillOneStudentTest() {
        ConceptGraph orig= ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(orig);
        HashMap<String, List<LearningObjectSuggestion>> objectSuggestionMap = LearningObjectSuggester.buildSuggestionMap(concepts,1,orig);

        Assert.assertEquals(2, objectSuggestionMap.size());
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(1).getId(), "Q3");
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(0).getId(), "Q10");
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(2).getId(), "Q6");
        Assert.assertEquals(objectSuggestionMap.get("While Loop").get(0).getId(), "Q10");
        Assert.assertEquals(objectSuggestionMap.get("While Loop").get(1).getId(), "Q6");


    }

    @Test
    public void suggestedOrderBuildLearningObjectListTest(){
        List<LearningObjectLinkRecord> myList = ExampleLearningObjectLinkRecordFactory.makeSimpleLOLRecords();
        myList.add(new LearningObjectLinkRecord("Q10", Arrays.asList("A"),1));
        ConceptGraph orig = new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(),
                myList, ExampleLearningObjectResponseFactory.makeSimpleResponses());

//        //from A nodes
        HashMap<String, Integer> testCompareA = new HashMap<String, Integer>();
        testCompareA.put("Q3",2);
        testCompareA.put("Q4",2);
        testCompareA.put("Q5",2);
        testCompareA.put("Q6",2);
        testCompareA.put("Q1",1);
        testCompareA.put("Q2",1);
        testCompareA.put("Q10",1);

        HashMap<String, Integer> learningSummaryFromA = orig.buildLearningObjectSummaryList("A");
        //makes sure that buildLearningObjectSummaryList works
        Assert.assertEquals(testCompareA,learningSummaryFromA);

        //build the suggested learning object list
        List<LearningObjectSuggestion> suggestedList = LearningObjectSuggester.buildLearningObjectSuggestionList(learningSummaryFromA, orig.getLearningObjectMap(),"A");



        //this is ordered based on "level"
        List<LearningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningObjectSuggestion("Q1", 1, LearningObjectSuggestion.Level.RIGHT, "A") );
        suggestListTest.add(new LearningObjectSuggestion("Q2", 1, LearningObjectSuggestion.Level.RIGHT, "A") );
        suggestListTest.add(new LearningObjectSuggestion("Q3", 2, LearningObjectSuggestion.Level.WRONG,"A") );
        suggestListTest.add(new LearningObjectSuggestion("Q4", 2, LearningObjectSuggestion.Level.WRONG, "A") );
        suggestListTest.add(new LearningObjectSuggestion("Q5", 2, LearningObjectSuggestion.Level.WRONG, "A") );
        suggestListTest.add(new LearningObjectSuggestion("Q6", 2, LearningObjectSuggestion.Level.WRONG, "A") );
        suggestListTest.add(new LearningObjectSuggestion("Q10", 1, LearningObjectSuggestion.Level.INCOMPLETE, "A") );

        //orders the list based off of "level"
        LearningObjectSuggester.sortSuggestions(suggestedList);

        for (int i =0; i<suggestedList.size(); i++){

            Assert.assertEquals(suggestedList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestedList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestedList.get(i).getLevel(),suggestListTest.get(i).getLevel());
            Assert.assertEquals(suggestedList.get(i).getReasoning(), suggestListTest.get(i).getReasoning());

        }
    }

    @Test
    public void research1Test(){
        try{
        CSVReader csvReader = new CSVReader("test/testresources/ManuallyCreated/researchAssessment1.csv");

            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson("test/testresources/ManuallyCreated/researchConceptGraph.json");
            List<LearningObjectLinkRecord> LOLRlist = LearningObjectLinkRecord.buildListFromJson("test/testresources/ManuallyCreated/researchResource1.json");

            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());
            graph.calcKnowledgeEstimates();

            List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(graph);
            SuggestionResource res =  new SuggestionResource(graph, concepts);

            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion>  wrongTest = res.wrongList;

            Assert.assertEquals(incomTest.get(0).getId(), "How are while loops and booleans related?");
            Assert.assertEquals(incomTest.get(1).getId(), "What are the important rules for variable names?");
            Assert.assertEquals(incomTest.get(2).getId(), "What are the things you need for a while loop?");
            Assert.assertEquals(incomTest.get(3).getId(), "What is an assignment statement?");
            Assert.assertEquals(incomTest.get(4).getId(), "What is 5%7?");
            Assert.assertEquals(incomTest.get(5).getId(), "Write the following in camel case");
            Assert.assertEquals(incomTest.get(6).getId(), "What are some of the statements we have already seen?");


            Assert.assertEquals(wrongTest.get(0).getId(), "What are the differences and similarities between for loops and while loops?");
            Assert.assertEquals(wrongTest.get(1).getId(), "What is a statement?");
            Assert.assertEquals(wrongTest.get(2).getId(), "What is the proper while loop diction?");


        }catch (Exception e){
            Assert.fail();
        }


        try{
        CSVReader csvReader2 = new CSVReader("test/testresources/ManuallyCreated/researchAssessment2.csv");

            ConceptGraphRecord graphRecord2 = ConceptGraphRecord.buildFromJson("test/testresources/ManuallyCreated/researchConceptGraph.json");
            List<LearningObjectLinkRecord> LOLRlist2 = LearningObjectLinkRecord.buildListFromJson("test/testresources/ManuallyCreated/researchResource2.json");

            ConceptGraph graph2 = new ConceptGraph(graphRecord2, LOLRlist2, csvReader2.getManualGradedResponses());
            graph2.calcKnowledgeEstimates();

            List<ConceptNode> concepts2 = LearningObjectSuggester.conceptsToWorkOn(graph2);
            SuggestionResource res2 =  new SuggestionResource(graph2, concepts2);

            List<LearningObjectSuggestion> incomTest2 = res2.incompleteList;
            List<LearningObjectSuggestion>  wrongTest2 = res2.wrongList;

            Assert.assertEquals(incomTest2.get(0).getId(), "When do you want to cause a side effect?");

            Assert.assertEquals(wrongTest2.get(0).getId(), "What is printed then the following statements execute?");
            Assert.assertEquals(wrongTest2.get(1).getId(), "How many parameters can you have?");
            Assert.assertEquals(wrongTest2.get(2).getId(), "What is an assignment token?");
            Assert.assertEquals(wrongTest2.get(3).getId(), "When do you want to allow side effects?");
            Assert.assertEquals(wrongTest2.get(4).getId(), "What is a reference diagram?");


        }catch (Exception e){
            Assert.fail();
        }

    }


}
