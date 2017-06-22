package edu.ithaca.dragonlab.ckc.suggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 6/6/2017.
 */
public class SuggestionResource {

    public List<LearningObjectSuggestion> wrongList;
    public List<LearningObjectSuggestion> incompleteList;
    public HashMap<String, List<LearningObjectSuggestion>> suggestionMap;


    public SuggestionResource(ConceptGraph graph, List<ConceptNode> concepts){
        this.incompleteList= new ArrayList<LearningObjectSuggestion>();
        this.wrongList= new ArrayList<LearningObjectSuggestion>();
        this.suggestionMap=null;
        completeList(graph, 0, concepts );
        completeList(graph,1, concepts);


    }

    public SuggestionResource(){
        this.incompleteList= new ArrayList<LearningObjectSuggestion>();
        this.wrongList= new ArrayList<LearningObjectSuggestion>();
        this.suggestionMap= new HashMap<>();
    }


    /**
     *Creates a list of the LearningObjects where it goes from the object with highest individual pathnum from highest to the lowest
     *@param suggestionMap - suggested Concept Nodes and a list of LearningObjectSuggestions
     *@return a list of strings of ordered names
     */
    public static List<String> sortHighToLow(HashMap<String, List<LearningObjectSuggestion>> suggestionMap){
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
     *go through map and pick the first suggestions, and go through all concepts then go through all concepts again and get the second LearningObject
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
        for (List<LearningObjectSuggestion> lists : suggestionMap.values()) {
            max += lists.size();
        }
        if (max != 0) {

            List<String> suggestionOrder = sortHighToLow(suggestionMap);

            int itr = 0;
            while (itr < max) {
                for (int i = 0; i < suggestionOrder.size(); i++) {
                    List<LearningObjectSuggestion> LOSList = suggestionMap.get(suggestionOrder.get(i));
                    if (itr < LOSList.size()) {
                        LearningObjectSuggestion sug = LOSList.get(itr);

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



    public String toString(int choice){
        List<LearningObjectSuggestion> list;
        if (choice==0){
            list = incompleteList;
        }else{
            list = wrongList;
        }
        String st = "";
        for (LearningObjectSuggestion los: list){
            st += los.toString();
        }
        return st;
    }

}
