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

    public void addMember(Group gr){
        Map<String, ConceptGraph> list = gr.getStudents();
        this.studentGraphMap.putAll(list);

    }

    public void addMember(Map<String, ConceptGraph> addStudent){
        this.studentGraphMap.putAll(addStudent);
    }

    public void addMember(String st, ConceptGraph graph){
        this.studentGraphMap.put(st,graph);
    }

    public void removeMember(String remStudent){
        this.studentGraphMap.remove(remStudent);
    }



    ///////testing

    public boolean contains(String name){
        if(this.studentGraphMap.containsKey(name)){
            return true;
        }else{
            return false;
        }

    }

    public List<String> getStudentNames(){
        Map<String, ConceptGraph> graphMap = this.studentGraphMap;

        List<String> nameList = new ArrayList<>();
        nameList.addAll(graphMap.keySet());

        return nameList;
    }

    public ConceptGraph getGraph (String name){
        return this.studentGraphMap.get(name);

    }

    public String toString (){

        String list = "";

        for(String name: studentGraphMap.keySet()){
            list += name + " " ;
        }
        return list;


    }

}
