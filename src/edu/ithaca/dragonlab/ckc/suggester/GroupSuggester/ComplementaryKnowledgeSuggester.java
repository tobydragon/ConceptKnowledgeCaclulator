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
        List<String> studentlist = getCompleteStudentList(map);
        List<String> assignedStudents = new ArrayList<>();
        List<String> questioningStudents = new ArrayList<>();

        for(int i =0; i< studentlist.size(); i++){
            for(int x=0; x< studentlist.size(); x++){

                //if the pairing is not to itself and either of the possibly paired students aren't already paired with someone else
                if((!studentlist.get(i).equals(studentlist.get(x))) && !assignedStudents.contains(studentlist.get(i)) && !assignedStudents.contains(studentlist.get(x))){

                    boolean complementary = false;
                    try {
                        complementary = isComplementary(map, studentlist.get(i), studentlist.get(x), groupSoFar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

                        gr.addRationale(groupSoFar.getRationale() + " ,Complementary Knowledge");
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

    /**
     * This returns if the two students knowledge is complementary. This means that they both have the same graph and that they both have the same
     * knowledge estimate on the same parent concept, but different knowledge estimates in the children.
     * @param map The String to ConceptGraph Map
     * @param name1 Student 1
     * @param name2 Student 2
     * @param groupSoFar The current group of students
     * @return
     */
    private boolean isComplementary(Map<String, ConceptGraph> map, String name1, String name2, Group groupSoFar) throws Exception {
        ConceptGraph graph = new ConceptGraph();

        // if the current first student's concept isn't equal to "no suggestions" and the current second student isn't equal to "no suggestions"
        //"no suggestions" as a the concept resource in the current group will return null because there is never a concept in the graph called "no suggestions"
        if(map.get(name1).findNodeById(groupSoFar.getConcept()) != null && map.get(name2).findNodeById(groupSoFar.getConcept()) != null){
            return graph.isComplementary(map.get(name1).findNodeById(groupSoFar.getConcept()), map.get(name2).findNodeById(groupSoFar.getConcept()));
        }else{
            return false;
        }
    }


    /**
     * Returns a list of all the students. This is to be used to iterate and modify all the students in the class
     * @param map
     * @return list of strings which are the student names
     */
    private List<String> getCompleteStudentList(Map<String, ConceptGraph> map){
        List<String> studentList = new ArrayList<>();
        studentList.addAll(map.keySet());

        return studentList;
    }

}
