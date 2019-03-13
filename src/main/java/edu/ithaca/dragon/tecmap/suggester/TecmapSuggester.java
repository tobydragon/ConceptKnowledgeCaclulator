package edu.ithaca.dragon.tecmap.suggester;

import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Group;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.GroupSuggester;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Suggester;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TecmapSuggester implements  TecmapSuggesterAPI {
    TecmapAPI tecmapAPI;

    public TecmapSuggester(TecmapAPI tecmapAPI) {
       this.tecmapAPI = tecmapAPI;
    }

    public List<String> suggestConceptsForUser(String userId){
        if (tecmapAPI.getCurrentState() == TecmapState.assessmentConnected){
            ConceptGraph userGraph = tecmapAPI.getConceptGraphForUser(userId);
            if (userGraph != null){
                List<ConceptNode> nodeList = ConceptGraphSuggesterLibrary.suggestConcepts(userGraph);
                //TODO make functional style for parallelism
                List<String> suggestedConceptIDList = new ArrayList<>();
                for (ConceptNode node : nodeList) {
                    suggestedConceptIDList.add(node.getID());
                }
                return suggestedConceptIDList;
            }
        }
        return null;
    }

    public OrganizedLearningResourceSuggestions suggestResourcesForUser (String userId){
        if (tecmapAPI.getCurrentState() == TecmapState.assessmentConnected){
            ConceptGraph userGraph = tecmapAPI.getConceptGraphForUser(userId);
            if (userGraph != null){
                List<ConceptNode> concepts = ConceptGraphSuggesterLibrary.suggestConcepts(userGraph);
                return new OrganizedLearningResourceSuggestions(userGraph, concepts);
            }
        }
        return null;
    } //used to be calcIndividualGraphSuggestions

    public OrganizedLearningResourceSuggestions suggestResourcesForSpecificConceptForUser(String userId, String conceptId){
        if (tecmapAPI.getCurrentState() == TecmapState.assessmentConnected){
            ConceptGraph userGraph = tecmapAPI.getConceptGraphForUser(userId);
            if (userGraph != null){
                ConceptNode node = userGraph.findNodeById(conceptId);
                if (node != null) {
                    List<ConceptNode> concepts = new ArrayList<>(Arrays.asList(node));
                    return new OrganizedLearningResourceSuggestions(userGraph, concepts);
                }
            }
        }
        return null;
    } //used to be calcIndividualSpecificConceptSuggestions

    public List<Group> suggestGroups(List<Suggester> groupTypeList, int groupSize) {
        if (tecmapAPI.getCurrentState() == TecmapState.assessmentConnected){
            Map<String, ConceptGraph> userToConceptGraphMap = tecmapAPI.getUserToConceptGraphMap();
            GroupSuggester sug = new GroupSuggester();
            List<Group> initialGroup = GroupSuggester.getGroupList(userToConceptGraphMap);
            return sug.grouping(initialGroup, groupSize, groupTypeList);
        }
        return null;
    }
}
