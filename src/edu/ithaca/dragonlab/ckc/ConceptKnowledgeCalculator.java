package edu.ithaca.dragonlab.ckc;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.CohortConceptGraphsRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConceptKnowledgeCalculator implements ConceptKnowledgeCalculatorAPI{
    public enum Mode{
        STRUCTUREGRAPH, COHORTGRAPH
    }

    public Mode currentMode = null;


    private static final String OUTPUT_PATH = "out/";

    //if user types in invalid input, the computer will create graph out of last valid input
    private String structureFileName;
    private String lastWorkingStructureName;

    //graphs
    private CohortConceptGraphs cohortConceptGraphs;
    private ConceptGraph structureGraph;

    //to replace just the graph
    private String structureFile;
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


        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);
        currentMode= Mode.COHORTGRAPH;
        structureFileName = structureFilename;


        //output the json representing the tree form of the graph
        CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptGraphsRecord();
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
        if (currentMode== Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userId);
                List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);
                return new SuggestionResource(userGraph, concepts);

            } else {
                return new SuggestionResource(null, null);
            }
        }else{
            System.out.println("wrong mode");
            return null;
        }
    }


    @Override
    public SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) {
        if (currentMode== Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userId);

                HashMap<String, List<LearningObjectSuggestion>> suggestionMap = LearningObjectSuggester.specificConceptSuggestionMap(1, userGraph, conceptId);

                List<ConceptNode> concepts = new ArrayList<ConceptNode>();
                return new SuggestionResource(userGraph, concepts);

            } else {
                return new SuggestionResource(null, null);
            }
        }else{
            System.out.println("wrong mode");
            return null;
        }

    }


    @Override
    public List<ConceptNode> calcIndividualConceptNodesSuggestions(String userID){
        if(currentMode == Mode.COHORTGRAPH){
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userID);

                return LearningObjectSuggester.conceptsToWorkOn(userGraph);

            } else {
                return new ArrayList<ConceptNode>();
            }
        }else{
            System.out.println("wrong mode");
            return null;
        }
    }

    public Mode getCurrentmode(){
        return currentMode;
    }

    public String getStructureFileName (){
        return structureFileName;
    }

    public void setStructureFileName(String file){
        structureFileName=file;
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


    //for testing purposes

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }



}
