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




//        for(String name: getMaps.keySet()){
//            ConceptGraph userGraph = getMaps.get(name);
//
//            double sum = getSum(userGraph);
//
//            System.out.println(name + " " + sum);
//
//            knowledgeSums.put(name, sum);
//
//        }

        //testing
        Collection<ConceptGraph> lsit = getMaps.values();
        List<ConceptGraph> list = new ArrayList<>();
        list.addAll(lsit);


//        double sum1 = getSum(list.get(0));
//        System.out.println("TEST " + sum1);
//


//        System.out.println(list.get(0).getName());

//        Map<String, ConceptNode> map = list.get(0).getNodeMap();
//        double sum =0;
//        for(ConceptNode node: map.values()){
//
//            sum = sum+ node.getKnowledgeEstimate();
//
//        }
//
//        System.out.println("ACTUAL " + sum);

        knowledgeSums.put("s1", 0.84483833234);
        knowledgeSums.put("s2", .7823423);
        knowledgeSums.put("s3", .123000000);
        knowledgeSums.put("s4", .5767834534);


        //create groups with the most similar users
        //groups will go from most similar to least similar
        List<List<String>> groupings = new ArrayList<>();

        double temp = 1 ;
        String st1 = "";
        String st2 = "";

        Map<String, Double> tempMap = new HashMap<>();
        tempMap.putAll(knowledgeSums);


        List<String> repeats = new ArrayList<>();


        for(String name: tempMap.keySet()){
            double sum = tempMap.get(name);

            for(String name2: tempMap.keySet()){
                double sum2 = tempMap.get(name2);

                if(!name.equals(name2)){

                    if(!name.equals(name2) && !(repeats.contains(name+"+"+name2) || repeats.contains(name2+"+"+name))) {


//
                    if((Math.abs(sum- sum2))< temp){
                        temp = (Math.abs(sum- sum2));
                        st1= name;
                        st2 = name2;

                    }

                        repeats.add(name+"+"+name2);

                    }
                }
            }


        }


        System.out.println(temp);
        System.out.println(st1 + " " + st2);







        return null;

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
