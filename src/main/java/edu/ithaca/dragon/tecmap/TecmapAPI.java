package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.util.List;

public interface TecmapAPI {

    /**
     * Creates a tree representation of a single structureGraph representing the structure of this tecmap
     * All nodes that have multiple parents are duplicated to create the tree structure
     * @return a ConceptGraphRecord representing the tree representation
     */
    ConceptGraphRecord createStructureTree();

    /**
     * Creates a list all conceptIds in the structureGraph
     * @return a list of strings, one for each conceptId, each surrounded by quotes
     */
    List<String> createConceptIdListToPrint();

    /**
     * Creates a list of LearningResourceRecords, one for each of the current AssessmentItems associated with this tecmap
     * These objects will contain all default values, meaning that have no connections to concepts
     * @return the list of LearningResourceRecords, or an empty list if there are no assessmentItems
     */
    List<LearningResourceRecord> createBlankLearningResourceRecordsFromAssessment();

    /**
     * Creates a list of graphs: One average structureGraph, and one for each student for which there is data
     * each structureGraph is a tree representation of that structureGraph. All nodes that have multiple parents are duplicated to create the tree structure
     * @return a CohortConceptGraphsRecord representing all of the graphs related to this tecmap
     * //TODO: what if there is not data for it?
     */
    CohortConceptGraphsRecord createCohortTree();

    /**
     * @return the current state of the object, denoted as a TecmapState enum
     */
    TecmapState getCurrentState();

    //------------------ Suggestion Functionality ------------- //

    /**
     * Calculates all suggestions for the individual structureGraph, returns an object containing two ordered lists:
     * an ordered list of new resources to try
     * an ordered list of resources that have had unsuccessful assessments in the past
     *
     * @return OrganizedLearningResourceSuggestions
     */
//    OrganizedLearningResourceSuggestions suggestResourcesForUser (String userId);              //calcIndividualGraphSuggestions

    /**
     * Calculates suggestions specific to a certain Concept for an individual structureGraph
     * @return Suggestion Resource object with two order resource lists
     */
//    OrganizedLearningResourceSuggestions suggestResourcesForSpecificConceptForUser(String userId, String conceptId)   ; //calcIndividualSpecificConceptSuggestions


//    List<Group> suggestGroups (List<Suggester> groupTypeList, int groupSize)  //calcSmallGroups

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
