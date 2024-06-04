package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.util.DataUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by home on 5/19/17.
 */
public class TreeConverterTest {

    @Test
    public void makeNextIdTest() {
        assertEquals("Title-1", TreeConverter.makeNextId("Title"));
        assertEquals("Title-4",TreeConverter.makeNextId("Title-3"));
        assertEquals("Title-35", TreeConverter.makeNextId("Title-34"));
        assertEquals("MyTitle-4", TreeConverter.makeNextId("My-Title-3"));
        assertEquals("MyTitleName-1", TreeConverter.makeNextId("My-Title-Name"));
    }

    @Test
    public void simpleCheckNumEachIDTest(){
        //checks if the tree has the right number of copies per orig node
        int numA = 0;
        int numB = 0;
        int numC = 0;

        ConceptGraphRecord treeLists = TreeConverter.makeTreeCopy(ExampleConceptGraphFactory.makeSimpleCompleteWithData()).buildConceptGraphRecord();
        for(ConceptRecord node : treeLists.getConcepts()){
            if(node.getLabel().equals("A")){
                numA++;
            }else if(node.getLabel().equals("B")){
                numB++;
            }else if(node.getLabel().equals("C")){
                numC++;
            }
        }
        assertEquals(1, numA);
        assertEquals(1, numB);
        assertEquals(2, numC);
    }

    @Test
    public void treeConversionTest(){
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSimpleCompleteWithData(), 3, 3, 4, 3, 6,16 );
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeMedium(), 4, 5, 7, 6, 0, 0);
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeComplex(), 5, 8, 13, 12, 0, 0);
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSuperComplex(), 6, 11, 24, 23, 0, 0);
    }

    public void checkTreeConversionByNodesAndLinksNumbers(ConceptGraph graphToTest, int expectedGraphNodeCount, int expectedGraphLinkCount, int expectedTreeNodeCount, int expectedTreeLinkCount, int expectedResourcesCount, int expectedResponsesCount){
        ConceptGraphRecord graphLists = graphToTest.buildConceptGraphRecord();
        assertEquals(expectedGraphNodeCount, graphLists.getConcepts().size());
        assertEquals(expectedGraphLinkCount, graphLists.getLinks().size());

        ConceptGraph tree = TreeConverter.makeTreeCopy(graphToTest);
        ConceptGraphRecord treeLists = tree.buildConceptGraphRecord();
        //System.out.println(treeLists);

        assertEquals(expectedTreeNodeCount, treeLists.getConcepts().size());
        assertEquals(expectedTreeLinkCount, treeLists.getLinks().size());

        //Resources and responses are not nodes in the tree, and so their objects are linked to multiple nodes, and so
        //there should be the same number in both graphs
        assertEquals(expectedResourcesCount, graphToTest.getAssessmentItemMap().size());
        assertEquals(expectedResponsesCount, graphToTest.responsesCount());
        assertEquals(expectedResourcesCount, tree.getAssessmentItemMap().size());
        assertEquals(expectedResponsesCount, tree.responsesCount());
        //but they should be different, equivalent objects across tree and graph
        for (Map.Entry<String, AssessmentItem> entry: graphToTest.getAssessmentItemMap().entrySet()){
            AssessmentItem treeCopy = tree.getAssessmentItemMap().get(entry.getKey());
            assertEquals(entry.getValue(), treeCopy);
            assertNotSame(entry.getValue(), treeCopy);
        }

        //check that each tree copy of the same node in the graph has the same links
        for (List<String> nodeCopies : tree.createSameLabelMap().values()){
            ConceptNode first = tree.findNodeById(nodeCopies.get(0));
            for (String nodeCopyId : nodeCopies){
                ConceptNode next = tree.findNodeById(nodeCopyId);
                assertEquals(first.getKnowledgeEstimate() , next.getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
                assertEquals(first.getAssessmentItemMap().size() , next.getAssessmentItemMap().size());
            }
        }


    }

    //TODO: fis the original creators
    @Test
    public void treeToTreeTest(){
//        treeToTreeCheck(ExampleConceptGraphFactory.makeSimpleInputTree());
//        treeToTreeCheck(ExampleConceptGraphFactory.makeComplexInputTree());
    }

    public void treeToTreeCheck(ConceptGraph treeToTest){
        ConceptGraphRecord initialLists = treeToTest.buildConceptGraphRecord();
        ConceptGraph treeFromTree = TreeConverter.makeTreeCopy(treeToTest);

        ConceptGraphRecord postLists = treeFromTree.buildConceptGraphRecord();
        assertEquals(postLists.getConcepts().size(), initialLists.getConcepts().size());
        assertEquals(postLists.getLinks().size(), initialLists.getLinks().size());
    }

    @Test
    public void mediumReTreeTest(){
        ConceptGraph myGraph = TreeConverter.makeTreeCopy(ExampleConceptGraphFactory.makeMedium());
        ConceptGraph myTree = TreeConverter.makeTreeCopy(myGraph);
        ConceptGraph myTree2 = TreeConverter.makeTreeCopy(myTree);

        ConceptGraphRecord lists2 = myTree.buildConceptGraphRecord();
        ConceptGraphRecord lists3 = myTree2.buildConceptGraphRecord();

        assertEquals(lists2.getConcepts().size(), lists3.getConcepts().size());
        assertEquals(lists2.getLinks().size(), lists3.getLinks().size());
        assertEquals(lists3.getLinks().size() + 1, lists3.getConcepts().size());
    }

}
