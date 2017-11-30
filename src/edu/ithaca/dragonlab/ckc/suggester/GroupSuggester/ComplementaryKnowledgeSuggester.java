package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 11/28/2017.
 */
public class ComplementaryKnowledgeSuggester extends Suggester{

    //have to do concept suggester first
    // bucket, concept, bysize

    @Override
    public List<Group> suggestGroup(Group groupSoFar, Group extraMembers) {
        List<Group > actualGroupings = new ArrayList<>();

        Map<String, ConceptGraph> map = groupSoFar.getStudents();

            List<String> studentlist = groupStudents(map);

        boolean isC = false;
        System.out.println(studentlist);
        for(int i=0; i< studentlist.size()-1; i=i+2){
            System.out.println(i + " " + map.get(studentlist.get(i)).getName() + " " + map.get(studentlist.get(i+1)).getName());
            isC = isComp(map, map.get(studentlist.get(i)).getName(), map.get(studentlist.get(i+1)).getName(), groupSoFar);
            if(isC){
                Map<String, ConceptGraph> tempMap = new HashMap<>();
                tempMap.put(map.get(studentlist.get(i)).getName(), map.get(studentlist.get(i)));
                Group gr = new Group(tempMap, "");

                gr.addMember(map.get(studentlist.get(i+1)).getName(), map.get(studentlist.get(i+1))) ;
                actualGroupings.add(gr);

            }else{
                Map<String, ConceptGraph> tempMap = new HashMap<>();
                tempMap.put(map.get(studentlist.get(i)).getName(), map.get(studentlist.get(i)));
                Group gr = new Group(tempMap, "");
                actualGroupings.add(gr);
            }
        }


        return actualGroupings;

    }

    private boolean isComp(Map<String, ConceptGraph> map, String name1, String name2, Group groupSoFar) {

        ConceptGraph graph = new ConceptGraph();

        if(map.get(name1).findNodeById(groupSoFar.getConcept()) != null && map.get(name2).findNodeById(groupSoFar.getConcept()) != null){
            return graph.isComplementary(map.get(name1).findNodeById(groupSoFar.getConcept()), map.get(name2).findNodeById(groupSoFar.getConcept()));

        }else{
            return false;
        }
    }


    private List<String> groupStudents(Map<String, ConceptGraph> map){
        List<String> studentList = new ArrayList<>();
        studentList.addAll(map.keySet());

        return studentList;
    }

}
