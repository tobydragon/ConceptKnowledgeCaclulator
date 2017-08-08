package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.*;

/**
 * Created by mkimmitchell on 8/8/17.
 */
public class ConceptDiffGroupSuggester extends GroupSuggester{


    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice, String subject) {

        List<List<String>> groupings = new ArrayList<>();


        Map<String, ConceptGraph> getMaps = getUserMap(graphs);

        Map<List<String>, Double> diffMap = new HashMap<>();

        String st1 = "";
        String st2 = "";

        List<String> repeats = new ArrayList<>();


        for (String name : getMaps.keySet()) {
            ConceptGraph userGraph = getMaps.get(name);


            for (String name2 : getMaps.keySet()) {
                ConceptGraph userGraph2 = getMaps.get(name2);

                if(!name.equals(name2)){

                    if (!name.equals(name2) && !(repeats.contains(name + "+" + name2) || repeats.contains(name2 + "+" + name))) {


                        double difNum = setupDiff(userGraph, userGraph2, subject);

                        List<String> posGroup = new ArrayList<>();
                        posGroup.add(name);
                        posGroup.add(name2);

                        diffMap.put(posGroup, difNum);

                        repeats.add(name + "+" + name2);

                    }

                }
            }

        }


        System.out.println(diffMap);

        while(groupings.size()!= (getMaps.size()/choice)){
            double temp = graphs.getAvgGraph().getAllNodeIds().size();




        }





        return groupings;
    }



    public double setupDiff(ConceptGraph graph1 , ConceptGraph graph2 ,String subject) {


        if (subject.equals("all")) {
            double ex = 0;

            for (ConceptNode root : graph1.getRoots()) {
                ConceptNode node2 = graph2.findNodeById(root.getID());

                double total = root.calcDiff(new ArrayList<>(), graph2);

                ex += total;

            }

            return ex;

        } else {

            ConceptNode node = graph1.findNodeById(subject);

            ConceptNode node2 = graph2.findNodeById(subject);


            double total = node.calcDiff(new ArrayList<>(), graph2);


            return total;


        }

    }




        @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }



}
