package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Group;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Suggester;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;

import java.util.List;

public interface SuggestingTecmapAPI extends TecmapAPI {

    /**
     * Suggest the concepts on which to focus for a specific user
     * @param userID
     * @return List of Concept ID strings (empty list if no suggestions), or null for invalid user or invalid tecmap
     */
    List<String> suggestConceptsForUser(String userID);  //calcIndividualConceptNodesSuggestions​

    OrganizedLearningResourceSuggestions suggestResourcesForUser(String userId); // //calcIndividualGraphSuggestions

    OrganizedLearningResourceSuggestions suggestResourcesForSpecificConceptForUser(String userId, String conceptId); //calcIndividualSpecificConceptSuggestions

    /**
     * Suggest groups based on what type of suggesters you use, and given group size
     * @param groupTypeList
     * @param groupSize
     * @return List of Groups that are students grouped by your suggestions parameters
     */
    List<Group> suggestGroups(List<Suggester> groupTypeList, int groupSize); //calcSmallGroups
}
