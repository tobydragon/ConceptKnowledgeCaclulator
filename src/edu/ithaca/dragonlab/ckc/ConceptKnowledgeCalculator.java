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

    private static final String OUTPUT_PATH = "out/";


    private CohortConceptGraphs cohortConceptGraphs;

    public ConceptKnowledgeCalculator() {
        cohortConceptGraphs = null;
    }

    public ConceptKnowledgeCalculator(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException{
        clearAndCreateCohortData(structureFilename, resourceFilename, assessmentFilename);

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
        if (cohortConceptGraphs != null) {
            ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userId);

            List<ConceptNode> concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);
            SuggestionResource orderedLists =  new SuggestionResource(userGraph,concepts);
            List<LearningObjectSuggestion> toTry = orderedLists.incompleteList;
            List<LearningObjectSuggestion> tryAgain = orderedLists.wrongList;

            return orderedLists;
        }

        else {
            return new SuggestionResource(null, null);
        }
    }


    @Override
    public SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) {
        if (cohortConceptGraphs != null) {
            ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userId);

            HashMap<String, List<LearningObjectSuggestion>> suggestionMap= LearningObjectSuggester.specificConceptSuggestionMap(1, userGraph, conceptId);

            List<ConceptNode> concepts = new ArrayList<ConceptNode>();
            SuggestionResource res =  new SuggestionResource(userGraph, concepts);

            res.setSuggestionMap(suggestionMap);
            List<LearningObjectSuggestion> incomTest = res.incompleteList;
            List<LearningObjectSuggestion> wrongTest = res.wrongList;

            return res;
        }
        else {
            return new SuggestionResource(null, null);
        }

    }


    public List<ConceptNode> calcIndividualConceptNodesSuggestions(String userID){
        if (cohortConceptGraphs != null) {
            ConceptGraph userGraph = cohortConceptGraphs.getUserGraph(userID);

            List<ConceptNode> suggestedConceptList = LearningObjectSuggester.conceptsToWorkOn(userGraph);

            return suggestedConceptList;
        }
        else {
            return new ArrayList<ConceptNode>();
        }
    }

}
