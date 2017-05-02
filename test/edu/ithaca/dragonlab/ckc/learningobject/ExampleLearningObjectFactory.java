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

//    public static ConceptGraphRecord simpleTreeLearningObjectWithKnowledgeEstimates(){
//        List<LearningObject> cnList = new ArrayList<>();
//        List<LinkRecord> clList = new ArrayList<>();
//
//        cnList.add(new LearningObject("Q1"));
//        LearningObject node = cnList.get(0);
//        node.addResponse(new LearningObjectResponse("user", "123", 1.0));
//        cnList.add(new LearningObject("Q2"));
//        LearningObject node1 = cnList.get(1);
//        node1.addResponse(new LearningObjectResponse("user", "123", 0.0));
//        cnList.add(new LearningObject("Q3"));
//        LearningObject node2 = cnList.get(2);
//        node2.addResponse(new LearningObjectResponse("user", "123", 0.0));
//        cnList.add(new LearningObject("Q4"));
//        LearningObject node3 = cnList.get(3);
//        node3.addResponse(new LearningObjectResponse("user", "123", 0.9));
//        cnList.add(new LearningObject("Q5"));
//        LearningObject node4 = cnList.get(4);
//        node1.addResponse(new LearningObjectResponse("user", "123", -0.5));
//        cnList.add(new LearningObject("Q6"));
//        LearningObject node5 = cnList.get(5);
//        node1.addResponse(new LearningObjectResponse("user", "123", 0.0));
//
//        clList.add(new LinkRecord("B","Q1"));
//        clList.add(new LinkRecord("B","Q2"));
//        clList.add(new LinkRecord("C","Q3"));
//        clList.add(new LinkRecord("C","Q4"));
//        clList.add(new LinkRecord("C","Q5"));
//        clList.add(new LinkRecord("C","Q6"));
//
//        return new ConceptGraphRecord(cnList,clList);
//
//    }

    public static ConceptGraphRecord simpleTreeLearningObjectWithKnowledgeEstimates(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("Q1"));
        ConceptNode node = cnList.get(0);
        node.setKnowledgeEstimate(1.0);
        cnList.add(new ConceptNode("Q2"));
        ConceptNode node1 = cnList.get(1);
        node1.setKnowledgeEstimate(0.0);
        cnList.add(new ConceptNode("Q3"));
        ConceptNode node2 = cnList.get(2);
        node2.setKnowledgeEstimate(0.0);
        cnList.add(new ConceptNode("Q4"));
        ConceptNode node3 = cnList.get(3);
        node3.setKnowledgeEstimate(0.9);
        cnList.add(new ConceptNode("Q5"));
        ConceptNode node4 = cnList.get(4);
        node4.setKnowledgeEstimate(-0.5);
        cnList.add(new ConceptNode("Q6"));
        ConceptNode node5 = cnList.get(5);
        node5.setKnowledgeEstimate(0.0);


        clList.add(new LinkRecord("B","Q1"));
        clList.add(new LinkRecord("B","Q2"));
        clList.add(new LinkRecord("C","Q3"));
        clList.add(new LinkRecord("C","Q4"));
        clList.add(new LinkRecord("C","Q5"));
        clList.add(new LinkRecord("C","Q6"));

        return new ConceptGraphRecord(cnList,clList);
    }
}
