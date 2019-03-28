package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.util.List;
import java.util.Map;

public interface TecmapAPI {

    /**
     * Creates a tree representation of a single structureGraph representing the structure of this tecmap
     * All nodes that have multiple parents are duplicated to create the tree structure
     * @return a ConceptGraphRecord representing the tree representation
     */
    ConceptGraphRecord createStructureTree();

    /**
     * Creates a list all conceptIds in the structureGraph
     * @return a list of strings, one for each conceptId
     */
    List<String> conceptIdList();


    /**
     * @return the current ResourceRecords if they exist. If not, it makes a new list from assessments if they exist.
     *          If there are no assessments and no current links, returns an empty list
     */
    List<LearningResourceRecord> currentLearningResourceRecords();

    /**
     * Creates a list of graphs: One average structureGraph, and one for each student for which there is data
     * each structureGraph is a tree representation of that structureGraph. All nodes that have multiple parents are duplicated to create the tree structure
     * @return a CohortConceptGraphsRecord representing all of the graphs related to this tecmap
     * //TODO: what if there is not data for it?
     */
    CohortConceptGraphsRecord createCohortTree();

    /**
     * Gets the average conceptGraph in order to get a graph that has all of the data for a cohortConceptGraph
     * @return an average conceptGraph for a tecmap, null if not available
     */
    ConceptGraph getAverageConceptGraph();

    /**
     * @return the ConceptGraph associated with the userId, null if not available
     */
    ConceptGraph getConceptGraphForUser(String userId);

    /**
     * @return a map of all userIds to their ConceptGraphs, null if not available
     */
    Map<String, ConceptGraph> getUserToConceptGraphMap();

    /**
     * @return the current state of the object, denoted as a TecmapState enum
     */
    TecmapState getCurrentState();

    /**
     * @return list of LearningMaterialRecords created through the jsonToLearningMaterialRecord method
     */
    List<LearningMaterialRecord> retrieveLearningMaterialRecords();

}
