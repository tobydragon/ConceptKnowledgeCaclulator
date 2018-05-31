package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.util.Collection;
import java.util.List;

public interface TecmapAPI {

    /**
     * Creates a tree representation of a single graph representing the structure of this tecmap
     * All nodes that have multiple parents are duplicated to create the tree structure
     * @return a ConceptGraphRecord representing the tree representation
     */
    ConceptGraphRecord createStructureTree();

    /**
     * Creates a list all conceptIds in the graph
     * @return a list of strings, one for each conceptId, each surrounded by quotes
     */
    List<String> createConceptIdListToPrint();

    /**
     * Creates a list of LearningResourceRecords, one for each of the current AssessmentItems associated with this tecmap
     * These objects will contain all default values, meaning that have no connections to concepts
     * @return the list of LearningResourceRecords
     */
    List<LearningResourceRecord> createBlankLearningResourceRecordsFromAssessment();

    //TODO
//    /**
//     * Creates a list of graphs: One average graph, and one for each student for which there is data
//     * each graph is a tree representation of that graph. All nodes that have multiple parents are duplicated to create the tree structure
//     * @return a CohortConceptGraphsRecord representing all of the graphs related to this tecmap
//     */
//    CohortConceptGraphsRecord createCohortTree();
//
//


//    suggestConceptsForSingleUser,               //calcIndividualConceptNodesSuggestions​
//    suggestResourcesForSingleUser,              //calcIndividualGraphSuggestions
//    suggestResourcesForSingleUserSingleConcept, //calcIndividualSpecificConceptSuggestions
//    suggestGroups

}
