package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
//import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.CohortConceptGraphsRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
//import stats.BasicRFunctions;

import java.io.IOException;
import java.util.*;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConceptKnowledgeCalculator implements ConceptKnowledgeCalculatorAPI{
    private static final String OUTPUT_PATH = "out/";

    //graphs
    private CohortConceptGraphs cohortConceptGraphs;
    private ConceptGraph structureGraph;


    public enum Mode{
        STRUCTUREGRAPH, COHORTGRAPH
    }

    private Mode currentMode = null;

    //if user types in invalid input, the computer will create graph out of last valid input
    private List<String> structureFileName;
    private List<String> lastWorkingStructureName;

    //saved in order of : structure file name, resource file name, assessment file name
    private List<List<String>> previouslySavedCohortFiles;
    private List<List<String>> saveCohortFiles;


    private List<String> structureFiles;
    private List<String> resourceFiles;
    private List<String> assessmentFiles;



    public ConceptKnowledgeCalculator() {
        cohortConceptGraphs = null;
        structureGraph = null;

    }

    public ConceptKnowledgeCalculator(String structureFileName) throws IOException{
        structureFiles = new ArrayList<>();

        List<String> struct = new ArrayList<>();
        clearAndCreateStructureData(struct);
    }

    public ConceptKnowledgeCalculator(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException{
        structureFiles = new ArrayList<>();
        resourceFiles= new ArrayList<>();
        assessmentFiles = new ArrayList<>();

        List<String> struct= new ArrayList<>();
        List<String> resource = new ArrayList<>();
        List<String> assess= new ArrayList<>();

        struct.add(structureFilename);
        resource.add(resourceFilename);
        assess.add(assessmentFilename);

        clearAndCreateCohortData(struct, resource, assess);

    }

    @Override
    public void clearAndCreateStructureData(List<String> structureFilename) throws IOException{
        structureGraph= null;

        structureFiles.add(structureFilename.get(0));
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));



        structureGraph = new ConceptGraph(structureRecord);
        currentMode= Mode.STRUCTUREGRAPH;

    }

    @Override
    public void clearAndCreateCohortData(List<String> structureFilename, List<String> resourceFilename, List<String> assessmentFilename) throws IOException {
        cohortConceptGraphs = null;

        structureFiles.addAll(structureFilename);
        resourceFiles.addAll(resourceFilename);
        assessmentFiles.addAll(assessmentFilename);

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));


        List<LearningObjectLinkRecord> linkRecord = new ArrayList<>();
        for (String rFiles : resourceFiles){
            List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(rFiles);
            linkRecord.addAll(temp);
        }

        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        List<LearningObjectResponse> assessments = new ArrayList<>();
        for (String aname: assessmentFiles){
            CSVReader csvReader = new CSVReader(aname);
            List<LearningObjectResponse> temp = csvReader.getManualGradedResponses();
            assessments.addAll(temp);
        }

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        //to use in console
        currentMode= Mode.COHORTGRAPH;
//
//        saveCohortFiles.add(structureFiles);
//        saveCohortFiles.add(resourceFiles);
//        saveCohortFiles.add(assessmentFiles);

        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
        String file = OUTPUT_PATH + "ckcCurrent.json";
        toFile.writeToJson(file);
    }

    @Override
    public void addAnotherLO(String secondResourceFile) throws IOException {

        resourceFiles.add(secondResourceFile);
        clearAndCreateCohortData(structureFiles,resourceFiles,assessmentFiles);

    }

    @Override
    public void additionalLOR(String secondAssessmentFilename) throws IOException {
        assessmentFiles.add(secondAssessmentFilename);
        clearAndCreateCohortData(structureFiles,resourceFiles,assessmentFiles);


    }

    @Override
    public String getCohortGraphsUrl() {

        //depends on which mode
            //TODO: need to find a way to offer a URL
            return "To view graph, right-click index.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

    }

    @Override
    public SuggestionResource calcIndividualGraphSuggestions(String userId) throws Exception {

        if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userId);
                if (userGraph!=null){
                    List<ConceptNode> concepts;

                    concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);
                    System.out.println(concepts);


                    return new SuggestionResource(userGraph, concepts);

                }else {
                    throw new Exception("Invalid User ID");
                }

        } else {
            return new SuggestionResource();
        }
    }

    @Override
    public SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) throws Exception {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userId);
                if(userGraph!=null){
                    ConceptNode node;
                    node = userGraph.findNodeById(conceptId);
                    if(node!= null){
                        List<ConceptNode> concepts = new ArrayList<ConceptNode>();
                        concepts.add(node);

                        return new SuggestionResource(userGraph, concepts);

                    }else{
                        throw new Exception("Invalid Concept");
                    }
                }else{
                    throw new Exception("Invalid User ID");
                }
            } else {
                return new SuggestionResource();
            }
    }


    @Override
    public List<String> calcIndividualConceptNodesSuggestions(String userID) throws Exception{
        if (cohortConceptGraphs != null) {
            ConceptGraph userGraph;
            userGraph = cohortConceptGraphs.getUserGraph(userID);

            if(userGraph!=null) {
                List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(userGraph);
                List<String> suggestedConceptIDList = new ArrayList<>();
                for (ConceptNode node : nodeList) {
                    suggestedConceptIDList.add(node.getID());
                }

                return suggestedConceptIDList;
            }else{
                throw new Exception("Invalid User ID");
            }
        } else {
            return new ArrayList<>();
        }
    }


    public double getLearningObjectAvg(String learningObject){
        ConceptGraph graph = cohortConceptGraphs.getAvgGraph();
        Map<String, LearningObject> loMap = graph.getLearningObjectMap();
        Collection<LearningObject> objList = loMap.values();
        ArrayList<LearningObject> list;
        if (objList instanceof List)
        {
            list = (ArrayList<LearningObject>) objList;
        }
        else
        {
            list = new ArrayList<LearningObject>(objList);
        }
//        KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(list);
//        LearningObject concept = loMap.get(learningObject);
//        double result = BasicRFunctions.LearningObjectAvg(myMatrix, concept);
//
//        return result;

        return 0;
    }


    public List<String> getResourceFiles(){
        return resourceFiles;
    }

    public List<String> getAssessmentFiles(){
        return assessmentFiles;
    }

    public List<String> getStructureFileNames() {
        return structureFiles;
    }



    public Mode getCurrentmode(){
        return currentMode;
    }

    public void setCurrentMode(Mode mode){
        currentMode = mode;
    }

    public void setStructureFiles(List<String> file) {
        structureFiles= file;
    }

    public void setLastWorkingStructureName(List<String> fileName){
        lastWorkingStructureName = fileName;
    }

    public List<String> getLastWorkingStructureName(){
        return lastWorkingStructureName;
    }

    public  List<List<String>> getPreviouslySavedCohortFile(){
        return previouslySavedCohortFiles;
    }

    public void setPreviouslySavedCohortFiles(List<String> files){
        previouslySavedCohortFiles.add(files);
    }

    public List<List<String>> getSavedCohortFile(){
        return saveCohortFiles;
    }


    public List<String> getStructureFiles(){
        return structureFiles;
    }


    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }


    //for testing purposes

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }




}
