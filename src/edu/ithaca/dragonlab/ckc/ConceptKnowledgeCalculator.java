package edu.ithaca.dragonlab.ckc;

import com.sun.org.apache.xpath.internal.operations.Mod;
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
        structureGraph= null;

        structureFiles.add(structureFilename.get(0));
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));

        structureGraph = new ConceptGraph(structureRecord);
        currentMode= Mode.STRUCTUREGRAPH;

    }

    @Override
    public void clearAndCreateCohortData(List<String> structureFilename, List<String> resourceFilename, List<String> assessmentFilename) throws IOException {
        cohortConceptGraphs = null;

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

        //to use in console
        currentMode= Mode.COHORTGRAPH;

        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
        String file = OUTPUT_PATH + "ckcCurrent.json";
        toFile.writeToJson(file);
    }

    @Override
    public void addAnotherLO(String secondResourceFile) throws Exception {
        if(currentMode==Mode.STRUCTUREGRAPH || currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT|| currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE || currentMode==Mode.COHORTGRAPH) {
            resourceFiles.add(secondResourceFile);
            clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void additionalLOR(String secondAssessmentFilename) throws Exception {
        if(currentMode==Mode.COHORTGRAPH || currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE || currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT) {
            assessmentFiles.add(secondAssessmentFilename);
            clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void removeLORFile(String assessmentFile) throws Exception {
        if(currentMode==Mode.COHORTGRAPH || currentMode== Mode.STRUCTUREGRAPHWITHASSESSMENT){
            if (assessmentFiles.size()<1){
                throw new Exception("You don't have any files to remove");
            }else {
                assessmentFiles.remove(assessmentFile);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
            }
        }else{
            throw new Exception("Wrong mode");
        }

    }

    @Override
    public void replaceLOFile(String resourceFile) throws Exception {
        if(currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE || currentMode==Mode.COHORTGRAPH){
            resourceFiles.clear();
            resourceFiles.add(resourceFile);

            clearAndCreateCohortData(structureFiles,resourceFiles,assessmentFiles);
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
            throw new Exception("Wrong mdoe");
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
                    System.out.println(concepts);


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


    public List<String> getStructureFiles(){
        return structureFiles;
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

    public Mode getCurrentMode(){
        return currentMode;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }

}
