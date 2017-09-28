package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class random extends GroupSuggester{
    int groupSize;

    public random(int size){
        //group can't be bigger than class size
        groupSize = size;

    }


    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings){
        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();

        Map<String, ConceptGraph> extraMembers = new HashMap<>();

        for(Map<String, ConceptGraph> groupSoFar: groupings) {

            List<String> copyGroups = new ArrayList<>();
            copyGroups.addAll(groupSoFar.keySet());
            copyGroups.addAll(extraMembers.keySet());

            if (groupSoFar.size() > groupSize) {


                for (int i = 0; i < groupSoFar.size() / groupSize; i++) {

                    Map<String, ConceptGraph> groups = new HashMap<>();

                    int itr = 0;
                    while (itr < groupSize) {
                        Collections.shuffle(copyGroups);
                        String user = copyGroups.get(0);

                        if (groupSoFar.containsKey(user)) {
                            groups.put(user, groupSoFar.get(user));
                        } else {
                            groups.put(user, extraMembers.get(user));
                            extraMembers.remove(user);
                        }
                        copyGroups.remove(user);

                        itr++;
                    }
                    actualGroupings.add(groups);

                }

                if (copyGroups.size() > 0) {
                    for (int i = 0; i < copyGroups.size(); i++) {
                        extraMembers.put(copyGroups.get(i), groupSoFar.get(copyGroups.get(i)));
                    }
                }


            }else{

                List<String> copyGroups2 = new ArrayList<>();
                copyGroups2.addAll(groupSoFar.keySet());
                Collections.shuffle(copyGroups);

                Map<String, ConceptGraph> groups2 = new HashMap<>();

                for(String name: copyGroups2){
                    groups2.put(name, groupSoFar.get(name));
                }
                actualGroupings.add(groups2);


            }


            }

            if (extraMembers.size() > 0) {

                if ((groupSize + extraMembers.size()) > groupSize + 1) {

//            if(extraMembers.size()> groupSize-1 && ((groupSize+extraMembers.size())==groupSize+2)){
                    //if the extra member amount is less 1 less than the size of all the other groups
                    Map<String, ConceptGraph> groups = new HashMap<>();
                    groups.putAll(extraMembers);
                    actualGroupings.add(groups);

                } else {
                    // if the  extra amount of members is a less than 1 less than a normal group size
                    int itr = 0;
                    for (String mem : extraMembers.keySet()) {
                        Map<String, ConceptGraph> placeHolder = actualGroupings.get(itr);
                        placeHolder.put(mem, extraMembers.get(mem));
                    }
                }
            }





        return actualGroupings;

    }


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }
}