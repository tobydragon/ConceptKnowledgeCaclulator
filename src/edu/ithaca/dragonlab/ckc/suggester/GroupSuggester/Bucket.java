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

    List<Integer> ranges;

    public Bucket(List<Integer> ranges) throws Exception {
        if(ranges.size()<0){
            throw new Exception();
        }else{
            this.ranges = ranges;
            this.ranges.add(0,0);
            this.ranges.add(100);

        }
    }

    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings) {
        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();


        for(Map<String, ConceptGraph> groupSoFar: groupings){
            List<Map<String, ConceptGraph>> buckets = new ArrayList<>();


            Map<String, ConceptGraph> bad = new HashMap<>();
            Map<String, ConceptGraph> okay = new HashMap<>();
            Map<String, ConceptGraph> good = new HashMap<>();

            Map<String, Double> knowledgeSums = new HashMap<>();
            for(String name: groupSoFar.keySet()){
                ConceptGraph userGraph = groupSoFar.get(name);
                String subject = "all";
                double sum = calcSum(userGraph,subject);
                knowledgeSums.put(name, (sum/userGraph.getAllNodeIds().size())*100);
            }

//            int r =0;
//            for(Integer range: ranges){
//
//                for(String student:knowledgeSums.keySet()){
//
//                    if(ranges.get(r+1)!=100){
//                        if(knowledgeSums.get(student)>ranges.get(r) && ){
//
//                        }
//
//                    }
//
//
//
//
//                }
//
//            }

            for(String student: knowledgeSums.keySet()){

                if(knowledgeSums.get(student)>ranges.get(0) && knowledgeSums.get(student)< ranges.get(1)){
                    bad.put(student,groupSoFar.get(student));
                } else if((knowledgeSums.get(student)> ranges.get(1)) && (knowledgeSums.get(student) < ranges.get(2))){
                    okay.put(student,groupSoFar.get(student));
                }else {
                    good.put(student,groupSoFar.get(student));
                }
            }

            actualGroupings.add(bad);
            actualGroupings.add(okay);
            actualGroupings.add(good);
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
