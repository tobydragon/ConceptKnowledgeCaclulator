package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.io.LinkRecord;
import edu.ithaca.dragonlab.ckc.io.NodesAndIDLinks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 * 2/15/17
 */
public class ExampleConceptGraphFactory {

    public static ConceptGraph makeSimple(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));

        clList.add(new LinkRecord("A","B")); //A -> B
        clList.add(new LinkRecord("A","C")); //A -> C
        clList.add(new LinkRecord("B","C")); //B -> C

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);

    }

    public static ConceptGraph makeMedium(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));

        clList.add(new LinkRecord("A","B")); //A -> B
        clList.add(new LinkRecord("A","C")); //A -> C
        clList.add(new LinkRecord("B","C")); //B -> C
        clList.add(new LinkRecord("B","D")); //B -> D
        clList.add(new LinkRecord("C","D")); //C -> D

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeComplex(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));

        clList.add(new LinkRecord("A","B")); //A -> B
        clList.add(new LinkRecord("A","C")); //A -> C
        clList.add(new LinkRecord("B","C")); //B -> C
        clList.add(new LinkRecord("B","D")); //B -> D
        clList.add(new LinkRecord("C","D")); //C -> D
        clList.add(new LinkRecord("A","E")); //A -> E
        clList.add(new LinkRecord("C","E")); //C -> E
        clList.add(new LinkRecord("D","E")); //D -> E

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeSuperComplex(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));
        cnList.add(new ConceptNode("F"));

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

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeSimpleInputTree(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));

        clList.add( new LinkRecord("A","B"));
        clList.add(new LinkRecord("A","C"));
        clList.add(new LinkRecord("B","D"));
        clList.add(new LinkRecord("C","E"));

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList, clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeComplexInputTree(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<LinkRecord> linkList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));
        cnList.add(new ConceptNode("F"));
        cnList.add(new ConceptNode("G"));
        cnList.add(new ConceptNode("H"));
        cnList.add(new ConceptNode("I"));

        LinkRecord link = new LinkRecord("A","B");
        linkList.add(link);
        link = new LinkRecord("A","C");
        linkList.add(link);
        link = new LinkRecord("B","D");
        linkList.add(link);
        link = new LinkRecord("C","E");
        linkList.add(link);
        link = new LinkRecord("C","F");
        linkList.add(link);
        link = new LinkRecord("D","G");
        linkList.add(link);
        link = new LinkRecord("G","H");
        linkList.add(link);
        link = new LinkRecord("G","I");
        linkList.add(link);

        NodesAndIDLinks lists = new NodesAndIDLinks(cnList, linkList);
        return new ConceptGraph(lists);
    }
}
