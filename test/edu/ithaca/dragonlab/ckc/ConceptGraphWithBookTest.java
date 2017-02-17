package edu.ithaca.dragonlab.ckc;

/**
 * Created by tdragon on 2/15/17.
 */
public class ConceptGraphWithBookTest {

    //TODO: when book code is reintegrated

//    @Test
//	public void ConceptGraphFromBookNodesTest(){
//		NodesAndIDLinks lists = graphFromBook.buildNodesAndLinks();
//		Assert.assertEquals(217, lists.getNodes().size());
//	}
//
//	@Test
//	public void ConceptGraphFromBookLinksTest(){
//		NodesAndIDLinks lists = graphFromBook.buildNodesAndLinks();
//		Assert.assertEquals(216, lists.getLinks().size());
//	}

//  	@Test
//	public void ConceptGraphFromBookSelectionTest(){
//		NodesAndIDLinks lists = graphFromBook.buildNodesAndLinks();
//		String[] selectionTitles = {"BooleanValuesandBooleanExpressions", "Logicaloperators", "PrecedenceofOperators", "ConditionalExecutionBinarySelection", "OmittingtheelseClauseUnarySelection", "Nestedconditionals", "Chainedconditionals", "BooleanFunctions"};
//		List<ConceptNode> nodes = lists.getNodes();
//		List<ConceptNode> selectionNodes = new ArrayList<ConceptNode>();
//
//		ConceptNode selectionNode = null;
//
//		for(int i = 0; i < nodes.size(); i++){
//			if(nodes.get(i).getLabel().equals("Selection")){
//				selectionNode = nodes.get(i);
//			}
//		}
//
//		List<ConceptNode> selectionChildren = selectionNode.getChildren();
//
//		for(int i = 0; i < nodes.size(); i++){
//			for(int j = 0; j < selectionTitles.length; j++){
//				if(nodes.get(i).getID().contains(selectionTitles[j])){
//					selectionNodes.add(nodes.get(i));
//				}
//			}
//		}
//
//		Assert.assertEquals(8, selectionChildren.size()-2-1); //sub 2 for exercises and glossary, sub 1 for intro page
//		Assert.assertEquals(8, selectionNodes.size()-1); //sub for duplicate BooleanFunctions
//	}
}
