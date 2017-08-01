package edu.ithaca.dragonlab.ckc.suggester.GroupSuggesterTest;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.GroupSuggester;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.randomGroupSuggestion;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggesterTest {


    @Test
    public void randomGroupSuggesterTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new randomGroupSuggestion();

        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        for(List<String> lists : groupings){
            System.out.println(lists);
        }

        //test to see each name is only used once in the groupings list

    }


}
