package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectFactory {

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

    //TODO: make work with new concept graph data structre
//    public static ConceptGraphRecord simpleTreeLearningObjectWithKnowledgeEstimates(){
//        List<ConceptNode> cnList = new ArrayList<>();
//        List<LinkRecord> clList = new ArrayList<>();
//
//        cnList.add(new ConceptNode("Q1"));
//        ConceptNode node = cnList.get(0);
//        node.setKnowledgeEstimate(1.0);
//        cnList.add(new ConceptNode("Q2"));
//        ConceptNode node1 = cnList.get(1);
//        node1.setKnowledgeEstimate(0.0);
//        cnList.add(new ConceptNode("Q3"));
//        ConceptNode node2 = cnList.get(2);
//        node2.setKnowledgeEstimate(0.0);
//        cnList.add(new ConceptNode("Q4"));
//        ConceptNode node3 = cnList.get(3);
//        node3.setKnowledgeEstimate(0.9);
//        cnList.add(new ConceptNode("Q5"));
//        ConceptNode node4 = cnList.get(4);
//        node4.setKnowledgeEstimate(-0.5);
//        cnList.add(new ConceptNode("Q6"));
//        ConceptNode node5 = cnList.get(5);
//        node5.setKnowledgeEstimate(0.0);
//
//
//        clList.add(new LinkRecord("B","Q1"));
//        clList.add(new LinkRecord("B","Q2"));
//        clList.add(new LinkRecord("C","Q3"));
//        clList.add(new LinkRecord("C","Q4"));
//        clList.add(new LinkRecord("C","Q5"));
//        clList.add(new LinkRecord("C","Q6"));
//
//        return new ConceptGraphRecord(cnList,clList);
//    }

  public static List<LearningObject> makeSimpleLearningObject(){
        List<LearningObject> cnList = new ArrayList<>();

        cnList.add(new LearningObject("Q1"));
        cnList.add(new LearningObject("Q2"));
        cnList.add(new LearningObject("Q3"));
        cnList.add(new LearningObject("Q4"));
        cnList.add(new LearningObject("Q5"));
        cnList.add(new LearningObject("Q6"));

//        cnList.get(0).addResponse(new LearningObjectResponse("student1", "Q1", 1));
//        cnList.get(1).addResponse(new LearningObjectResponse("student1", "Q2", 1));
//        cnList.get(2).addResponse(new LearningObjectResponse("student1", "Q3", 1));
//        cnList.get(3).addResponse(new LearningObjectResponse("student1", "Q4", 1));
//        cnList.get(4).addResponse(new LearningObjectResponse("student1", "Q5", 1));
//        cnList.get(5).addResponse(new LearningObjectResponse("student1", "Q6", 1));
//
//
//        cnList.get(0).addResponse(new LearningObjectResponse("student2", "Q1", 1));
//        cnList.get(1).addResponse(new LearningObjectResponse("student2", "Q2", 1));
//        cnList.get(2).addResponse(new LearningObjectResponse("student2", "Q3", 1));
//        cnList.get(3).addResponse(new LearningObjectResponse("student2", "Q4", 0));
//        cnList.get(4).addResponse(new LearningObjectResponse("student2", "Q5", 0));
//        cnList.get(5).addResponse(new LearningObjectResponse("student2", "Q6", 0));
//
//
//        cnList.get(0).addResponse(new LearningObjectResponse("student3", "Q1", 1));
//        cnList.get(1).addResponse(new LearningObjectResponse("student3", "Q2", 1));
//        cnList.get(2).addResponse(new LearningObjectResponse("student3", "Q3", 0));
//        cnList.get(3).addResponse(new LearningObjectResponse("student3", "Q4", 0));

        return cnList;
    }

    public static List<LearningObjectLinkRecord> makeSimpleLOLRecords(){
        List<LearningObjectLinkRecord> clList = new ArrayList<>();

        clList.add(new LearningObjectLinkRecord("Q1", Arrays.asList("B")));
        clList.add(new LearningObjectLinkRecord("Q2", Arrays.asList("B")));
        clList.add(new LearningObjectLinkRecord("Q3", Arrays.asList("C")));
        clList.add(new LearningObjectLinkRecord("Q4", Arrays.asList("C")));
        clList.add(new LearningObjectLinkRecord("Q5", Arrays.asList("C")));
        clList.add(new LearningObjectLinkRecord("Q6", Arrays.asList("C")));

        return clList;
    }


}
