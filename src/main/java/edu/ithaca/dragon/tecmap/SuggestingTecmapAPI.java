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

    // ------ R analysis functionality ------//


    /**
     * Prints a matrix of factors (columns) and learning objects (rows) that tell how  connected multiple learning
     * objects are to just a few factors
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     */
//    void getFactorMatrix();

    /**
     * Uses data from the students and the structure given by the user to create a structureGraph showing the weights
     * between the bottom layer of data and the concepts they influence.
     */
//    void createConfirmatoryGraph();

    /**
     * Takes connections stored in CohortConceptGraphs and creates a text file
     * in the format readable for confirmatory graphing
     */
//    void createModelFile();
}
