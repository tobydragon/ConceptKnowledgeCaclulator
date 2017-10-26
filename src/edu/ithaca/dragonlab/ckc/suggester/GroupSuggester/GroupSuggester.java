//package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;
//
//import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
//import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
//
//import java.util.*;
///**
// * Created by mkimmitchell on 7/31/17.
// */
//public class GroupSuggester {
//    int size;
//    Bucket sug;
//
//    public GroupSuggester(List<Map<String, ConceptGraph>> groupings, int groupSize, int groupingType){
////        grouping(groupings, groupSize, groupingType);
//    }
//
//
//    public List<Map<String, ConceptGraph>>  grouping(List<Map<String, ConceptGraph>> groupings, int groupSize, int groupingType){
//        size = groupSize;
//
//        if(groupingType == 0) {
//            //bucket
//            try {
//                Bucket sug = new Bucket(new ArrayList<>());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
////        }else if(groupingType ==1){
////            // concept
////
////            sug = new Concept(size);
////        }else if(groupingType == 2){
////            //by size
////
////            sug = new BySize(size);
////        }
//        //else
//        // jigsaw
//
//        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();
//
//        Map<String, ConceptGraph> extraMembers = new HashMap<>();
//
//
//        for(Map<String, ConceptGraph> groupSoFar: groupings) {
//
//            if(extraMembers.size()>0){
//                groupSoFar.putAll(extraMembers);
//            }
///*
//        if (extraMembers.size() > 0) {
//            System.out.println(extraMembers.keySet());
//
//            if ((groupSize + extraMembers.size()) > groupSize + 1) {
//
////            if(extraMembers.size()> groupSize-1 && ((groupSize+extraMembers.size())==groupSize+2)){
//                //if the extra member amount is less 1 less than the size of all the other groups
//                Map<String, ConceptGraph> groups = new HashMap<>();
//                groups.putAll(extraMembers);
//                actualGroupings.add(groups);
//
//            } else {
//                // if the  extra amount of members is a less than 1 less than a normal group size
//                int itr = 0;
//                for (String mem : extraMembers.keySet()) {
//                    Map<String, ConceptGraph> placeHolder = actualGroupings.get(itr);
//                    placeHolder.put(mem, extraMembers.get(mem));
//                }
//            }
//        }
// */
//            List<Map<String, ConceptGraph>> temp  = sug.suggestGroup(groupSoFar, extraMembers);
//            actualGroupings.addAll(temp);
//
//
//        }
//
//
//
//        return actualGroupings;
//        }
//
//
//
//    public List<String> getUsersList(CohortConceptGraphs graphs) {
//        List<String> userList = new ArrayList<>();
//
//        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();
//        userList.addAll(userToGraph.keySet());
//
//        return userList;
//    }
//
//    public static Map<String, ConceptGraph> getUserMap(CohortConceptGraphs graphs){
//        Map<String, ConceptGraph> map = new HashMap<>();
//
//        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();
//
//        map.putAll(userToGraph);
//
//        return map;
//    }
//}


package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;
/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggester {
    int size;
    Bucket sug;

    public GroupSuggester(List<Group> groupings, int groupSize, int groupingType){
//        grouping(groupings, groupSize, groupingType);
    }


    public List<Group>  grouping(List<Group> groupings, int groupSize, int groupingType){
        size = groupSize;

        if(groupingType == 0) {
            //bucket
            try {
                Bucket sug = new Bucket(new ArrayList<>());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        }else if(groupingType ==1){
//            // concept
//
//            sug = new Concept(size);
//        }else if(groupingType == 2){
//            //by size
//
//            sug = new BySize(size);
//        }
        //else
        // jigsaw

        List<Group> actualGroupings = new ArrayList<>();

        Group extraMembers = new Group(new HashMap<>(), " ");


        for(Group groupSoFar: groupings) {

            if(extraMembers.getSize()>0){
                groupSoFar.combinegroups(extraMembers);
            }


            List<Group> temp  = sug.suggestGroup(groupSoFar, extraMembers);

//            actualGroupings.add()

            actualGroupings.addAll(temp);
        }

        return actualGroupings;
    }



    public List<String> getUsersList(CohortConceptGraphs graphs) {
        List<String> userList = new ArrayList<>();

        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();
        userList.addAll(userToGraph.keySet());

        return userList;
    }

    public static Map<String, ConceptGraph> getUserMap(CohortConceptGraphs graphs){
        Map<String, ConceptGraph> map = new HashMap<>();

        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();

        map.putAll(userToGraph);

        return map;
    }
}
