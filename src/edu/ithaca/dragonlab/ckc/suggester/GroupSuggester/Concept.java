package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/28/2017.
 */
public abstract class Concept extends Suggester {
    int groupSize;

    public Concept(int size){
        groupSize=size;
    }

    @Override
    public List<Group> suggestGroup(Group groupings, Group extraMembers) {

//        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();
//
//
////        Map<String,ConceptGraph> extraMembers = new HashMap<>();
//
//
////        for (Map<String, ConceptGraph> groupSoFar : groupings) {
//
//                                                                //concept2studentList
//            Map<String, List<Map<String,ConceptGraph>>> conceptMap= createConceptMap(groupSoFar, extraMembers);
//
//
//            for(String conceptName: conceptMap.keySet()) {
//                System.out.println(conceptName);
//
//                //the list of maps of groups of students and their graphs
//                List<Map<String, ConceptGraph>> mapGroups = conceptMap.get(conceptName);
//
//                if(mapGroups.size()== groupSize){
//                    //mapGroups.size() % groupSize == 0
////                    List<Map<String, ConceptGraph>> groupCopy = conceptMap.get(conceptName);
////                    groupCopy.addAll(mapGroups);
//
//                    int groupAmount = mapGroups.size()%groupSize;
//                    //while (groupAmount>0){
//                    Map<String, ConceptGraph> tempTotalGroups = new HashMap<>();
//
//                    for(Map<String,ConceptGraph> singleGroup: mapGroups){
//
//                        for(String name: singleGroup.keySet()){
//                            tempTotalGroups.put(name,singleGroup.get(name) );
//
//                            if(extraMembers.containsKey(name)){
//                                extraMembers.remove(name);
//                            }
//                        }
//                    }
//
//                    actualGroupings.add(tempTotalGroups);
//
//                 //} for while loop
//
//                }else if(mapGroups.size()>groupSize && mapGroups.size()%groupSize != 0){
//                    //the size is larger than the groupsize, so there can be mutiple groups, but there will be some left out
//
//                }else{
//                    //the size is smaller than the groupSize thus all of them are put in the extraMember list.
//
//                    Map<String, ConceptGraph> tempTotalGroups = new HashMap<>();
//
//                    for(Map<String,ConceptGraph> map7: mapGroups){
//
//                        for(String name: map7.keySet()){
//                            tempTotalGroups.put(name,map7.get(name) );
//                        }
//                    }
//
//                    extraMembers.putAll(tempTotalGroups);
//
//                }
//
//
//                if(extraMembers.size()>groupSize){
//                    //do something about it
//                }else{
//                    //add all of the students
//                }
//
//
//            }
//
//
////        }
//        return actualGroupings;

        return null;
    }

    public static Map<String, List<Map<String,ConceptGraph>>> createConceptMap(Map<String, ConceptGraph> groupSoFar, Map<String, ConceptGraph> extraMembers){
        Map<String, List<Map<String,ConceptGraph>>> conceptMap= new HashMap<>();
            //concept, list of maps of users and their concept graphs

        Map<String, ConceptGraph> totalStudents = new HashMap<>();
        totalStudents.putAll(groupSoFar);
        totalStudents.putAll(extraMembers);


        for(String name: totalStudents.keySet()){
            List<String> conceptsToWorkOn = getPriority(totalStudents.get(name));
            System.out.println("name " + name + " " + conceptsToWorkOn);
            String firstConcept = "no suggestions";
            if(conceptsToWorkOn.size()>0){
                firstConcept = conceptsToWorkOn.get(0);

            }

            if (conceptMap.containsKey(firstConcept)){
                List<Map<String, ConceptGraph>> tempValue = conceptMap.get(firstConcept);

                Map<String, ConceptGraph> additionalGroup = new HashMap<>();
                additionalGroup.put(name, totalStudents.get(name));
                tempValue.add(additionalGroup);

            }else{
                List<Map<String, ConceptGraph>> tempValue = new ArrayList<>();

                Map<String, ConceptGraph> additionalGroup = new HashMap<>();
                additionalGroup.put(name, totalStudents.get(name));
                tempValue.add(additionalGroup);

                conceptMap.put(firstConcept, tempValue);


            }
        }


        return conceptMap;
    }




    public static List<String> getPriority(ConceptGraph graph){
        List<String> stringConcepts = new ArrayList<>();

        List<ConceptNode> nodes = LearningObjectSuggester.conceptsToWorkOn(graph);
        for(ConceptNode node: nodes){
            stringConcepts.add(node.getID());
        }

        return stringConcepts;
        }



}
