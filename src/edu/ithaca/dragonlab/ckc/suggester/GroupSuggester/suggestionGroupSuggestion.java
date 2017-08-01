package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.util.*;

/**
 * Created by mkimmitchell on 8/1/17.
 */
public class suggestionGroupSuggestion extends GroupSuggester {



    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {

        Map <String, ConceptGraph> user = getUserMap(graphs);

        List<ConceptGraph> graphList = new ArrayList<>();
        graphList.addAll(user.values());


        Map<String, SuggestionResource> userMap = new HashMap<>();


        for(ConceptGraph graph: graphList){
//            System.out.println(graph.getName());
            List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(graph);

            userMap.put(graph.getName(),new SuggestionResource(graph, nodeList) );

        }


        for(String name: userMap.keySet()){
            SuggestionResource sugres = userMap.get(name);

            System.out.println("name " + name + " incom " + sugres.incompleteList + " \n wrong "+ sugres.wrongList );

        }









//        if (user.size() % choice == 0 && user.size() >= choice) {
//
//
//        } else if (user.size() < choice && user.size() % choice != 0) {
//            //there aren't enough students
//            //warning. there won't be one full group
//
//
//        } else {
//            //user.size()%choice!=0 && user.size()>= choice
//
//            //there will be one group that doesn't have a full group
//
//
//        }
        return null;
    }


}
