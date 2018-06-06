package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;

import java.util.List;

public interface SuggestingTecmapAPI extends TecmapAPI {

    List<String> suggestConceptsForUser(String userID);  //calcIndividualConceptNodesSuggestions​

    OrganizedLearningResourceSuggestions suggestResourcesForUser(String userId); // //calcIndividualGraphSuggestions

    OrganizedLearningResourceSuggestions suggestResourcesForSpecificConceptForUser(String userId, String conceptId); //calcIndividualSpecificConceptSuggestions
}
