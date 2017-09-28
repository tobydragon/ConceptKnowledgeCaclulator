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
public class bucket extends GroupSuggester{

    List<Integer> ranges;

    public bucket(List<Integer> ranges) throws Exception {
        if(ranges.size()>2){
            throw new Exception();
        }else{
            this.ranges = ranges;

        }
    }

    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings) {
        List<Map<String, ConceptGraph>> actualGroupings = new ArrayList<>();

        Map<String, ConceptGraph> bad = new HashMap<>();
        Map<String, ConceptGraph> okay = new HashMap<>();
        Map<String, ConceptGraph> good = new HashMap<>();


        for(Map<String, ConceptGraph> groupSoFar: groupings){

            Map<String, Double> knowledgeSums = new HashMap<>();
            for(String name: groupSoFar.keySet()){
                ConceptGraph userGraph = groupSoFar.get(name);
                String subject = "all";
                double sum = calcSum(userGraph,subject);
                knowledgeSums.put(name, (sum/userGraph.getAllNodeIds().size())*100);
            }


            for(String student: knowledgeSums.keySet()){
                if(knowledgeSums.get(student)<ranges.get(0)){
                    bad.put(student,groupSoFar.get(student));
                } else if((knowledgeSums.get(student)> ranges.get(0)) && (knowledgeSums.get(student) < ranges.get(1))){
                    okay.put(student,groupSoFar.get(student));
                }else {
                    good.put(student,groupSoFar.get(student));
                }
            }


        }
        actualGroupings.add(bad);
        actualGroupings.add(okay);
        actualGroupings.add(good);

//        for(Map<String,ConceptGraph> map: actualGroupings){
//                for(String name: map.keySet()){
//                    System.out.println(name);
//                }
//
//            System.out.println("end group");
//        }

        return actualGroupings;
    }


    /**
     * Calculates the sum of all of the student's knowledge estimates on the whole concept graph or a specific part of it
     * @param s1 student's concept graph
     * @param subject 'all' for the entire tree or a concept node IDs
     * @return double the sum of the estimates
     */
    public double calcSum (ConceptGraph s1, String subject){

        if(subject.equals("all")){
            double ex = 0;

            for(ConceptNode roots: s1.getRoots()){
                double total = roots.countTotalKnowledgeEstimate(new ArrayList<>());
                ex+= total;

            }

            return ex;

        }else{

            ConceptNode node = s1.findNodeById(subject);


            return node.countTotalKnowledgeEstimate(new ArrayList<>());

        }
    }



    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }

}
