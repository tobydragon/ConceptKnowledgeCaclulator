package edu.ithaca.dragonlab.ckc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.util.ErrorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//TODO: make data that will test appropriately, see ExampleLearningObjectResponseFactory
public class GroupConceptGraphsTest {

	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

	@Test
	public void userCountTest(){
		GroupConceptGraphs group = new GroupConceptGraphs(ExampleConceptGraphFactory.makeSimple(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Assert.assertEquals(2,group.userCount());
	}
	
	@Test
	public void addSummariesTest(){
		GroupConceptGraphs group = new GroupConceptGraphs(ExampleConceptGraphFactory.makeSimple(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Map<String, ConceptGraph> userGraphMap = group.getUserToGraphMap();
		
		ConceptGraph user2Graph = userGraphMap.get("CLTestStudent1");
		NodesAndIDLinks user2Nodes = user2Graph.buildNodesAndLinks();
		
		Assert.assertEquals(6,user2Nodes.getNodes().size());
		Assert.assertEquals(6,user2Nodes.getLinks().size());
	}
	
	
	@Test
	public void getAllGraphsTest(){
		GroupConceptGraphs group = new GroupConceptGraphs(ExampleConceptGraphFactory.makeSimple(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Map<String, ConceptGraph> userGraphMap = group.getUserToGraphMap();
		
		List<String> namesList = new ArrayList<String>();
		for (String name: userGraphMap.keySet()){
			namesList.add(name);
		}
		Assert.assertEquals("CLTestStudent2",namesList.get(0));
		Assert.assertEquals("CLTestStudent1",namesList.get(1));
		Assert.assertEquals(2,namesList.size());
		
		ConceptGraph user2Tree = userGraphMap.get("CLTestStudent2").graphToTree();
		NodesAndIDLinks user2Nodes = user2Tree.buildNodesAndLinks();
		
		
		Assert.assertEquals(8,user2Nodes.getNodes().size());
		Assert.assertEquals(7,user2Nodes.getLinks().size());
	}
	
	@Test
	public void testCalcDistFromAvg(){
		GroupConceptGraphs group = new GroupConceptGraphs(ExampleConceptGraphFactory.makeSimple(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Map<String, ConceptGraph> userGraphMap = group.getUserToGraphMap();
		
		ConceptGraph user2 = userGraphMap.get("CLTestStudent2");
		NodesAndIDLinks user2NL = user2.buildNodesAndLinks();
		ConceptNode testNode = user2NL.getNodes().get(0);
		Assert.assertEquals(.5, testNode.getDistanceFromAvg(),0);
		System.out.println(testNode.getDistanceFromAvg());
		ConceptNode testNode2 = user2NL.getNodes().get(2);
		System.out.println(testNode2.getDistanceFromAvg());
		Assert.assertEquals(1, testNode2.getDistanceFromAvg(),0);
		
		
	}
	
	//TODO: make this test functional, why is half commented out? (may not have been working in previous project)
	public void jsonTester(){
		ObjectMapper mapper = new ObjectMapper();
		
		GroupConceptGraphs group = new GroupConceptGraphs(ExampleConceptGraphFactory.makeSimple(), ExampleLearningObjectResponseFactory.makeSimpleResponses());

		try {
			//writes JSON to file
			mapper.writeValue(new File("out/test/sampleoutput/GroupCOnceptGraphSimple.json"), group.getAllNamedGraphs());
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
			NodesAndIDLinks nodes = mapper.readValue(new File("war/conffiles/domainfiles/conceptgraph/domainStructure.json"), NodesAndIDLinks.class);
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
		NodesAndIDLinks user2Nodes = user2Tree.buildNodesAndLinks();
		
		Assert.assertEquals(2, group.getUserToGraphMap().keySet().size());
		Assert.assertEquals(8, user2Nodes.getNodes().size());
		Assert.assertEquals(7, user2Nodes.getLinks().size());
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



	