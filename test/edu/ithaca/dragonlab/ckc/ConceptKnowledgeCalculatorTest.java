package edu.ithaca.dragonlab.ckc;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
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
    public void getModeTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }

    @Test
    public void getCohortData(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
            Assert.assertEquals(ckc.getStructureFiles().get(0),"test/testresources/basicRealisticExampleConceptGraphOneStudent.json" );
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }


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

            Assert.assertEquals(concepts.size(), 2);
            Assert.assertEquals(concepts.get(0), "If Statement");
            Assert.assertEquals(concepts.get(1), "While Loop");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(expected = Exception.class)
    public void calcIndividualConceptNodesSuggestionsBadInputTest() throws Exception {
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
            ckc.calcIndividualConceptNodesSuggestions("baduser");

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }


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
            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest.size(),5);
            Assert.assertEquals(incomTest.get(0).getId(),"Q10");

            Assert.assertEquals(wrongTest.size(),3);
            Assert.assertEquals(wrongTest.get(0).getId(), "Q9");
        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }


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
            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest.size(), 0);
            Assert.assertEquals(incomTest, new ArrayList<>());

            Assert.assertEquals(wrongTest.size(), 1);
            Assert.assertEquals(wrongTest.get(0).getId(), "Lab 4: Recursion");


        } catch (Exception e) {
            Assert.fail("Unable to find user");
        }

    }

    @Test
    public void  calcIndividualGraphSuggestionsWIthEmptyListTest() throws Exception {
            ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("resources/comp220/comp220Graph.json","resources/comp220/comp220Resources.json", "localresources/comp220ExampleDataPortion.csv");
            SuggestionResource res = ckc.calcIndividualGraphSuggestions("s17");

            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            Assert.assertEquals(incomTest,new ArrayList<>());
            Assert.assertEquals(wrongTest.get(0).getId(), "Lab 5: Comparing Searches");
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }



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
            Assert.assertEquals(resource.incompleteList.get(0).getId(),"Q10" );
            Assert.assertEquals(resource.incompleteList.get(1).getId(),"Q3" );
            Assert.assertEquals(resource.incompleteList.get(2).getId(),"Q6" );

            Assert.assertEquals(resource.wrongList.get(0).getId(),"Q9" );
            Assert.assertEquals(resource.wrongList.get(1).getId(),"Q1" );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void additionalLORTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");

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

        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }



        try {
            ckc.additionalLOR("test/testresources/mediumGraphTestGradeBook.csv");
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
            Assert.assertEquals(postMasterList1.get(16).getLearningObjectId(), "Q14");
            Assert.assertEquals(postMasterList1.get(17).getLearningObjectId(), "Q14");

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            ckc.additionalLOR("test/testresources/simpleGraphTest.csv");
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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void removeLORFileTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
            List<String> test = new ArrayList<>();
            test.add("test/testresources/basicRealisticExampleGradeBook2.csv");
            Assert.assertEquals(ckc.getAssessmentFiles(),test);
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }


        try {
            ckc.removeLORFile("test/testresources/basicRealisticExampleGradeBook2.csv");
            List<String> test1 = new ArrayList<>();
            Assert.assertEquals(ckc.getAssessmentFiles(),test1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(expected = Exception.class)
    public void removeEmptyLORList() throws Exception{
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
        try {
            ckc.removeLORFile("test/testresources/basicRealisticExampleGradeBook2.csv");
            ckc.removeLORFile("test/remove");

        } catch (IOException e) {
            Assert.fail("There are negative files");
        }
    }

    @Test
    public void replaceLOFile (){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
            List<String> test = new ArrayList<>();
            test.add("test/testresources/basicRealisticExampleLOLRecordOneStudent.json");
            Assert.assertEquals(ckc.getResourceFiles(),test);
            ckc.replaceLOFile("test/testresources/simpleChangeNameLOL.json");

            test.clear();
            test.add("test/testresources/simpleChangeNameLOL.json");

            Assert.assertEquals(ckc.getResourceFiles(), test);

        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");
        }
    }

    @Test
    public void addLOFileTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");

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
        } catch (IOException e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }




        try {
            ckc.addAnotherLO("test/testresources/simpleChangeNameLOL.json");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConceptAvgTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            Assert.fail("Unable to load default files, please choose files manually.");
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
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");

            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);
            //the constructor starts out as a cohort concept graph and there should only be one file in each of the lists of files. The structure graph should be null, because we're not on that mode
            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(),getTest );
            getTest.clear();

            getTest.add( "test/testresources/basicRealisticExampleLOLRecordOneStudent.json");
            Assert.assertEquals(ckc.getResourceFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/basicRealisticExampleGradeBook2.csv");
            Assert.assertEquals(ckc.getAssessmentFiles(), getTest);

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);
            getTest.clear();


            //STRUCTURE MODE
            //now in structure mode. Because of this cohort concept graph should be null as well as all the extra data (LO and LOR files). Structure graph should not be null now because we are in that mode.
            //it should hold on to blank resource and assessment files. The structure file should have one file in it.
            ckc.switchToStructure();
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);

            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);

            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(), getTest);

            Assert.assertEquals(ckc.getResourceFiles(), new ArrayList<>());
            Assert.assertEquals(ckc.getAssessmentFiles(), new ArrayList<>());

            Assert.assertNotEquals(ckc.getStructureGraph(), null);
            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);
            getTest.clear();


            //STRUCTURE MODE WITH ASSESSMENT
            //switched to structure mode with assessment. This should still have a structure graph, but then also hold on to the files. Because additional LOR was called from structure graph mode, the assessment file should not be filled with the one file.
            ckc.additionalLOR("test/testresources/mediumGraphTestGradeBook.csv");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHASSESSMENT);

            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(), getTest);
            getTest.clear();

            Assert.assertEquals(ckc.getResourceFiles(), new ArrayList<>());

            getTest.add("test/testresources/mediumGraphTestGradeBook.csv");
            Assert.assertEquals(ckc.getAssessmentFiles(), getTest);

            Assert.assertNotEquals(ckc.getStructureGraph(), null);
            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);
            getTest.clear();


            //CONCEPT GRAPH MODE
            //switched back into concept graph mode because now the three lists of files are filled up with at least one file.
            //all of the previous files should be stored in structure files, resource files, and assessment files
            ckc.addAnotherLO("test/testresources/simpleGraphTestLearningObjects.json");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);
            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/simpleGraphTestLearningObjects.json");
            Assert.assertEquals(ckc.getResourceFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/mediumGraphTestGradeBook.csv");
            Assert.assertEquals(ckc.getAssessmentFiles(),getTest);
            getTest.clear();

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);




            //STRUCTURE GRAPH WITH RESOURCE
            //to test more, going from cohort graph to structure graph.
            // changing from structure graph to structure graph with resources. Cohort graph should be null and strucuture graph shouldn't be. The structure file should have one file in it and resource file list should have one in it. The assessment file should be empty
            ckc.switchToStructure();

            ckc.addAnotherLO("test/testresources/simpleGraphTestLearningObjects.json");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE);
            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/simpleGraphTestLearningObjects.json");
            Assert.assertEquals(ckc.getResourceFiles(),  getTest);
            getTest.clear();

            Assert.assertEquals(ckc.getAssessmentFiles(), new ArrayList<>());
            Assert.assertNotEquals(ckc.getStructureGraph(), null);
            Assert.assertEquals(ckc.getCohortConceptGraphs(), null);


            //COHORT GRAPH
            //this is switching from structure graph mode with resources to cohort graph mode. All the lists of files should have at least one file in them and structure graph should equal null;
            ckc.additionalLOR("test/testresources/simpleGraphTest.csv");
            Assert.assertEquals(ckc.getCurrentMode(), ConceptKnowledgeCalculator.Mode.COHORTGRAPH);

            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/simpleGraphTestLearningObjects.json");
            Assert.assertEquals(ckc.getResourceFiles(),  getTest);
            getTest.clear();

            getTest.add("test/testresources/simpleGraphTest.csv");
            Assert.assertEquals(ckc.getAssessmentFiles(),getTest);
            getTest.clear();

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);



            //STRUCTURE GRAPH WITH RESOURCE
            //to further testing, going back to structure more
            //to switch from structure graph with resource, adding LOR makes all the file lists have at least one file in them, thus a cohort graph can be made. Structure graph should not equal null and all file lists should have one file in them
            ckc.switchToStructure();

            ckc.addLORAndLO("test/testresources/simpleGraphTestLearningObjects.json", "test/testresources/simpleGraphTest.csv" );
            getTest.add("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            Assert.assertEquals(ckc.getStructureFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/simpleGraphTestLearningObjects.json");
            Assert.assertEquals(ckc.getResourceFiles(), getTest);
            getTest.clear();

            getTest.add("test/testresources/simpleGraphTest.csv" );
            Assert.assertEquals(ckc.getAssessmentFiles(), getTest);

            Assert.assertEquals(ckc.getStructureGraph(), null);
            Assert.assertNotEquals(ckc.getCohortConceptGraphs(), null);


        } catch (Exception e) {
            Assert.fail("Unable to load default files. Test unable to run");

        }
    }

}
