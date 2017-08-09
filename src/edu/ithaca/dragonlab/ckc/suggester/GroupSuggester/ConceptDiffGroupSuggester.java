package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.*;

/**
 * Created by mkimmitchell on 8/8/17.
 */
public class ConceptDiffGroupSuggester extends GroupSuggester{

    public Map<List<String>, Double> createDiffMap (Map<String, ConceptGraph> getMaps, String startingSubject){
        Map<List<String>, Double> diffMap = new HashMap<>();

        List<String> repeatedStudents = new ArrayList<>();

        for (String student1 : getMaps.keySet()) {
            ConceptGraph student1Graph = getMaps.get(student1);

            for (String student2 : getMaps.keySet()) {
                ConceptGraph student2Graph = getMaps.get(student2);

                if(!student1.equals(student2)){

                    if (!student1.equals(student2) && !(repeatedStudents.contains(student1 + "+" + student2) || repeatedStudents.contains(student2 + "+" + student1))) {

                        double difNum = calcDiff(student1Graph, student2Graph, startingSubject);
                        List<String> posGroup = new ArrayList<>();
                        posGroup.add(student1);
                        posGroup.add(student2);
                        diffMap.put(posGroup, difNum);
                        repeatedStudents.add(student1 + "+" + student2);

                    }
                }
            }
        }

        return diffMap;

    }


    public List<List<String>> twoGroups(List<List<String>> groupings, HashMap<List<String>, Double> sortedUserMap, Map<String, ConceptGraph> getMaps, int choice){
        List<String> repeatedNames = new ArrayList<>();

        for(List<String> studentList: sortedUserMap.keySet()){

            String firstStudent = studentList.get(0);
            String secondStudent = studentList.get(1);

            if(!(repeatedNames.contains(firstStudent)) && !(repeatedNames.contains(secondStudent))){

                repeatedNames.add(firstStudent);
                repeatedNames.add(secondStudent);

                List<String> group = new ArrayList<>();

                group.add(firstStudent);
                group.add(secondStudent);

                String diff = new Double(sortedUserMap.get(studentList)).toString();
                group.add(diff);

                groupings.add(group);
            }


        }

        if(getMaps.size()%choice!=0){
            List<String> usersTemp = new ArrayList<>();
            usersTemp.addAll(getMaps.keySet());
            usersTemp.removeAll(repeatedNames);

            List<String> group = new ArrayList<>();
            group.addAll(usersTemp);

            groupings.add(group);
        }


        return groupings;


    }


    public List<List< String>> threeGroups (List<List<String>> groupings, Map<String, ConceptGraph> getMaps, int choice, String subject, CohortConceptGraphs graphs){
        //to remove the difference number that was added
        for(List<String> group : groupings){
            if(group.size()>2){
                group.remove(group.size()-1);

            }
        }

        List<List<String>> trioGroup = new ArrayList<>();

        for(int i =0; i< (getMaps.size()/choice); i++){

            List<String> group = groupings.get(0);
            trioGroup.add(group);
            groupings.remove(group);
        }


        List<String> leftOver = new ArrayList<>();
        for(List<String> list: groupings){

            leftOver.addAll(list);
        }

        List<String> leftOver3 = new ArrayList<>();
        leftOver3.addAll(leftOver);

        HashMap<String , Integer> addMap = new HashMap<>();

        List<String > rr = new ArrayList<>();
        for(String list: leftOver){

            String leftOverName = "";
            String trioTempTemp = "";
            int index =0;

            double temp = graphs.getAvgGraph().getAllNodeIds().size();

            for(int i=0; i< trioGroup.size(); i++){
                List<String> actualList = trioGroup.get(i);
                String name = actualList.get(0);

                if(getMaps.containsKey(list) && getMaps.containsKey(name)){

                    ConceptGraph map1 = getMaps.get(list);
                    ConceptGraph map2 = getMaps.get(name);

                    if (calcDiff(map1, map2, subject) < temp && (!rr.contains(name))) {

                        temp = calcDiff(map1, map2, subject);
                        leftOverName = list;
                        trioTempTemp = name;
                        index = i;

                    }
                }
            }

            rr.add(trioTempTemp);
            leftOver3.remove(leftOverName);


            if(!addMap.containsValue(index)){
                addMap.put(leftOverName, index);

            }
        }


        for(String name: addMap.keySet()){
            int num = addMap.get(name);

            if(trioGroup.size()>0){
                List<String> group2 = trioGroup.get(num);
                group2.add(name);

            }
        }


        if(leftOver3.size()>0){
            List<String> group = new ArrayList<>();
            group.addAll(leftOver3);
            trioGroup.add(group);
        }

        return trioGroup;
    }


    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice, String subject) {

        Map<String, ConceptGraph> getMaps = getUserMap(graphs);
        Map<List<String>, Double> differenceMap = createDiffMap(getMaps, subject);
        HashMap<List<String>, Double> sortedUserMap = sortByValues(differenceMap);

        List<List<String>> groupings = twoGroups(new ArrayList<>(),sortedUserMap, getMaps, choice);

        if (choice==3) {

             return threeGroups (groupings, getMaps, choice, subject, graphs);
        }

        return groupings;
    }



    private static  HashMap<List<String>, Double> sortByValues(Map<List<String>, Double> map) {
        List hashmapList = new LinkedList(map.entrySet());


        Collections.sort(hashmapList, new Comparator() {
            public int compare(Object o1, Object o2) {

                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        }

        );

        HashMap<List<String>, Double> sortedHashMap = new LinkedHashMap();
        for (Iterator itr = hashmapList.iterator(); itr.hasNext(); ) {
            Map.Entry entry = (Map.Entry) itr.next();

            List<String> key = (List<String>) entry.getKey();
            Double value = (Double) entry.getValue();

            sortedHashMap.put(key, value);
        }
        return sortedHashMap;
    }





    public double calcDiff(ConceptGraph graph1 , ConceptGraph graph2 ,String subject) {

        if (subject.equals("all")) {
            double ex = 0;

            for (ConceptNode root : graph1.getRoots()) {
                double total = root.calcDiff(new ArrayList<>(), graph2);
                ex += total;
            }

            return ex;

        } else {

            ConceptNode node = graph1.findNodeById(subject);
            return node.calcDiff(new ArrayList<>(), graph2);
        }
    }




        @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }



}
