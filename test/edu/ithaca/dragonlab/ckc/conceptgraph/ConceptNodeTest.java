package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ConceptNodeTest {


    @Test
    public void buildLearningObjectSummaryOccurrenceSimpleTest(){
		//test the creation of graph
    	//test findNodeBy first
		//then test the nodes

        //tests the size of the buildLearningObjectList


        ConceptGraph graph = ExampleConceptGraphFactory.makeSimple();
        graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

        HashMap<String, Integer> testA = new HashMap<String, Integer>();
        graph.findNodeById("A").buildLearningObjectSummaryList(testA);
        Assert.assertEquals(6, testA.size());

        HashMap<String, Integer> testB = new HashMap<String, Integer>();
        graph.findNodeById("B").buildLearningObjectSummaryList(testB);
        Assert.assertEquals(6,testB.size());

        HashMap<String, Integer> testC = new HashMap<String, Integer>();
        graph.findNodeById("C").buildLearningObjectSummaryList(testC);
        Assert.assertEquals(4, testC.size());
//


    }







    @Test
	public void makeNameWithInitialInputTest() {
		Assert.assertEquals("Title-1", ConceptNode.makeName("Title"));
	}
	
	@Test 
	public void makeNameWithExpectedInputTest(){
		Assert.assertEquals("Title-4", ConceptNode.makeName("Title-3"));
	}
	
	@Test
	public void makeNameWithTwoDigitInputTest(){
		Assert.assertEquals("Title-35", ConceptNode.makeName("Title-34"));
	}
	
	@Test
	public void makeNameWithDoubleDashTest(){
		Assert.assertEquals("MyTitle-4", ConceptNode.makeName("My-Title-3"));
	}
	
	@Test
	public void makeNameWithDoubleDashNoNumTest(){
		Assert.assertEquals("MyTitleName-1", ConceptNode.makeName("My-Title-Name"));
	}

	
}
