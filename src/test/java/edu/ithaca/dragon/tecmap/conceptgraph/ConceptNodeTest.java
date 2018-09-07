package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConceptNodeTest {

    @Test
    public void isAncestorTest() {
        ConceptGraph orig = ExampleConceptGraphFactory.mediumTestGraphTest();

        ConceptNode node = orig.findNodeById("C");
        boolean num = orig.findNodeById("A").isAncestorOf(node);
        Assert.assertEquals(true,num);

        ConceptNode node2 = orig.findNodeById("F");
        boolean num2 = orig.findNodeById("A").isAncestorOf(node2);
        Assert.assertEquals(true,num2);


        ConceptNode node3 = orig.findNodeById("D");
        boolean num3 = orig.findNodeById("B").isAncestorOf(node3);
        Assert.assertEquals(true,num3);


        ConceptNode node4 = orig.findNodeById("D");
        boolean num4 = orig.findNodeById("G").isAncestorOf(node4);
        Assert.assertEquals(false,num4);

        ConceptNode node5 = orig.findNodeById("F");
        boolean num5 = orig.findNodeById("A").isAncestorOf(node5);
        Assert.assertEquals(true,num5);

        ConceptNode node6 = orig.findNodeById("D");
        boolean num6 = orig.findNodeById("A").isAncestorOf(node6);
        Assert.assertEquals(true,num6);
    }

    @Test
    public void buildLearningObjectSummaryOccurrenceSimpleTest(){
		//test the creation of graph
    	//test findNodeBy first
		//then test the nodes

        //tests the size of the buildLearningObjectList


        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        HashMap<String, Integer> testA = new HashMap<String, Integer>();
        graph.findNodeById("A").buildLearningMaterialPathCount(testA);
        Assert.assertEquals(6, testA.size());

        HashMap<String, Integer> testB = new HashMap<String, Integer>();
        graph.findNodeById("B").buildLearningMaterialPathCount(testB);
        Assert.assertEquals(6,testB.size());

        HashMap<String, Integer> testC = new HashMap<String, Integer>();
        graph.findNodeById("C").buildLearningMaterialPathCount(testC);
        Assert.assertEquals(4, testC.size());
//
    }

    @Test
    public void getLongestPathToLeaf() throws IOException {
        List<LearningResourceRecord> loRecords = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json");
        ConceptGraph conceptGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json"),
                loRecords);

        //Test on root with 3 on one side & two on the other
        ConceptNode testNode = conceptGraph.findNodeById("Intro CS");
        assertEquals(3, testNode.getLongestPathToLeaf());

        //Test non-root w/ 2 on one side & one on other
        testNode = conceptGraph.findNodeById("Loops");
        assertEquals(2, testNode.getLongestPathToLeaf());

        //Test on node w/ path of 1 and no other paths
        testNode = conceptGraph.findNodeById("While Loops");
        assertEquals(1, testNode.getLongestPathToLeaf());

        //Test on leaf
        testNode = conceptGraph.findNodeById("Boolean Expressions");
        assertEquals(0, testNode.getLongestPathToLeaf());
    }

}
