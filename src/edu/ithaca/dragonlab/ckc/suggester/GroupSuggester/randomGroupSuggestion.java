package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;
/**
 * Created by mkimmitchell on 7/31/17.
 */
public class randomGroupSuggestion extends GroupSuggester{


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {

        List<String> user = getUsers(graphs);

        List<String> userListCopy = new ArrayList<>();
        userListCopy.addAll(user);

        List<List<String>> groupings = new ArrayList<>();

        if(user.size()%choice==0 && user.size()>= choice){

            int whileNum = user.size();
            while(whileNum>0) {

                List<String> group = new ArrayList<>();
                Collections.shuffle(userListCopy);

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userListCopy.get(choices-1));
                    userListCopy.remove(userListCopy.get(choices-1));
                    choices--;
                }

                groupings.add(group);

                whileNum= whileNum-choice;
            }

            return groupings;

        }else if (user.size()<choice && user.size()%choice!=0){
            //there aren't enough students
            List<String> group = new ArrayList<>();
            group.addAll(user);

            groupings.add(group);

            return groupings;
        }else{

            //user.size()%choice!=0 && user.size()>= choice


            for(int i = user.size()%choice; i> 0 ; i--){
                List<String> group = new ArrayList<>();
                Collections.shuffle(userListCopy);

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userListCopy.get(choices-1));
                    userListCopy.remove(userListCopy.get(choices-1));
                    choices--;
                }

                groupings.add(group);

            }

            List<String> group = new ArrayList<>();
            group.addAll(userListCopy);

            groupings.add(group);

            return groupings;
//            System.out.println(userListCopy);


        }
    }





}


