package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.suggester.ConceptGraphSuggesterLibrary;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Group;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.GroupSuggester;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Suggester;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentConnectedState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuggestingTecmap extends Tecmap implements SuggestingTecmapAPI{

    public SuggestingTecmap(ConceptGraph graph, List<LearningResourceRecord> links, List<AssessmentItem> assessmentItemsStructureList, List<AssessmentItemResponse> assessmentItemResponses) {
        super(graph, links, assessmentItemsStructureList, assessmentItemResponses);
    }

    public List<String> suggestConceptsForUser(String userId){
        if (state instanceof AssessmentConnectedState){
            ConceptGraph userGraph = ((AssessmentConnectedState)state).getGraphForUser(userId);
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
        if (state instanceof AssessmentConnectedState){
            ConceptGraph userGraph = ((AssessmentConnectedState)state).getGraphForUser(userId);
            if (userGraph != null){
                List<ConceptNode> concepts = ConceptGraphSuggesterLibrary.suggestConcepts(userGraph);
                return new OrganizedLearningResourceSuggestions(userGraph, concepts);
            }
        }
        return null;
    } // //calcIndividualGraphSuggestions

    public OrganizedLearningResourceSuggestions suggestResourcesForSpecificConceptForUser(String userId, String conceptId){
        if (state instanceof AssessmentConnectedState){
            ConceptGraph userGraph = ((AssessmentConnectedState)state).getGraphForUser(userId);
            if (userGraph != null){
                ConceptNode node = userGraph.findNodeById(conceptId);
                if (node != null) {
                    List<ConceptNode> concepts = new ArrayList<>(Arrays.asList(node));
                    return new OrganizedLearningResourceSuggestions(userGraph, concepts);
                }
            }
        }
        return null;
    } //calcIndividualSpecificConceptSuggestions

    public List<Group> suggestGroups(List<Suggester> groupTypeList, int groupSize) {
        if (state instanceof AssessmentConnectedState) {
            CohortConceptGraphs userGraphs = ((AssessmentConnectedState)state).getCohortConceptGraphs();
            GroupSuggester sug = new GroupSuggester();

            List<Group> initialGroup = GroupSuggester.getGroupList(userGraphs);

            return sug.grouping(initialGroup, groupSize, groupTypeList);
        }
        return null;
    }
}
