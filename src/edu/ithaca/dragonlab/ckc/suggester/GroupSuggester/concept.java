package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/28/2017.
 */
public class concept extends GroupSuggester {
    int groupSize;

    public concept(int size){
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
        List hashmapList = new LinkedList(pri.entrySet());

        Collections.sort(hashmapList, new Comparator() {
                    public int compare(Object o1, Object o2) {

                        return ((Comparable) ((Map.Entry) (o1))).compareTo(((Map.Entry) (o2)));
                    }
                }
        );

        HashMap<ConceptGraph, List<String>> sortedMap = new LinkedHashMap();
        for (Iterator itr = hashmapList.iterator(); itr.hasNext(); ) {
            Map.Entry entry = (Map.Entry) itr.next();

            ConceptGraph key = (ConceptGraph) entry.getKey();
            List<String> value = (List<String>) entry.getValue();

//            Integer value = (Integer) entry.getValue();

            sortedMap.put(key, value);
        }

        return sortedMap;
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
