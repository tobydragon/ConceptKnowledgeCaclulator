package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.util.TestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class ConceptGraphTest {
	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

        @Test
    public void SuggestedConceptNodeMapSimpleTest() {
        ConceptGraph orig= PracticalConceptGraphTest.simpleTestGraphTest();
        //what it is
        HashMap<ConceptNode, List<learningObjectSuggestion>> objectSuggestionMap = orig.SuggestedConceptNodeMap();
        //what it should be
        HashMap<ConceptNode, List<learningObjectSuggestion>> mapTest = new HashMap<>();

        List<learningObjectSuggestion> testList3 = new ArrayList<>();
        mapTest.put(new ConceptNode("C"), testList3);

//        Assert.assertEquals(objectSuggestionMap.size(), mapTest.size());

        System.out.print("actual map (objectsuggestionMap)");
        for (ConceptNode key : objectSuggestionMap.keySet()) {
//            List<learningObjectSuggestion> listlist2 = mapTest.get(key);

            List<learningObjectSuggestion> SuggestionObjectList = objectSuggestionMap.get(key);
            System.out.println("\n");
            System.out.print(key.getID() + " " + key.getKnowledgeEstimate()+ ": ");
            for (int x=0; x<SuggestionObjectList.size(); x++){
                System.out.print(SuggestionObjectList.get(x).getId()+ "- " + SuggestionObjectList.get(x).getLevel()+ " "+ " pathnum "+SuggestionObjectList.get(x).getPathNum()+ " " );
            }
        }


        System.out.println("\n");
        System.out.print("\n testing against (maptest)");
        for (ConceptNode name : mapTest.keySet()) {

            List<learningObjectSuggestion> testObjectList = mapTest.get(name);
            System.out.println("\n");
            System.out.print(name.getID() + " " + name.getKnowledgeEstimate()+ ": ");
            for (int x=0; x<testObjectList.size(); x++){
                System.out.print(testObjectList.get(x).getId()+ "- " + testObjectList.get(x).getLevel()+ " "+ " pathnum "+testObjectList.get(x).getPathNum()+ " " );

            }
        }


//        for (ConceptNode key : mapTest.keySet()) {
//
//            List<learningObjectSuggestion> objectSuggestionList = objectSuggestionMap.get(key);
//            List<learningObjectSuggestion> mapTestList = mapTest.get(key);
//
//            System.out.println(key.getID());
//            if (!key.getLabel().equals("C")){
//
////                System.out.println("list size " + objectSuggestionList.size());
//                System.out.println("list size 2 " + mapTestList.size());
//
//            }
//
////            if (list2 != null){
////                Assert.assertEquals(list1,list2);
////
////            }else{
////                System.out.println("BAD!");
////            }
//
//        }

    }



    @Test
    public void SuggestedConceptNodeMapWillOneStudentTest() {
        ConceptGraph orig= PracticalConceptGraphTest.willExampleConceptGraphTestOneStudent();

        //what it is
        HashMap<ConceptNode, List<learningObjectSuggestion>> objectSuggestionMap = orig.SuggestedConceptNodeMap();
        //what it should be
        HashMap<ConceptNode, List<learningObjectSuggestion>> mapTest = new HashMap<>();

        List<learningObjectSuggestion> testList = new ArrayList<>();
        testList.add(new learningObjectSuggestion("Q6",1, learningObjectSuggestion.Level.INCOMPLETE));
        testList.add(new learningObjectSuggestion("Q7",1, learningObjectSuggestion.Level.INCOMPLETE));
        mapTest.put(new ConceptNode("If Statement"), testList);

        List<learningObjectSuggestion> testList2 = new ArrayList<>();
        testList2.add(new learningObjectSuggestion("Q7", 1, learningObjectSuggestion.Level.INCOMPLETE));
        mapTest.put(new ConceptNode("Boolean"), testList2);
//        Assert.assertEquals(objectSuggestionMap.size(), mapTest.size());
//        for (ConceptNode key : mapTest.keySet()) {
//
//            ConceptNode name = key;
//            List<learningObjectSuggestion> list2 = objectSuggestionMap.get(name);
//            if (list2 != null){
//                List<learningObjectSuggestion> list1 = mapTest.get(name);
//                Assert.assertEquals(list1,list2);
//
//            }
//        }

        for (ConceptNode key : objectSuggestionMap.keySet()) {

            ConceptNode name = key;

            List<learningObjectSuggestion> SuggestionObjectList = objectSuggestionMap.get(key);
            System.out.println("\n");
            System.out.print(name.getID() + " " + name.getKnowledgeEstimate()+ ": \n");
            for (int x=0; x<SuggestionObjectList.size(); x++){
                System.out.println(SuggestionObjectList.get(x).getId());
                System.out.println(SuggestionObjectList.get(x).getLevel());
                System.out.println("pathnum "+SuggestionObjectList.get(x).getPathNum()+ "\n");

//                System.out.print(listlist.get(x).getId()+ "- " + listlist.get(x).getLevel()+ " "+ " pathnum "+listlist.get(x).getPathNum()+ " " );

            }
//
        }

    }
    @Test
    public void buildLearningObjectListSimpleTest(){
        ConceptGraph orig = ExampleConceptGraphFactory.makeSimple();
        orig.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

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
    public void suggestionComparatorTest(){

        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 2,learningObjectSuggestion.Level.INCOMPLETE) );
        suggestList.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.WRONG) );
        Collections.sort(suggestList, new learningObjectSuggestionComparator());


        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 2,learningObjectSuggestion.Level.INCOMPLETE) );


        for (int i =0; i<suggestListTest.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
//
//        suggestListTest.add(new learningObjectSuggestion("Q3", 2,learningObjectSuggestion.Level.INCOMPLETE) );
//        suggestListTest.add(new learningObjectSuggestion("Q4", 3,learningObjectSuggestion.Level.INCOMPLETE) );

    }

    @Test
    public void suggestedOrderBuildLearningObjectListTest(){
        ConceptGraph orig = ExampleConceptGraphFactory.makeSimple();
        orig.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

//        //from A nodes
        HashMap<String, Integer> testCompareA = new HashMap<String, Integer>();
        testCompareA.put("Q3",2);
        testCompareA.put("Q4",2);
        testCompareA.put("Q5",2);
        testCompareA.put("Q6",2);
        testCompareA.put("Q1",1);
        testCompareA.put("Q2",1);
        HashMap<String, Integer> learningSummaryFromA = orig.buildLearningObjectSummaryList("A");
        //makes sure that buildLearningObjectSummaryList works
        Assert.assertEquals(testCompareA,learningSummaryFromA);

        //build the sugggested learning object list
        List<learningObjectSuggestion> suggestedList = orig.buildLearningObjectSuggestionList(learningSummaryFromA);


        for (int i =0; i< suggestedList.size(); i++) {
            learningObjectSuggestion node = suggestedList.get(i);
            if (node.getId().equals("Q1")) {
                node.setLevel(learningObjectSuggestion.Level.RIGHT);
            }
            if (node.getId().equals("Q3")){
                node.setLevel(learningObjectSuggestion.Level.WRONG);
            }
            if (node.getId().equals("Q4")){
                node.setLevel(learningObjectSuggestion.Level.RIGHT);

            }
            if (node.getId().equals("Q6")){
                node.setLevel(learningObjectSuggestion.Level.WRONG);
            }
        }


        //this is ordered based on "level"
        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q4", 2,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q3", 2,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q6", 2,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q5", 2,learningObjectSuggestion.Level.INCOMPLETE) );
        suggestListTest.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.INCOMPLETE) );



        //orders the list based off of "level"
        orig.suggestedOrderBuildLearningObjectList(suggestedList);

//        //who should call orderedList


        for (int i =0; i<suggestedList.size(); i++){

            Assert.assertEquals(suggestedList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestedList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestedList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }


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

        Assert.assertEquals(1,     graph.findNodeById("B").getLearningObjectMap().get("Q1").calcKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1,     graph.findNodeById("B").getLearningObjectMap().get("Q2").calcKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.667, graph.findNodeById("C").getLearningObjectMap().get("Q3").calcKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.333, graph.findNodeById("C").getLearningObjectMap().get("Q4").calcKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5,   graph.findNodeById("C").getLearningObjectMap().get("Q5").calcKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5,   graph.findNodeById("C").getLearningObjectMap().get("Q6").calcKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
    }

    @Test
    public void simpleCheckNumEachIDTest(){
        //checks if the tree has the right number of copies per orig node
        int numA = 0;
        int numB = 0;
        int numC = 0;

        ConceptGraphRecord treeLists = ExampleConceptGraphFactory.makeSimple().graphToTree().buildNodesAndLinks();
        for(ConceptNode node : treeLists.getNodes()){
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
		Assert.assertEquals(expectedGraphNodeCount, graphLists.getNodes().size());
		Assert.assertEquals(expectedGraphLinkCount, graphLists.getLinks().size());

		ConceptGraphRecord treeLists = graphToTest.graphToTree().buildNodesAndLinks();

		Assert.assertEquals(expectedTreeNodeCount, treeLists.getNodes().size());
		Assert.assertEquals(expectedTreeLinkCount, treeLists.getLinks().size());
	}

    @Test
    public void treeToTreeTest(){
        treeToTreeCheck(ExampleConceptGraphFactory.makeSimpleInputTree());
        treeToTreeCheck(ExampleConceptGraphFactory.makeComplexInputTree());
    }

    public void treeToTreeCheck(ConceptGraph treeToTest){
        ConceptGraphRecord initialLists = treeToTest.buildNodesAndLinks();
        ConceptGraph treeFromTree = treeToTest.graphToTree();

        ConceptGraphRecord postLists = treeFromTree.buildNodesAndLinks();
        Assert.assertEquals(postLists.getNodes().size(), initialLists.getNodes().size());
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

        Assert.assertEquals(lists2.getNodes().size(), lists3.getNodes().size());

        Assert.assertEquals(lists2.getLinks().size(), lists3.getLinks().size());

        Assert.assertEquals(lists3.getLinks().size() + 1, lists3.getNodes().size());
    }

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
	@Test
	public void makeConceptGraphFromFileTest(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
        try {
			ConceptGraphRecord lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecord.class);
			Assert.assertEquals(11, lists.getNodes().size());
			Assert.assertEquals(11, lists.getLinks().size());
			
			ConceptGraph myGraph = new ConceptGraph(lists);
			ConceptGraph myTree = myGraph.graphToTree();
			
			ConceptGraphRecord listsFromTree = myTree.buildNodesAndLinks();
			
			Assert.assertEquals(16, listsFromTree.getNodes().size());
			Assert.assertEquals(15, listsFromTree.getLinks().size());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
    @Test
	public void makeJsonFromConceptGraphTest(){

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		String outputLocation = "out/test/sampleoutput/CarrieJsonGraph.json";
		
        try {
			ConceptGraphRecord lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecord.class);
			try{
			mapper.writeValue(new File(outputLocation), lists);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
				
		String jsonString = "";
		//reads json from file
		try {
			for (String line : Files.readAllLines(Paths.get(outputLocation))) {
			    jsonString += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int numID = jsonString.split("id").length-1;
		jsonString = new String(jsonString);
		int numLink = jsonString.split("parent").length-1;
		
		Assert.assertEquals(11, numID);
		Assert.assertEquals(11, numLink);
	}

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
    @Test
	public void makeJsonFromConceptGraphTreeTest(){

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		String outputLocation = "out/test/sampleoutput/CarrieJsonGraph.json";
		
        try {
			ConceptGraphRecord lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecord.class);
			ConceptGraph tree = new ConceptGraph(lists).graphToTree();
			
			try{
			mapper.writeValue(new File(outputLocation), tree.buildNodesAndLinks());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
				
		String jsonString = "";
		//reads json from file
		try {
			for (String line : Files.readAllLines(Paths.get(outputLocation))) {
			    jsonString += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int numID = jsonString.split("id").length-1;
		int numLink = jsonString.split("parent").length-1;
		
		Assert.assertEquals(16, numID);
		Assert.assertEquals(15, numLink);
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

        Assert.assertEquals(0.625, graph.findNodeById("A").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.75, graph.findNodeById("B").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, graph.findNodeById("C").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
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

	
