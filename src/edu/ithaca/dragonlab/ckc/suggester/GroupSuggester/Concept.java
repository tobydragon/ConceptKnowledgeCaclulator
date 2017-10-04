package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/28/2017.
 */
public class Concept extends GroupSuggester {
    int groupSize;

    public Concept(int size){
        groupSize=size;
    }

    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings) {

        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();


        Map<ConceptGraph, List<String>> extraMembers = new HashMap<>();


        for (Map<String, ConceptGraph> groupSoFar : groupings) {

            Map<ConceptGraph, List<String>> priorityMap= new HashMap<>();

            Map<ConceptGraph, List<String>> pri= new HashMap<>();
            pri.putAll(priorityMap);
            pri.putAll(extraMembers);
            //order this map


            for(String name: groupSoFar.keySet()){
                List<String> conceptsToWorkOn = getPriority(groupSoFar.get(name));
                priorityMap.put(groupSoFar.get(name), conceptsToWorkOn);
//                System.out.println(name + " " + conceptsToWorkOn);
            }

            //loop to get groups of people who are matching

            System.out.println(orderMap(pri));



//            for(ConceptGraph student: pri.keySet()){
//
//            }







        }
        return null;

    }

    public static Map<ConceptGraph, List<String>> orderMap(Map<ConceptGraph, List<String>> pri){


        return null;
    }




    public static List<String> getPriority(ConceptGraph graph){
        List<String> stringConcepts = new ArrayList<>();

        List<ConceptNode> nodes = LearningObjectSuggester.conceptsToWorkOn(graph);
        for(ConceptNode node: nodes){
            stringConcepts.add(node.getID());
        }

        return stringConcepts;
        }



        @Override
        public List<List<String>> suggestGroup (CohortConceptGraphs graphs,int choice){
            return null;
        }




}
