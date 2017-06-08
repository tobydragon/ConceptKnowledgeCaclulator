package edu.ithaca.dragonlab.ckc.suggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;

import java.util.*;

/**
 * Created by home on 5/19/17.
 */
public class LearningObjectSuggester {

/*
goes through the graph and creates a map of ConceptNodes that are between 55%-75% and are not ancestors (unless the child node does better than 75%)
then it sorts the list of LearningObjectSuggestions and takes the incomplete learningObjects. (The list is ordered by incomplete, wrong, and right and
within each of those categories based on the highest importance value to lowest).
@param the graph
@param choices: 1= incomplete, 0 = wrong
return the map of incomplete learningObjectSuggestions in order of highest importance value to lowest
 */
    public static HashMap<String, List<LearningObjectSuggestion>> buildSuggestionMap(ConceptGraph graph, Integer choice){

        HashMap<String, List<LearningObjectSuggestion>> suggestedConceptNodeMap = new HashMap<>();
        for (String key : graph.getAllNodeIds()) {
            ConceptNode node = graph.findNodeById(key);
//            System.out.println("estimate "+ node.getID() + node.getKnowledgeEstimate());

            List<LearningObjectSuggestion> testList = new ArrayList<>();
            if(node.getKnowledgeEstimate()>=0.55 && node.getKnowledgeEstimate()<=0.75) {
                //if false, then it is not an ancestor, therefore it can be added to the list
                boolean anc = graph.ancestry(node);
                if (!anc) {
                    HashMap<String, Integer> map = graph.buildLearningObjectSummaryList(key);
                    List<LearningObjectSuggestion> list = buildLearningObjectSuggestionList(map, graph.getLearningObjectMap());
                    sortSuggestions(list);
                    for (int i = 0; i < list.size(); i++) {
                        //if it is incomplete
                        if (choice.equals(1)){
                            if (list.get(i).getLevel().equals(LearningObjectSuggestion.Level.INCOMPLETE)) {
                                //then add it
                                testList.add(list.get(i));
                            }
                        }else{
                            if (list.get(i).getLevel().equals(LearningObjectSuggestion.Level.WRONG)) {
                                //then add it
                                testList.add(list.get(i));
                            }
                        }

                    }
//                    System.out.println("the testList size is "+testList.size());
                    suggestedConceptNodeMap.put(node.getID(), testList);
                }
            }
        }

        return suggestedConceptNodeMap;
    }

    public static void sortSuggestions(List<LearningObjectSuggestion> myList){
        Collections.sort(myList, new LearningObjectSuggestionComparator());
    }

    /*
    takes a map of strings and creates a list of learningObjectSuggestion that hold if the learningObject was incomplete, wrong, or right
     */
    public static List<LearningObjectSuggestion> buildLearningObjectSuggestionList(Map<String, Integer> summaryList, Map<String, LearningObject> learningObjectMap){
        //build objects through the hashmap
        //iterate through the hashmap and make the new objects
        //put object in list
        List<LearningObjectSuggestion> myList = new ArrayList<LearningObjectSuggestion>();
        for (String key : summaryList.keySet()){
            int lineNum = summaryList.get(key);
            LearningObject node = learningObjectMap.get(key);
            double estimate = node.calcKnowledgeEstimate();
            LearningObjectSuggestion.Level level;
            //fix to fit preconditions
            LearningObjectSuggestion.Level levelIn;
            List<LearningObjectResponse> resList = node.getResponses();
            if(resList.size()==0){
                levelIn = LearningObjectSuggestion.Level.INCOMPLETE;
                LearningObjectSuggestion suggestionNode = new LearningObjectSuggestion(key,lineNum,levelIn);
                myList.add(suggestionNode);

            }else{
                if(estimate> 0 && estimate<= 0.59){
                    level = LearningObjectSuggestion.Level.WRONG;
                }else{
                    level = LearningObjectSuggestion.Level.RIGHT;
                }
                LearningObjectSuggestion suggestionNode = new LearningObjectSuggestion(key,lineNum,level);
                myList.add(suggestionNode);

            }


        }
        return myList;
    }
}
