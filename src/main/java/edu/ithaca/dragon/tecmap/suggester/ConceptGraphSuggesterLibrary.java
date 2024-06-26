package edu.ithaca.dragon.tecmap.suggester;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.util.*;

public class ConceptGraphSuggesterLibrary {

    public static double MAX= .85;
    //MAX is used to figure out if a LearningResourceSuggestion is RIGHT or WRONG. (Assuming we already know it's not incomplete)
    //For a LearningResourceSuggestion to be wrong it has to be less than MAX

    /**
     * Suggest the concepts on which to focus, by finding the lowest children with knowledge estimate is less than MAX
     * Does not suggest concepts if students haven't taken the assessment because KE is 0
     * @param graphForSuggestions
     * @return a list of references to the actual ConceptNodes for the graphForSuggestions // add to comment
     */
    public static List<ConceptNode> suggestConcepts(ConceptGraph graphForSuggestions){
        List<ConceptNode> suggestedConceptList = new ArrayList<>();
        //TODO: convert to functional style for parallelism
        for (String key : graphForSuggestions.getAllNodeIds()) {
            ConceptNode node = graphForSuggestions.findNodeById(key);
            if (node.getKnowledgeEstimate() > 0 && node.getKnowledgeEstimate() <= MAX) {
                addIfLowestDescendant(node, suggestedConceptList);
            }
        }
        return suggestedConceptList;
    }

    /**
     * adds the nodeToPotentiallyAdd to the suggestedList if it is not an ancestor of one already suggested,
     * if it adds, it also removes any ancestors
     * @param nodeToPotentiallyAdd
     * @param suggestedList
     * @post the node might be added to the suggestedList (if lowest descendant), and then ancestors of the node might be removed
     */
    private static void addIfLowestDescendant(ConceptNode nodeToPotentiallyAdd, List<ConceptNode> suggestedList){
        //if this nodeToPotentiallyAdd is an ancestor of the nodeToPotentiallyAdd already in the suggestion list, skip it
        for(ConceptNode ancNode: suggestedList){
            if (nodeToPotentiallyAdd.isAncestorOf(ancNode)){
                return;
            }
        }
        //when we suggest a descendant, remove any ancestors of that nodeToPotentiallyAdd from the list.
        List<ConceptNode> ancesList= new ArrayList<>();
        for (ConceptNode trackNode : suggestedList) {
            if (trackNode.isAncestorOf(nodeToPotentiallyAdd)) {
                ancesList.add(trackNode);
            }
        }
        suggestedList.removeAll(ancesList);
        suggestedList.add(nodeToPotentiallyAdd);
    }


    /**
     *takes a list of suggested concepts (each list is ordered by incomplete, wrong, and right and within each of those categories based on the highest importance value to lowest).
     * does not suggest LearningResource that is connected to concepts not on the suggestedConceptList
     *@param graph Concept graph
     *@param choice: 1= incomplete, 0 = wrong
     *@return the map of learningResourceSuggestions in order of highest importance value to lowest
     */
    public static HashMap<String, List<LearningResourceSuggestion>> buildSuggestionMap(List<ConceptNode> suggestedConceptList, Integer choice, ConceptGraph graph){

        HashMap<String, List<LearningResourceSuggestion>> suggestedConceptNodeMap = new HashMap<>();

        for (int x =0; x< suggestedConceptList.size(); x++) {
            ConceptNode concept = suggestedConceptList.get(x);

            List<LearningResourceSuggestion> testList = new ArrayList<>();

            Map<String, Integer> map = graph.buildLearningResourcePathCount(concept.getID());
            Map<String, Integer> linkMap = graph.buildDirectConceptLinkCount();

            List<LearningResourceSuggestion> list = buildLearningResourceSuggestionList(map, graph.getAssessmentItemMap(), concept.getID(), linkMap);

            sortSuggestions(list);


            for (int i = 0; i < list.size(); i++) {

                //if it is incomplete
                if (choice.equals(1)) {
                    if (list.get(i).getLevel().equals(LearningResourceSuggestion.Level.INCOMPLETE)) {
                        //then add it
                        testList.add(list.get(i));
                    }
                } else {
                    if (list.get(i).getLevel().equals(LearningResourceSuggestion.Level.WRONG)) {
                        //then add it
                        testList.add(list.get(i));
                    }
                }
            }
//            System.out.println(suggestedConceptNodeMap);
            suggestedConceptNodeMap.put(concept.getID(), testList);
        }
        return suggestedConceptNodeMap;
    }



    public static void sortSuggestions(List<LearningResourceSuggestion> myList){
        Collections.sort(myList, new LearningResourceSuggestionComparator());
    }

    /**
    *takes a map of strings and creates a list of learningObjectSuggestion that holds if the learningObject was incomplete, wrong, or right, the pathNum, and the Concept that caused the AssessmentItem to be suggested
    *@param summaryList- map of the summaryList (map of the LearningResource and the pathNum from a certain start)
    *@param  assessmentItemMap- map of all AssessmentItem
    *@param causedConcept- the ID of ConceptNode that the LearningResource came from
    *@returns a list of the created LearningResourceSuggestions
    */
    public static List<LearningResourceSuggestion> buildLearningResourceSuggestionList(Map<String, Integer> summaryList, Map<String, AssessmentItem> assessmentItemMap, String causedConcept, Map<String, Integer> directLinkMap){
        List<LearningResourceSuggestion> myList = new ArrayList<LearningResourceSuggestion>();
        for (String key : summaryList.keySet()){
            int lineNum = summaryList.get(key);
            AssessmentItem assessmentItem = assessmentItemMap.get(key);
            double estimate = assessmentItem.calcKnowledgeEstimate();

            int directConceptLinkCount = directLinkMap.get(assessmentItem.getId());

            LearningResourceSuggestion.Level level;
            //fix to fit preconditions
            LearningResourceSuggestion.Level levelIn;
            List<AssessmentItemResponse> resList = assessmentItem.getResponses();

            if(resList.size()==0){
                levelIn = LearningResourceSuggestion.Level.INCOMPLETE;
                LearningResourceSuggestion suggestionNode = new LearningResourceSuggestion(key,lineNum,levelIn, causedConcept, directConceptLinkCount);
                myList.add(suggestionNode);

            }else{

                if(estimate>= 0 && estimate<= MAX){
                    level = LearningResourceSuggestion.Level.WRONG;
                }else{
                    level = LearningResourceSuggestion.Level.RIGHT;
                }
                LearningResourceSuggestion suggestionNode = new LearningResourceSuggestion(key,lineNum,level,causedConcept, directConceptLinkCount);
                myList.add(suggestionNode);

            }

        }
        return myList;
    }
}
