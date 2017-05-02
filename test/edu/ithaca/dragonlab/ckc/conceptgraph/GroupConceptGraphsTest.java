package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.util.TestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: make data that will test appropriately, see ExampleLearningObjectResponseFactory
public class GroupConceptGraphsTest {

	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

	@Test
	public void userCountTest(){
		ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
		graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
		GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Assert.assertEquals(3,group.userCount());
	}
	
	@Test
	public void addSummariesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
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
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(0.5, group.getAvgGraph().findNodeById("C").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.75, group.getAvgGraph().findNodeById("B").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.625, group.getAvgGraph().findNodeById("A").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(1, group.getUserToGraphMap().get("student1").findNodeById("A").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserToGraphMap().get("student1").findNodeById("B").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserToGraphMap().get("student1").findNodeById("C").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.438, group.getUserToGraphMap().get("student2").findNodeById("A").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.625, group.getUserToGraphMap().get("student2").findNodeById("B").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.25, group.getUserToGraphMap().get("student2").findNodeById("C").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.25, group.getUserToGraphMap().get("student3").findNodeById("A").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, group.getUserToGraphMap().get("student3").findNodeById("B").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0, group.getUserToGraphMap().get("student3").findNodeById("C").getKnowledgeEstimate(), TestUtil.OK_FLOAT_MARGIN);
    }



    //TODO: fix to current data from example
	@Test
	public void calcDistFromAvgTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Map<String, ConceptGraph> userGraphMap = group.getUserToGraphMap();
		
		ConceptGraph user = userGraphMap.get("student1");
		ConceptGraphRecord userNL = user.buildNodesAndLinks();
		ConceptNode testNode = userNL.getNodes().get(0);
		Assert.assertEquals(.5, testNode.getKnowledgeDistanceFromAvg(),0);
		System.out.println(testNode.getKnowledgeDistanceFromAvg());
		ConceptNode testNode2 = userNL.getNodes().get(2);
		System.out.println(testNode2.getKnowledgeDistanceFromAvg());
		Assert.assertEquals(1, testNode2.getKnowledgeDistanceFromAvg(),0);

	}
	
	//TODO: make this test functional, why is half commented out? (may not have been working in previous project)
	@Test
    public void jsonTester(){
		ObjectMapper mapper = new ObjectMapper();

        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());
        GroupConceptGraphs group = new GroupConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
		try {
			//writes JSON to file
			mapper.writeValue(new File("out/test/sampleoutput/GroupConceptGraphSimple.json"), group.getAllNamedGraphs());
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		/*
		try {
			//Reads in the file that was written earlier
			GroupConceptGraphs gcg = mapper.readValue(new File("out/test/sampleoutput/GroupCOnceptGraphSimple.json"), GroupConceptGraphs.class);
			
			Assert.assertEquals(2,gcg.getUserToGraphMap().keySet().size());
			Assert.assertEquals(7, gcg.getAllGraphs().get(1).getIDLinks().size());
			Assert.assertEquals(8, gcg.getAllGraphs().get(1).getNodes().size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	//TODO: this isn't a test, because it doesn't do any automated checking...
	public void jsonTesterWithBigData(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			//Reads in the file that was written earlier
			
			List<LearningObjectResponse> sums = new ArrayList<>();
			ConceptGraphRecord nodes = mapper.readValue(new File("war/conffiles/domainfiles/conceptgraph/domainStructure.json"), ConceptGraphRecord.class);
			ConceptGraph graph = new ConceptGraph(nodes);
			GroupConceptGraphs group = new GroupConceptGraphs(graph,sums);
			try {
				//writes JSON to file
				mapper.writeValue(new File("war/TreeDisplay/testInput/inputBigData"+".json"), group.getAllNamedGraphs());
            } catch (Exception e) {
                e.printStackTrace();
            }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Test
	public void jsonOutputTest(){
		
		//Writes the JSON File for GCG
		GroupConceptGraphs group = new GroupConceptGraphs("out/test/sampleoutput/GroupConceptGraph-jsonOutputTest",
				ExampleConceptGraphFactory.makeSimple(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		
		ConceptGraph user2Tree = group.getAllGraphs().get(1).graphToTree();
		ConceptGraphRecord user2Nodes = user2Tree.buildNodesAndLinks();
		
		Assert.assertEquals(3, group.getUserToGraphMap().keySet().size());
		Assert.assertEquals(4, user2Nodes.getNodes().size());
		Assert.assertEquals(3, user2Nodes.getLinks().size());

		//TODO: This shouldn't be part of an automated test if it doesn't result in asserts
		/*
		try {
			//Reads in the file that was written earlier
			GroupConceptGraphs gcg = mapper.readValue(new File("war/TreeDisplay/input.json"), GroupConceptGraphs.class);
			
			Assert.assertEquals(2,gcg.getUserToGraphMap().keySet().size());
			Assert.assertEquals(7, gcg.getAllGraphs().get(1).getIDLinks().size());
			Assert.assertEquals(8, gcg.getAllGraphs().get(1).getNodes().size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}



	