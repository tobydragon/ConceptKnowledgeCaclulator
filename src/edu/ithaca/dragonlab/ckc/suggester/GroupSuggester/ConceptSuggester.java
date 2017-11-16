package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/28/2017.
 */
public class ConceptSuggester extends Suggester {

    @Override
    public List<Group> suggestGroup(Group groupSoFar, Group extraMembers){
        List<Group> actualGroupings = new ArrayList<>();
        Map<String, Group> concept2StudentList= createConceptMap(groupSoFar);

//        for(String concept: concept2StudentList.keySet()){
//            Group gr = concept2StudentList.get(concept);
//            System.out.println("conept " + concept + " " + gr);
//        }

        for(String conceptName: concept2StudentList.keySet()){

            Group foundGroup = concept2StudentList.get(conceptName);
            actualGroupings.add(foundGroup);

        }

        return actualGroupings;

    }

    /**
     * creates a map of suggested concept ID to group of students who need to work on the concept (key)
     * @param groupSoFar
     * @return map of suggested concept ID to group of students
     */
    public static  Map<String, Group>  createConceptMap(Group groupSoFar){
        Map<String, Group> conceptMap= new HashMap<>();
            //concept, list of maps of users and their concept graphs

        Map<String, ConceptGraph> totalStudents = new HashMap<>();
        totalStudents = groupSoFar.getStudents();

        for(String name: totalStudents.keySet()){
            List<String> conceptsToWorkOn = getConcepts(totalStudents.get(name));
            String firstConcept = "no suggestions";
            if(conceptsToWorkOn.size()>0){
                firstConcept = conceptsToWorkOn.get(0);
            }

            if (conceptMap.containsKey(firstConcept)){

                //the group of students associated to the already found concept
                Group foundGroup = conceptMap.get(firstConcept);
                foundGroup.addMember(name, totalStudents.get(name));

            }else{

                Map<String, ConceptGraph> addedGroup = new HashMap<>();
                addedGroup.put(name, totalStudents.get(name));
                Group tempGroup = new Group(addedGroup, name);

                conceptMap.put(firstConcept, tempGroup);
            }
        }


        return conceptMap;
    }


    /**
     * Gets the list of concept titles that the student should work on. If there are no concepts that need to be worked on then the list is empty
     * @param graph
     * @return list of concept names
     */
    public static List<String> getConcepts(ConceptGraph graph){
        List<String> stringConcepts = new ArrayList<>();

        List<ConceptNode> nodes = LearningObjectSuggester.conceptsToWorkOn(graph);
        for(ConceptNode node: nodes){
            stringConcepts.add(node.getID());
        }

        return stringConcepts;
        }


}
