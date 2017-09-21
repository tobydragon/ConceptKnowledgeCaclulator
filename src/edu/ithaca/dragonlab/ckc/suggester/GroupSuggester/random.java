package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class random extends GroupSuggester{

    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings){
        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();

        int initalGroupSize = groupings.get(0).size();
        int stopItr = initalGroupSize/2;
        int itr = 0;

        List<String> userList = new ArrayList<>();
        userList.addAll(groupings.get(0).keySet());

        while(itr != stopItr){
            Map<String, ConceptGraph> group = new HashMap<>();

            Collections.shuffle(userList);

            int choices = 2;
            while (choices>0){
                System.out.println(choices);
                //change from 0
                group.put(userList.get(choices-1), groupings.get(0).get(userList.get(choices-1)));

                userList.remove(userList.get(choices-1));
                choices--;
            }
            actualGroupings.add(group);

            itr++;
        }

        if(groupings.get(0).size()%2!=0 && actualGroupings.size()>0){
            //if the group can't be evenly be put into 2
            for(int i=0; i<userList.size(); i++){
                //change 0
                Map<String, ConceptGraph> tempMap = actualGroupings.get(0);
                tempMap.put(userList.get(i), groupings.get(0).get(userList.get(i)));

            }

        }else{
            Map<String, ConceptGraph> group = new HashMap<>();
            for(int x=0; x<userList.size(); x++){
                //change 0
                group.put(userList.get(x), groupings.get(0).get(userList.get(x)));

            }
            actualGroupings.add(group);
        }

//                    for(Map<String,ConceptGraph> map: actualGroupings){
//                for(String name: map.keySet()){
//                    System.out.println(name);
//                }
//            }

        return actualGroupings;

    }


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }
}