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
        STRUCTUREGRAPH, COHORTGRAPH, STRUCTUREGRAPHWITHRESOURCE, STRUCTUREGRAPHWITHASSESSMENT
    }

    private Mode currentMode;

    //is only one file for structure and one for resource files.
    private List<String> structureFiles;
    private List<String> resourceFiles;

    //there is allowed multiple files for the assessment files
    private List<String> assessmentFiles;

    public ConceptKnowledgeCalculator() {
        cohortConceptGraphs = null;
        structureGraph = null;
        currentMode= null;
        structureFiles = new ArrayList<>();
        resourceFiles = new ArrayList<>();
        assessmentFiles = new ArrayList<>();
    }

    public ConceptKnowledgeCalculator(String structureFileName) throws IOException{
        this();
        structureFiles.add(structureFileName);
        currentMode= Mode.STRUCTUREGRAPH;
        clearAndCreateStructureData(new ArrayList<>());
    }

    public ConceptKnowledgeCalculator(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException{
        this();
        structureFiles.add(structureFilename);
        resourceFiles.add(resourceFilename);
        assessmentFiles.add(assessmentFilename);
        currentMode= Mode.COHORTGRAPH;
        clearAndCreateCohortData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public void clearAndCreateStructureData(List<String> structureFilename) throws IOException{
        structureGraph = null;
        cohortConceptGraphs=null;

        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));
        structureGraph = new ConceptGraph(structureRecord);

    }

    @Override
    public void clearAndCreateCohortData(List<String> structureFilename, List<String> resourceFilename, List<String> assessmentFilename) throws IOException {
        cohortConceptGraphs = null;
        structureGraph = null;

        //to change the structure file, clear the old list and add the new one.
        if (structureFilename.size()!=0 &&!structureFiles.contains(structureFilename.get(0))) {
            structureFiles.clear();
            structureFiles.add(structureFilename.get(0));
        }

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


        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
        String file = OUTPUT_PATH + "ckcCurrent.json";
        toFile.writeToJson(file);
    }

    @Override
    public void switchToStructure() throws Exception {
        if (currentMode == Mode.COHORTGRAPH){
            clearAndCreateStructureData(structureFiles);
            resourceFiles.clear();
            assessmentFiles.clear();
            currentMode = Mode.STRUCTUREGRAPH;
            cohortConceptGraphs=null;
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void replaceGraph(String graph) throws Exception{

        List<String>  structure = new ArrayList<>();
        structure.add(graph);
        clearAndCreateCohortData(structure,resourceFiles,assessmentFiles);

    }

    @Override
    public void addLORAndLO(String LO, String LOR) throws  Exception{
        if(currentMode == Mode.STRUCTUREGRAPH) {
            resourceFiles.add(LO);
            assessmentFiles.add(LOR);
            try{
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode = Mode.COHORTGRAPH;

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void addAnotherLO(String secondResourceFile) throws Exception {

        if(currentMode== Mode.STRUCTUREGRAPH || currentMode== Mode.STRUCTUREGRAPHWITHRESOURCE){
                resourceFiles.add(secondResourceFile);
                currentMode=Mode.STRUCTUREGRAPHWITHRESOURCE;

        }else if(currentMode== Mode.COHORTGRAPH || currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT){
            try {
                resourceFiles.add(secondResourceFile);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode= Mode.COHORTGRAPH;
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            throw new Exception("Wrong mode");
        }

    }

    @Override
    public void additionalLOR(String secondAssessmentFilename) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            assessmentFiles.add(secondAssessmentFilename);
            clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);

        } else if(currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE ){
            assessmentFiles.add(secondAssessmentFilename);
            try {
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode = Mode.COHORTGRAPH;
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT ){
            assessmentFiles.add(secondAssessmentFilename);

        }else if(currentMode== Mode.STRUCTUREGRAPH){
            try {
                assessmentFiles.add(secondAssessmentFilename);
                currentMode = Mode.STRUCTUREGRAPHWITHASSESSMENT;
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            throw new Exception("Wrong mode");

        }
    }

    @Override
    public void removeLORFile(String assessmentFile) throws Exception {
        if(assessmentFiles.size()<1){
            throw new Exception("You don't have any files");

        }else {
            if (currentMode == Mode.COHORTGRAPH) {
                assessmentFiles.remove(assessmentFile);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);

            }else if(currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT){
                assessmentFiles.remove(assessmentFile);

            }
        }
    }

    @Override
    public void replaceLOFile(String resourceFile) throws Exception {
        if(currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE || currentMode==Mode.COHORTGRAPH){
            resourceFiles.clear();
            resourceFiles.add(resourceFile);
            clearAndCreateCohortData(structureFiles,resourceFiles,assessmentFiles);

        }else if(currentMode==Mode.STRUCTUREGRAPH){
            try {
                resourceFiles.clear();
                resourceFiles.add(resourceFile);
                clearAndCreateStructureData(structureFiles);
                currentMode = Mode.STRUCTUREGRAPHWITHRESOURCE;
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT){
            try {
                resourceFiles.clear();
                resourceFiles.add(resourceFile);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode = Mode.COHORTGRAPH;
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public String getCohortGraphsUrl() throws Exception {
        if(currentMode==Mode.COHORTGRAPH){
            //depends on which mode
            //TODO: need to find a way to offer a URL
            return "To view graph, right-click index.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

        }else if(currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT){
            return "To view graph, right-click index.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

        }else if(currentMode==Mode.STRUCTUREGRAPH){
            return "To view graph, right-click index.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

        }else if(currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE){
            return "To view graph, right-click index.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public SuggestionResource calcIndividualGraphSuggestions(String userId) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userId);
                if (userGraph != null) {
                    List<ConceptNode> concepts;

                    concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);

                    return new SuggestionResource(userGraph, concepts);

                } else {
                    throw new Exception("Invalid User ID");
                }

            } else {
                return new SuggestionResource();
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userId);
                if (userGraph != null) {
                    ConceptNode node;
                    node = userGraph.findNodeById(conceptId);
                    if (node != null) {
                        List<ConceptNode> concepts = new ArrayList<ConceptNode>();
                        concepts.add(node);

                        return new SuggestionResource(userGraph, concepts);

                    } else {
                        throw new Exception("Invalid Concept");
                    }
                } else {
                    throw new Exception("Invalid User ID");
                }
            } else {
                return new SuggestionResource();
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }


    @Override
    public List<String> calcIndividualConceptNodesSuggestions(String userID) throws Exception{
        if(currentMode==Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userID);

                if (userGraph != null) {
                    List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(userGraph);
                    List<String> suggestedConceptIDList = new ArrayList<>();
                    for (ConceptNode node : nodeList) {
                        suggestedConceptIDList.add(node.getID());
                    }

                    return suggestedConceptIDList;
                } else {
                    throw new Exception("Invalid User ID");
                }
            } else {
                return new ArrayList<>();
            }
        }else{
            throw new Exception("Wrong Mode");
        }
    }

    public double getLearningObjectAvg(String learningObject) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            ConceptGraph graph = cohortConceptGraphs.getAvgGraph();
            Map<String, LearningObject> loMap = graph.getLearningObjectMap();
            Collection<LearningObject> objList = loMap.values();
            ArrayList<LearningObject> list;
            if (objList instanceof List) {
                list = (ArrayList<LearningObject>) objList;
            } else {
                list = new ArrayList<LearningObject>(objList);
            }
        KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(list);
        LearningObject concept = loMap.get(learningObject);
        double result = BasicRFunctions.LearningObjectAvg(myMatrix, concept);

        return result;
        }else{
            throw new Exception("Wrong Mode");
        }
    }


    public List<String> getAssessmentFiles(){
        return assessmentFiles;
    }

    public Mode getCurrentMode(){
        return currentMode;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }


    //testing purposes
    public List<String> getStructureFiles(){
        return structureFiles;
    }

    public List<String> getResourceFiles(){
        return resourceFiles;
    }

}
