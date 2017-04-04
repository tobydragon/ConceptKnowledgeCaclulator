package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectFactory {

    public static ConceptGraphRecord makeSimpleLearningObjectDef(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("Q1"));
        cnList.add(new ConceptNode("Q2"));
        cnList.add(new ConceptNode("Q3"));
        cnList.add(new ConceptNode("Q4"));
        cnList.add(new ConceptNode("Q5"));
        cnList.add(new ConceptNode("Q6"));


        clList.add(new LinkRecord("B","Q1"));
        clList.add(new LinkRecord("B","Q2"));
        clList.add(new LinkRecord("C","Q3"));
        clList.add(new LinkRecord("C","Q4"));
        clList.add(new LinkRecord("C","Q5"));
        clList.add(new LinkRecord("C","Q6"));

        return new ConceptGraphRecord(cnList,clList);
    }
}
