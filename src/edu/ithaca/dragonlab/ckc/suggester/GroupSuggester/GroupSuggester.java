package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.*;
/**
 * Created by mkimmitchell on 7/31/17.
 */
public abstract class GroupSuggester {

    public abstract void suggestGroup(CohortConceptGraphs graphs, int choice);


    public List<String> getUsers(CohortConceptGraphs graphs) {
        List<String> userList = new ArrayList<>();
        Map<String, ConceptGraph> userToGraph = graphs.getUserToGraph();

        userList.addAll(userToGraph.keySet());

        return userList;
    }
}
