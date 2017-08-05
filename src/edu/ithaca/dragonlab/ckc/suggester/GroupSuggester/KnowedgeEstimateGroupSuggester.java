package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;


import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.*;

/**
 * Created by mkimmitchell on 8/4/17.
 */
public class KnowedgeEstimateGroupSuggester extends GroupSuggester{


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        //the list of groups of least difference to the most difference

        Map<String, ConceptGraph> getMaps = getUserMap(graphs);

        Map<String, Double> knowledgeSums = new HashMap<>();




        for(String name: getMaps.keySet()){
            ConceptGraph userGraph = getMaps.get(name);

            double sum = getSum(userGraph);

            System.out.println(name + " " + sum);

            knowledgeSums.put(name, sum);

        }

        //create groups with the most similar users
        //groups will go from most similar to least similar
        List<List<String>> groupings = new ArrayList<>();


        Map<String, Double> tempMap = new HashMap<>();
        tempMap.putAll(knowledgeSums);




        //testing
        Collection<ConceptGraph> lsit = getMaps.values();
        List<ConceptGraph> list = new ArrayList<>();
        list.addAll(lsit);




        Map<String, ConceptNode> map = list.get(0).getNodeMap();
        double sum =0;
        for(ConceptNode node: map.values()){

            sum = sum+ node.getKnowledgeEstimate();

        }

        System.out.println("ACTUAL " + sum);

//        knowledgeSums.put("s1", 0.84483833234);
//        knowledgeSums.put("s2", .7823423);
//        knowledgeSums.put("s3", .123000000);
//        knowledgeSums.put("s4", .5767834534);
//        knowledgeSums.put("s5", .3498273942);
//        knowledgeSums.put("s6", .12312324);


        //this is for groups of 2

        while(groupings.size() != getMaps.size()/choice) {
            double temp = 1 ;
            String st1 = "";
            String st2 = "";

            List<String> repeats = new ArrayList<>();

            for (String name : tempMap.keySet()) {
                double sum1 = tempMap.get(name);

                for (String name2 : tempMap.keySet()) {
                    double sum2 = tempMap.get(name2);

                    if (!name.equals(name2)) {

                        if (!name.equals(name2) && !(repeats.contains(name + "+" + name2) || repeats.contains(name2 + "+" + name))) {


                            if ((Math.abs(sum1 - sum2)) < temp) {
                                temp = (Math.abs(sum1 - sum2));
                                st1 = name;
                                st2 = name2;

                            }

                            repeats.add(name + "+" + name2);


                        }
                    }
                }
            }

            List<String> group = new ArrayList<>();
            group.add(st1);
            group.add(st2);

            tempMap.remove(st1, tempMap.get(st1));
            tempMap.remove(st2, tempMap.get(st2));


            groupings.add(group);

        }

        System.out.println(groupings);


        return groupings;

    }

        public double getSum (ConceptGraph s1){
        List<String> allNodes = new ArrayList<>();


            double ex = 0;

            for(ConceptNode roots: s1.getRoots()){
                double sum =0;
                double total = roots.example(sum, allNodes);
                ex+= total;

            }

        return ex;
    }





}
