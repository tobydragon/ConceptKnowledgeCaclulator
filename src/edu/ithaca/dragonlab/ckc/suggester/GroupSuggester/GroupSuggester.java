package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;
/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggester {
    int size;
    Suggester  sug;


    public List<Group>  grouping(List<Group> groupings, int groupSize, int groupingType, List<List<Integer>> range, boolean random){
        size = groupSize;

        if(groupingType == 0) {
            //bucket
            try {
                sug = new Bucket(range);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(groupingType ==1){
            sug = new Concept(size);

        }else if(groupingType==2){
            sug = new BySize(groupSize, random);

        }else{
            //jigsaw
        }

        List<Group> actualGroupings = new ArrayList<>();
        Group extraMembers = new Group(new HashMap<>(), " ");

        for(Group groupSoFar: groupings) {

            if(extraMembers.getSize()>0){
                groupSoFar.combinegroups(extraMembers);
            }

            List<Group> temp  = sug.suggestGroup(groupSoFar, extraMembers);


            actualGroupings.addAll(temp);
        }

        //if there are left over students
        //todo fix this so that it can work with any amount of student group sizes

        if(extraMembers.getSize()>0){
            if(extraMembers.getSize()>size){

                Suggester two = new BySize(groupSize, random);
                List<Group> temp = two.suggestGroup(extraMembers, new Group());
                actualGroupings.addAll(temp);

            }else if (extraMembers.getSize() == size/2) {

                Group tempGr = actualGroupings.get(actualGroupings.size()-1);
                tempGr.addMember(extraMembers);

            }else{
                actualGroupings.add(extraMembers);


            }
        }


        return actualGroupings;
    }



//    public List<String> getUsersList(CohortConceptGraphs graphs) {
//        List<String> userList = new ArrayList<>();
//
//        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();
//        userList.addAll(userToGraph.keySet());
//
//        return userList;
//    }



    private static Map<String, ConceptGraph> getUserMap(CohortConceptGraphs graphs){
        Map<String, ConceptGraph> map = new HashMap<>();

        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();

        map.putAll(userToGraph);

        return map;
    }


    public List<Group> getGroupList (CohortConceptGraphs graphs){
        Map<String, ConceptGraph> userMap = getUserMap(graphs);

        List<Group> groupings1 = new ArrayList<>();
        Group gr = new Group();

        for(String name: userMap.keySet()){

            Map<String, ConceptGraph> student = new HashMap<>();
            student.put(name, userMap.get(name));
            gr.addMember(student);

        }

        groupings1.add(gr);


        return groupings1;
    }


}
