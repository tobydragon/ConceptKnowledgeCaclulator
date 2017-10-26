package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class BySize{
    int groupSize;

    public BySize(int size){
        //group can't be bigger than class size
        groupSize = size;

    }


    public List<Map<String, ConceptGraph>> suggestGroup(Group groupSoFar, Group extraMembers){
        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();

        Map<String, ConceptGraph> group = groupSoFar.getStudents();
        Map<String, ConceptGraph> exMem = extraMembers.getStudents();


        List<String> copyGroups = new ArrayList<>();
            copyGroups.addAll(group.keySet());

            if (group.size() > groupSize) {

                for (int i = 0; i < group.size() / groupSize; i++) {

                    Group groups = new Group();

                    int itr = 0;
                    while (itr < groupSize) {
                        Collections.shuffle(copyGroups);
                        String user = copyGroups.get(0);
//                        Map<String, ConceptGraph> st = new HashMap<>();
//                        st.put(user, )

                        if (group.containsKey(user)) {
                            groups.addMembers(user, group.get(user));
//                            groups.put(user, group.get(user));
                        } else {
                            groups.addMembers(user, exMem.get(user));
//                            groups.put(user, exMem.get(user));
                            extraMembers.removeMember(user);
                        }
                        copyGroups.remove(user);

                        itr++;
                    }
                    actualGroupings.add(groups);

                }

                if (copyGroups.size() > 0) {
                    extraMembers.addMembers(copyGroups);
//                    for (int i = 0; i < copyGroups.size(); i++) {
//                        extraMembers.addMembers();
//                                put(copyGroups.get(i), group.get(copyGroups.get(i)));
//                    }
                }


            }else{

                List<String> copyGroups2 = new ArrayList<>();
                copyGroups2.addAll(group.keySet());
                Collections.shuffle(copyGroups);

                Map<String, ConceptGraph> groups2 = new HashMap<>();

                for(String name: copyGroups2){
                    groups2.put(name, group.get(name));
                }
                actualGroupings.add(groups2);


            }


        return actualGroupings;

    }

}