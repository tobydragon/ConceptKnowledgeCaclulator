package edu.ithaca.dragon.tecmap.suggester;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mia Kimmich Mitchell on 6/6/2017.
 */
public class SuggestionResource {

    public List<LearningResourceSuggestion> wrongList;
    public List<LearningResourceSuggestion> incompleteList;
    public HashMap<String, List<LearningResourceSuggestion>> suggestionMap;


    public SuggestionResource(ConceptGraph graph, List<ConceptNode> concepts){
        this();
        completeList(graph, 0, concepts );
        completeList(graph,1, concepts);
    }

    public SuggestionResource(){
        this.incompleteList= new ArrayList<LearningResourceSuggestion>();
        this.wrongList= new ArrayList<LearningResourceSuggestion>();
        this.suggestionMap= new HashMap<>();
    }


    /**
     *Creates a list of the LearningObjects where it goes from the object with highest individual pathnum from highest to the lowest
     *@param suggestionMap - suggested Concept Nodes and a list of LearningObjectSuggestions
     *@return a list of strings of ordered names
     */
    public static List<String> sortHighToLow(HashMap<String, List<LearningResourceSuggestion>> suggestionMap){
        List<String> workingList = new ArrayList<>();
        List<String> orderedNames = new ArrayList<>();

        workingList.addAll(suggestionMap.keySet());


        int maxPathNum =0;
        while (workingList.size()!=0) {
            String save= "";
            String nameWithNoList = "";

            for (int x = 0; x < workingList.size(); x++) {
                String name = workingList.get(x);

                if(suggestionMap.get(name).size()!=0) {

                    if (suggestionMap.get(name).get(0).getPathNum() > maxPathNum) {

                        maxPathNum = suggestionMap.get(name).get(0).getPathNum();
                        save = name;
                    }
                }else{
                    nameWithNoList=name;
                }

            }
            //add the ones that don't have lists attached

            if (workingList.contains(save)) {
                workingList.remove(save);
                orderedNames.add(save);
            }

            if (workingList.contains(nameWithNoList)){
                workingList.remove(nameWithNoList);
                orderedNames.add(nameWithNoList);
            }
            maxPathNum=0;
        }
        return orderedNames;
    }

    /**
     *Creates a list of incomplete or wrong learningObjects that are ordered from greatest importance level to least
     *go through map and pick the first suggestions, and go through all concepts then go through all concepts again and get the second AssessmentItem
     *@param graph of concepts
     *@param choice  0= wrong list, 1= incomplete list
     */
    public void completeList(ConceptGraph graph, int choice, List<ConceptNode> concepts) {

        if (choice == 1) {
//            incomplete
            suggestionMap = LearningObjectSuggester.buildSuggestionMap(concepts, 1,graph);
        } else {
//            //wrong
            suggestionMap = LearningObjectSuggester.buildSuggestionMap(concepts,0,graph);
        }

        int max = 0;
        for (List<LearningResourceSuggestion> lists : suggestionMap.values()) {
            max += lists.size();
        }
        if (max != 0) {

            List<String> suggestionOrder = sortHighToLow(suggestionMap);
            int itr = 0;
            while (itr < max) {
                for (int i = 0; i < suggestionOrder.size(); i++) {
                    List<LearningResourceSuggestion> LOSList = suggestionMap.get(suggestionOrder.get(i));
                    if (itr < LOSList.size()) {
                        LearningResourceSuggestion sug = LOSList.get(itr);

                        if (choice == 1) {
                            incompleteList.add(sug);
                        } else {
                            wrongList.add(sug);
                        }
                    }
                }
                itr++;
            }
        }
    }

    /**
     * this goes through the list and finds repeats of resource id's and creates a list that only has one occurence of each resources and all of there related concepts.
     * @param choice
     * @return
     */
    public String toString(int choice){
        List<LearningResourceSuggestion> list;
        if (choice==0){
            list = incompleteList;
        }else{
            list = wrongList;
        }
        //finds out which resources have repeated concepts
        //Hashmap<learning suggestion resource, Concept>
        HashMap<String, String> repeatList = new HashMap<>();

        for (LearningResourceSuggestion los: list){
            if(repeatList.keySet().contains(los.getId())){
                String name = repeatList.get(los.getId());
                repeatList.put(los.getId(), name += " & " +los.getReasoning());

            }else{
                repeatList.put(los.getId(), los.getReasoning());
            }
        }


        //So repeated resources don't get added to the string multiple times
        List<String>  list2 = new ArrayList<>();
        String st = "";
        for (LearningResourceSuggestion los: list){
            if(list2.contains(los.getId())){

            }else{
                list2.add(los.getId());

                if(repeatList.keySet().contains(los.getId())){
                    st+= "Resource: " + los.getId()+ "\t Concepts it relates to: " + repeatList.get(los.getId())+ "\t Importance: "+ los.getPathNum() + "\t Direct Concept Links: " + los.getDirectConceptLinkCount() +"\n";
                }else{
                    st += los.toString();

                }
            }
        }

        return st;
    }


}
