package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggester {
    int size;
    Suggester  sug;

    /**
     * creates groups based off of the list of groups.These are the final groups
     * @param groupings
     * @param extraMembers
     * @return
     */
    public List<Group > createGroups (List<Group> groupings, Group extraMembers){
        List<Group> actualGroupings = new ArrayList<>();

        for (Group groupSoFar : groupings) {

            if (extraMembers.getSize() > 0) {
                groupSoFar.combinegroups(extraMembers);
            }

            List<Group> temp = sug.suggestGroup(groupSoFar, extraMembers);
            actualGroupings.addAll(temp);
        }


        //if there are left over students
        //todo fix this so that it can work with any amount of student group sizes
        if (extraMembers.getSize() > 0) {
            if (extraMembers.getSize() > size) {

                Suggester two = new BySizeSuggester(size, false );
                List<Group> temp = two.suggestGroup(extraMembers, new Group());

                List<String> assignedStudents = new ArrayList<>();
                for(Group gr: temp){
                    assignedStudents.addAll(gr.getStudentNames());
                }

                for(String name: assignedStudents){
                    if(extraMembers.contains(name)){
                        extraMembers.removeMember(name);
                    }
                }

                actualGroupings.addAll(temp);

                Group tempGr = actualGroupings.get(actualGroupings.size() - 1);
                tempGr.addRationale(" ,Extra Members");
                tempGr.addMember(extraMembers);

            } else if (extraMembers.getSize() == size / 2) {

                Group tempGr = actualGroupings.get(actualGroupings.size() - 1);
                tempGr.addRationale( " ,Extra Members");
                tempGr.addMember(extraMembers);

            } else {
                actualGroupings.add(extraMembers);

            }
        }

        return actualGroupings;
    }


    /**
     * loops through all of the suggesters and calls a function that will group the students based on that suggester. That list gets updated as the suggester list is iterated through
     * @param groupings
     * @param groupSize
     * @param suggesterList
     * @return
     */
    public List<Group>  grouping(List<Group> groupings, int groupSize, List<Suggester> suggesterList){
        size = groupSize;
        List<Group> actualGroupings = new ArrayList<>();
        Group extraMembers = new Group(new HashMap<>(), " ");
        actualGroupings.addAll(groupings);

        for(Suggester sugType: suggesterList) {
            sug = sugType;
            actualGroupings = createGroups(actualGroupings, extraMembers);
        }

        return actualGroupings;
    }


    private static Map<String, ConceptGraph> getUserMap(CohortConceptGraphs graphs){
        Map<String, ConceptGraph> map = new HashMap<>();

        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();

        map.putAll(userToGraph);

        return map;
    }


    public static List<Group> getGroupList (CohortConceptGraphs graphs){
        Map<String, ConceptGraph> userMap = getUserMap(graphs);

        List<Group> groupings1 = new ArrayList<>();
        Group gr = new Group();

        for(String name: userMap.keySet()){

            Map<String, ConceptGraph> student = new HashMap<>();
            student.put(name, userMap.get(name));
            gr.addMember(student);

        }

        groupings1.add(gr);

        return groupings1;
    }


}
