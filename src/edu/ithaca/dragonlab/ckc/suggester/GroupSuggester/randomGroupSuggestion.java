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

        List<String> user = getUsersList(graphs);

        List<String> userListCopy = new ArrayList<>();
        userListCopy.addAll(user);

        List<List<String>> groupingsOfGroups = new ArrayList<>();

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

                groupingsOfGroups.add(group);

                whileNum= whileNum-choice;
            }


        }else if (user.size()<choice && user.size()%choice!=0){
            //there aren't enough students
            //warning. there won't be one full group

            Collections.shuffle(userListCopy);
            List<String> group = new ArrayList<>();
            group.addAll(userListCopy);

            groupingsOfGroups.add(group);

        }else{
            //user.size()%choice!=0 && user.size()>= choice

            //there will be one group that doesn't have a full group

            for(int i = user.size()/choice; i> 0 ; i--){

                List<String> group = new ArrayList<>();
                Collections.shuffle(userListCopy);

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userListCopy.get(choices-1));
                    userListCopy.remove(userListCopy.get(choices-1));
                    choices--;
                }

                groupingsOfGroups.add(group);

            }

            List<String> group = new ArrayList<>();
            group.addAll(userListCopy);

            groupingsOfGroups.add(group);

        }
        return groupingsOfGroups;

    }





}


