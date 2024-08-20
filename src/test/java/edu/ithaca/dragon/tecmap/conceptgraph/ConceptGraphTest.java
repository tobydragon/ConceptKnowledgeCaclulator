package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.*;
import edu.ithaca.dragon.tecmap.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(testCompareA, orig.buildLearningResourcePathCount("A"));

        //from B node
        HashMap<String, Integer> testCompareB = new HashMap<String, Integer>();
        testCompareB.put("Q1",1);
        testCompareB.put("Q2",1);
        testCompareB.put("Q3",1);
        testCompareB.put("Q4",1);
        testCompareB.put("Q5",1);
        testCompareB.put("Q6",1);

        assertEquals(testCompareB, orig.buildLearningResourcePathCount("B"));


        //from c node
        HashMap<String, Integer> testCompareC = new HashMap<String, Integer>();
        testCompareC.put("Q3",1);
        testCompareC.put("Q4",1);
        testCompareC.put("Q5",1);
        testCompareC.put("Q6",1);

        assertEquals(testCompareC, orig.buildLearningResourcePathCount("C"));

        //from a node not in graph
        assertEquals(null, orig.buildLearningResourcePathCount("W"));
    }


	@Test
    public void copyConstructorTest(){
	    ConceptGraph orig = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

	    ConceptGraph copy = new ConceptGraph(orig, "Copy");
	    assertNotNull(copy.findNodeById("A"));
	    assertEquals(copy.findNodeById("C"), copy.findNodeById("B").getChildren().get(0));
    }

    @Test
    public void buildGraphFromRecordAndRecordFromGraphTest(){
        ConceptGraphRecord origRecord = ExampleConceptGraphRecordFactory.makeSimple();
        ConceptGraph toTest = new ConceptGraph(origRecord);
        ConceptGraphRecord newRecord = toTest.buildConceptGraphRecord();
        assertEquals(3, newRecord.getConcepts().size());
        assertEquals(3, newRecord.getLinks().size());
        assertThat(newRecord.getConcepts(), containsInAnyOrder(origRecord.getConcepts().toArray()));
        assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks().toArray()));

        origRecord = ExampleConceptGraphRecordFactory.makeSuperComplex();
        toTest = new ConceptGraph(origRecord);
        newRecord = toTest.buildConceptGraphRecord();
        assertEquals(6, newRecord.getConcepts().size());
        assertEquals(11, newRecord.getLinks().size());
        assertThat(newRecord.getConcepts(), containsInAnyOrder(origRecord.getConcepts().toArray()));
        assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks().toArray()));
    }

    @Test
    public void buildGraphConstructorTest(){
        ConceptGraphRecord structure = ExampleConceptGraphRecordFactory.makeSimple();
        List<LearningResourceRecord> lolinks = ExampleLearningObjectLinkRecordFactory.makeSimpleLOLRecords();
        List<AssessmentItemResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        ConceptGraph graph = new ConceptGraph(structure, lolinks, responses);

        assertEquals(2, graph.findNodeById("A").getChildren().size());
        assertEquals(1, graph.findNodeById("B").getChildren().size());
        assertEquals(0, graph.findNodeById("C").getChildren().size());

        assertEquals(6, graph.getAssessmentItemMap().size());
        assertEquals(0, graph.findNodeById("A").getAssessmentItemMap().size());
        assertEquals(2, graph.findNodeById("B").getAssessmentItemMap().size());
        assertEquals(4, graph.findNodeById("C").getAssessmentItemMap().size());

        assertEquals(3, graph.getAssessmentItemMap().get("Q1").getResponses().size());
        assertEquals(3, graph.getAssessmentItemMap().get("Q4").getResponses().size());
        assertEquals(2, graph.getAssessmentItemMap().get("Q6").getResponses().size());
    }

    @Test
    public void addLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        assertEquals(2, graph.findNodeById("B").getAssessmentItemMap().size());
        assertEquals(4, graph.findNodeById("C").getAssessmentItemMap().size());
    }

    @Test
    public void addLearningObjectsAndResponsesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        assertEquals(3, graph.findNodeById("B").getAssessmentItemMap().get("Q1").getResponses().size());
        assertEquals(3, graph.findNodeById("B").getAssessmentItemMap().get("Q2").getResponses().size());
        assertEquals(3, graph.findNodeById("C").getAssessmentItemMap().get("Q3").getResponses().size());
        assertEquals(3, graph.findNodeById("C").getAssessmentItemMap().get("Q4").getResponses().size());
        assertEquals(2, graph.findNodeById("C").getAssessmentItemMap().get("Q5").getResponses().size());
        assertEquals(2, graph.findNodeById("C").getAssessmentItemMap().get("Q6").getResponses().size());

        assertEquals(1,     graph.findNodeById("B").getAssessmentItemMap().get("Q1").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(1,     graph.findNodeById("B").getAssessmentItemMap().get("Q2").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.667, graph.findNodeById("C").getAssessmentItemMap().get("Q3").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.333, graph.findNodeById("C").getAssessmentItemMap().get("Q4").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.5,   graph.findNodeById("C").getAssessmentItemMap().get("Q5").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.5,   graph.findNodeById("C").getAssessmentItemMap().get("Q6").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    @Test
    public void linkLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        //TODO: Did this line matter, because it can't work like that anymore...
        //graph.addLearningObjects(ExampleLearningObjectLinkRecordFactory.makeSimpleLearningObjectDef());

        AssessmentItem duplicateObject = new AssessmentItem(LearningResourceRecord.generateUniqueID(null),"Q1");
        AssessmentItemResponse duplicateResponse = new AssessmentItemResponse("user1","_PracticeProblem1","Q1",1);

        // Tries to add Q1 but it already exists so it does not get added and returns false
        duplicateObject.addResponse(duplicateResponse);
        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        assertEquals(-1, graph.linkAssessmentItem(duplicateObject,concepts));


        // New question to be linked in to the existing graph
        AssessmentItem myObject = new AssessmentItem("_PracticeProblem7","Q7");
        AssessmentItemResponse myResponse = new AssessmentItemResponse("user1","_PracticeProblem7","Q7",1);

        concepts.add("D");
        myObject.addResponse(myResponse);


        // Tests adding myObject to B C and D and returns 2 because it only added to B and C but D does not exist
        assertEquals(2, graph.linkAssessmentItem(myObject, concepts));
        // all info is correct
        assertEquals(myObject, graph.findNodeById("B").getAssessmentItemMap().get("Q7"));
        assertEquals(myObject.getResponses().size(),graph.findNodeById("B").getAssessmentItemMap().get("Q7").getResponses().size());
        assertEquals("Q7",graph.findNodeById("B").getAssessmentItemMap().get("Q7").getText());
        assertEquals("user1",graph.findNodeById("B").getAssessmentItemMap().get("Q7").getResponses().get(0).getUserId());
        assertEquals(3,graph.findNodeById("B").getAssessmentItemMap().size());
        // If adding learning object to multiple different Concepts, it points to the same learning object
        assertSame(graph.findNodeById("B").getAssessmentItemMap().get("Q7"), graph.findNodeById("C").getAssessmentItemMap().get("Q7"));
        assertEquals("Q7",graph.getAssessmentItemMap().get("Q7").getText());
        // Makes sure the new question was only added once to Learning Object map (previously had 6 questions, now 7)
        assertEquals(7,graph.getAssessmentItemMap().size());
    }

    @Test
    public void addLearningObjectsFromLearningObjectLinkRecordsTest(){
        //Creating graph
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        List<AssessmentItemResponse> responses = new ArrayList<>();

        responses.add( new AssessmentItemResponse("user1","_PracticeProblem1","Q1",1));
        responses.add( new AssessmentItemResponse("user1","_PracticeProblem7","Q7",1));

        //Creating learningObjectLinkedRecord list
        List<LearningResourceRecord> learningObjectLinkRecords = new ArrayList<>();

        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        LearningResourceRecord question7Record = new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE),concepts, 1, 1, "Q7");
        learningObjectLinkRecords.add(question7Record);

        graph.addLearningResourcesFromRecords(learningObjectLinkRecords);
        graph.addAssessmentItemResponses(responses);

        // all info is correct
        assertEquals(1,graph.findNodeById("B").getAssessmentItemMap().get("Q7").getResponses().size());
        assertEquals("Q7",graph.findNodeById("B").getAssessmentItemMap().get("Q7").getText());
        assertEquals("user1",graph.findNodeById("B").getAssessmentItemMap().get("Q7").getResponses().get(0).getUserId());
        assertEquals(3,graph.findNodeById("B").getAssessmentItemMap().size());
        // If adding learning object to multiple different Concepts, it points to the same learning object
        assertSame(graph.findNodeById("B").getAssessmentItemMap().get("Q7"), graph.findNodeById("C").getAssessmentItemMap().get("Q7"));
        assertEquals("Q7",graph.getAssessmentItemMap().get("Q7").getText());
        // Makes sure the new question was only added once to Learning Object map (previously had 6 questions, now 7)
        assertEquals(7,graph.getAssessmentItemMap().size());
    }

	@Test
	public void calcKnowledgeEstimateTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        graph.calcDataImportance();
        graph.calcKnowledgeEstimates();

        assertEquals(0.5, graph.findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.688, graph.findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.615, graph.findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    @Test
    public void calcKnowledgeEstimateMoreComplexTest() {

        ConceptGraph orig = ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        assertEquals(0.806, orig.findNodeById("Boolean").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.783090, orig.findNodeById("Boolean Expressions").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.746, orig.findNodeById("If Statement").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        assertEquals(0.722, orig.findNodeById("While Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.566, orig.findNodeById("Counting").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        assertEquals(0.575, orig.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

    }

        @Test
    public void buildDirectConceptLinkCountTest() {

        ConceptGraph orig = ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        Map<String, Integer> map = orig.buildDirectConceptLinkCount();

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

        assertEquals(q1, 1);
        assertEquals(q2, 1);
        assertEquals(q3, 2);
        assertEquals(q4, 2);
        assertEquals(q5, 2);
        assertEquals(q6, 1);
        assertEquals(q7, 1);
        assertEquals(q8, 1);
        assertEquals(q9, 3);
        assertEquals(q10, 4);
        assertEquals(q11, 1);
        assertEquals(q12, 1);
        assertEquals(q13, 1);
        assertEquals(q14, 1);

    }

    @Test
    public void getAssessmentItemsBelowAssessmentIDNoDepthGiven() throws IOException {
        List<LearningResourceRecord> loRecords = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json");
        ConceptGraph conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                loRecords);

        List<String> assessmentsBelowQ4 = conceptGraph.getAssessmentsBelowAssessmentID("Q4");

        assertEquals(6, assessmentsBelowQ4.size());
        assertThat(assessmentsBelowQ4, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5"));

        //Check that traversal includes the assessments on the node(s) with the given ID
        List<String> assessmentsBelowQ1 = conceptGraph.getAssessmentsBelowAssessmentID("Q1");
        assertThat(assessmentsBelowQ1, containsInAnyOrder("HW3", "HW1", "HW2"));
    }

    @Test
    public void getAssessmentItemsBelowAssessmentIDGivenDepth() throws IOException {
        List<LearningResourceRecord> loRecords = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json");
        ConceptGraph conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                loRecords);

        //Test with negative steps down
        List<String> assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowAssessmentID("Q4", -1);
        assertNull(assessmentsXBelowQ4);

        //Test with 0 steps down
        assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowAssessmentID("Q4", 0);
        assertEquals(0, assessmentsXBelowQ4.size());

        //Test 1 Step down
        assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowAssessmentID("Q4", 1);
        assertEquals(4, assessmentsXBelowQ4.size());
        assertThat(assessmentsXBelowQ4, containsInAnyOrder("Q2", "HW4", "Q3", "HW5"));

        //Test 2 Steps Down
        assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowAssessmentID("Q4", 2);
        assertEquals(6, assessmentsXBelowQ4.size());
        assertThat(assessmentsXBelowQ4, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5"));

        //Check that traversal includes the assessments on the node(s) with the given ID @ 0 steps
        List<String> assessmentsBelowQ1 = conceptGraph.getAssessmentsBelowAssessmentID("Q1", 0);
        assertThat(assessmentsBelowQ1, containsInAnyOrder("HW3"));

        //And with 1 step down
        assessmentsBelowQ1 = conceptGraph.getAssessmentsBelowAssessmentID("Q1", 1);
        assertThat(assessmentsBelowQ1, containsInAnyOrder("HW3", "HW1", "HW2"));
    }

    @Test
    public void getNodesWithAssessmentId() throws IOException {
        List<LearningResourceRecord> loRecords = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json");
        ConceptGraph conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                loRecords);

        List<ConceptNode> nodesWithQ4 = conceptGraph.getNodesWithAssessmentText("Q4");
        assertEquals(1, nodesWithQ4.size());
        assertEquals("Loops", nodesWithQ4.get(0).getID());
    }

    @Test
    public void getAssessmentsBelowNode() throws IOException {
        List<LearningResourceRecord> loRecords = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json");
        ConceptGraph conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                loRecords);

        List<ConceptNode> nodesWithQ4 = conceptGraph.getNodesWithAssessmentText("Q4");
        //Test 0 steps down
        Set<String> assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowNode(new HashSet<>(), nodesWithQ4.get(0), 0);
        assertEquals(0, assessmentsXBelowQ4.size());

        //Test 1 Step down
        assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowNode(new HashSet<>(), nodesWithQ4.get(0), 1);
        assertEquals(4, assessmentsXBelowQ4.size());
        assertThat(assessmentsXBelowQ4, containsInAnyOrder("Q2", "HW4", "Q3", "HW5"));

        //Test 2 Steps Down
        assessmentsXBelowQ4 = conceptGraph.getAssessmentsBelowNode(new HashSet<>(),nodesWithQ4.get(0), 2);
        assertEquals(6, assessmentsXBelowQ4.size());
        assertThat(assessmentsXBelowQ4, containsInAnyOrder("Q2", "HW4", "HW1", "HW2", "Q3", "HW5"));

    }

}

	
