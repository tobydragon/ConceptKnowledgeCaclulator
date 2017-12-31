package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 10/25/2017.
 */
public class Group {

    Map<String, ConceptGraph> studentGraphMap;
    String id;
    String concept;
    String rationale;
    //rationale
    //resource

    public Group(Map<String, ConceptGraph> map, String id) {
        this.studentGraphMap = map;
        this.id = id;
        this.rationale= "";


    }

    public Group(Map<String, ConceptGraph> map, String id, String conceptID){
        this.studentGraphMap= map;
        this.id = id;
        this.concept = conceptID;
        this.rationale = "";
    }

    public Group(){
        this.studentGraphMap = new HashMap<>();
        this.id = "";
        this.concept = "";
        this.rationale = "";
    }

    public String getConcept(){
        return this.concept;
    }

    public void setConcept(String conceptString){

        this.concept = conceptString;
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

    public void addRationale(String rationale){
        this.rationale += (" " + rationale);
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

    public String getRationale(){
        return this.rationale;
    }

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

    /**
     * to print out the student group list and at the end the list of rationale
     * @param type
     * @return
     */
    public String toString(int type){
        String list = "";
        for(String name: studentGraphMap.keySet()){
            list += name + " " ;
        }
        list += " rational:" + this.rationale;
        return list;
    }


    public String toString (){
        String list = "";

        for(String name: studentGraphMap.keySet()){
            list += name + " " ;
        }
        return list;


    }

}
