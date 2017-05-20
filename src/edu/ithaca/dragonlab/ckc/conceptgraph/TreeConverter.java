package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by home on 5/19/17.
 */
public class TreeConverter {

    private static final String symbol = "-";


    //TODO: There are some major issues here in that it does not copy any info about learningObjects, or create new nodeMaps or learningObjectMaps
    //Currently, it is only used as an output, in which case this should be moved to io and make records not Graphs...
    //Otherwise, if full graphs are needed, we need to create leanrngOjects as well as ConceptNodes, and create the maps of both whil we create.
    public static ConceptGraph makeTreeCopy(ConceptGraph graphToCopy){
        List<ConceptNode> newRoots = new ArrayList<>();
        HashMap<String, List<String>> initMultCopies = new HashMap<String, List<String>>();
        for(ConceptNode root : graphToCopy.getRoots()){
            newRoots.add(makeTreeNodeCopy(root, initMultCopies));
        }
        return new ConceptGraph(newRoots, graphToCopy.getName());
    }

    public static ConceptNode makeTreeNodeCopy(ConceptNode nodeToMakeTreeCopyOf, HashMap<String, List<String>> labelToListOfIds){
        ConceptNode nodeCopy;
        List<String> idsList = labelToListOfIds.get(nodeToMakeTreeCopyOf.getLabel());
        //if there are no copies, make a new list to store all the copies and add it to the map
        if(idsList == null){
            String nextId = makeNextId(nodeToMakeTreeCopyOf.getLabel());
            ConceptRecord nodeData = new ConceptRecord(nodeToMakeTreeCopyOf, nextId);
            nodeCopy = new ConceptNode(nodeData);
            idsList = new ArrayList<>();
            labelToListOfIds.put(nodeCopy.getLabel(), idsList);
            //else get the previous name from the list and make a new name from it
        }else{
            String prevId = idsList.get(idsList.size()-1);
            String nextId = makeNextId(prevId);
            ConceptRecord nodeData = new ConceptRecord(nodeToMakeTreeCopyOf, nextId);
            nodeCopy = new ConceptNode(nodeData);
        }
        //add the new name to the list
        idsList.add(nodeCopy.getID());

        for(ConceptNode origChild : nodeToMakeTreeCopyOf.getChildren()){
            ConceptNode childCopy = makeTreeNodeCopy(origChild, labelToListOfIds);
            nodeCopy.addChild(childCopy);
        }
        return nodeCopy;
    }

    /***
     * makes next name based on prevName, everything before "-" symbol is the name and the number after it will iterate
     * @param prevName
     * @return String next name
     */
    public static String makeNextId(String prevName) {
        if(prevName != "" && prevName != null){
            String[] nameList = prevName.split(symbol);
            String name = "";
            for(int i = 0; i < nameList.length-1; i++){
                name += nameList[i];
            }
            int num = 0;

            if(nameList.length <= 1){
                String fullName = nameList[0] + "-1";
                return fullName;
            }else{
                try{
                    String numString = nameList[nameList.length-1];
                    num = Integer.parseInt(numString);
                    num += 1;
                }catch(NumberFormatException e){
                    name += nameList[nameList.length-1];
                    num = 1;
                }
            }


            String fullName = name+"-"+num;
            return fullName;
        }else{
            return "";
        }
    }
}
