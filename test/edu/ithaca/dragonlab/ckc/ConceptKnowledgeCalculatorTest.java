package edu.ithaca.dragonlab.ckc;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Mia Kimmich Mitchell on 6/9/2017.
 */
public class ConceptKnowledgeCalculatorTest {

    static Logger logger = LogManager.getLogger(ConceptKnowledgeCalculator.class);

//    @Test
//    public void getAndSetModeTest(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
//        } catch (IOException e) {
//            Assert.fail("Unable to load default files. Test unable to run");
//
//        }
//        Assert.assertEquals(ckc.getCurrentmode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);
//
//        ckc.setCurrentMode(ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);
//        Assert.assertEquals(ckc.getCurrentmode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);
//    }
//
//    @Test
//    public void getAndSetCohortData(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
//        } catch (IOException e) {
//            Assert.fail("Unable to load default files. Test unable to run");
//
//        }
//
//        Assert.assertEquals(ckc.getStructureFileNames().get(0),"test/testresources/basicRealisticExampleConceptGraphOneStudent.json" );
//        List<String> structureTest = new ArrayList<>();
//        structureTest.add("test/testresources/mediumGraphTestConceptNodes.json");
//        ckc.setStructureFiles(structureTest);
//        Assert.assertEquals(ckc.getStructureFiles().get(0), "test/testresources/mediumGraphTestConceptNodes.json");
//    }
//




    @Test
    public void calcIndividualConceptNodesSuggestionsTest(){

        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        List<String> concepts = null;
        try {
            concepts = ckc.calcIndividualConceptNodesSuggestions("bspinache1");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Assert.assertEquals(concepts.size(), 2);
        Assert.assertEquals(concepts.get(0), "If Statement");
        Assert.assertEquals(concepts.get(1), "While Loop");

    }

    @Test(expected = Exception.class)
    public void calcIndividualConceptNodesSuggestionsBadInputTest() throws Exception {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        ckc.calcIndividualConceptNodesSuggestions("baduser");

    }

    @Test
    public void  calcIndividualGraphSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        SuggestionResource res = null;
        try {
            res = ckc.calcIndividualGraphSuggestions("bspinache1");
        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }
        List<LearningObjectSuggestion> incomTest = res.incompleteList;
        List<LearningObjectSuggestion> wrongTest = res.wrongList;

        Assert.assertEquals(incomTest.size(),5);
        Assert.assertEquals(incomTest.get(0).getId(),"Q10");

        Assert.assertEquals(wrongTest.size(),3);
        Assert.assertEquals(wrongTest.get(0).getId(), "Q9");

    }

    @Test
    public void  realDataCalcIndividualGraphSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc =new ConceptKnowledgeCalculator("resources/comp220/comp220Graph.json",
                    "resources/comp220/comp220Resources.json",
                    "localresources/comp220ExampleDataPortion.csv");

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        SuggestionResource res = null;
        try {
            res = ckc.calcIndividualGraphSuggestions("s13");
        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }
        List<LearningObjectSuggestion> incomTest = res.incompleteList;
        List<LearningObjectSuggestion> wrongTest = res.wrongList;


        Assert.assertEquals(incomTest.size(),5);
        Assert.assertEquals(incomTest.get(0).getId(),"Q10");
        Assert.assertEquals(incomTest.get(1).getId(),"Q10");
        Assert.assertEquals(incomTest.get(2).getId(),"Q3");
        Assert.assertEquals(incomTest.get(3).getId(),"Q6");
        Assert.assertEquals(incomTest.get(4).getId(),"Q6");

        Assert.assertEquals(wrongTest.size(),3);
        Assert.assertEquals(wrongTest.get(0).getId(), "Q9");
        Assert.assertEquals(wrongTest.get(1).getId(), "Q9");
        Assert.assertEquals(wrongTest.get(2).getId(), "Q1");

    }

    @Test
    public void  calcIndividualGraphSuggestionsWIthEmptyListTest() throws Exception {
            ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("resources/comp220/comp220Graph.json","resources/comp220/comp220Resources.json", "localresources/comp220ExampleDataPortion.csv");

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }

        SuggestionResource res = ckc.calcIndividualGraphSuggestions("s17");

        List<LearningObjectSuggestion> incomTest = res.incompleteList;
        List<LearningObjectSuggestion> wrongTest = res.wrongList;

        Assert.assertEquals(incomTest,new ArrayList<>());
        Assert.assertEquals(wrongTest.get(0).getId(), "Lab 5: Comparing Searches");

    }


    @Test
    public void calcIndividualSpecificConceptSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");


        }

        SuggestionResource resource = null;
        try {
            resource = ckc.calcIndividualSpecificConceptSuggestions("bspinache1", "If Statement");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(resource.incompleteList.get(0).getId(),"Q10" );
        Assert.assertEquals(resource.incompleteList.get(1).getId(),"Q3" );
        Assert.assertEquals(resource.incompleteList.get(2).getId(),"Q6" );

        Assert.assertEquals(resource.wrongList.get(0).getId(),"Q9" );
        Assert.assertEquals(resource.wrongList.get(1).getId(),"Q1" );

    }

    @Test
    public void additionalLORTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }

        List<LearningObjectResponse> originalMasterList = new ArrayList<>();

        CohortConceptGraphs originalGraphs = ckc.getCohortConceptGraphs();
        ConceptGraph conGraph = originalGraphs.getAvgGraph();
        Map<String, LearningObject> origLearningMap =  conGraph.getLearningObjectMap();

        Collection<LearningObject> origLearningObList = origLearningMap.values();
        for (LearningObject origLearningOb: origLearningObList){
            List<LearningObjectResponse> origLOR= origLearningOb.getResponses();
            originalMasterList.addAll(origLOR);
        }


        Assert.assertEquals(originalMasterList.size(), 10);
        Assert.assertEquals(originalMasterList.get(0).getLearningObjectId(), "Q1");
        Assert.assertEquals(originalMasterList.get(1).getLearningObjectId(), "Q2");
        Assert.assertEquals(originalMasterList.get(2).getLearningObjectId(), "Q4");
        Assert.assertEquals(originalMasterList.get(3).getLearningObjectId(), "Q5");
        Assert.assertEquals(originalMasterList.get(4).getLearningObjectId(), "Q7");
        Assert.assertEquals(originalMasterList.get(5).getLearningObjectId(), "Q8");
        Assert.assertEquals(originalMasterList.get(6).getLearningObjectId(), "Q9");
        Assert.assertEquals(originalMasterList.get(7).getLearningObjectId(), "Q11");
        Assert.assertEquals(originalMasterList.get(8).getLearningObjectId(), "Q12");
        Assert.assertEquals(originalMasterList.get(9).getLearningObjectId(), "Q14");

        try {
            ckc.additionalLOR("test/testresources/mediumGraphTestGradeBook.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<LearningObjectResponse> postMasterList1 = new ArrayList<>();

        CohortConceptGraphs postGraphs = ckc.getCohortConceptGraphs();
        ConceptGraph postCG = postGraphs.getAvgGraph();
        Map<String, LearningObject> postLOMap =  postCG.getLearningObjectMap();


        Collection<LearningObject> postLOList = postLOMap.values();
        for (LearningObject postlearningObject: postLOList){
            List<LearningObjectResponse> postLOR = postlearningObject.getResponses();
            postMasterList1.addAll(postLOR);
        }

        Assert.assertEquals(postMasterList1.size(), 18);
        Assert.assertEquals(originalMasterList.get(0).getLearningObjectId(), "Q1");
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
        Assert.assertEquals(postMasterList1.get(16).getLearningObjectId(), "Q14");
        Assert.assertEquals(postMasterList1.get(17).getLearningObjectId(), "Q14");


        try {
            ckc.additionalLOR("test/testresources/simpleGraphTest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<LearningObjectResponse> postMasterList2 = new ArrayList<>();

        CohortConceptGraphs postGraphs2 = ckc.getCohortConceptGraphs();
        ConceptGraph postCG2 = postGraphs2.getAvgGraph();
        Map<String, LearningObject> postLOMap2 =  postCG2.getLearningObjectMap();

        Collection<LearningObject> postLOList2 = postLOMap2.values();
        for (LearningObject postlearningObject2: postLOList2){
            List<LearningObjectResponse> postLOR2 = postlearningObject2.getResponses();
            postMasterList2.addAll(postLOR2);
        }


        Assert.assertEquals(postMasterList2.size(), 24);
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
        Assert.assertEquals(postMasterList2.get(22).getLearningObjectId(), "Q14");

    }

    @Test
    public void addLOFileTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }

        List<LearningObject> originalMasterList = new ArrayList<>();

        CohortConceptGraphs originalGraphs = ckc.getCohortConceptGraphs();
        ConceptGraph conGraph = originalGraphs.getAvgGraph();
        Map<String, LearningObject> origLearningMap =  conGraph.getLearningObjectMap();

        Collection<LearningObject> origLearningObList = origLearningMap.values();

        originalMasterList.addAll(origLearningObList);

        Assert.assertEquals(originalMasterList.size(), 14);
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
        Assert.assertEquals(originalMasterList.get(13).getId(), "Q14");



        try {
            ckc.addAnotherLO("test/testresources/simpleChangeNameLOL.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<LearningObject> postMasterList = new ArrayList<>();

        CohortConceptGraphs postGraphs = ckc.getCohortConceptGraphs();
        ConceptGraph postCG = postGraphs.getAvgGraph();
        Map<String, LearningObject> postLOMap =  postCG.getLearningObjectMap();
        Collection<LearningObject> postLOList = postLOMap.values();
        postMasterList.addAll(postLOList);

        Assert.assertEquals(postMasterList.size(), 19);
        Assert.assertEquals(postMasterList.get(0).getId(), "Q1");
        Assert.assertEquals(postMasterList.get(1).getId(), "Q2");
        Assert.assertEquals(postMasterList.get(2).getId(), "Q3");
        Assert.assertEquals(postMasterList.get(3).getId(), "Q4");
        Assert.assertEquals(postMasterList.get(4).getId(), "Q5");
        Assert.assertEquals(postMasterList.get(5).getId(), "Q6");
        Assert.assertEquals(postMasterList.get(6).getId(), "Q7");
        Assert.assertEquals(postMasterList.get(7).getId(), "Q8");
        Assert.assertEquals(postMasterList.get(8).getId(), "Q9");
        Assert.assertEquals(postMasterList.get(9).getId(), "resource2");
        Assert.assertEquals(postMasterList.get(10).getId(), "resource3");
        Assert.assertEquals(postMasterList.get(11).getId(), "resource4");
        Assert.assertEquals(postMasterList.get(12).getId(), "resource5");
        Assert.assertEquals(postMasterList.get(13).getId(), "resource1");
        Assert.assertEquals(postMasterList.get(14).getId(), "Q11");
        Assert.assertEquals(postMasterList.get(15).getId(), "Q10");
        Assert.assertEquals(postMasterList.get(16).getId(), "Q13");
        Assert.assertEquals(postMasterList.get(17).getId(), "Q12");
        Assert.assertEquals(postMasterList.get(18).getId(), "Q14");

    }

    @Test
    public void getConceptAvgTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files, please choose files manually.");
        }
        Assert.assertEquals(1, ckc.getLearningObjectAvg("Q4"), 0);
        Assert.assertEquals(0.75, ckc.getLearningObjectAvg("Q14"), 0);
    }

}
