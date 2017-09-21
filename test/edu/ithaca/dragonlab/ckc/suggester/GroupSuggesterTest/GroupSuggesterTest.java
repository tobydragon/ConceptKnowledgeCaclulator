package edu.ithaca.dragonlab.ckc.suggester.GroupSuggesterTest;

import com.sun.org.apache.bcel.internal.classfile.ConstantCP;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.*;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggesterTest {


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


        GroupSuggester obj = new RandomGroupSuggester();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(), 3);
        Assert.assertEquals(groupings.get(0).size(),2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 1);

        Assert.assertNotEquals(groupings.get(0).get(0), groupings.get(0).get(1), groupings.get(1).get(0));

        //groups of three
        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).size(),3);
        Assert.assertNotEquals(groupings2.get(0).get(0), groupings2.get(0).get(1), groupings2.get(0).get(2));


    }


    @Test
    public void random() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new random();


        Map<String, ConceptGraph> userMap = obj.getUserMap(graphs);

        List<Map<String, ConceptGraph>> groupings1 = new ArrayList<>();
        groupings1.add(userMap);

        //groups of two
        List<Map<String, ConceptGraph>> groupings = obj.suggestGroup(groupings1);


    }

    @Test
    public void randomGroupTestLessStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraph.json", "test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new RandomGroupSuggester();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).size(),1);


//        //groups of three
        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).size(),1);


    }



    @Test
    public void randomGroupTestEvenStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new RandomGroupSuggester();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(), 3);
        Assert.assertEquals(groupings.get(0).size(),2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 2);

        List<String> test = new ArrayList<>();
        test.add(groupings.get(0).get(0));
        test.add( groupings.get(0).get(1));
        test.add(groupings.get(1).get(0));
        test.add( groupings.get(1).get(1));
        test.add(groupings.get(2).get(0));
        test.add(groupings.get(2).get(1));

        Assert.assertEquals(test.size(),6);
        Assert.assertEquals(test.contains("s1"), true);
        Assert.assertEquals(test.contains("s2"), true);
        Assert.assertEquals(test.contains("s3"), true);
        Assert.assertEquals(test.contains("s4"), true);
        Assert.assertEquals(test.contains("s5"), true);
        Assert.assertEquals(test.contains("s6"), true);

        //groups of three
        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).size(),3);
        Assert.assertEquals(groupings2.get(1).size(),3);
        Assert.assertNotEquals(groupings2.get(0).get(0), groupings2.get(0).get(1), groupings2.get(0).get(2));
        Assert.assertNotEquals(groupings2.get(1).get(0), groupings2.get(1).get(1), groupings2.get(1).get(2));


    }




    @Test
    public void suggestionGroupTestOddStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {

            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new ResourceGroupSuggester();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(),3);

        Assert.assertEquals(groupings.get(0).size(), 3);
        Assert.assertEquals(groupings.get(1).size(), 3);

        Assert.assertEquals(groupings.get(0).get(0),"s4" );
        Assert.assertEquals(groupings.get(0).get(1),"s5" );
        Assert.assertEquals(groupings.get(0).get(2),"something challenging" );

        Assert.assertEquals(groupings.get(1).get(0),"s3" );
        Assert.assertEquals(groupings.get(1).get(1),"s2" );
        Assert.assertEquals(groupings.get(1).get(2),"What are values are accessed by?" );

        Assert.assertEquals(groupings.get(2).get(0),"s1" );
        Assert.assertEquals(groupings.get(2).get(1),"No other students" );


        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(),2);
        Assert.assertEquals(groupings2.get(0).size(), 4);
        Assert.assertEquals(groupings2.get(0).get(0), "s4");
        Assert.assertEquals(groupings2.get(0).get(1), "s5");
        Assert.assertEquals(groupings2.get(0).get(2), "s1");
        Assert.assertEquals(groupings2.get(0).get(3), "something challenging");

        Assert.assertEquals(groupings2.get(1).size(), 3);
        Assert.assertEquals(groupings2.get(1).get(0), "s3");
        Assert.assertEquals(groupings2.get(1).get(1), "s2");
        Assert.assertEquals(groupings2.get(1).get(2), "No other students");

    }

    @Test
    public void suggestionGroupTestEvenStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new ResourceGroupSuggester();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(),3);
        Assert.assertEquals(groupings.get(0).get(0), "s3");
        Assert.assertEquals(groupings.get(0).get(1), "s5");
        Assert.assertEquals(groupings.get(1).get(0), "s1");
        Assert.assertEquals(groupings.get(1).get(1), "s2");
        Assert.assertEquals(groupings.get(2).get(0), "s6");
        Assert.assertEquals(groupings.get(2).get(1), "s4");

        List<List<String>> groupings3 = obj.suggestGroup(graphs, 3);
        Assert.assertEquals(groupings3.size(),3);
        Assert.assertEquals(groupings3.get(0).get(0), "s3");
        Assert.assertEquals(groupings3.get(0).get(1), "s5");
        Assert.assertEquals(groupings3.get(1).get(0), "s1");
        Assert.assertEquals(groupings3.get(1).get(1), "s2");
        Assert.assertEquals(groupings3.get(2).get(0), "s4");
        Assert.assertEquals(groupings3.get(2).get(1), "s6");


        ResourceNCubeGroupSuggester obj2 = new ResourceNCubeGroupSuggester();
        List<List<String>> groupings2 = obj2.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(),2);

        Assert.assertEquals(groupings2.get(0).size(), 4);
        Assert.assertEquals(groupings2.get(0).get(0), "s3");
        Assert.assertEquals(groupings2.get(0).get(1), "s5");
        Assert.assertEquals(groupings2.get(0).get(2), "s2");
        Assert.assertEquals(groupings2.get(0).get(3), "How are while loops and booleans related?");

        Assert.assertEquals(groupings2.get(1).size(), 4);
        Assert.assertEquals(groupings2.get(1).get(0), "s1");
        Assert.assertEquals(groupings2.get(1).get(1), "s6");
        Assert.assertEquals(groupings2.get(1).get(2), "s4");
        Assert.assertEquals(groupings2.get(1).get(3), "Random pairing");


    }

    @Test
    public void suggestionGroupTestLessLeftOverStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new ResourceGroupSuggester();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(),1);
        Assert.assertEquals(groupings.get(0).size(), 2);
        Assert.assertEquals(groupings.get(0).get(0), "bspinache1");
        Assert.assertEquals(groupings.get(0).get(1), "No other students");


        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);
        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).size(), 3);
        Assert.assertEquals(groupings2.get(0).get(0), "bspinache1");
        Assert.assertEquals(groupings2.get(0).get(1), "No other students");
        Assert.assertEquals(groupings2.get(0).get(2), "No other students");


        ResourceNCubeGroupSuggester obj2 = new ResourceNCubeGroupSuggester();

        List<List<String>> groupings3 = obj2.suggestGroup(graphs, 3);
        Assert.assertEquals(groupings3.size(), 1);
        Assert.assertEquals(groupings3.get(0).size(), 2);
        Assert.assertEquals(groupings3.get(0).get(0), "bspinache1");
    }


    @Test
    public void mapSumGroupSuggestiongetSumTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);
        GraphSumGroupSuggester sug = new GraphSumGroupSuggester();

        ConceptGraph usergraph = graphs.getUserGraph("s5");

        double sum = sug.calcSum(usergraph, "Values and Data Types");
        Assert.assertEquals(sum, 3.3575,  DataUtil.OK_FLOAT_MARGIN);

        double sum2 = sug.calcSum(usergraph, "all");
        Assert.assertEquals(sum2, 12.87208,  DataUtil.OK_FLOAT_MARGIN);



        ConceptGraph usergraph2 = graphs.getUserGraph("s4");
        double sum3 = sug.calcSum(usergraph2, "all");
        Assert.assertEquals(sum3, 1.0,  DataUtil.OK_FLOAT_MARGIN);



    }

    @Test
    public void mapSumGroupSuggestionEvenStudents(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        GraphSumGroupSuggester sug = new GraphSumGroupSuggester();

        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");

        Assert.assertEquals(groupings.size(),3 );
        Assert.assertEquals(groupings.get(0).size(), 2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 2);

        Assert.assertEquals(groupings.get(0).get(0), "s3");
        Assert.assertEquals(groupings.get(0).get(1), "s5");

        Assert.assertEquals(groupings.get(1).get(0), "s6");
        Assert.assertEquals(groupings.get(1).get(1), "s1");


        Assert.assertEquals(groupings.get(2).get(0), "s4");
        Assert.assertEquals(groupings.get(2).get(1), "s2");


        List<List<String>> groupings2 = sug.suggestGroup(graphs, 2, "Expressions and Statements");

        Assert.assertEquals(groupings2.size(),3 );
        Assert.assertEquals(groupings2.get(0).size(), 2);
        Assert.assertEquals(groupings2.get(1).size(), 2);
        Assert.assertEquals(groupings2.get(2).size(), 2);

        Assert.assertEquals(groupings2.get(0).get(0), "s5");
        Assert.assertEquals(groupings2.get(0).get(1), "s1");

        Assert.assertEquals(groupings2.get(1).get(0), "s3");
        Assert.assertEquals(groupings2.get(1).get(1), "s6");


        Assert.assertEquals(groupings2.get(2).get(0), "s4");
        Assert.assertEquals(groupings2.get(2).get(1), "s2");


        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");

        Assert.assertEquals(grouping3.size(),2 );
        Assert.assertEquals(grouping3.get(0).size(), 3);
        Assert.assertEquals(grouping3.get(1).size(), 3);

        Assert.assertEquals(grouping3.get(0).get(0), "s3");
        Assert.assertEquals(grouping3.get(0).get(1), "s5");
        Assert.assertEquals(grouping3.get(0).get(2), "s2");

        Assert.assertEquals(grouping3.get(1).get(0), "s6");
        Assert.assertEquals(grouping3.get(1).get(1), "s1");
        Assert.assertEquals(grouping3.get(1).get(2), "s4");

    }


    @Test
    public void mapSumGroupSuggestionOddStudents(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        GraphSumGroupSuggester sug = new GraphSumGroupSuggester();

        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");

        Assert.assertEquals(groupings.size(),3 );
        Assert.assertEquals(groupings.get(0).size(), 2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 1);

        Assert.assertEquals(groupings.get(0).get(0), "s4");
        Assert.assertEquals(groupings.get(0).get(1), "s5");

        Assert.assertEquals(groupings.get(1).get(0), "s3");
        Assert.assertEquals(groupings.get(1).get(1), "s2");


        Assert.assertEquals(groupings.get(2).get(0), "s1");


        List<List<String>> groupings2 = sug.suggestGroup(graphs, 2, "Expressions and Statements");

        Assert.assertEquals(groupings2.size(),3 );
        Assert.assertEquals(groupings2.get(0).size(), 2);
        Assert.assertEquals(groupings2.get(1).size(), 2);
        Assert.assertEquals(groupings2.get(2).size(), 1);

        Assert.assertEquals(groupings2.get(0).get(0), "s4");
        Assert.assertEquals(groupings2.get(0).get(1), "s5");

        Assert.assertEquals(groupings2.get(1).get(0), "s3");
        Assert.assertEquals(groupings2.get(1).get(1), "s2");


        Assert.assertEquals(groupings2.get(2).get(0), "s1");


        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");

        Assert.assertEquals(grouping3.size(),2 );
        Assert.assertEquals(grouping3.get(0).size(), 3);
        Assert.assertEquals(grouping3.get(1).size(), 2);

        Assert.assertEquals(grouping3.get(0).get(0), "s4");
        Assert.assertEquals(grouping3.get(0).get(1), "s5");
        Assert.assertEquals(grouping3.get(0).get(2), "s1");

        Assert.assertEquals(grouping3.get(1).get(0), "s3");
        Assert.assertEquals(grouping3.get(1).get(1), "s2");

    }



    @Test
    public void mapSumGroupSuggestionlessStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraph.json", "test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GraphSumGroupSuggester sug = new GraphSumGroupSuggester();

        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");
        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).size(), 1);


        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");

        Assert.assertEquals(grouping3.size(), 1);
        Assert.assertEquals(grouping3.get(0).size(), 1);

        Assert.assertEquals(grouping3.get(0).get(0), "bspinache1");


    }

    @Test
    public void conceptDiffGroupSuggestiongetcalcDiffTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);
        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();

        ConceptGraph usergraph = graphs.getUserGraph("s6");
        ConceptGraph usergraph2 = graphs.getUserGraph("s1");


        double sum = sug.calcDiff(usergraph, usergraph2, "Values and Data Types");
        Assert.assertEquals(sum, 0.5025,  DataUtil.OK_FLOAT_MARGIN);

        double sum2 = sug.calcDiff(usergraph, usergraph2, "all");
        Assert.assertEquals(sum2, 2.0866,  DataUtil.OK_FLOAT_MARGIN);
    }


    @Test
    public void ConceptDiffGroupSuggestionEvenStudents(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();

        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");


        Assert.assertEquals(groupings.size(),3 );
        Assert.assertEquals(groupings.get(0).size(), 2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 2);

        Assert.assertEquals(groupings.get(0).get(0), "s3");
        Assert.assertEquals(groupings.get(0).get(1), "s5");

        Assert.assertEquals(groupings.get(1).get(0), "s6");
        Assert.assertEquals(groupings.get(1).get(1), "s2");


        Assert.assertEquals(groupings.get(2).get(0), "s4");
        Assert.assertEquals(groupings.get(2).get(1), "s1");


        List<List<String>> groupings2 = sug.suggestGroup(graphs, 2, "Expressions and Statements");

        Assert.assertEquals(groupings2.size(),3 );
        Assert.assertEquals(groupings2.get(0).size(), 2);
        Assert.assertEquals(groupings2.get(1).size(), 2);
        Assert.assertEquals(groupings2.get(2).size(), 2);

        Assert.assertEquals(groupings2.get(0).get(0), "s5");
        Assert.assertEquals(groupings2.get(0).get(1), "s1");

        Assert.assertEquals(groupings2.get(1).get(0), "s3");
        Assert.assertEquals(groupings2.get(1).get(1), "s6");


        Assert.assertEquals(groupings2.get(2).get(0), "s4");
        Assert.assertEquals(groupings2.get(2).get(1), "s2");


        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");

        Assert.assertEquals(grouping3.size(),2 );
        Assert.assertEquals(grouping3.get(0).size(), 3);
        Assert.assertEquals(grouping3.get(1).size(), 3);

        Assert.assertEquals(grouping3.get(0).get(0), "s3");
        Assert.assertEquals(grouping3.get(0).get(1), "s5");
        Assert.assertEquals(grouping3.get(0).get(2), "s1");

        Assert.assertEquals(grouping3.get(1).get(0), "s6");
        Assert.assertEquals(grouping3.get(1).get(1), "s2");
        Assert.assertEquals(grouping3.get(1).get(2), "s4");

    }


    @Test
    public void ConceptDiffGroupSuggestionLessStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraph.json", "test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();

        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");
        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).size(), 1);


        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");

        Assert.assertEquals(grouping3.size(), 1);
        Assert.assertEquals(grouping3.get(0).size(), 1);

        Assert.assertEquals(grouping3.get(0).get(0), "bspinache1");


    }


    @Test
    public void ConceptDiffGroupSuggestionOddStudents(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();

        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");


        Assert.assertEquals(groupings.size(),3 );
        Assert.assertEquals(groupings.get(0).size(), 2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 1);

        Assert.assertEquals(groupings.get(0).get(0), "s4");
        Assert.assertEquals(groupings.get(0).get(1), "s5");

        Assert.assertEquals(groupings.get(1).get(0), "s3");
        Assert.assertEquals(groupings.get(1).get(1), "s2");


        Assert.assertEquals(groupings.get(2).get(0), "s1");




        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");

        Assert.assertEquals(grouping3.size(),2 );
        Assert.assertEquals(grouping3.get(0).size(), 3);
        Assert.assertEquals(grouping3.get(1).size(), 2);

        Assert.assertEquals(grouping3.get(0).get(0), "s4");
        Assert.assertEquals(grouping3.get(0).get(1), "s5");
        Assert.assertEquals(grouping3.get(0).get(2), "s3");

        Assert.assertEquals(grouping3.get(1).get(0), "s2");
        Assert.assertEquals(grouping3.get(1).get(1), "s1");

    }



    @Test
    public void groupTestRealData() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("resources/comp220/comp220Graph.json","resources/comp220/comp220Resources.json","localresources/comp220/comp220ExampleDataPortion.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


//        GroupSuggester obj = new RandomGroupSuggester();
//
//        List<List<String>> groupings2 = obj.suggestGroup(graphs, 2);
//        Assert.assertEquals(groupings2.size(), 19);
//
////
//        List<List<String>> groupings = obj.suggestGroup(graphs, 3);
//        Assert.assertEquals(groupings.size(), 13);
//
//
//
//        GroupSuggester group = new ResourceNCubeGroupSuggester();
//
//        List<List<String>> groupings3 = group.suggestGroup(graphs, 3);
//        Assert.assertEquals(groupings3.size(), 13);
//
//
//
//        List<List<String>> groupings4 = group.suggestGroup(graphs, 2);
//        Assert.assertEquals(groupings4.size(), 19);
//
//
//
//        ResourceNCubeGroupSuggester teams = new ResourceNCubeGroupSuggester();
//        List<List<String>> grouping1 = teams.suggestGroup(graphs, 3);
//        Assert.assertEquals(grouping1.size(), 13);
//
//
//        List<List<String>> grouping = teams.suggestGroup(graphs, 2);
//        Assert.assertEquals(grouping.size(), 19);
//
//
//
//        GraphSumGroupSuggester sug = new GraphSumGroupSuggester();
//
//        List<List<String>> groupings5 = sug.suggestGroup(graphs, 2, "all");
//        Assert.assertEquals(groupings5.size(), 19);
//
//
//        List<List<String>> grouping6 = sug.suggestGroup(graphs, 3, "all");
//
//        Assert.assertEquals(grouping6.size(), 13);
//

        ConceptDiffGroupSuggester sug2 = new ConceptDiffGroupSuggester();

//        List<List<String>> groupings7 = sug2.suggestGroup(graphs, 2, "all");
//        Assert.assertEquals(groupings7.size(), 19);


        List<List<String>> grouping8 = sug2.suggestGroup(graphs, 3, "all");
        Assert.assertEquals(grouping8.size(), 13);


    }



}
