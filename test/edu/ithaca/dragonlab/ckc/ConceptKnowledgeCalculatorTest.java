package edu.ithaca.dragonlab.ckc;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.io.*;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import stats.RFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static edu.ithaca.dragonlab.ckc.util.DataUtil.OK_FLOAT_MARGIN;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Mia Kimmich Mitchell on 6/9/2017.
 */
public class ConceptKnowledgeCalculatorTest {

    static Logger logger = LogManager.getLogger(ConceptKnowledgeCalculator.class);

    @Test
    public void getModeTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }

    @Test
    public void getCohortData(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            Assert.assertEquals(ckc.currentStructure().get(0),"test/testresources/ManuallyCreated/basicRealisticConceptGraph.json" );
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }


    @Test
    public void calcIndividualConceptNodesSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        List<String> concepts = null;
        try {
            concepts = ckc.calcIndividualConceptNodesSuggestions("bspinache1");

            Assert.assertEquals(concepts.size(), 1);
            Assert.assertEquals(concepts.get(0), "Boolean");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    public void clearandCreateStructureGraphTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");

            List<String> struct = new ArrayList<>();
            struct.add("test/testresources/ManuallyCreated/researchConceptGraph.json");

            ckc.clearAndCreateStructureData(struct);

            Assert.assertEquals(ckc.currentStructure().get(0),"test/testresources/ManuallyCreated/researchConceptGraph.json" );

            Assert.assertEquals(ckc.currentResource().size(),0);
            Assert.assertEquals(ckc.currentAssessment().size(), 0);

            struct.clear();
            struct.add("test/testresources/ManuallyCreated/simpleConceptGraph.json");
            ckc.clearAndCreateStructureData(struct);
            Assert.assertEquals(ckc.currentStructure().get(0),"test/testresources/ManuallyCreated/simpleConceptGraph.json" );

            Assert.assertEquals(ckc.currentResource().size(),0);
            Assert.assertEquals(ckc.currentAssessment().size(), 0);


        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }


    @Test
    public void updateStructureWithAnotherFile() {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            ckc.addResource("test/testresources/ManuallyCreated/researchResource1.json");

            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE);
            Assert.assertEquals(ckc.currentResource().size(), 1);
            Assert.assertEquals(ckc.currentStructure().get(0), "test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentResource().size(),1);
            Assert.assertEquals(ckc.currentResource().get(0), "test/testresources/ManuallyCreated/researchResource1.json");
            Assert.assertEquals(ckc.currentAssessment().size(), 0);

            ckc.updateStructureFile("test/testresources/ManuallyCreated/researchConceptGraph.json");

            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE);
            Assert.assertEquals(ckc.currentResource().size(),1);
            Assert.assertEquals(ckc.currentResource().get(0), "test/testresources/ManuallyCreated/researchResource1.json");
            Assert.assertEquals(ckc.currentAssessment().size(), 0);

            Assert.assertEquals(ckc.currentStructure().get(0), "test/testresources/ManuallyCreated/researchConceptGraph.json");


        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }

    

    @Test
    public void clearandCreateCohortGraphTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");

            List<String> struct = new ArrayList<>();
            struct.add("test/testresources/ManuallyCreated/researchConceptGraph.json");
            List<String> res = new ArrayList<>();
            res.add("test/testresources/ManuallyCreated/researchResource1.json");
            List<String> assess = new ArrayList<>();
            assess.add("test/testresources/ManuallyCreated/researchAssessment1.csv");

            ckc.clearAndCreateCohortData(struct,res,assess);

            Assert.assertEquals(ckc.currentStructure().size(), 1);
            Assert.assertEquals(ckc.currentResource().size(),1);
            Assert.assertEquals(ckc.currentAssessment().size(), 1);
            Assert.assertEquals(ckc.currentStructure().get(0),"test/testresources/ManuallyCreated/researchConceptGraph.json" );
            Assert.assertEquals(ckc.currentResource().get(0),"test/testresources/ManuallyCreated/researchResource1.json" );
            Assert.assertEquals(ckc.currentAssessment().get(0), "test/testresources/ManuallyCreated/researchAssessment1.csv");



        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }


    }





    @Test(expected = Exception.class)
    public void calcIndividualConceptNodesSuggestionsBadInputTest() throws Exception {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            ckc.calcIndividualConceptNodesSuggestions("baduser");

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }


    }

    @Test
    public void  calcIndividualGraphSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        SuggestionResource res = null;
        try {
            res = ckc.calcIndividualGraphSuggestions("bspinache1");
            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest.size(),2);
            Assert.assertEquals(incomTest.get(0).getId(),"Q6");

            Assert.assertEquals(incomTest.get(1).getId(),"Q10");

            Assert.assertEquals(wrongTest.size(),2);
            Assert.assertEquals(wrongTest.get(0).getId(), "Q7");
            Assert.assertEquals(wrongTest.get(1).getId(), "Q9");

        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }


    }

    @Test
    public void  realDataCalcIndividualGraphSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc =new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/comp220GraphExample.json",
                    "test/testresources/ManuallyCreated/comp220Resources.json",
                    "testlocalresources/comp220ExampleDataPortion.csv");

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        SuggestionResource res = null;
        try {
            res = ckc.calcIndividualGraphSuggestions("s13");
            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest.size(), 0);
            Assert.assertEquals(incomTest, new ArrayList<>());

            Assert.assertEquals(wrongTest.size(), 3);
            Assert.assertEquals(wrongTest.get(0).getId(), "Lab 4: Recursion");
            Assert.assertEquals(wrongTest.get(1).getId(), "Lab 2: Array Library");
            Assert.assertEquals(wrongTest.get(2).getId(), "Lab 3: Comparing Array Library Efficiency");


        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }

    }



    @Test
    public void  realDataCalcIndividualGraphSuggestionsTest2(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc =new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/comp220GraphExample.json",
                    "test/testresources/ManuallyCreated/comp220Resources.json",
                    "testlocalresources/comp220ExampleDataPortion.csv");

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        SuggestionResource res = null;
        try {

            res = ckc.calcIndividualGraphSuggestions("s10");

            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest.size(), 0);
            Assert.assertEquals(incomTest, new ArrayList<>());

            Assert.assertEquals(wrongTest.size(), 7);
            Assert.assertEquals(wrongTest.get(0).getId(), "Lab 4: Recursion");
            Assert.assertEquals(wrongTest.get(1).getId(), "Lab 8: Comparing Arrays and Linked Lists");
            Assert.assertEquals(wrongTest.get(2).getId(), "Lab 4: Recursion");
            Assert.assertEquals(wrongTest.get(3).getId(), "Lab 2: Array Library");
            Assert.assertEquals(wrongTest.get(4).getId(), "Lab 7: Linked List");
            Assert.assertEquals(wrongTest.get(5).getId(), "Lab 5: Comparing Searches");
            Assert.assertEquals(wrongTest.get(6).getId(), "Lab 3: Comparing Array Library Efficiency");


        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }

    }



    @Test
    public void  calcIndividualGraphSuggestionsWIthEmptyListTest() throws Exception {
            ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/comp220GraphExample.json","test/testresources/ManuallyCreated/comp220Resources.json", "testlocalresources/comp220ExampleDataPortion.csv");
            SuggestionResource res = ckc.calcIndividualGraphSuggestions("s17");

            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest,new ArrayList<>());

            Assert.assertEquals(wrongTest.get(0).getId(), "Lab 4: Recursion");
            Assert.assertEquals(wrongTest.get(1).getId(), "Lab 8: Comparing Arrays and Linked Lists");
            Assert.assertEquals(wrongTest.get(2).getId(), "Lab 4: Recursion");
            Assert.assertEquals(wrongTest.get(3).getId(), "Lab 6: ArrayList and Testing");
            Assert.assertEquals(wrongTest.get(4).getId(), "Lab 5: Comparing Searches");
            Assert.assertEquals(wrongTest.get(5).getId(), "Lab 7: Linked List");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }



    }


    @Test
    public void calcIndividualSpecificConceptSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");


        }

        SuggestionResource resource = null;
        try {
            resource = ckc.calcIndividualSpecificConceptSuggestions("bspinache1", "If Statement");

            Assert.assertEquals(resource.incompleteList.get(0).getId(),"Q10" );
            Assert.assertEquals(resource.incompleteList.get(1).getId(),"Q6" );
            Assert.assertEquals(resource.incompleteList.get(2).getId(),"Q3" );

            Assert.assertEquals(resource.wrongList.get(0).getId(),"Q9" );
            Assert.assertEquals(resource.wrongList.get(1).getId(),"Q1" );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void addAssignmentTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");

            List<LearningObjectResponse> originalMasterList = new ArrayList<>();

            CohortConceptGraphs originalGraphs = ckc.getCohortConceptGraphs();
            ConceptGraph conGraph = originalGraphs.getAvgGraph();
            Map<String, LearningObject> origLearningMap =  conGraph.getLearningObjectMap();

            Collection<LearningObject> origLearningObList = origLearningMap.values();
            for (LearningObject origLearningOb: origLearningObList){
                List<LearningObjectResponse> origLOR= origLearningOb.getResponses();
                originalMasterList.addAll(origLOR);
            }



            Assert.assertEquals(originalMasterList.size(), 11);
            Assert.assertEquals(originalMasterList.get(0).getLearningObjectId(), "Q1");
            Assert.assertEquals(originalMasterList.get(1).getLearningObjectId(), "Q2");
            Assert.assertEquals(originalMasterList.get(2).getLearningObjectId(), "Q4");
            Assert.assertEquals(originalMasterList.get(3).getLearningObjectId(), "Q5");
            Assert.assertEquals(originalMasterList.get(4).getLearningObjectId(), "Q7");
            Assert.assertEquals(originalMasterList.get(5).getLearningObjectId(), "Q8");
            Assert.assertEquals(originalMasterList.get(6).getLearningObjectId(), "Q9");
            Assert.assertEquals(originalMasterList.get(7).getLearningObjectId(), "Q11");
            Assert.assertEquals(originalMasterList.get(8).getLearningObjectId(), "Q12");
            Assert.assertEquals(originalMasterList.get(9).getLearningObjectId(), "Q15");
            Assert.assertEquals(originalMasterList.get(10).getLearningObjectId(), "Q14");


        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }



        try {
            ckc.addAssessement("test/testresources/ManuallyCreated/mediumAssessment.csv");

            List<LearningObjectResponse> postMasterList1 = new ArrayList<>();

            CohortConceptGraphs postGraphs = ckc.getCohortConceptGraphs();
            ConceptGraph postCG = postGraphs.getAvgGraph();
            Map<String, LearningObject> postLOMap =  postCG.getLearningObjectMap();


            Collection<LearningObject> postLOList = postLOMap.values();
            for (LearningObject postlearningObject: postLOList){
                List<LearningObjectResponse> postLOR = postlearningObject.getResponses();
                postMasterList1.addAll(postLOR);
            }


            Assert.assertEquals(postMasterList1.size(), 19);
            Assert.assertEquals(postMasterList1.get(0).getLearningObjectId(), "Q1");
            Assert.assertEquals(postMasterList1.get(1).getLearningObjectId(), "Q2");
            Assert.assertEquals(postMasterList1.get(2).getLearningObjectId(), "Q3");
            Assert.assertEquals(postMasterList1.get(3).getLearningObjectId(), "Q4");
            Assert.assertEquals(postMasterList1.get(4).getLearningObjectId(), "Q4");
            Assert.assertEquals(postMasterList1.get(5).getLearningObjectId(), "Q5");
            Assert.assertEquals(postMasterList1.get(6).getLearningObjectId(), "Q5");
            Assert.assertEquals(postMasterList1.get(7).getLearningObjectId(), "Q6");
            Assert.assertEquals(postMasterList1.get(8).getLearningObjectId(), "Q7");
            Assert.assertEquals(postMasterList1.get(9).getLearningObjectId(), "Q7");
            Assert.assertEquals(postMasterList1.get(10).getLearningObjectId(), "Q8");
            Assert.assertEquals(postMasterList1.get(11).getLearningObjectId(), "Q9");
            Assert.assertEquals(postMasterList1.get(12).getLearningObjectId(), "Q9");
            Assert.assertEquals(postMasterList1.get(13).getLearningObjectId(), "Q11");
            Assert.assertEquals(postMasterList1.get(14).getLearningObjectId(), "Q11");
            Assert.assertEquals(postMasterList1.get(15).getLearningObjectId(), "Q12");
            Assert.assertEquals(postMasterList1.get(16).getLearningObjectId(), "Q15");
            Assert.assertEquals(postMasterList1.get(17).getLearningObjectId(), "Q14");
            Assert.assertEquals(postMasterList1.get(18).getLearningObjectId(), "Q14");

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            ckc.addAssessement("test/testresources/ManuallyCreated/simpleAssessment.csv");
            List<LearningObjectResponse> postMasterList2 = new ArrayList<>();

            CohortConceptGraphs postGraphs2 = ckc.getCohortConceptGraphs();
            ConceptGraph postCG2 = postGraphs2.getAvgGraph();
            Map<String, LearningObject> postLOMap2 =  postCG2.getLearningObjectMap();

            Collection<LearningObject> postLOList2 = postLOMap2.values();
            for (LearningObject postlearningObject2: postLOList2){
                List<LearningObjectResponse> postLOR2 = postlearningObject2.getResponses();
                postMasterList2.addAll(postLOR2);
            }

            Assert.assertEquals(postMasterList2.size(), 25);
            Assert.assertEquals(postMasterList2.get(0).getLearningObjectId(), "Q1");
            Assert.assertEquals(postMasterList2.get(1).getLearningObjectId(), "Q1");
            Assert.assertEquals(postMasterList2.get(2).getLearningObjectId(), "Q2");
            Assert.assertEquals(postMasterList2.get(3).getLearningObjectId(), "Q2");
            Assert.assertEquals(postMasterList2.get(4).getLearningObjectId(), "Q3");
            Assert.assertEquals(postMasterList2.get(5).getLearningObjectId(), "Q3");
            Assert.assertEquals(postMasterList2.get(6).getLearningObjectId(), "Q4");
            Assert.assertEquals(postMasterList2.get(8).getLearningObjectId(), "Q4");
            Assert.assertEquals(postMasterList2.get(10).getLearningObjectId(), "Q5");
            Assert.assertEquals(postMasterList2.get(12).getLearningObjectId(), "Q6");
            Assert.assertEquals(postMasterList2.get(14).getLearningObjectId(), "Q7");
            Assert.assertEquals(postMasterList2.get(16).getLearningObjectId(), "Q8");
            Assert.assertEquals(postMasterList2.get(18).getLearningObjectId(), "Q9");
            Assert.assertEquals(postMasterList2.get(20).getLearningObjectId(), "Q11");
            Assert.assertEquals(postMasterList2.get(22).getLearningObjectId(), "Q15");
            Assert.assertEquals(postMasterList2.get(23).getLearningObjectId(), "Q14");
            Assert.assertEquals(postMasterList2.get(24).getLearningObjectId(), "Q14");




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void removeLORFileTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            List<String> test = new ArrayList<>();
            test.add("test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            Assert.assertEquals(ckc.currentAssessment(),test);
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }


        try {
            ckc.removeAssessmentFile("test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            List<String> test1 = new ArrayList<>();
            Assert.assertEquals(ckc.currentAssessment(),test1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(expected = Exception.class)
    public void removeEmptyLORList() throws Exception{
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
        try {
            ckc.removeAssessmentFile("test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            ckc.removeAssessmentFile("test/remove");

        } catch (IOException e) {
            Assert.fail("There are negative files");
        }
    }

    @Test
    public void replaceLOFile (){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            List<String> test = new ArrayList<>();
            test.add("test/testresources/ManuallyCreated/basicRealisticResource.json");
            Assert.assertEquals(ckc.currentResource(),test);
            ckc.replaceResourceFile("test/testresources/ManuallyCreated/simpleChangeNameLOL.json");

            test.clear();
            test.add("test/testresources/ManuallyCreated/simpleChangeNameLOL.json");

            Assert.assertEquals(ckc.currentResource(), test);

        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }

    @Test
    public void addLOFileTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");

            List<LearningObject> originalMasterList = new ArrayList<>();

            CohortConceptGraphs originalGraphs = ckc.getCohortConceptGraphs();
            ConceptGraph conGraph = originalGraphs.getAvgGraph();
            Map<String, LearningObject> origLearningMap =  conGraph.getLearningObjectMap();

            Collection<LearningObject> origLearningObList = origLearningMap.values();

            originalMasterList.addAll(origLearningObList);



            Assert.assertEquals(originalMasterList.size(), 15);
            Assert.assertEquals(originalMasterList.get(0).getId(), "Q1");
            Assert.assertEquals(originalMasterList.get(1).getId(), "Q2");
            Assert.assertEquals(originalMasterList.get(2).getId(), "Q3");
            Assert.assertEquals(originalMasterList.get(3).getId(), "Q4");
            Assert.assertEquals(originalMasterList.get(4).getId(), "Q5");
            Assert.assertEquals(originalMasterList.get(5).getId(), "Q6");
            Assert.assertEquals(originalMasterList.get(6).getId(), "Q7");
            Assert.assertEquals(originalMasterList.get(7).getId(), "Q8");
            Assert.assertEquals(originalMasterList.get(8).getId(), "Q9");
            Assert.assertEquals(originalMasterList.get(9).getId(), "Q11");
            Assert.assertEquals(originalMasterList.get(10).getId(), "Q10");
            Assert.assertEquals(originalMasterList.get(11).getId(), "Q13");
            Assert.assertEquals(originalMasterList.get(12).getId(), "Q12");
            Assert.assertEquals(originalMasterList.get(13).getId(), "Q15");
            Assert.assertEquals(originalMasterList.get(14).getId(), "Q14");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }




        try {
            ckc.addResource("test/testresources/ManuallyCreated/simpleChangeNameLOL.json");

            List<LearningObject> postMasterList = new ArrayList<>();

            CohortConceptGraphs postGraphs = ckc.getCohortConceptGraphs();
            ConceptGraph postCG = postGraphs.getAvgGraph();
            Map<String, LearningObject> postLOMap =  postCG.getLearningObjectMap();
            Collection<LearningObject> postLOList = postLOMap.values();
            postMasterList.addAll(postLOList);


            Assert.assertEquals(postMasterList.size(), 15);
            Assert.assertEquals(postMasterList.get(0).getId(), "Q1");
            Assert.assertEquals(postMasterList.get(1).getId(), "Q2");
            Assert.assertEquals(postMasterList.get(2).getId(), "Q5");
            Assert.assertEquals(postMasterList.get(3).getId(), "Q7");
            Assert.assertEquals(postMasterList.get(4).getId(), "Q8");
            Assert.assertEquals(postMasterList.get(5).getId(), "resource2");
            Assert.assertEquals(postMasterList.get(6).getId(), "Q9");
            Assert.assertEquals(postMasterList.get(7).getId(), "resource3");
            Assert.assertEquals(postMasterList.get(8).getId(), "resource4");
            Assert.assertEquals(postMasterList.get(9).getId(), "resource5");
            Assert.assertEquals(postMasterList.get(10).getId(), "resource1");
            Assert.assertEquals(postMasterList.get(11).getId(), "Q10");
            Assert.assertEquals(postMasterList.get(12).getId(), "Q13");
            Assert.assertEquals(postMasterList.get(13).getId(), "Q12");
            Assert.assertEquals(postMasterList.get(14).getId(), "Q14");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConceptAvgTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files");

        }
        try {
            Assert.assertEquals(1, ckc.getLearningObjectAvg("Q4"), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(0.75, ckc.getLearningObjectAvg("Q14"), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void modeTest(){

        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            List<String> getTest = new ArrayList<>();

            //COHORT MODE
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");

            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);
            //the constructor starts out as a cohort Concept graph and there should only be one file in each of the lists of files. The structure graph should be null, because we're not on that mode
            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(),getTest );
            getTest.clear();

            getTest.add( "test/testresources/ManuallyCreated/basicRealisticResource.json");
            Assert.assertEquals(ckc.currentResource(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
            Assert.assertEquals(ckc.currentAssessment(), getTest);

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);
            getTest.clear();


            //STRUCTURE MODE
            //now in structure mode. Because of this cohort Concept graph should be null as well as all the extra data (LO and LOR files). Structure graph should not be null now because we are in that mode.
            //it should hold on to blank resource and assessment files. The structure file should have one file in it.
            ckc.switchToStructure();
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);

            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);

            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(), getTest);

            Assert.assertEquals(ckc.currentResource(), new ArrayList<>());
            Assert.assertEquals(ckc.currentAssessment(), new ArrayList<>());

            Assert.assertNotEquals(ckc.getStructureGraph(), null);
            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);
            getTest.clear();


            //STRUCTURE MODE WITH ASSESSMENT
            //switched to structure mode with assessment. This should still have a structure graph, but then also hold on to the files. Because additional LOR was called from structure graph mode, the assessment file should not be filled with the one file.
            ckc.addAssessement("test/testresources/ManuallyCreated/mediumAssessment.csv");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHASSESSMENT);

            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(), getTest);
            getTest.clear();

            Assert.assertEquals(ckc.currentResource(), new ArrayList<>());

            getTest.add("test/testresources/ManuallyCreated/mediumAssessment.csv");
            Assert.assertEquals(ckc.currentAssessment(), getTest);

            Assert.assertNotEquals(ckc.getStructureGraph(), null);
            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);
            getTest.clear();


            //CONCEPT GRAPH MODE
            //switched back into Concept graph mode because now the three lists of files are filled up with at least one file.
            //all of the previous files should be stored in structure files, resource files, and assessment files
            ckc.addResource("test/testresources/ManuallyCreated/simpleResource.json");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);
            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/simpleResource.json");
            Assert.assertEquals(ckc.currentResource(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/mediumAssessment.csv");
            Assert.assertEquals(ckc.currentAssessment(),getTest);
            getTest.clear();

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);




            //STRUCTURE GRAPH WITH RESOURCE
            //to test more, going from cohort graph to structure graph.
            // changing from structure graph to structure graph with resources. Cohort graph should be null and strucuture graph shouldn't be. The structure file should have one file in it and resource file list should have one in it. The assessment file should be empty
            ckc.switchToStructure();

            ckc.addResource("test/testresources/ManuallyCreated/simpleResource.json");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE);
            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/simpleResource.json");
            Assert.assertEquals(ckc.currentResource(),  getTest);
            getTest.clear();

            Assert.assertEquals(ckc.currentAssessment(), new ArrayList<>());
            Assert.assertNotEquals(ckc.getStructureGraph(), null);
            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);


            //COHORT GRAPH
            //this is switching from structure graph mode with resources to cohort graph mode. All the lists of files should have at least one file in them and structure graph should equal null;
            ckc.addAssessement("test/testresources/ManuallyCreated/simpleAssessment.csv");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);

            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/simpleResource.json");
            Assert.assertEquals(ckc.currentResource(),  getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/simpleAssessment.csv");
            Assert.assertEquals(ckc.currentAssessment(),getTest);
            getTest.clear();

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);



//            //STRUCTURE GRAPH WITH RESOURCE
//            //to further testing, going back to structure more
//            //to switch from structure graph with resource, adding LOR makes all the file lists have at least one file in them, thus a cohort graph can be made. Structure graph should not equal null and all file lists should have one file in them
            ckc.switchToStructure();

            ckc.addResourceAndAssessment("test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv" );
            getTest.add("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");
            Assert.assertEquals(ckc.currentStructure(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/simpleResource.json");
            Assert.assertEquals(ckc.currentResource(), getTest);
            getTest.clear();

            getTest.add("test/testresources/ManuallyCreated/simpleAssessment.csv" );
            Assert.assertEquals(ckc.currentAssessment(), getTest);

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);


        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }
    }

    @Test
    public void noDataMode(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test.json", "test.json", "test.csv");
            Assert.assertNotEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.NODATA);

        } catch (IOException e) {
            ckc = new ConceptKnowledgeCalculator();
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.NODATA);
        }
    }


    @Test
    public void getUserListTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files");
        }
        List<String> actualList = new ArrayList<>();
        actualList.add("bspinache1");
        try {
            Assert.assertEquals(actualList, ckc.getUserIdList());
        }catch (Exception e){
            Assert.fail();
        }


    }

    //TODO: Fix StudentAvgTest. Stopped working once  implementing factor analysis and column headers in RMatrix
    /**
    @Test
    public void getStudentAvgTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files");
        }
        Assert.assertEquals(0.502, ckc.getStudentAvg("bspinache1"), OK_FLOAT_MARGIN);
    }
    */


    @Test
    public void csvToResourceTest(){
        try {
            String testFilepath = "test/testresources/practicalExamples/SystemCreated/mediumGraphLearnObjectLinkRecordsCreationTest.json";

            List<String> csvFiles = new ArrayList<>();
            csvFiles.add("test/testresources/ManuallyCreated/mediumAssessment.csv");
            ConceptKnowledgeCalculator.csvToResource(csvFiles, testFilepath);

            List<LearningObjectLinkRecord> recordsFromFile = LearningObjectLinkRecord.buildListFromJson(testFilepath);
            Assert.assertNotNull(recordsFromFile);
            //TODO:test that these LOLRecords are good compared to the input csv file, they just won't have any concepts in their lists
            LearningObjectLinkRecord currRec = recordsFromFile.get(0);
            Assert.assertEquals("Q1", currRec.getLearningObject());
            LearningObjectLinkRecord nextRec = recordsFromFile.get(13);
            Assert.assertEquals("Q14", nextRec.getLearningObject());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("File(s) not found");

        }


    }

//This function is tested through output reading and should only be uncommented during debugging
    /**
    @Test
    public void getFactorMatrixTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessmentMoreUsers.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files");
        }
        ckc.getFactorMatrix();

    }
*/

    //calls void function that creates a graph. Tested through output

    /**
    @Test
    public void createConfirmatoryGraphTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/comp220GraphExample.json",
    "test/testresources/ManuallyCreated/comp220Resources.json",
    "localresources/comp220/comp220ExampleDataPortionCleaned.csv");
            ckc.createConfirmatoryGraph();
        } catch (Exception e) {
            Assert.fail("Unable to read assessment file");
        }
    }
*/

    @Test
    public void research1test() throws Exception {

        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            List<String> getTest = new ArrayList<>();

            //COHORT MODE
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");

            ckc.getCohortGraphsUrl();

            SuggestionResource sug = ckc.calcIndividualGraphSuggestions("s1");

            List<LearningObjectSuggestion> wrongTest = sug.wrongList;
            List<LearningObjectSuggestion> incomTest = sug.incompleteList;

            Assert.assertEquals(incomTest.size(), 1);
            Assert.assertEquals(incomTest.get(0).getId(), "What are the things you need for a while loop?");

            Assert.assertEquals(wrongTest.size(), 4);
            Assert.assertEquals(wrongTest.get(0).getId(), "What are the differences and similarities between for loops and while loops?");
            Assert.assertEquals(wrongTest.get(1).getId(), "What is the proper for loop diction?");
            Assert.assertEquals(wrongTest.get(2).getId(), "Are strings mutable?");
            Assert.assertEquals(wrongTest.get(3).getId(), "What is the proper while loop diction?");


        } catch (IOException e) {
            Assert.fail("Unable to load default files");
        }
    }



    @Test
    public void research2test() throws Exception {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");

            SuggestionResource sug2 = ckc.calcIndividualGraphSuggestions("s2") ;

            List<LearningObjectSuggestion> wrongTest2 = sug2.wrongList;
            List<LearningObjectSuggestion> incomTest2 = sug2.incompleteList;


            ckc.getCohortGraphsUrl();

            Assert.assertEquals(incomTest2.size(), 1);
            Assert.assertEquals(incomTest2.get(0).getId(), "What are values are accessed by?");

            Assert.assertEquals(wrongTest2.size(), 5);
            Assert.assertEquals(wrongTest2.get(0).getId(), "What’s a value");
            Assert.assertEquals(wrongTest2.get(1).getId(), "Write a function that will calculate if the substring 'the' is in any user input");
            Assert.assertEquals(wrongTest2.get(2).getId(), "Is a dictionary ordered?");
            Assert.assertEquals(wrongTest2.get(3).getId(), "When do you want to allow side effects?");
            Assert.assertEquals(wrongTest2.get(4).getId(), "Write a function that will calculate if the substring 'the' is in any user input");




        }catch (IOException e){
            Assert.fail("unable to load files");
        }
    }


//    @Test
//    public void randomGroupSuggestionsTest() throws Exception {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
//
//           List<List<String>> groupings = ckc.randomGroupSuggestions(2);
//
//            Assert.assertEquals(groupings.size(), 3);
//            Assert.assertEquals(groupings.get(0).size(),2);
//            Assert.assertEquals(groupings.get(1).size(), 2);
//            Assert.assertEquals(groupings.get(2).size(), 1);
//
//            Assert.assertNotEquals(groupings.get(0).get(0), groupings.get(0).get(1), groupings.get(1).get(0));
//
//            //groups of three
//            List<List<String>> groupings2 = ckc.randomGroupSuggestions(3);
//
//            Assert.assertEquals(groupings2.size(), 2);
//            Assert.assertEquals(groupings2.get(0).size(),3);
//            Assert.assertNotEquals(groupings2.get(0).get(0), groupings2.get(0).get(1), groupings2.get(0).get(2));
//
//
//        }catch (IOException e){
//            Assert.fail("unable to load files");
//        }
//    }


    @Test(expected = Exception.class)
    public void suggestionGroupSuggestions() throws Exception {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");

            List<List<String>> groupings = ckc.resourceGroupSuggestions(2);

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


            List<List<String>> groupings2 = ckc.resourceGroupSuggestions(3);

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


            try {
                List<List<String>> groupings3 = ckc.resourceGroupSuggestions(4);
            }catch (IOException e){
                Assert.fail("invalid group size");

            }


        }catch (IOException e){
            Assert.fail("unable to load files");
        }
    }



//    @Test
//    public void graphSumGroupSuggestions() throws Exception {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
//
//            List<List<String>> groupings = ckc.graphSumGroupSuggestions(2, "all");
//
//
//            Assert.assertEquals(groupings.size(),3 );
//            Assert.assertEquals(groupings.get(0).size(), 2);
//            Assert.assertEquals(groupings.get(1).size(), 2);
//            Assert.assertEquals(groupings.get(2).size(), 1);
//
//            Assert.assertEquals(groupings.get(0).get(0), "s4");
//            Assert.assertEquals(groupings.get(0).get(1), "s5");
//
//            Assert.assertEquals(groupings.get(1).get(0), "s3");
//            Assert.assertEquals(groupings.get(1).get(1), "s2");
//
//            Assert.assertEquals(groupings.get(2).get(0), "s1");
//
//
//            List<List<String>> groupings2 = ckc.graphSumGroupSuggestions( 2, "Expressions and Statements");
//
//            Assert.assertEquals(groupings2.size(),3 );
//            Assert.assertEquals(groupings2.get(0).size(), 2);
//            Assert.assertEquals(groupings2.get(1).size(), 2);
//            Assert.assertEquals(groupings2.get(2).size(), 1);
//
//            Assert.assertEquals(groupings2.get(0).get(0), "s4");
//            Assert.assertEquals(groupings2.get(0).get(1), "s5");
//
//            Assert.assertEquals(groupings2.get(1).get(0), "s3");
//            Assert.assertEquals(groupings2.get(1).get(1), "s2");
//
//
//            Assert.assertEquals(groupings2.get(2).get(0), "s1");
//
//
//            List<List<String>> grouping3 = ckc.graphSumGroupSuggestions(3, "all");
//
//            Assert.assertEquals(grouping3.size(),2 );
//            Assert.assertEquals(grouping3.get(0).size(), 3);
//            Assert.assertEquals(grouping3.get(1).size(), 2);
//
//            Assert.assertEquals(grouping3.get(0).get(0), "s4");
//            Assert.assertEquals(grouping3.get(0).get(1), "s5");
//            Assert.assertEquals(grouping3.get(0).get(2), "s1");
//
//            Assert.assertEquals(grouping3.get(1).get(0), "s3");
//            Assert.assertEquals(grouping3.get(1).get(1), "s2");
//
//        }catch (IOException e){
//            Assert.fail("unable to load files");
//        }
//    }


}
