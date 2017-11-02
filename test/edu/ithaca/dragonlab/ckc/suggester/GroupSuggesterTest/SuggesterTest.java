package edu.ithaca.dragonlab.ckc.suggester.GroupSuggesterTest;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.BySizeSuggester;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.Group;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.GroupSuggester;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkimmitchell on 11/2/17.
 */
public class SuggesterTest {



    @Test
    public void randomGroupTestOddStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        BySizeSuggester sug  = new BySizeSuggester(2, true);

        List<Group> list = GroupSuggester.getGroupList(graphs);

        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());


        //groups of 2
        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(1).getSize(), 2);

//        //groups of three
//        List<Group> groupings2 = sug.grouping(groupings1, 3, 2 , null, true);
//        Assert.assertEquals(groupings2.size(), 2);
//        Assert.assertEquals(groupings2.get(0).getSize(), 3);
//        Assert.assertEquals(groupings2.get(1).getSize(), 2);
//

    }


}
