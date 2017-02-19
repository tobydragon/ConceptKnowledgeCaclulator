package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.ConceptNode;
import edu.ithaca.dragonlab.ckc.IDLink;
import edu.ithaca.dragonlab.ckc.NodesAndIDLinks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectFactory {

    public static NodesAndIDLinks makeSimpleLearningObjectDef(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> clList = new ArrayList<>();

        cnList.add(new ConceptNode("Q1"));
        cnList.add(new ConceptNode("Q2"));
        cnList.add(new ConceptNode("Q3"));
        cnList.add(new ConceptNode("Q4"));
        cnList.add(new ConceptNode("Q5"));
        cnList.add(new ConceptNode("Q6"));

        clList.add(new IDLink("B","Q1"));
        clList.add(new IDLink("B","Q2"));
        clList.add(new IDLink("C","Q3"));
        clList.add(new IDLink("C","Q4"));
        clList.add(new IDLink("C","Q5"));
        clList.add(new IDLink("C","Q6"));

        return new NodesAndIDLinks(cnList,clList);
    }
}
