package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.CohortConceptGraphsRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConceptKnowledgeCalculator implements ConceptKnowledgeCalculatorAPI{
    public enum Mode{
        STRUCTUREGRAPH, COHORTGRAPH
    }

    private Mode currentMode = null;

    private boolean hasmultipleAssessmentFile = false;
    private boolean hasMultipleResourceFiles = false;

    private static final String OUTPUT_PATH = "out/";

    //if user types in invalid input, the computer will create graph out of last valid input
    private String structureFileName;
    private String lastWorkingStructureName;

    private String [] previouslySavedCohortFiles;
    private String [] saveCohortFiles;

    //graphs
    private CohortConceptGraphs cohortConceptGraphs;
    private ConceptGraph structureGraph;

    //to replace just the graph
    private  String resourceFile;
    private String assessmentFile;

    public ConceptKnowledgeCalculator() {
        cohortConceptGraphs = null;
        structureGraph = null;

    }
    public ConceptKnowledgeCalculator(String structureFileName) throws IOException{
        clearAndCreateStructureData(structureFileName);
    }

    public ConceptKnowledgeCalculator(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException{
        clearAndCreateCohortData(structureFilename, resourceFilename, assessmentFilename);

    }


    @Override
    public void clearAndCreateStructureData(String structureFilename) throws IOException{
        structureGraph= null;
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFilename);

        structureGraph = new ConceptGraph(structureRecord);
        currentMode= Mode.STRUCTUREGRAPH;
        structureFileName = structureFilename;

    }

    @Override
    public void clearAndCreateCohortData(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException {
        cohortConceptGraphs = null;

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFilename);
        List<LearningObjectLinkRecord> linkRecord = LearningObjectLinkRecord.buildListFromJson(resourceFilename);
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        CSVReader csvReader = new CSVReader(assessmentFilename);
        List<LearningObjectResponse> assessments = csvReader.getManualGradedResponses();
        //add more


        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        //to use in console
        currentMode= Mode.COHORTGRAPH;
        structureFileName = structureFilename;
        resourceFile=resourceFilename;
        assessmentFile= assessmentFilename;

        String[] test = new String[]{structureFilename, resourceFile, assessmentFile};
        saveCohortFiles=test;



        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
        String file = OUTPUT_PATH + "ckcCurrent.json";
        toFile.writeToJson(file);
    }

    @Override
    public void addAnotherLO(String secondResourceFile) throws IOException{
        cohortConceptGraphs = null;

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFileName);


        List<LearningObjectLinkRecord> firstResource = LearningObjectLinkRecord.buildListFromJson(resourceFile);
        List<LearningObjectLinkRecord> secondResource = LearningObjectLinkRecord.buildListFromJson(secondResourceFile);

        List<LearningObjectLinkRecord> combinedResource = new LinkedList<>();
        combinedResource.addAll(firstResource);
        combinedResource.addAll(secondResource);

        ConceptGraph graph = new ConceptGraph(structureRecord, combinedResource);

        //create the data to be used to create and populate the graph copies
        CSVReader csvReader = new CSVReader(assessmentFile);
        List<LearningObjectResponse> assessments = csvReader.getManualGradedResponses();
        //add more


        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        //to use in console
        currentMode= Mode.COHORTGRAPH;
        hasMultipleResourceFiles=true;


        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
        String file = OUTPUT_PATH + "ckcCurrent.json";
        toFile.writeToJson(file);


    }

    @Override
    public void additionalLOR(String secondAssessmentFilename) throws IOException {
        cohortConceptGraphs = null;

        //create the graph structure to be copied for each user

        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFileName);
        List<LearningObjectLinkRecord> linkRecord = LearningObjectLinkRecord.buildListFromJson(resourceFile);
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //old assessment file name
        String oldAssessmentFile = assessmentFile;
        CSVReader csvR = new CSVReader(oldAssessmentFile);
        List<LearningObjectResponse> firstLOR = csvR.getManualGradedResponses();

        //new assessment file name
        CSVReader csvReader = new CSVReader(secondAssessmentFilename);
        List<LearningObjectResponse> assessments = csvReader.getManualGradedResponses();


        List<LearningObjectResponse> combinedAssessments = new ArrayList<>();
        combinedAssessments.addAll(firstLOR);
        combinedAssessments.addAll(assessments);


        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, combinedAssessments);

        //to use in console
        currentMode= Mode.COHORTGRAPH;
        hasmultipleAssessmentFile=true;


        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
        String file = OUTPUT_PATH + "ckcCurrent.json";
        toFile.writeToJson(file);
    }

    @Override
    public String getCohortGraphsUrl() {

            //TODO: need to find a way to offer a URL
            return "To view graph, right-click index.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

    }




    @Override
    public SuggestionResource calcIndividualGraphSuggestions(String userId) {
        try{
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userId);
                List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);
                return new SuggestionResource(userGraph, concepts);

            } else {
                return new SuggestionResource();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) {
        try{
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                try {
                    userGraph = cohortConceptGraphs.getUserGraph(userId);
                }catch(Exception e) {
                    e.printStackTrace();
//                    System.out.println("Can't find user");
                    userGraph= new ConceptGraph();
                }

                ConceptNode node;
                try {
                    node = userGraph.findNodeById(conceptId);
                }catch(Exception e) {
                    e.printStackTrace();
//                    System.out.println("Can't find the concept");
                    node = new ConceptNode();
                }

                List<ConceptNode> concepts = new ArrayList<ConceptNode>();
                concepts.add(node);

                return new SuggestionResource(userGraph, concepts);



            } else {
                return new SuggestionResource();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return new SuggestionResource();
    }


    @Override
    public List<String> calcIndividualConceptNodesSuggestions(String userID){
        try{
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                try {
                    userGraph = cohortConceptGraphs.getUserGraph(userID);

                }catch (Exception e){
                    e.printStackTrace();
                    userGraph= new ConceptGraph();
//                    System.out.println("Can't find that user");
                }

                if(userGraph!=null) {
                    List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(userGraph);

                    List<String> suggestedConceptIDList = new ArrayList<>();
                    for (ConceptNode node : nodeList) {
                        suggestedConceptIDList.add(node.getID());
                    }

                    return suggestedConceptIDList;
                }else{
                    return new ArrayList<>();
                }

            } else {
                return new ArrayList<>();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void setResourceFile(String file){
        resourceFile = file;
    }

    public String getResourceFile(){
        return resourceFile;
    }

    public void setAssessmentFile(String file){
        assessmentFile = file;
    }

    public String getAssessmentFile(){
        return assessmentFile;
    }

    public String getStructureFileName() {
        return structureFileName;
    }

    public void setStructureFileName(String file) {
        structureFileName= file;
    }


    public Mode getCurrentmode(){
        return currentMode;
    }

    public void setCurrentMode(Mode mode){
        currentMode = mode;
    }

    public void setLastWorkingStructureName(String fileName){
        lastWorkingStructureName = fileName;
    }

    public String getLastWorkingStructureName(){
        return lastWorkingStructureName;
    }


    public boolean gethasMultipleAssessment(){
        return hasmultipleAssessmentFile;
    }

    public String [] getPreviouslySavedCohortFile(){
        return previouslySavedCohortFiles;
    }

    public void setPreviouslySavedCohortFiles(String [] files){
        previouslySavedCohortFiles = files;
    }

    public String [] getSavedCohortFile(){
        return saveCohortFiles;
    }

    public void setSavedCohortFiles(String [] files){
        saveCohortFiles = files;
    }


    //for testing purposes

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }



}
