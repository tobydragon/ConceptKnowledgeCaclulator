package edu.ithaca.dragonlab.ckc;

import java.io.IOException;
import java.util.List;

/**
 * Created by tdragon on 6/8/17.
 */
public interface ConceptKnowledgeCalculatorAPI {

    /**
     * Clears all previous data, saves a new graph to be used for the cohort with the structure given,
     * Creates representations of the specified resources and links them to the existing structure graph (to be used for the entire cohort),
     * Creates representations of individual (per user) and average assessments associated with resources
     * and Calculates knowledge estimates for the cohort, meaning estimates for all nodes in the average graph and in all individual graphs
     * @param structureFilename a json file specifying graph structure
     * @param resourceFilename a json file listing the resources and their links to the concepts
     * @param assessmentFilename a csv file containing rows of students and columns labeled with resourceIds
     */
    void clearAndCreateCohortData(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException;


    /**
     *
     * @return the URL where the current graphs can be seen
     */
    String getCohortGraphsUrl();


    //TODO: replace String return with whatever object holds suggestions
    /**
     * Calculates all suggestions for the individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     * @return ???
     */
    String calcIndividualGraphSuggestions(String userId);


    //TODO: replace String return with whatever object holds suggestions
    /**
     * Calculates suggestions specific to a certain concept for an individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     * @return ???
     */
    String calcIndividualConceptSuggestions(String userId, String conceptId);


}
