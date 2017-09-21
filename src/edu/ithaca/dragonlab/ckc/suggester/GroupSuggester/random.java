package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mia Kimmich Mitchell on 9/20/2017.
 */
public class random extends GroupSuggester{




    @Override
    public List<Map<String, ConceptGraph>> suggestGroup(List<Map<String, ConceptGraph>> groupings){


        System.out.println(groupings.get(0).size());


        return null;

    }


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {
        return null;
    }
}