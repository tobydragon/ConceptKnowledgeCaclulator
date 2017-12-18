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

    public static double MAX= .85;
    public static double MIN = .60;
    //MIN and MAX are used for suggesting concept nodes. For a concept node to be considered for giving suggestions it must between MIN and MAX.

    //MAX is used to figure out if a LearningObjectSuggestion is RIGHT or WRONG. (Assuming we already know it's not incomplete)
    //For a LearningObjectSuggestion to be wrong it has to be less than MAX

    /**
     * Creates a list of ConceptNodes that are between the knowledge range and are not ancestors
     * @param graph
     * @return
     */
    public static List<ConceptNode> conceptsToWorkOn(ConceptGraph graph){
        List<ConceptNode> suggestedConceptList = new ArrayList<ConceptNode>();

        for (String key : graph.getAllNodeIds()) {
            ConceptNode node = graph.findNodeById(key);

            if (node.getKnowledgeEstimate() >= MIN && node.getKnowledgeEstimate() <= MAX) {
                graph.updateSuggestionList(node, suggestedConceptList);
            }
        }


        return suggestedConceptList;
    }


    /**
     *sorts the list of LearningObjectSuggestions and takes the incomplete learningObjects. (The list is ordered by incomplete, wrong, and right and within each of those categories based on the highest importance value to lowest).
     *@param graph Concept graph
     *@param choice: 1= incomplete, 0 = wrong
     *@return the map of incomplete learningObjectSuggestions in order of highest importance value to lowest
     */
    public static HashMap<String, List<LearningObjectSuggestion>> buildSuggestionMap(List<ConceptNode> suggestedConceptList, Integer choice, ConceptGraph graph){

        HashMap<String, List<LearningObjectSuggestion>> suggestedConceptNodeMap = new HashMap<>();

        for (int x =0; x< suggestedConceptList.size(); x++) {
            ConceptNode concept = suggestedConceptList.get(x);

            List<LearningObjectSuggestion> testList = new ArrayList<>();

            HashMap<String, Integer> map = graph.buildLearningObjectSummaryList(concept.getID());
            HashMap<String, Integer> linkMap = graph.buildDirectConceptLinkCount();

            List<LearningObjectSuggestion> list = buildLearningObjectSuggestionList(map, graph.getLearningObjectMap(), concept.getID(), linkMap);

            sortSuggestions(list);



            for (int i = 0; i < list.size(); i++) {

                //if it is incomplete
                if (choice.equals(1)) {
                    if (list.get(i).getLevel().equals(LearningObjectSuggestion.Level.INCOMPLETE)) {
                        //then add it
                        testList.add(list.get(i));
                    }
                } else {
                    if (list.get(i).getLevel().equals(LearningObjectSuggestion.Level.WRONG)) {
                        //then add it

                        testList.add(list.get(i));
                    }
                }

            }
            suggestedConceptNodeMap.put(concept.getID(), testList);
        }
        return suggestedConceptNodeMap;
    }



    public static void sortSuggestions(List<LearningObjectSuggestion> myList){
        Collections.sort(myList, new LearningObjectSuggestionComparator());
    }

    /**
    *takes a map of strings and creates a list of learningObjectSuggestion that holds if the learningObject was incomplete, wrong, or right, the pathNum, and the Concept that caused the LearningObject to be suggested
    *@param summaryList- map of the summaryList (map of the LearningObjects and the pathNum from a certain start)
    *@param  learningObjectMap- map of all of the learningObjects
    *@param causedConcept- the ID of ConceptNode that the learningObject came from
    *@returns a list of the created LearningObjectSuggestions
    */
    public static List<LearningObjectSuggestion> buildLearningObjectSuggestionList(Map<String, Integer> summaryList, Map<String, LearningObject> learningObjectMap, String causedConcept, Map<String, Integer> directLinkMap){
        List<LearningObjectSuggestion> myList = new ArrayList<LearningObjectSuggestion>();
        for (String key : summaryList.keySet()){
            int lineNum = summaryList.get(key);
            LearningObject node = learningObjectMap.get(key);
            double estimate = node.calcKnowledgeEstimate();

            int directConceptLinkCount = directLinkMap.get(node.getId());

            LearningObjectSuggestion.Level level;
            //fix to fit preconditions
            LearningObjectSuggestion.Level levelIn;
            List<LearningObjectResponse> resList = node.getResponses();

            if(resList.size()==0){
                levelIn = LearningObjectSuggestion.Level.INCOMPLETE;
                LearningObjectSuggestion suggestionNode = new LearningObjectSuggestion(key,lineNum,levelIn, causedConcept, directConceptLinkCount);
                myList.add(suggestionNode);

            }else{

                if(estimate>= 0 && estimate<= MAX){
                    level = LearningObjectSuggestion.Level.WRONG;
                }else{
                    level = LearningObjectSuggestion.Level.RIGHT;
                }
                LearningObjectSuggestion suggestionNode = new LearningObjectSuggestion(key,lineNum,level,causedConcept, directConceptLinkCount);
                myList.add(suggestionNode);

            }

        }

        return myList;
    }
}
