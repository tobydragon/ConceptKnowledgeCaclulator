package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 10/25/2017.
 */
public class Group {

    Map<String, ConceptGraph> studentGraphMap;
    String id;

    public Group(Map<String, ConceptGraph> map, String id){
        this.studentGraphMap = map;
        this.id = id;

    }

    public Group(){
        this.studentGraphMap = new HashMap<>();
        this.id = "";

    }

    public Map<String, ConceptGraph> getStudents(){
        return studentGraphMap;
    }

    public int getSize(){
        return studentGraphMap.size();
    }


    public void combinegroups(Group addedMem){
        this.studentGraphMap.putAll(addedMem.getStudents());

    }

    public void addMembers(Map<String, ConceptGraph> addStudent){
        this.studentGraphMap.putAll(addStudent);
    }

    public void addMembers(String st, ConceptGraph graph){
        this.studentGraphMap.put(st,graph);
    }


    public void removeMember(Map<String,ConceptGraph> remStudent){
        this.studentGraphMap.remove(remStudent);
    }

    public void removeMember(String remStudent){
        this.studentGraphMap.remove(remStudent);
    }


    public String toString (){
        String list = "";

        for(String name: studentGraphMap.keySet()){
            list += name ;
        }

        return list;


    }

}
