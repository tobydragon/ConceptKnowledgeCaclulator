package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.io.IOException;
import java.util.List;

/**
 * Created by tdragon on 6/8/17.
 */
public interface ConceptKnowledgeCalculatorAPI {

    /**
     * Clears all previous data and saves the new graph
     *
     * @param structureFileName
     * @throws IOException
     */
    void clearAndCreateStructureData(String structureFileName) throws IOException;


    /**
     * Clears all previous data, saves a new graph to be used for the cohort with the structure given,
     * Creates representations of the specified resources and links them to the existing structure graph (to be used for the entire cohort),
     * Creates representations of individual (per user) and average assessments associated with resources
     * and Calculates knowledge estimates for the cohort, meaning estimates for all nodes in the average graph and in all individual graphs
     *
     * @param structureFilename  a json file specifying graph structure
     * @param resourceFilename   a json file listing the resources and their links to the concepts
     * @param assessmentFilename a csv file containing rows of students and columns labeled with resourceIds
     */
    void clearAndCreateCohortData(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException;


    /**
     * @return the URL where the current graphs can be seen
     */
    String getCohortGraphsUrl();


    /**
     * Calculates all suggestions for the individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     *
     * @return ???
     */
    SuggestionResource calcIndividualGraphSuggestions(String userId);


    /**
     * Calculates suggestions specific to a certain concept for an individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     *
     * @return ???
     */
    SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId);


    /**
     * Calculates a list of conceptNode based on an individual graph , returns a the list of ConceptNodes
     *
     * @param userID
     * @return
     */
    List<ConceptNode> calcIndividualConceptNodesSuggestions(String userID);

    ConceptKnowledgeCalculator.Mode getCurrentmode();
    void setCurrentMode(ConceptKnowledgeCalculator.Mode mode);

    String getStructureFileName();
    void setStructureFileName(String file);

    /**
     * If the user gives a bad file name, the graph will be remade with the last known working file name
     * @param fileName
     */
    void setLastWorkingStructureName(String fileName);

    String getLastWorkingStructureName();


    //just for testing
    ConceptGraph getStructureGraph();
    CohortConceptGraphs getCohortConceptGraphs();


}
