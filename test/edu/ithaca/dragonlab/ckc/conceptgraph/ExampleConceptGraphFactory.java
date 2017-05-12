package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.io.*;

import java.util.ArrayList;
import java.util.List;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;

/**
 * @author tdragon
 * 2/15/17
 */
public class ExampleConceptGraphFactory {

    public static ConceptGraph makeSimple(){
        return new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple());
    }

    public static ConceptGraph makeSimpleWithData(){
        return new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(),
                ExampleLearningObjectFactory.makeSimpleLOLRecords(),
                ExampleLearningObjectResponseFactory.makeSimpleResponses());
    }

//TODO: make work with new concept graph
//    public static ConceptGraph makeSimpleWithEstimates(){
//        List<ConceptNode> cnList = new ArrayList<>();
//        List<LinkRecord> clList = new ArrayList<>();
//
//        cnList.add(new ConceptNode("A"));
//        ConceptNode node = cnList.get(0);
//        node.setKnowledgeEstimate(0.5);
//
//        cnList.add(new ConceptNode("B"));
//        ConceptNode node2 = cnList.get(1);
//        node2.setKnowledgeEstimate(-0.5);
//        cnList.add(new ConceptNode("C"));
//        ConceptNode node3 = cnList.get(2);
//        node3.setKnowledgeEstimate(-0.7);
//
//        clList.add(new LinkRecord("A","B")); //A -> B
//        clList.add(new LinkRecord("A","C")); //A -> C
//        clList.add(new LinkRecord("B","C")); //B -> C
//
//        ConceptGraphRecord inputNodesAndLinks = new ConceptGraphRecord(cnList,clList);
//        return new ConceptGraph(inputNodesAndLinks);
//
//    }

    public static ConceptGraph makeMedium(){
        return new ConceptGraph(ExampleConceptGraphRecordFactory.makeMedium());
    }

    public static ConceptGraph makeComplex(){
        return new ConceptGraph(ExampleConceptGraphRecordFactory.makeComplex());
    }

    public static ConceptGraph makeSuperComplex(){
        return new ConceptGraph(ExampleConceptGraphRecordFactory.makeSuperComplex());
    }

    //TODO: update these to new format
//    public static ConceptGraph makeSimpleInputTree(){
//        List<ConceptNode> cnList = new ArrayList<>();
//        List<LinkRecord> clList = new ArrayList<>();
//
//        cnList.add(new ConceptNode("A"));
//        cnList.add(new ConceptNode("B"));
//        cnList.add(new ConceptNode("C"));
//        cnList.add(new ConceptNode("D"));
//        cnList.add(new ConceptNode("E"));
//
//        clList.add( new LinkRecord("A","B"));
//        clList.add(new LinkRecord("A","C"));
//        clList.add(new LinkRecord("B","D"));
//        clList.add(new LinkRecord("C","E"));
//
//        ConceptGraphRecordOld inputNodesAndLinks = new ConceptGraphRecordOld(cnList, clList);
//        return new ConceptGraph(inputNodesAndLinks);
//    }
//
//    public static ConceptGraph makeComplexInputTree(){
//        List<ConceptNode> cnList = new ArrayList<>();
//        List<LinkRecord> linkList = new ArrayList<>();
//
//        cnList.add(new ConceptNode("A"));
//        cnList.add(new ConceptNode("B"));
//        cnList.add(new ConceptNode("C"));
//        cnList.add(new ConceptNode("D"));
//        cnList.add(new ConceptNode("E"));
//        cnList.add(new ConceptNode("F"));
//        cnList.add(new ConceptNode("G"));
//        cnList.add(new ConceptNode("H"));
//        cnList.add(new ConceptNode("I"));
//
//        LinkRecord link = new LinkRecord("A","B");
//        linkList.add(link);
//        link = new LinkRecord("A","C");
//        linkList.add(link);
//        link = new LinkRecord("B","D");
//        linkList.add(link);
//        link = new LinkRecord("C","E");
//        linkList.add(link);
//        link = new LinkRecord("C","F");
//        linkList.add(link);
//        link = new LinkRecord("D","G");
//        linkList.add(link);
//        link = new LinkRecord("G","H");
//        linkList.add(link);
//        link = new LinkRecord("G","I");
//        linkList.add(link);
//
//        ConceptGraphRecordOld lists = new ConceptGraphRecordOld(cnList, linkList);
//        return new ConceptGraph(lists);
//    }


     public static ConceptGraph willExampleConceptGraphTestOneStudent() {
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader("test/testresources/basicRealisticExampleGradeBook2.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords("test/testresources/basicRealisticExampleLOLRecordOneStudent.json");

            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());
            graph.calcKnowledgeEstimates();
            //System.out.println(graph.toString());
            return graph;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public static ConceptGraph simpleTestGraphTest() {
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader("test/testresources/simpleGraphTest.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson("test/testresources/simpleGraphTestConceptNodes.json");
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords("test/testresources/simpleGraphTestLearningObjects.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());
            graph.calcKnowledgeEstimates();
            //            System.out.println(graph.toString());

            return graph;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static ConceptGraph mediumTestGraphTest() {
        try {
            return new ConceptGraph(ConceptGraphRecord.buildFromJson("test/testresources/mediumGraphTestConceptNodes.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
