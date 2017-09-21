package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;
/**
 * Created by mkimmitchell on 7/31/17.
 */
public class RandomGroupSuggester extends GroupSuggester{


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        /*

         */

        List<String> user = getUsersList(graphs);

        List<String> userListCopy = new ArrayList<>();
        userListCopy.addAll(user);

        List<List<String>> groupingsOfGroups = new ArrayList<>();

        /*
        The number of student will evenly divide into the group size
        while there are users unpaired
        shuffle the copy of the userList, get the first users in the shuffled list, choice number of times
        put the users in a sublist called group
        remove users from copy of user list
        put the group in the master list called groupings

         */
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


            /*
            there aren't as many students as there is the choice size
            shuffle the user list, add them to the sublist, and add sublist to main list
             */
        }else if (user.size()<choice && user.size()%choice!=0){
            //there aren't enough students
            //warning. there won't be one full group

            Collections.shuffle(userListCopy);
            List<String> group = new ArrayList<>();
            group.addAll(userListCopy);

            groupingsOfGroups.add(group);

        }else{
            /*
            if there are most students than choice size, but not all of them will fit evenly into a group (there will be a group that's not completly filled up)

            the amount of full groups that can be created = userList.size/choice
            create for loop with the amount of full groups
            shuffle the copy of the userlist, get the first users in the shuffled list, choice number of times
            remove from copy of userlist

            create subgroup, add the users, add subgroup to master list called groupings

            for left over students in userList, add in subgroup, add subgroup to master list
             */


            for(int i = user.size()/choice; i> 0 ; i--){

                List<String> group = new ArrayList<>();
                Collections.shuffle(userListCopy);

                int choices = choice;
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

    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings) {
        return null;
    }


}


