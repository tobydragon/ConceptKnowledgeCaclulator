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

    /**
     * Create a list of lists of two students. The list of pairs of students are ordered by the least Knowledge estimate difference to the greatest
     * @param sortedUserPairMap a linked map where the key is a list with the pair of students and the value is their knowledge estimate difference
     * @param userList a set of all the student IDs
     * @return groupings. A list of list of students
     */
    public List<List<String>> createGroupsOfTwo(HashMap<List<String>, Double> sortedUserPairMap, Set<String> userList){
        /*
        iterate through the ordered user pair map will difference
        beause the map is already ordered by the smallest difference num to the largest, you can go in order
        get the pair
        make sure that the pair has not already been calculated (and then add to the repeated list)

        create a sublist called group and add the pair of students
        add the sublist to the main list called Groupings
        because the map is already ordered, groupings will have pairs of students with the least knowledge estimate difference to the greatest


        if there are left over students, add them to a sublist and add the sublist to groupings
         */

        List<List<String>> groupings = new ArrayList<>();
        List<String> repeatedNames = new ArrayList<>();

        for(List<String> studentList: sortedUserPairMap.keySet()){

            String firstStudent = studentList.get(0);
            String secondStudent = studentList.get(1);

            if(!(repeatedNames.contains(firstStudent)) && !(repeatedNames.contains(secondStudent))){

                repeatedNames.add(firstStudent);
                repeatedNames.add(secondStudent);

                List<String> group = new ArrayList<>();

                group.add(firstStudent);
                group.add(secondStudent);


                groupings.add(group);
            }


        }

        if(userList.size()%2!=0){
            List<String> usersTemp = new ArrayList<>();
            usersTemp.addAll(userList);
            usersTemp.removeAll(repeatedNames);

            List<String> group = new ArrayList<>();
            group.addAll(usersTemp);

            groupings.add(group);
        }


        return groupings;


    }


    /**
     * Creates a list of groups of 3 based on the closest knowledge estimate difference to the furthest
     * @param groupings list of groups of 2
     * @param getMaps a map with all of the students and their associated concept graphs
     * @param subject if difference calculation on the student's entire concept graphs or a part of it
     * @param maxDifference the max difference between two concept graphs is a count of all of the concept nodes
     * @return a list of groups of 3 that are ordered by the cloest knowledge estimates to the furthest
     */
    public List<List< String>> createGroupsOfThree (List<List<String>> groupings, Map<String, ConceptGraph> getMaps, String subject, int maxDifference){
        List<List<String>> trioGroup = new ArrayList<>();

        /*
        List of groups of 3 size =  studentListSize/3
        get the first studentListSize/3 of pairs of students because they are the pairs of students that have the closest knowledge estimates
        and there will be the closest rounded number of groups for groups of 3

        The rest of pairs of students will be added to a list of students called LeftOver

        Iterate through the list of left over students and then through each of the pairs of students
        For each of the iterations of the pairs of students, get the first student in the pair and find it's knowledge estimate difference
        Save the index of the pair of students with the lowest knowledge estimate difference and which leftOver student it's currently on in the thirdStudentMap
        Removes the currently leftOver student from the leftOver student list

        iterates through the map, get's the group at the index in triogroup and adds the new group member to it and removes it from

        iterates through the rest of left over students, creates trio groups, and adds the group to the main grouping list trioGroups
         */

        for(int i =0; i< (getMaps.size()/3); i++){
            List<String> group = groupings.get(0);
            trioGroup.add(group);
            groupings.remove(group);
        }


        List<String> leftOver = new ArrayList<>();
        for(List<String> list: groupings){
            leftOver.addAll(list);
        }

        List<String> leftOverTemp = new ArrayList<>();
        leftOverTemp.addAll(leftOver);


        HashMap<String , Integer> thirdStudentMap = new HashMap<>();
        List<String > repeatGroup = new ArrayList<>();

        for(String list: leftOver){

            String leftOverName = "";
            String trioTempTemp = "";
            int index =0;

            double temp = maxDifference;

            for(int i=0; i< trioGroup.size(); i++){
                List<String> actualList = trioGroup.get(i);
                String name = actualList.get(0);

                if(getMaps.containsKey(list) && getMaps.containsKey(name)){

                    ConceptGraph map1 = getMaps.get(list);
                    ConceptGraph map2 = getMaps.get(name);
                    double differnce = calcDiff(map1, map2, subject);

                    if (differnce < temp && (!repeatGroup.contains(name))) {

                        temp = differnce;
                        leftOverName = list;
                        trioTempTemp = name;
                        index = i;

                    }
                }
            }

            repeatGroup.add(trioTempTemp);
            leftOverTemp.remove(leftOverName);

            if(!thirdStudentMap.containsValue(index) && !leftOverName.equals("")){
                thirdStudentMap.put(leftOverName, index);

            }
        }


        //get's the group at the index in triogroup and adds the new group memeber to it
        for(String name: thirdStudentMap.keySet()){
            int num = thirdStudentMap.get(name);

            if(trioGroup.size()>0){
                List<String> group2 = trioGroup.get(num);
                group2.add(name);

            }
        }


        if(leftOverTemp.size()>0){
            List<String> group = new ArrayList<>();
            group.addAll(leftOverTemp);
            trioGroup.add(group);
        }

        return trioGroup;
    }


    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice, String subject) {

        Map<String, ConceptGraph> getMaps = getUserMap(graphs);
        Map<List<String>, Double> differenceMap = createDiffMap(getMaps, subject);
        HashMap<List<String>, Double> sortedUserPairMap = sortByValues(differenceMap);

        //sorted user pair map is a map sorted by the difference values
        List<List<String>> groupings = createGroupsOfTwo(sortedUserPairMap, getMaps.keySet());


        if (choice==3) {

            return createGroupsOfThree (groupings, getMaps, subject, graphs.getAvgGraph().getAllNodeIds().size());

        }

        return groupings;
    }


    /**
     * Creates a linked hashmap of ordered pairs of students based on the lowest to highest knowledge estimate difference (value)
     * @param map map of each of the pairs and their knowledge estimate difference
     * @return linked Hash map
     */
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


    /**
     * Calculates the difference between the knowledge estimates of two students' graphs
     * @param graph1 student one's graph
     * @param graph2 student two's graph
     * @param subject if the calculation should be on the entirety of the student's graphs (all) or if it should start on a specific concept node (concept node ID)
     * @return
     */
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
