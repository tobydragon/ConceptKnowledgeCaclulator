package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningResourceRecord;
import edu.ithaca.dragonlab.ckc.learningobject.*;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class ConceptGraphTest {
	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

    @Test
    public void buildLearningObjectListSimpleTest(){
        ConceptGraph orig = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        //from A nodes
        HashMap<String, Integer> testCompareA = new HashMap<String, Integer>();
        testCompareA.put("Q1",1);
        testCompareA.put("Q2",1);
        testCompareA.put("Q3",2);
        testCompareA.put("Q4",2);
        testCompareA.put("Q5",2);
        testCompareA.put("Q6",2);

        Assert.assertEquals(testCompareA, orig.buildLearningObjectSummaryList("A"));

        //from B node
        HashMap<String, Integer> testCompareB = new HashMap<String, Integer>();
        testCompareB.put("Q1",1);
        testCompareB.put("Q2",1);
        testCompareB.put("Q3",1);
        testCompareB.put("Q4",1);
        testCompareB.put("Q5",1);
        testCompareB.put("Q6",1);

        Assert.assertEquals(testCompareB, orig.buildLearningObjectSummaryList("B"));


        //from c node
        HashMap<String, Integer> testCompareC = new HashMap<String, Integer>();
        testCompareC.put("Q3",1);
        testCompareC.put("Q4",1);
        testCompareC.put("Q5",1);
        testCompareC.put("Q6",1);

        Assert.assertEquals(testCompareC, orig.buildLearningObjectSummaryList("C"));

        //from a node not in graph
        Assert.assertEquals(null, orig.buildLearningObjectSummaryList("W"));
    }


	@Test
    public void copyConstructorTest(){
	    ConceptGraph orig = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

	    ConceptGraph copy = new ConceptGraph(orig, "Copy");
	    Assert.assertNotNull(copy.findNodeById("A"));
	    Assert.assertEquals(copy.findNodeById("C"), copy.findNodeById("B").getChildren().get(0));
    }

    @Test
    public void buildGraphFromRecordAndRecordFromGraphTest(){
        ConceptGraphRecord origRecord = ExampleConceptGraphRecordFactory.makeSimple();
        ConceptGraph toTest = new ConceptGraph(origRecord);
        ConceptGraphRecord newRecord = toTest.buildConceptGraphRecord();
        Assert.assertEquals(3, newRecord.getConcepts().size());
        Assert.assertEquals(3, newRecord.getLinks().size());
        Assert.assertThat(newRecord.getConcepts(), containsInAnyOrder(origRecord.getConcepts().toArray()));
        Assert.assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks().toArray()));

        origRecord = ExampleConceptGraphRecordFactory.makeSuperComplex();
        toTest = new ConceptGraph(origRecord);
        newRecord = toTest.buildConceptGraphRecord();
        Assert.assertEquals(6, newRecord.getConcepts().size());
        Assert.assertEquals(11, newRecord.getLinks().size());
        Assert.assertThat(newRecord.getConcepts(), containsInAnyOrder(origRecord.getConcepts().toArray()));
        Assert.assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks().toArray()));
    }

    @Test
    public void buildGraphConstructorTest(){
        ConceptGraphRecord structure = ExampleConceptGraphRecordFactory.makeSimple();
        List<LearningResourceRecord> lolinks = ExampleLearningObjectLinkRecordFactory.makeSimpleLOLRecords();
        List<LearningObjectResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        ConceptGraph graph = new ConceptGraph(structure, lolinks, responses);

        Assert.assertEquals(2, graph.findNodeById("A").getChildren().size());
        Assert.assertEquals(1, graph.findNodeById("B").getChildren().size());
        Assert.assertEquals(0, graph.findNodeById("C").getChildren().size());

        Assert.assertEquals(6, graph.getLearningObjectMap().size());
        Assert.assertEquals(0, graph.findNodeById("A").getLearningObjectMap().size());
        Assert.assertEquals(2, graph.findNodeById("B").getLearningObjectMap().size());
        Assert.assertEquals(4, graph.findNodeById("C").getLearningObjectMap().size());

        Assert.assertEquals(3, graph.getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(3, graph.getLearningObjectMap().get("Q4").getResponses().size());
        Assert.assertEquals(2, graph.getLearningObjectMap().get("Q6").getResponses().size());
    }

    @Test
    public void addLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        Assert.assertEquals(2, graph.findNodeById("B").getLearningObjectMap().size());
        Assert.assertEquals(4, graph.findNodeById("C").getLearningObjectMap().size());
    }

    @Test
    public void addLearningObjectsAndResponsesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        Assert.assertEquals(3, graph.findNodeById("B").getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(3, graph.findNodeById("B").getLearningObjectMap().get("Q2").getResponses().size());
        Assert.assertEquals(3, graph.findNodeById("C").getLearningObjectMap().get("Q3").getResponses().size());
        Assert.assertEquals(3, graph.findNodeById("C").getLearningObjectMap().get("Q4").getResponses().size());
        Assert.assertEquals(2, graph.findNodeById("C").getLearningObjectMap().get("Q5").getResponses().size());
        Assert.assertEquals(2, graph.findNodeById("C").getLearningObjectMap().get("Q6").getResponses().size());

        Assert.assertEquals(1,     graph.findNodeById("B").getLearningObjectMap().get("Q1").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1,     graph.findNodeById("B").getLearningObjectMap().get("Q2").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.667, graph.findNodeById("C").getLearningObjectMap().get("Q3").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.333, graph.findNodeById("C").getLearningObjectMap().get("Q4").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5,   graph.findNodeById("C").getLearningObjectMap().get("Q5").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5,   graph.findNodeById("C").getLearningObjectMap().get("Q6").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    @Test
    public void linkLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        //TODO: Did this line matter, because it can't work like that anymore...
        //graph.addLearningObjects(ExampleLearningObjectLinkRecordFactory.makeSimpleLearningObjectDef());

        LearningObject duplicateObject = new LearningObject("Q1");
        LearningObjectResponse duplicateResponse = new LearningObjectResponse("user1","Q1",1);

        // Tries to add Q1 but it already exists so it does not get added and returns false
        duplicateObject.addResponse(duplicateResponse);
        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        Assert.assertEquals(-1, graph.linkLearningObjects(duplicateObject,concepts));


        // New question to be linked in to the existing graph
        LearningObject myObject = new LearningObject("Q7");
        LearningObjectResponse myResponse = new LearningObjectResponse("user1","Q7",1);

        concepts.add("D");
        myObject.addResponse(myResponse);


        // Tests adding myObject to B C and D and returns 2 because it only added to B and C but D does not exist
        Assert.assertEquals(2, graph.linkLearningObjects(myObject, concepts));
        // all info is correct
        Assert.assertEquals(myObject, graph.findNodeById("B").getLearningObjectMap().get("Q7"));
        Assert.assertEquals(myObject.getResponses().size(),graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().size());
        Assert.assertEquals("Q7",graph.findNodeById("B").getLearningObjectMap().get("Q7").getId());
        Assert.assertEquals("user1",graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().get(0).getUserId());
        Assert.assertEquals(3,graph.findNodeById("B").getLearningObjectMap().size());
        // If adding learning object to multiple different Concepts, it points to the same learning object
        Assert.assertEquals(true, graph.findNodeById("B").getLearningObjectMap().get("Q7") == graph.findNodeById("C").getLearningObjectMap().get("Q7"));
        Assert.assertEquals("Q7",graph.getLearningObjectMap().get("Q7").getId());
        // Makes sure the new question was only added once to Learning Object map (previously had 6 questions, now 7)
        Assert.assertEquals(7,graph.getLearningObjectMap().size());
    }

    @Test
    public void addLearningObjectsFromLearningObjectLinkRecordsTest(){
        //Creating graph
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        List<LearningObjectResponse> responses = new ArrayList<>();

        responses.add( new LearningObjectResponse("user1","Q1",1));
        responses.add( new LearningObjectResponse("user1","Q7",1));

        //Creating learningObjectLinkedRecord list
        List<LearningResourceRecord> learningObjectLinkRecords = new ArrayList<>();

        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        LearningResourceRecord question7Record = new LearningResourceRecord("Q7", Arrays.asList(LearningResource.Type.ASSESSMENT, LearningResource.Type.PRACTICE),concepts, 1, 1);
        learningObjectLinkRecords.add(question7Record);

        graph.addLearningResourcesFromRecords(learningObjectLinkRecords);
        graph.addLearningObjectResponses(responses);

        // all info is correct
        Assert.assertEquals(1,graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().size());
        Assert.assertEquals("Q7",graph.findNodeById("B").getLearningObjectMap().get("Q7").getId());
        Assert.assertEquals("user1",graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().get(0).getUserId());
        Assert.assertEquals(3,graph.findNodeById("B").getLearningObjectMap().size());
        // If adding learning object to multiple different Concepts, it points to the same learning object
        Assert.assertEquals(true, graph.findNodeById("B").getLearningObjectMap().get("Q7") == graph.findNodeById("C").getLearningObjectMap().get("Q7"));
        Assert.assertEquals("Q7",graph.getLearningObjectMap().get("Q7").getId());
        // Makes sure the new question was only added once to Learning Object map (previously had 6 questions, now 7)
        Assert.assertEquals(7,graph.getLearningObjectMap().size());
    }

	@Test
	public void calcKnowledgeEstimateTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        graph.calcDataImportance();
        graph.calcKnowledgeEstimates();

        Assert.assertEquals(0.5, graph.findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.688, graph.findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.615, graph.findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    @Test
    public void calcKnowledgeEstimateMoreComplexTest() {

        ConceptGraph orig = ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        Assert.assertEquals(0.806, orig.findNodeById("Boolean").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.783090, orig.findNodeById("Boolean Expressions").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.746, orig.findNodeById("If Statement").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.722, orig.findNodeById("While Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.566, orig.findNodeById("Counting").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.575, orig.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

    }

    @Test
    public void isComplementaryTest() {

        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraphTest.json", "test/testresources/ManuallyCreated/simpleResourceTest.json", "test/testresources/ManuallyCreated/simpleAssessmentTest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();

        ConceptGraph gr = graphs.getUserGraph("s1");
        ConceptNode node1 = gr.findNodeById("A");

        ConceptGraph gr2 = graphs.getUserGraph("s2");
        ConceptNode node2 = gr2.findNodeById("A");

        ConceptGraph gr3 = graphs.getUserGraph("s3");
        ConceptNode node3 = gr3.findNodeById("A");

        ConceptGraph gr4 = graphs.getUserGraph("s4");
        ConceptNode node4 = gr4.findNodeById("A");

        try {
            Assert.assertEquals(gr.isComplementary(node1, node2), true);
            Assert.assertEquals(gr.isComplementary(node1, node3), false);
            Assert.assertEquals(gr.isComplementary(node1, node4), false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void calcTotalKnowledgeEstimateTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraphTest.json", "test/testresources/ManuallyCreated/simpleResourceTest.json", "test/testresources/ManuallyCreated/simpleAssessmentTest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();

        ConceptGraph gr = graphs.getUserGraph("s1");
        Assert.assertEquals(1.7999999999999998, gr.calcTotalKnowledgeEstimate("all"), DataUtil.OK_FLOAT_MARGIN);

        ConceptGraph gr2 = graphs.getUserGraph("s2");
        Assert.assertEquals(1.7999999999999998, gr2.calcTotalKnowledgeEstimate("all"), DataUtil.OK_FLOAT_MARGIN);


        ConceptGraph gr3 = graphs.getUserGraph("s3");
        Assert.assertEquals(1.7999999999999998, gr3.calcTotalKnowledgeEstimate("all"), DataUtil.OK_FLOAT_MARGIN);

        ConceptGraph gr4 = graphs.getUserGraph("s4");
        Assert.assertEquals(1.7999999999999998, gr4.calcTotalKnowledgeEstimate("all"), DataUtil.OK_FLOAT_MARGIN);

    }

        @Test
    public void buildDirectConceptLinkCountTest() {

        ConceptGraph orig = ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        HashMap<String, Integer> map = orig.buildDirectConceptLinkCount();

        int q1 =  map.get("Q1");
        int q2 =  map.get("Q2");
        int q3 =  map.get("Q3");
        int q4 =  map.get("Q4");
        int q5 =  map.get("Q5");
        int q6 =  map.get("Q6");
        int q7 =  map.get("Q7");
        int q8 =  map.get("Q8");
        int q9 =  map.get("Q9");
        int q10 =  map.get("Q10");
        int q11 =  map.get("Q11");
        int q12 =  map.get("Q12");
        int q13 =  map.get("Q13");
        int q14 =  map.get("Q14");

        Assert.assertEquals(q1, 1);
        Assert.assertEquals(q2, 1);
        Assert.assertEquals(q3, 2);
        Assert.assertEquals(q4, 2);
        Assert.assertEquals(q5, 2);
        Assert.assertEquals(q6, 1);
        Assert.assertEquals(q7, 1);
        Assert.assertEquals(q8, 1);
        Assert.assertEquals(q9, 3);
        Assert.assertEquals(q10, 4);
        Assert.assertEquals(q11, 1);
        Assert.assertEquals(q12, 1);
        Assert.assertEquals(q13, 1);
        Assert.assertEquals(q14, 1);

    }



}

	
