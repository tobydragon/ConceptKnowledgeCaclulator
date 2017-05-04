package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class GroupConceptGraphsTest {

	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

	@Test
	public void userCountTest(){
		ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
		GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Assert.assertEquals(3,group.userCount());
	}

	@Test
	public void addSummariesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(6,group.getAvgGraph().getLearningObjectMap().size());
		Assert.assertEquals(6,group.getUserToGraphMap().get("student1").getLearningObjectMap().size());
        Assert.assertEquals(6,group.getUserToGraphMap().get("student2").getLearningObjectMap().size());
        Assert.assertEquals(6,group.getUserToGraphMap().get("student3").getLearningObjectMap().size());

        Assert.assertEquals(3,group.getAvgGraph().getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(2,group.getAvgGraph().getLearningObjectMap().get("Q5").getResponses().size());

        Assert.assertEquals(1,group.getUserToGraphMap().get("student1").getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(0,group.getUserToGraphMap().get("student3").getLearningObjectMap().get("Q5").getResponses().size());
    }

    @Test
    public void calcKnowledgeEstimateTest() {
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();
        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(0.5, group.getAvgGraph().findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.75, group.getAvgGraph().findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.625, group.getAvgGraph().findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(1, group.getUserToGraphMap().get("student1").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserToGraphMap().get("student1").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserToGraphMap().get("student1").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.438, group.getUserToGraphMap().get("student2").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.625, group.getUserToGraphMap().get("student2").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.25, group.getUserToGraphMap().get("student2").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.25, group.getUserToGraphMap().get("student3").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, group.getUserToGraphMap().get("student3").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0, group.getUserToGraphMap().get("student3").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }
//
//
//
//    //TODO: fix to current data from example
//	@Test
//	public void calcDistFromAvgTest(){
//        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();
//        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
//        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
//		Map<String, ConceptGraph> userGraphMap = group.getUserToGraphMap();
//
//		ConceptGraph user = userGraphMap.get("student1");
//		ConceptGraphRecord userNL = user.buildNodesAndLinks();
//		ConceptRecord testNode = userNL.getConcepts().get(0);
//		Assert.assertEquals(.5, testNode.getKnowledgeDistFromAvg(),0);
//		System.out.println(testNode.getKnowledgeDistFromAvg());
//		ConceptRecord testNode2 = userNL.getConcepts().get(2);
//		System.out.println(testNode2.getKnowledgeDistFromAvg());
//		Assert.assertEquals(1, testNode2.getKnowledgeDistFromAvg(),0);
//
//	}
//
//	//TODO: make this test functional, why is half commented out? (may not have been working in previous project)
//	@Test
//    public void jsonTester(){
//		ObjectMapper mapper = new ObjectMapper();
//
//        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();
//        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
//        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
//		try {
//			//writes JSON to file
//			mapper.writeValue(new File("out/test/sampleoutput/GroupConceptGraphSimple.json"), group.getAllNamedGraphs());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//		/*
//		try {
//			//Reads in the file that was written earlier
//			GroupConceptGraphs gcg = mapper.readValue(new File("out/test/sampleoutput/GroupCOnceptGraphSimple.json"), GroupConceptGraphs.class);
//
//			Assert.assertEquals(2,gcg.getUserToGraphMap().keySet().size());
//			Assert.assertEquals(7, gcg.getAllGraphs().get(1).getIDLinks().size());
//			Assert.assertEquals(8, gcg.getAllGraphs().get(1).getConcepts().size());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}*/
//	}
//
//
//
//	@Test
//	public void jsonOutputTest(){
//
//		//Writes the JSON File for GCG
//		GroupConceptGraphs group = new GroupConceptGraphs("out/test/sampleoutput/GroupConceptGraph-jsonOutputTest",
//				ExampleConceptGraphFactory.makeSimpleWithData(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//
//		ConceptGraph user2Tree = group.getAllGraphs().get(1).graphToTree();
//		ConceptGraphRecord user2Nodes = user2Tree.buildNodesAndLinks();
//
//		Assert.assertEquals(3, group.getUserToGraphMap().keySet().size());
//		Assert.assertEquals(4, user2Nodes.getConcepts().size());
//		Assert.assertEquals(3, user2Nodes.getLinks().size());
//
//		//TODO: This shouldn't be part of an automated test if it doesn't result in asserts
//		/*
//		try {
//			//Reads in the file that was written earlier
//			//TODO: fix path to file that will be written
//			GroupConceptGraphs gcg = mapper.readValue(new File("war/TreeDisplay/input.json"), GroupConceptGraphs.class);
//
//			Assert.assertEquals(2,gcg.getUserToGraphMap().keySet().size());
//			Assert.assertEquals(7, gcg.getAllGraphs().get(1).getIDLinks().size());
//			Assert.assertEquals(8, gcg.getAllGraphs().get(1).getConcepts().size());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}*/
//	}

}



	