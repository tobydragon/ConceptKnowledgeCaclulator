package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


public class ConceptGraphTest {
	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

	@Test
    public void copyConstructorTest(){
	    ConceptGraph orig = ExampleConceptGraphFactory.makeSimple();
	    orig.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
	    orig.addSummariesToGraph(ExampleLearningObjectResponseFactory.makeSimpleResponses());

	    ConceptGraph copy = new ConceptGraph(orig);
	    Assert.assertNotNull(copy.findNodeById("A"));
	    Assert.assertEquals(copy.findNodeById("C"), copy.findNodeById("B").getChildren().get(0));
    }

    @Test
    public void buildGraphFromRecordAndRecordFromGraphTest(){
        ConceptGraphRecord origRecord = ExampleConceptGraphRecordFactory.makeSimple();
        ConceptGraph toTest = new ConceptGraph(origRecord);
        ConceptGraphRecord newRecord = toTest.buildNodesAndLinks();
        Assert.assertThat(newRecord.getConcepts(), is(origRecord.getConcepts()));
        Assert.assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks()));

    }

    @Test
    public void addLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

        Assert.assertEquals(2, graph.findNodeById("B").getLearningObjectMap().size());
        Assert.assertEquals(4, graph.findNodeById("C").getLearningObjectMap().size());
    }

    @Test
    public void addLearningObjectsAndResponsesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
        graph.addSummariesToGraph(ExampleLearningObjectResponseFactory.makeSimpleResponses());

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
    public void simpleCheckNumEachIDTest(){
        //checks if the tree has the right number of copies per orig node
        int numA = 0;
        int numB = 0;
        int numC = 0;

        ConceptGraphRecord treeLists = ExampleConceptGraphFactory.makeSimple().graphToTree().buildNodesAndLinks();
        for(ConceptRecord node : treeLists.getConcepts()){
			if(node.getLabel().equals("A")){
                numA++;
            }else if(node.getLabel().equals("B")){
                numB++;
            }else if(node.getLabel().equals("C")){
                numC++;
            }
        }
        Assert.assertEquals(1, numA);
        Assert.assertEquals(1, numB);
        Assert.assertEquals(2, numC);
    }

    @Test
	public void treeConversionTest(){
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSimple(), 3, 3, 4, 3);
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeMedium(), 4, 5, 7, 6);
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeComplex(), 5, 8, 13, 12);
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSuperComplex(), 6, 11, 24, 23);
	}

	public void checkTreeConversionByNodesAndLinksNumbers(ConceptGraph graphToTest, int expectedGraphNodeCount, int expectedGraphLinkCount, int expectedTreeNodeCount, int expectedTreeLinkCount){
		ConceptGraphRecord graphLists = graphToTest.buildNodesAndLinks();
		Assert.assertEquals(expectedGraphNodeCount, graphLists.getConcepts().size());
		Assert.assertEquals(expectedGraphLinkCount, graphLists.getLinks().size());

		ConceptGraphRecord treeLists = graphToTest.graphToTree().buildNodesAndLinks();

		Assert.assertEquals(expectedTreeNodeCount, treeLists.getConcepts().size());
		Assert.assertEquals(expectedTreeLinkCount, treeLists.getLinks().size());
	}

	//TODO: fis the original creators
    @Test
    public void treeToTreeTest(){
//        treeToTreeCheck(ExampleConceptGraphFactory.makeSimpleInputTree());
//        treeToTreeCheck(ExampleConceptGraphFactory.makeComplexInputTree());
    }

    public void treeToTreeCheck(ConceptGraph treeToTest){
        ConceptGraphRecord initialLists = treeToTest.buildNodesAndLinks();
        ConceptGraph treeFromTree = treeToTest.graphToTree();

        ConceptGraphRecord postLists = treeFromTree.buildNodesAndLinks();
        Assert.assertEquals(postLists.getConcepts().size(), initialLists.getConcepts().size());
        Assert.assertEquals(postLists.getLinks().size(), initialLists.getLinks().size());
    }

    @Test
    public void mediumReTreeTest(){
        ConceptGraph myGraph = ExampleConceptGraphFactory.makeMedium().graphToTree();
        ConceptGraph myTree = myGraph.graphToTree();
        ConceptGraph myTree2 = myTree.graphToTree();

        ConceptGraphRecord lists1 = myGraph.buildNodesAndLinks();
        ConceptGraphRecord lists2 = myTree.buildNodesAndLinks();
        ConceptGraphRecord lists3 = myTree2.buildNodesAndLinks();

        Assert.assertEquals(lists2.getConcepts().size(), lists3.getConcepts().size());

        Assert.assertEquals(lists2.getLinks().size(), lists3.getLinks().size());

        Assert.assertEquals(lists3.getLinks().size() + 1, lists3.getConcepts().size());
    }

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
	@Test
	public void makeConceptGraphFromFileTest(){
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        try {
//			ConceptGraphRecord lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecordOld.class);
//			Assert.assertEquals(11, lists.getNodes().size());
//			Assert.assertEquals(11, lists.getLinks().size());
//
//			ConceptGraph myGraph = new ConceptGraph(lists);
//			ConceptGraph myTree = myGraph.graphToTree();
//
//			ConceptGraphRecordOld listsFromTree = myTree.buildNodesAndLinks();
//
//			Assert.assertEquals(16, listsFromTree.getNodes().size());
//			Assert.assertEquals(15, listsFromTree.getLinks().size());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
    @Test
	public void makeJsonFromConceptGraphTreeTest(){

//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//		String outputLocation = "out/test/sampleoutput/CarrieJsonGraph.json";
//
//        try {
//			ConceptGraphRecordOld lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecordOld.class);
//			ConceptGraph tree = new ConceptGraph(lists).graphToTree();
//
//			try{
//			mapper.writeValue(new File(outputLocation), tree.buildNodesAndLinks());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		String jsonString = "";
//		//reads json from file
//		try {
//			for (String line : Files.readAllLines(Paths.get(outputLocation))) {
//			    jsonString += line;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		int numID = jsonString.split("id").length-1;
//		int numLink = jsonString.split("parent").length-1;
//
//		Assert.assertEquals(16, numID);
//		Assert.assertEquals(15, numLink);
	}
    @Test
    public void linkLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

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
    public void addLearningObjectsFromLearningObjectLinkRecords(){
        //Creating graph
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

        //Creating Learning Object List
        List<LearningObject> learningObjects = new ArrayList<>();

        LearningObject duplicateObject = new LearningObject("Q1");
        LearningObjectResponse duplicateResponse = new LearningObjectResponse("user1","Q1",1);
        duplicateObject.addResponse(duplicateResponse);
        learningObjects.add(duplicateObject);

        LearningObject question7 = new LearningObject("Q7");
        LearningObjectResponse question7Response = new LearningObjectResponse("user1","Q7",1);
        question7.addResponse(question7Response);
        learningObjects.add(question7);




        //Creating learningObjectLinkedRecord list
        List<LearningObjectLinkRecord> learningObjectLinkRecords = new ArrayList<>();

        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        LearningObjectLinkRecord duplicateRecord = new LearningObjectLinkRecord(duplicateObject.getId(),concepts);
        learningObjectLinkRecords.add(duplicateRecord);
        LearningObjectLinkRecord question7Record = new LearningObjectLinkRecord(question7.getId(),concepts);
        learningObjectLinkRecords.add(question7Record);

        graph.addLearningObjectsFromLearningObjectLinkRecords(learningObjects,learningObjectLinkRecords);

        Assert.assertTrue(graph.getLearningObjectMap().get("Q1") != duplicateObject);
        // all info is correct
        Assert.assertEquals(question7, graph.findNodeById("B").getLearningObjectMap().get("Q7"));
        Assert.assertEquals(question7.getResponses().size(),graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().size());
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
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
        graph.addSummariesToGraph(ExampleLearningObjectResponseFactory.makeSimpleResponses());
        graph.calcKnowledgeEstimates();

        Assert.assertEquals(0.625, graph.findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.75, graph.findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, graph.findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    //TODO: Adapt to new graph creation once data is reinstated
//	@Test
//	public void calcActualCompTest() {
//        //create your selectionGraph and add responses to it
//
//		float delta = (float) .01;
//
//		//Checks children on If Statement to make sure their actualComps are correct
//		//(gotten from visualized graph)
//		//file:///Users/willsuchanek/git/metafora-project/MonitorInterventionMetafora/war/TreeDisplay/index.html
//		Assert.assertEquals(0, selectionListMap.get("Boolean").getKnowledgeEstimate(),delta);
//		Assert.assertEquals(1, selectionListMap.get("test_question6_4_2").getKnowledgeEstimate(),delta);
//		Assert.assertEquals(1, selectionListMap.get("test_question6_4_1").getKnowledgeEstimate(),delta);
//
//
//		//Calculates the actualComp for If statements children
//		float sumOfChildren = 0;
//		int childCounter = 0;
//		for(ConceptNode node :selectionListMap.get("If Statement").getChildren()){
//			sumOfChildren+=node.getKnowledgeEstimate();
//			childCounter++;
//		}
//		//makes sure the calculated number is equal to the number stored in If Statements
//		Assert.assertEquals(sumOfChildren/childCounter, selectionListMap.get("If Statement").getKnowledgeEstimate(),delta);
//
//		//Checks children on Control to make sure their actualComps are correct
//		//(gotten from visualized graph)
//		//file:///Users/willsuchanek/git/metafora-project/MonitorInterventionMetafora/war/TreeDisplay/index.html
//		Assert.assertEquals(.67, selectionListMap.get("If Statement").getKnowledgeEstimate(),delta);
//		Assert.assertEquals(0, selectionListMap.get("Loops").getKnowledgeEstimate(),delta);
//
//		//Calculates the actualComp for Control children
//		sumOfChildren = 0;
//		childCounter = 0;
//		for(ConceptNode node :selectionListMap.get("Control").getChildren()){
//			sumOfChildren+=node.getKnowledgeEstimate();
//			childCounter++;
//		}
//
//		//makes sure the calculated number is equal to the number stored in If Statements
//		Assert.assertEquals(sumOfChildren/childCounter, selectionListMap.get("Control").getKnowledgeEstimate(),delta);
//	}

}

	
