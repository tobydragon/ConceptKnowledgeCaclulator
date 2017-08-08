package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;


import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.*;

/**
 * Created by mkimmitchell on 8/4/17.
 */
public class GraphSumGroupSuggester extends GroupSuggester{


    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice, String subject) {
        //the list of groups of least difference to the most difference


        Map<String, ConceptGraph> getMaps = getUserMap(graphs);

        Map<String, Double> knowledgeSums = new HashMap<>();


        for (String name : getMaps.keySet()) {
            ConceptGraph userGraph = getMaps.get(name);

            double sum = getSum(userGraph, subject);
            knowledgeSums.put(name, sum);

        }


        //create groups with the most similar users
        //groups will go from most similar to least similar
        List<List<String>> groupings = new ArrayList<>();


        Map<String, Double> tempMap = new HashMap<>();
        tempMap.putAll(knowledgeSums);


        //this is for groups of 2

            while (groupings.size() != getMaps.size() / 2) {
                double temp = graphs.getAvgGraph().getAllNodeIds().size();
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

        if (tempMap.size() < choice && tempMap.size() % choice != 0) {
            List<String> group = new ArrayList<>();
            group.addAll(tempMap.keySet());

            groupings.add(group);

        }

        if(choice==2){
            return groupings;
        }

        if(choice==3){

            int groupNum = getMaps.size()/choice;

            List<List<String>> trioGroup = new ArrayList<>();

            for(int i =0; i< groupNum; i++){

                List<String> group = groupings.get(0);

                trioGroup.add(group);

                groupings.remove(group);

            }


            List<List<String>> trioTemp = new ArrayList<>();
            trioTemp.addAll(trioGroup);



            List<String> leftOver = new ArrayList<>();
            for(List<String> list: groupings){
                leftOver.addAll(list);
            }




            int x =0;
            while(trioTemp.size() != 0){

                double temp = graphs.getAvgGraph().getAllNodeIds().size();
                    String leftOverName = "";
                    int groupingIndex = 0;


                    for(int i=0; i<trioTemp.size(); i++){
                        List<String> actualList = trioTemp.get(i);

                        double groupNameMapSize = knowledgeSums.get(actualList.get(0));


                        for(String name: leftOver) {

                            double leftNamemapSize = knowledgeSums.get(name);

                            if ((Math.abs(groupNameMapSize - leftNamemapSize)) < temp) {
                                temp = (Math.abs(groupNameMapSize - leftNamemapSize));

                                leftOverName = name;
                                groupingIndex = x;


                            }
                        }
                    }



                if(!leftOverName.equals("")) {
                        List<String> tempList = trioGroup.get(groupingIndex);
                        tempList.add(leftOverName);
                    }

                    leftOver.remove(leftOverName);

                    if(groupingIndex < trioTemp.size()){
                        trioTemp.remove(groupingIndex);


                    }

                x++;
                }


            if(leftOver.size()!=0){
                List<String> group = new ArrayList<>();
                group.addAll(leftOver);
                trioGroup.add(group);
            }


            return  trioGroup;

        }




        return null;
    }


    public double getSum (ConceptGraph s1, String subject){

        if(subject.equals("all")){
            double ex = 0;

            for(ConceptNode roots: s1.getRoots()){
                double total = roots.countTotalKnowledgeEstimate(new ArrayList<>());
                ex+= total;

            }

            return ex;

        }else{

            ConceptNode node = s1.findNodeById(subject);


            double total = node.countTotalKnowledgeEstimate(new ArrayList<>());

            return total;


        }




    }


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }
}
