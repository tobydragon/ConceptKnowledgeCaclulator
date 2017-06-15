package edu.ithaca.dragonlab.ckc.suggester;

import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator;
import com.sun.org.apache.xpath.internal.SourceTree;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 6/6/2017.
 */
public class SuggestionResource {

    List<LearningObjectSuggestion> wrongList;
    List<LearningObjectSuggestion> incompleteList;

    public SuggestionResource(ConceptGraph graph){
        this.incompleteList= new ArrayList<LearningObjectSuggestion>();
        this.wrongList= new ArrayList<LearningObjectSuggestion>();
        completeList(graph, 0);
        completeList(graph,1);


    }

    /**
    *Creates a list of the LearningObjects where it goes from the object with highest individual pathnum from highest to the lowest
    *@param suggestionMap - suggested Concept Nodes and a list of LearningObjectSuggestions
    *@return a list of strings of ordered names
    */
    public List<String> sortHighToLow(HashMap<String, List<LearningObjectSuggestion>> suggestionMap){
        List<String> workingList = new ArrayList<>();
        List<String> orderedNames = new ArrayList<>();

        for (String name: suggestionMap.keySet()) {
            workingList.add(name);
        }

        int maxPathNum =0;
        while (workingList.size()!=0) {
            String save= "";

            for (int x = 0; x < workingList.size(); x++) {
                String name = workingList.get(x);

                if (suggestionMap.get(name).get(0).getPathNum() > maxPathNum) {

                    maxPathNum = suggestionMap.get(name).get(0).getPathNum();
                    save = name;
                }
            }

            if (workingList.contains(save)) {
                workingList.remove(save);
                orderedNames.add(save);
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
    public void completeList(ConceptGraph graph, int choice) {
        HashMap<String, List<LearningObjectSuggestion>> suggestionMap;
        if (choice == 1) {
//            incomplete
            List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(graph);
            suggestionMap = LearningObjectSuggester.buildSuggestionMap(concepts, 1,graph);
        } else {
//            //wrong
            List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(graph);
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






}