package edu.ithaca.dragonlab.ckc.util;

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

    @Test
    public void getAndSetModeTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }
        Assert.assertEquals(ckc.getCurrentmode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);

        ckc.setCurrentMode(ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);
        Assert.assertEquals(ckc.getCurrentmode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);
    }

    @Test
    public void getAndSetCohortData(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }

        Assert.assertEquals(ckc.getStructureFileName(),"test/testresources/basicRealisticExampleConceptGraphOneStudent.json" );
        ckc.setStructureFileName("test/testresources/mediumGraphTestConceptNodes.json");
        Assert.assertEquals(ckc.getStructureFileName(), "test/testresources/mediumGraphTestConceptNodes.json");


        Assert.assertEquals(ckc.getResourceFile(),"test/testresources/basicRealisticExampleLOLRecordOneStudent.json" );
        ckc.setResourceFile("test/testresources/mediumGraphLOL.json");
        Assert.assertEquals(ckc.getResourceFile(), "test/testresources/mediumGraphLOL.json");



        Assert.assertEquals(ckc.getAssessmentFile(),"test/testresources/basicRealisticExampleGradeBook2.csv" );
        ckc.setAssessmentFile("test/testresources/mediumGraphTestGradeBook.csv");
        Assert.assertEquals(ckc.getAssessmentFile(), "test/testresources/mediumGraphTestGradeBook.csv");


    }





    @Test
    public void calcIndividualConceptNodesSuggestionsTest(){

        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
                System.out.println("Unable to load default files, please choose files manually.");
                ckc = new ConceptKnowledgeCalculator();
        }

        List<ConceptNode> concepts = ckc.calcIndividualConceptNodesSuggestions("bspinache1");


        Assert.assertEquals(concepts.size(), 2);
        Assert.assertEquals(concepts.get(0).getID(), "If Statement");
        Assert.assertEquals(concepts.get(1).getID(), "While Loop");

    }





    @Test
    public void  calcIndividualGraphSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }

        SuggestionResource res = ckc.calcIndividualGraphSuggestions("bspinache1");
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
    public void calcIndividualSpecificConceptSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();

        }

        SuggestionResource resource = ckc.calcIndividualSpecificConceptSuggestions("bspinache1", "If Statement");
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
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
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
    }

//    @Test
//    public void addLOFileTest(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
//        } catch (IOException e) {
//            System.out.println("Unable to load default files, please choose files manually.");
//            ckc = new ConceptKnowledgeCalculator();
//        }
//
//        List<LearningObject> originalMasterList = new ArrayList<>();
//
//        CohortConceptGraphs originalGraphs = ckc.getCohortConceptGraphs();
//        ConceptGraph conGraph = originalGraphs.getAvgGraph();
//        Map<String, LearningObject> origLearningMap =  conGraph.getLearningObjectMap();
//
//        Collection<LearningObject> origLearningObList = origLearningMap.values();
//
//        originalMasterList.addAll(origLearningObList);
//
//        Assert.assertEquals(originalMasterList.size(), 14);
//        Assert.assertEquals(originalMasterList.get(0).getId(), "Q1");
//        Assert.assertEquals(originalMasterList.get(1).getId(), "Q2");
//        Assert.assertEquals(originalMasterList.get(2).getId(), "Q3");
//        Assert.assertEquals(originalMasterList.get(3).getId(), "Q4");
//        Assert.assertEquals(originalMasterList.get(4).getId(), "Q5");
//        Assert.assertEquals(originalMasterList.get(5).getId(), "Q6");
//        Assert.assertEquals(originalMasterList.get(6).getId(), "Q7");
//        Assert.assertEquals(originalMasterList.get(7).getId(), "Q8");
//        Assert.assertEquals(originalMasterList.get(8).getId(), "Q9");
//        Assert.assertEquals(originalMasterList.get(9).getId(), "Q11");
//        Assert.assertEquals(originalMasterList.get(10).getId(), "Q10");
//        Assert.assertEquals(originalMasterList.get(11).getId(), "Q13");
//        Assert.assertEquals(originalMasterList.get(12).getId(), "Q12");
//        Assert.assertEquals(originalMasterList.get(13).getId(), "Q14");
//
//
//
//        try {
//            ckc.addAnotherLO("test/testresources/simpleChangeNameLOL.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<LearningObject> postMasterList = new ArrayList<>();
//
//        CohortConceptGraphs postGraphs = ckc.getCohortConceptGraphs();
//        ConceptGraph postCG = postGraphs.getAvgGraph();
//        Map<String, LearningObject> postLOMap =  postCG.getLearningObjectMap();
//
//        Collection<LearningObject> postLOList = postLOMap.values();
//        postMasterList.addAll(postLOList);
//
//        //20!
//
////        for (LearningObject lo: postLOList){
////            System.out.println(lo.getId());
////        }
//
//    }

}
