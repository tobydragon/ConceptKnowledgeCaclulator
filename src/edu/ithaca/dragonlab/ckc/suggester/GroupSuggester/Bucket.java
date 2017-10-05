package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class Bucket extends GroupSuggester{

    //a list of list of ranges. Each list (within the list) is a specific range.
    List<List<Integer>> ranges;

    public Bucket(List<List<Integer>> ranges) throws Exception {
        if(ranges.size()<0){
            throw new Exception();

        }else{

            this.ranges =ranges;
        }
    }

    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings) {
        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();

        for(Map<String, ConceptGraph> groupSoFar: groupings){

            Map<String, Double> knowledgeSums = new HashMap<>();
            for(String name: groupSoFar.keySet()){
                ConceptGraph userGraph = groupSoFar.get(name);
                String subject = "all";
                double sum = calcSum(userGraph,subject);
                knowledgeSums.put(name, (sum/userGraph.getAllNodeIds().size())*100);
            }

            for(List<Integer> r: ranges){
                Map<String, ConceptGraph> map = new HashMap<>();

                for(String student:knowledgeSums.keySet()){

                    if ( knowledgeSums.get(student)>r.get(0) && knowledgeSums.get(student) < r.get(1) ){
                        map.put(student,groupSoFar.get(student));
                    }

                }

                actualGroupings.add(map);
            }

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



    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }

}
