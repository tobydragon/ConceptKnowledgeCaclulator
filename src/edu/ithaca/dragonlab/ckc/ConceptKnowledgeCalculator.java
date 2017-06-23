package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.CohortConceptGraphsRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import stats.BasicRFunctions;

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
    private String structureFileName;
    private String lastWorkingStructureName;

    //saved in order of : structure file name, resource file name, assessment file name
    private List<String> previouslySavedCohortFiles;
    private List<String> saveCohortFiles;


    //to replace just the graph
    private String assessmentFile;
    private  String resourceFile;

    private boolean hasmultipleAssessmentFile;
    private boolean hasMultipleResourceFiles;

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
        hasmultipleAssessmentFile = false;
        hasMultipleResourceFiles = false;

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

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        //to use in console
        currentMode= Mode.COHORTGRAPH;
        structureFileName = structureFilename;
        resourceFile=resourceFilename;
        assessmentFile= assessmentFilename;
        hasmultipleAssessmentFile = false;
        hasMultipleResourceFiles = false;


        List<String> files = new ArrayList<>();
        files.addAll(Arrays.asList(structureFilename,resourceFilename,assessmentFilename));
        saveCohortFiles = files;

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

        //the list of the combined learning objects
        List<LearningObjectLinkRecord> combinedResource = new LinkedList<>();
        combinedResource.addAll(firstResource);
        combinedResource.addAll(secondResource);

        ConceptGraph graph = new ConceptGraph(structureRecord, combinedResource);

        //create the data to be used to create and populate the graph copies
        CSVReader csvReader = new CSVReader(assessmentFile);
        List<LearningObjectResponse> assessments = csvReader.getManualGradedResponses();

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

        //combined assignment list
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
        KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(list);
        LearningObject concept = loMap.get(learningObject);
        double result = BasicRFunctions.LearningObjectAvg(myMatrix, concept);

        return result;
    }



    public String getResourceFile(){
        return resourceFile;
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

    public boolean getHasMultipleResource(){
        return hasMultipleResourceFiles;
    }


    public  List<String>  getPreviouslySavedCohortFile(){
        return previouslySavedCohortFiles;
    }

    public void setPreviouslySavedCohortFiles(List<String> files){
        previouslySavedCohortFiles = files;
    }

    public List<String> getSavedCohortFile(){
        return saveCohortFiles;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }


    //for testing purposes

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }




}
