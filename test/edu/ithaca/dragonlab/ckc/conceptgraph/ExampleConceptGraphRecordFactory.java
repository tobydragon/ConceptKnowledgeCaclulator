package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptRecord;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 * 2/15/17
 */
public class ExampleConceptGraphRecordFactory {

    public static ConceptGraphRecord makeSimple(){
        List<ConceptRecord> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptRecord("A"));
        cnList.add(new ConceptRecord("B"));
        cnList.add(new ConceptRecord("C"));

        clList.add(new LinkRecord("A","B")); //A -> B
        clList.add(new LinkRecord("A","C")); //A -> C
        clList.add(new LinkRecord("B","C")); //B -> C

        ConceptGraphRecord inputNodesAndLinks = new ConceptGraphRecord("Simple Example", cnList,clList);
        return inputNodesAndLinks;
        //return new ConceptGraph(inputNodesAndLinks);

    }

    public static ConceptGraphRecord makeMedium(){
        List<ConceptRecord> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptRecord("A"));
        cnList.add(new ConceptRecord("B"));
        cnList.add(new ConceptRecord("C"));
        cnList.add(new ConceptRecord("D"));

        clList.add(new LinkRecord("A","B")); //A -> B
        clList.add(new LinkRecord("A","C")); //A -> C
        clList.add(new LinkRecord("B","C")); //B -> C
        clList.add(new LinkRecord("B","D")); //B -> D
        clList.add(new LinkRecord("C","D")); //C -> D

        ConceptGraphRecord inputNodesAndLinks = new ConceptGraphRecord("Medium Example", cnList,clList);
        return inputNodesAndLinks;
        //return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraphRecord makeComplex(){
        List<ConceptRecord> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptRecord("A"));
        cnList.add(new ConceptRecord("B"));
        cnList.add(new ConceptRecord("C"));
        cnList.add(new ConceptRecord("D"));
        cnList.add(new ConceptRecord("E"));

        clList.add(new LinkRecord("A","B")); //A -> B
        clList.add(new LinkRecord("A","C")); //A -> C
        clList.add(new LinkRecord("B","C")); //B -> C
        clList.add(new LinkRecord("B","D")); //B -> D
        clList.add(new LinkRecord("C","D")); //C -> D
        clList.add(new LinkRecord("A","E")); //A -> E
        clList.add(new LinkRecord("C","E")); //C -> E
        clList.add(new LinkRecord("D","E")); //D -> E

        ConceptGraphRecord inputNodesAndLinks = new ConceptGraphRecord("Complex Example", cnList,clList);
        return inputNodesAndLinks;
        //return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraphRecord makeSuperComplex(){
        List<ConceptRecord> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptRecord("A"));
        cnList.add(new ConceptRecord("B"));
        cnList.add(new ConceptRecord("C"));
        cnList.add(new ConceptRecord("D"));
        cnList.add(new ConceptRecord("E"));
        cnList.add(new ConceptRecord("F"));

        clList.add(new LinkRecord("A","B"));
        clList.add(new LinkRecord("A","C"));
        clList.add(new LinkRecord("A","E"));
        clList.add(new LinkRecord("B","C"));
        clList.add(new LinkRecord("B","D"));
        clList.add(new LinkRecord("B","E"));
        clList.add(new LinkRecord("C","D"));
        clList.add(new LinkRecord("C","E"));
        clList.add(new LinkRecord("D","E"));
        clList.add(new LinkRecord("D","F"));
        clList.add(new LinkRecord("E","F"));

        ConceptGraphRecord inputNodesAndLinks = new ConceptGraphRecord("Super Complex", cnList,clList);
        return inputNodesAndLinks;
        //return new ConceptGraph(inputNodesAndLinks);
    }

    public static void main(String[] args){
//        //write all examples to file
//        try {
//            makeSimple().writeToJson("test/testresources/systemwritten/simpleStructureGraph.json");
//            makeMedium().writeToJson("test/testresources/systemwritten/mediumStructureGraph.json");
//            makeComplex().writeToJson("test/testresources/systemwritten/complexStructureGraph.json");
//            makeSuperComplex().writeToJson("test/testresources/systemwritten/superComplexStructureGraph.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
