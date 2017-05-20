package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptRecord;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by home on 5/19/17.
 */
public class TreeConverterTest {

    @Test
    public void makeNextIdTest() {
        Assert.assertEquals("Title-1", TreeConverter.makeNextId("Title"));
        Assert.assertEquals("Title-4",TreeConverter.makeNextId("Title-3"));
        Assert.assertEquals("Title-35", TreeConverter.makeNextId("Title-34"));
        Assert.assertEquals("MyTitle-4", TreeConverter.makeNextId("My-Title-3"));
        Assert.assertEquals("MyTitleName-1", TreeConverter.makeNextId("My-Title-Name"));
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
        Assert.assertEquals(1, numA);
        Assert.assertEquals(1, numB);
        Assert.assertEquals(2, numC);
    }

    @Test
    public void treeConversionTest(){
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSimpleCompleteWithData(), 3, 3, 4, 3);
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeMedium(), 4, 5, 7, 6);
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeComplex(), 5, 8, 13, 12);
        checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSuperComplex(), 6, 11, 24, 23);
    }

    public void checkTreeConversionByNodesAndLinksNumbers(ConceptGraph graphToTest, int expectedGraphNodeCount, int expectedGraphLinkCount, int expectedTreeNodeCount, int expectedTreeLinkCount){
        ConceptGraphRecord graphLists = graphToTest.buildConceptGraphRecord();
        Assert.assertEquals(expectedGraphNodeCount, graphLists.getConcepts().size());
        Assert.assertEquals(expectedGraphLinkCount, graphLists.getLinks().size());

        ConceptGraphRecord treeLists = TreeConverter.makeTreeCopy(graphToTest).buildConceptGraphRecord();

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
        ConceptGraphRecord initialLists = treeToTest.buildConceptGraphRecord();
        ConceptGraph treeFromTree = TreeConverter.makeTreeCopy(treeToTest);

        ConceptGraphRecord postLists = treeFromTree.buildConceptGraphRecord();
        Assert.assertEquals(postLists.getConcepts().size(), initialLists.getConcepts().size());
        Assert.assertEquals(postLists.getLinks().size(), initialLists.getLinks().size());
    }

    @Test
    public void mediumReTreeTest(){
        ConceptGraph myGraph = TreeConverter.makeTreeCopy(ExampleConceptGraphFactory.makeMedium());
        ConceptGraph myTree = TreeConverter.makeTreeCopy(myGraph);
        ConceptGraph myTree2 = TreeConverter.makeTreeCopy(myTree);

        ConceptGraphRecord lists2 = myTree.buildConceptGraphRecord();
        ConceptGraphRecord lists3 = myTree2.buildConceptGraphRecord();

        Assert.assertEquals(lists2.getConcepts().size(), lists3.getConcepts().size());
        Assert.assertEquals(lists2.getLinks().size(), lists3.getLinks().size());
        Assert.assertEquals(lists3.getLinks().size() + 1, lists3.getConcepts().size());
    }

}
