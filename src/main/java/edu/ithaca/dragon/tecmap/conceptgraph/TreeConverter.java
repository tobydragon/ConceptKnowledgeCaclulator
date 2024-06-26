package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.LearningResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by home on 5/19/17.
 */
public class TreeConverter {

    private static final String symbol = "-";

    public static ConceptGraph makeTreeCopy(ConceptGraph graphToCopy){
        Map<String, AssessmentItem> resourceMap = AssessmentItem.deepCopyLearningObjectMap(graphToCopy.getAssessmentItemMap());
        Map<String, LearningResource> materialMap = LearningResource.deepCopyLearningResourceMap(graphToCopy.getLearningMaterialMap());

        Map<String, ConceptNode> nodeMap = new HashMap<>();

        List<ConceptNode> newRoots = new ArrayList<>();
        HashMap<String, List<String>> initMultCopies = new HashMap<String, List<String>>();
        for(ConceptNode root : graphToCopy.getRoots()){
            newRoots.add(makeTreeNodeCopy(root, initMultCopies, nodeMap, resourceMap, materialMap));
        }
        return new ConceptGraph(newRoots, graphToCopy.getName(), resourceMap, materialMap, nodeMap);
    }


    public static ConceptNode makeTreeNodeCopy(ConceptNode nodeToMakeTreeCopyOf, HashMap<String, List<String>> labelToListOfIds, Map<String, ConceptNode>nodeMap, Map<String, AssessmentItem> resourceMap, Map<String, LearningResource> materialMap){
        ConceptNode nodeCopy;
        List<String> idsList = labelToListOfIds.get(nodeToMakeTreeCopyOf.getLabel());
        //if there are no copies, make a new list to store all the copies and add it to the map
        if(idsList == null){
            String nextId = makeNextId(nodeToMakeTreeCopyOf.getLabel());
            //Creates a copy of the node with the correct links to the already-existing resourceMap
            nodeCopy = new ConceptNode(nextId, nodeToMakeTreeCopyOf, resourceMap, materialMap);
            idsList = new ArrayList<>();
            labelToListOfIds.put(nodeCopy.getLabel(), idsList);
            //else get the previous name from the list and make a new name from it
        }else{
            String prevId = idsList.get(idsList.size()-1);
            String nextId = makeNextId(prevId);
            nodeCopy = new ConceptNode(nextId, nodeToMakeTreeCopyOf, resourceMap, materialMap);
        }
        //add the new name to the list
        idsList.add(nodeCopy.getID());
        //add the new node to the map
        nodeMap.put(nodeCopy.getID(), nodeCopy);

        for(ConceptNode origChild : nodeToMakeTreeCopyOf.getChildren()){
            ConceptNode childCopy = makeTreeNodeCopy(origChild, labelToListOfIds, nodeMap, resourceMap, materialMap);
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
