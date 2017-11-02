package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class BySize extends Suggester {
    int groupSize;
    boolean random;

    public BySize(int size, boolean israndom){
        //group can't be bigger than class size
        groupSize = size;
        random = israndom;

    }


    @Override
    public List<Group> suggestGroup(Group groupSoFar, Group extraMembers){
        List<Group> actualGroupings = new ArrayList<>();


        Map<String, ConceptGraph> group = groupSoFar.getStudents();
        Map<String, ConceptGraph> exMem = extraMembers.getStudents();


        List<String> copyGroups = new ArrayList<>();
        copyGroups.addAll(group.keySet());

            if (group.size() > groupSize) {

                for (int i = 0; i < group.size() / groupSize; i++) {

                    Group groups = new Group();

                    int itr = 0;
                    while (itr < groupSize) {
                        if(random) {
                            Collections.shuffle(copyGroups);
                        }
                        String user = copyGroups.get(0);

                        if (!exMem.containsKey(user)) {
                            groups.addMember(user, group.get(user));
                        } else {
                            groups.addMember(user, exMem.get(user));
                            extraMembers.removeMember(user);
                        }
                        copyGroups.remove(user);


                        itr++;
                    }


                    actualGroupings.add(groups);
                }

                if (copyGroups.size() > 0) {
                    for (int i = 0; i < copyGroups.size(); i++) {
                        extraMembers.addMember(copyGroups.get(i), group.get(copyGroups.get(i)));
                    }
                }


            }else{

                List<String> copyGroups2 = new ArrayList<>();
                copyGroups2.addAll(group.keySet());
                if(random) {
                    Collections.shuffle(copyGroups);
                }

                Group groups2 = new Group();

                for(String name: copyGroups2){
                    groups2.addMember(name, group.get(name));
                }
                actualGroupings.add(groups2);

            }


        return actualGroupings;

    }

}