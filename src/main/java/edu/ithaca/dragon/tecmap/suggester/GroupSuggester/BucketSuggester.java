package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class BucketSuggester extends Suggester {

    //a list of list of ranges. Each list (within the list) is a specific range.
    List<List<Integer>> ranges;

    public BucketSuggester(List<List<Integer>> ranges) throws Exception {
        if(ranges.size()<=0){
            throw new Exception();

        }else{

            this.ranges =ranges;
        }
    }

    @Override
    public List<Group> suggestGroup(Group groupSoFar, Group extraMembers) {
        List<Group> actualGroupings = new ArrayList<>();

            Map<String, Double> knowledgeSums = new HashMap<>();

            Map<String, ConceptGraph> group = groupSoFar.getStudents();


            for(String name: group.keySet()){
                ConceptGraph userGraph = group.get(name);
                String subject = "all";
                double sum = calcSum(userGraph,subject);
                knowledgeSums.put(name, (sum/userGraph.getAllNodeIds().size())*100);

            }


            for(List<Integer> r: ranges){
                Map<String, ConceptGraph> map = new HashMap<>();

                for(String student:knowledgeSums.keySet()){

                    if ( knowledgeSums.get(student)>r.get(0) && knowledgeSums.get(student) < r.get(1) ){
                        System.out.println("name "+ student + " knowledge " + knowledgeSums.get(student) +" range " + r.get(0) + " to " + r.get(1));

                        map.put(student,group.get(student));
                    }

                }

                Group newGroup = new Group(map, "");
                newGroup.addRationale(groupSoFar.getRationale() + " ,Bucket: " + r.get(0) + " - " + r.get(1));
                actualGroupings.add(newGroup);

            }

        return actualGroupings;
    }


    /**
     * Calculates the sum of all of the student's knowledge estimates on the whole Concept graph or a specific part of it
     * @param subject 'all' for the entire tree or a Concept node IDs
     * @return double the sum of the estimates
     */
    public double calcSum (ConceptGraph s1, String subject){

        return s1.calcTotalKnowledgeEstimate(subject);

    }


}
