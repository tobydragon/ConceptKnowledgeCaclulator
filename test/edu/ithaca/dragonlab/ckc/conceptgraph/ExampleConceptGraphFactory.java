package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;

/**
 * @author tdragon
 * 2/15/17
 */
public class ExampleConceptGraphFactory {

    public static ConceptGraph makeSimple(){
        return new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(),
                ExampleLearningObjectFactory.makeSimpleLearningObject(),
                ExampleLearningObjectFactory.makeSimpleLOLRecords());
    }

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

}
