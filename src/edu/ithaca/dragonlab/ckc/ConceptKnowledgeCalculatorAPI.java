package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
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
     * @param structureFilename
     * @throws IOException
     */
    void clearAndCreateStructureData(List<String> structureFilename) throws IOException;

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
    void clearAndCreateCohortData(List<String> structureFilename, List<String> resourceFilename, List<String> assessmentFilename) throws IOException;



    void addLORAndLO(String LO, String LOR) throws  Exception;

    void replaceGraph(String graph) throws Exception;

    void switchToStructure() throws Exception;



    void replaceLOFile(String resourceFile) throws Exception;

    /**
     * @return the URL where the current graphs can be seen
     */
    String getCohortGraphsUrl() throws Exception;


    /**
     * creates two lists of the learning object responses with the old file and the new file and combines them into one to use in the cohort graph
     * @param assessmentFilename
     * @throws IOException
     */
    void additionalLOR(String assessmentFilename) throws Exception;



    /**
     * adds another file of LearningObjects to the graph.
     * will only add learning objects that are associated with concepts already in the graph
     * will only add learning objects that are not already in the graph (aka no repeats)
     * @param secondResourceFile
     * @throws IOException
     */
    void addAnotherLO(String secondResourceFile) throws Exception;



    void removeLORFile(String assessmentFile) throws Exception;

    public String csvToResource() throws Exception;

    /**
     * Calculates all suggestions for the individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     *
     * @return SuggestionResource
     */
    SuggestionResource calcIndividualGraphSuggestions(String userId) throws Exception;


    /**
     * Calculates suggestions specific to a certain concept for an individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     *
     * @return Suggestion Resource
     */
    SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) throws Exception;

    /**
     * Calculates average knowledgeEstimates of a LearningObject across all students
     * @param learningObject matrix, Learningobject
     * @return average knowledgeEstimates for given LearningObject
     */


    double getLearningObjectAvg(String learningObject) throws Exception;

    /**
     * Collects a list of students from the current graph.
     * @pre a user selects to view a list of all users within the graph
     * @return list of userIds
     */
    List<String> getUserIdList();

    /**
     * Calculates a student's average knowledgeEstimates across all LearningObjects
     * @param user
     * @return the average of knowledgeEstimates
     */
    double getStudentAvg(String user);



    /**
     * Calculates a list of conceptNode based on an individual graph , returns a the list of ConceptNodes
     * @param userID
     * @return List of Concept Nodes
     */
    List<String> calcIndividualConceptNodesSuggestions(String userID) throws Exception;


    ConceptKnowledgeCalculator.Mode getCurrentMode();

    List<String> getResourceFiles();

    List<String> getAssessmentFiles();

    List<String> getStructureFiles();


    //just for testing
    ConceptGraph getStructureGraph();
    CohortConceptGraphs getCohortConceptGraphs();



}
