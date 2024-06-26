package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class BySizeSuggester extends Suggester {
    int groupSize;
    boolean random;

    public BySizeSuggester(int size, boolean isRandom){
        //group can't be bigger than class size
        groupSize = size;
        random = isRandom;

    }



    @Override
    public List<Group> suggestGroup(Group groupSoFar, Group extraMembers){
        List<Group> actualGroupings = new ArrayList<>();


        Map<String, ConceptGraph> group = groupSoFar.getStudentGraphMap();
        Map<String, ConceptGraph> exMem = extraMembers.getStudentGraphMap();


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

                    if(random){
                        groups.addRationale(groupSoFar.getRationale()+ " , Random");

                    }
                    if(!random){
                        groups.addRationale(groupSoFar.getRationale()+ " ,By Size: " + groupSize);
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

                Group group2 = new Group();

                for(String name: copyGroups2){
                    group2.addMember(name, group.get(name));

                }

                actualGroupings.add(group2);
                if(random){
                    group2.addRationale(groupSoFar.getRationale()+ " , Random");

                }
                if(!random){
                    group2.addRationale(groupSoFar.getRationale()+ " ,By Size: "+ groupSize);
                }
            }


        return actualGroupings;

    }

}