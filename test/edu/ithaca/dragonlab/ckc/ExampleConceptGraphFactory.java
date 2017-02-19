package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.ConceptGraph;
import edu.ithaca.dragonlab.ckc.ConceptNode;
import edu.ithaca.dragonlab.ckc.IDLink;
import edu.ithaca.dragonlab.ckc.NodesAndIDLinks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 * 2/15/17
 */
public class ExampleConceptGraphFactory {

    public static ConceptGraph makeSimple(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));

        clList.add(new IDLink("A","B")); //A -> B
        clList.add(new IDLink("A","C")); //A -> C
        clList.add(new IDLink("B","C")); //B -> C

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);

    }

    public static ConceptGraph makeMedium(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));

        clList.add(new IDLink("A","B")); //A -> B
        clList.add(new IDLink("A","C")); //A -> C
        clList.add(new IDLink("B","C")); //B -> C
        clList.add(new IDLink("B","D")); //B -> D
        clList.add(new IDLink("C","D")); //C -> D

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeComplex(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));

        clList.add(new IDLink("A","B")); //A -> B
        clList.add(new IDLink("A","C")); //A -> C
        clList.add(new IDLink("B","C")); //B -> C
        clList.add(new IDLink("B","D")); //B -> D
        clList.add(new IDLink("C","D")); //C -> D
        clList.add(new IDLink("A","E")); //A -> E
        clList.add(new IDLink("C","E")); //C -> E
        clList.add(new IDLink("D","E")); //D -> E

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeSuperComplex(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));
        cnList.add(new ConceptNode("F"));

        clList.add(new IDLink("A","B"));
        clList.add(new IDLink("A","C"));
        clList.add(new IDLink("A","E"));
        clList.add(new IDLink("B","C"));
        clList.add(new IDLink("B","D"));
        clList.add(new IDLink("B","E"));
        clList.add(new IDLink("C","D"));
        clList.add(new IDLink("C","E"));
        clList.add(new IDLink("D","E"));
        clList.add(new IDLink("D","F"));
        clList.add(new IDLink("E","F"));

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList,clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeSimpleInputTree(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> clList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));

        clList.add( new IDLink("A","B"));
        clList.add(new IDLink("A","C"));
        clList.add(new IDLink("B","D"));
        clList.add(new IDLink("C","E"));

        NodesAndIDLinks inputNodesAndLinks = new NodesAndIDLinks(cnList, clList);
        return new ConceptGraph(inputNodesAndLinks);
    }

    public static ConceptGraph makeComplexInputTree(){
        List<ConceptNode> cnList = new ArrayList<>();
        List<IDLink> linkList = new ArrayList<>();

        cnList.add(new ConceptNode("A"));
        cnList.add(new ConceptNode("B"));
        cnList.add(new ConceptNode("C"));
        cnList.add(new ConceptNode("D"));
        cnList.add(new ConceptNode("E"));
        cnList.add(new ConceptNode("F"));
        cnList.add(new ConceptNode("G"));
        cnList.add(new ConceptNode("H"));
        cnList.add(new ConceptNode("I"));

        IDLink link = new IDLink("A","B");
        linkList.add(link);
        link = new IDLink("A","C");
        linkList.add(link);
        link = new IDLink("B","D");
        linkList.add(link);
        link = new IDLink("C","E");
        linkList.add(link);
        link = new IDLink("C","F");
        linkList.add(link);
        link = new IDLink("D","G");
        linkList.add(link);
        link = new IDLink("G","H");
        linkList.add(link);
        link = new IDLink("G","I");
        linkList.add(link);

        NodesAndIDLinks lists = new NodesAndIDLinks(cnList, linkList);
        return new ConceptGraph(lists);
    }
}
