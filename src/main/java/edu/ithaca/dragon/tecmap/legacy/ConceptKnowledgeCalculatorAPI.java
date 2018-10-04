package edu.ithaca.dragon.tecmap.legacy;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Group;
import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.Suggester;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;

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


    /**
     * after verifying that the files are valid, the files are used to create a new ckc
     * @param resource
     * @param assignment
     * @throws Exception
     */
    void addResourceAndAssessment(String resource, String assignment) throws  Exception;

    /**
     * Creates a new ckc with the new graph file and all the old resource and assessment files.
     * @param graph
     * @throws Exception
     */
    void replaceCohortGraph(String graph) throws Exception;

    /**
     * Creates a new CKC with only the structure graph file.
     * All other files are cleared.
     * @throws Exception
     */
    void switchToStructure() throws Exception;

    /**
     * Verifies the resource file is valid and then creates a new CKC will the added and old files
     * @param resourceFile
     * @throws Exception
     */
    void replaceResourceFile(String resourceFile) throws Exception;

    /**
     * @return the URL where the current graphs can be seen
     */
    String getCohortGraphsUrl() throws Exception;


    /**
     * verifies the file is valid and then creates a new ckc will all the added and old files
     * @param assessmentFilename
     * @throws IOException
     */
    void addAssessement(String assessmentFilename) throws Exception;



    /**
     * Verifies the resourcce file is valid and then creates a new ckc with all the added and old files.
     * will only add learning objects that are associated with concepts already in the graph
     * will only add learning objects that are not already in the graph (aka no repeats)
     * @param secondResourceFile
     * @throws IOException
     */
    void addResource(String secondResourceFile) throws Exception;


    /**
     * Verifies the file is valid and then creates a new ckc without the removed files.
     * @param assessmentFile
     * @throws Exception
     */
    void removeAssessmentFile(String assessmentFile) throws Exception;


    /**
     * Uses csvfile(s) to write a JSON file and allows Structure with assessments
     * @return String that tells the user where the JSON file is being written to
     * @throws Exception
     */
    String csvToResource() throws Exception;

    /**
     * Calculates all suggestions for the individual graph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     *
     * @return OrganizedLearningResourceSuggestions
     */
    OrganizedLearningResourceSuggestions calcIndividualGraphSuggestions(String userId) throws Exception;


    /**
     * Calculates suggestions specific to a certain Concept for an individual graph
     * @return Suggestion Resource object with two order resource lists
     */
    OrganizedLearningResourceSuggestions calcIndividualSpecificConceptSuggestions(String userId, String conceptId) throws Exception;

    /**
     * Calculates average knowledgeEstimates of a ColumnItem across all students
     * @param learningObject matrix, Learningobject
     * @return average knowledgeEstimates for given ColumnItem
     */
    double getLearningObjectAvg(String learningObject) throws Exception;

    /**
     * Collects a list of students from the current graph.
     * @pre a user selects to view a list of all users within the graph
     * @return list of userIds
     */
    List<String> getUserIdList() throws Exception;

    /**
     * Calculates a student's average knowledgeEstimates across all LearningObjects
     * @param user
     * @return the average of knowledgeEstimates
     */
    double getStudentAvg(String user);

    /**
     * Prints a matrix of factors (columns) and learning objects (rows) that tell how  connected multiple learning
     * objects are to just a few factors
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     */
    void getFactorMatrix();

    /**
     * Uses data from the students and the structure given by the user to create a graph showing the weights
     * between the bottom layer of data and the concepts they influence.
     */
    void createConfirmatoryGraph();

    /**
     * Takes connections stored in CohortConceptGraphs and creates a text file
     * in the format readable for confirmatory graphing
     */
    void createModelFile();

    /**
     * Calculates a list of conceptNode based on an individual graph , returns a the list of ConceptNodes
     * @param userID
     * @return List of Concept Nodes
     */
    List<String> calcIndividualConceptNodesSuggestions(String userID) throws Exception;

    List<Group> calcSmallGroups(List<Suggester> groupTypeList, int groupSize) throws Exception;

    /**
     * When in structure mode, the ckc will clear and create a new structure ckc with the proper file
     * @param file
     * @throws IOException
     */
    void updateStructureFile(String file) throws IOException;


    ConceptKnowledgeCalculator.Mode getCurrentMode();

    ConceptKnowledgeCalculator.SuggestMode getCurrentSuggestMode();
    void toggleCurrentSuggestMode();

    /**
     * @return a copy of the assessment file list
     */
    List<String> currentAssessment();

    /**
     * @return a copy of the resource file list
     */
    List<String> currentResource();

    /**
     * @return a copy of the structure file list
     */
    List<String> currentStructure();

    /**
     * Checks that a specified assessment file is valid.
     * Used before adding to file lists that will be used in clear and create functions
     * @param name
     * @return true or false depending on if the file is valid
     * @throws IOException
     */
    boolean assessmentIsValid(String name)throws IOException;

    boolean structureIsValid(String name) throws IOException;

    boolean resourceIsValid(String name) throws IOException;


    //testing purposes
    ConceptGraph getStructureGraph();
    CohortConceptGraphs getCohortConceptGraphs();


}
