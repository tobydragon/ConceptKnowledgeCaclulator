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

            //randomize list, (when it's actually being used)
        boolean isC = false;

        List<String> assignedStudents = new ArrayList<>();
        List<String> questioningStudents = new ArrayList<>();

        for(int i =0; i< studentlist.size(); i++){
            for(int x=0; x< studentlist.size(); x++){

                //if the pairing is not to itself and either of the possibly paired students aren't already paired with someone else
                if((!studentlist.get(i).equals(studentlist.get(x))) && !assignedStudents.contains(studentlist.get(i)) && !assignedStudents.contains(studentlist.get(x))){

                    boolean complementary = isComp(map, studentlist.get(i), studentlist.get(x), groupSoFar);
                    if(complementary ){
                        assignedStudents.add(studentlist.get(i));
                        assignedStudents.add(studentlist.get(x));

                        if(extraMembers.contains(studentlist.get(i))){
                            extraMembers.removeMember(studentlist.get(i));
                        }
                        if(extraMembers.contains(studentlist.get(x))){
                            extraMembers.removeMember(studentlist.get(x));
                        }

                        Map<String, ConceptGraph> tempMap = new HashMap<>();
                        tempMap.put(map.get(studentlist.get(i)).getName(), map.get(studentlist.get(i)));
                        Group gr = new Group(tempMap, " ");
                        gr.addMember(map.get(studentlist.get(x)).getName(), map.get(studentlist.get(x))) ;

                        gr.addRationale(groupSoFar.getRationale() + " Complementary Knowledge");
                        actualGroupings.add(gr);
                    }

                }else{
                    if(!questioningStudents.contains(studentlist.get(i)) && !assignedStudents.contains(studentlist.get(i))){
                        questioningStudents.add(studentlist.get(i));
                    }

                    if(!questioningStudents.contains(studentlist.get(x))&& !assignedStudents.contains(studentlist.get(x))){
                        questioningStudents.add(studentlist.get(x));

                    }
                }
            }
        }

        for(int y =0; y<questioningStudents.size(); y++){
            if(!assignedStudents.contains(questioningStudents.get(y))){
                extraMembers.addMember(questioningStudents.get(y), map.get(questioningStudents.get(y)));
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
