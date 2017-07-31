package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;
/**
 * Created by mkimmitchell on 7/31/17.
 */
public class randomGroupSuggestion extends GroupSuggester{


    @Override
    public void suggestGroup(CohortConceptGraphs graphs, int choice) {

        Random rand = new Random();

        List<String> user = getUsers(graphs);

        if(user.size()%choice==0){

            HashMap<Integer, String> userMap = new HashMap<>();
            Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();

            int num =0;
            for(String currentUser: userToGraph.keySet()){
                userMap.put(num, currentUser);
                num++;
            }


            List<List<String>> groupings = new ArrayList<>();


            int whileNum = user.size()/choice;
            while(whileNum>0) {

                List<String> group = new ArrayList<>();

                int student = rand.nextInt(user.size());
                int student1 = rand.nextInt(user.size());

                System.out.println(student + " " + student1);

                groupings.add(group);
                whileNum--;

            }

            for(List<String> lists : groupings){
                System.out.println(lists);
            }


        }else{
            System.out.println("not even number of stdeutnst");
        }





    }





}


