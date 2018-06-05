package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.util.List;
import java.util.Map;

public interface TecmapDatastore {

    /**
     * assumes that you want the assessmentConnected state
     * @param idToRetrieve
     * @return the Tecmap associated with the idToRetrieve, or null if not found
     */
    TecmapAPI retrieveTecmapForId(String idToRetrieve);

    /**
     * finds and builds a tecmap from data in this datastore
     * @param idToRetrieve
     * @return the Tecmap associated with the idToRetrieve, or null if not found
     */
    TecmapAPI retrieveTecmapForId(String idToRetrieve, TecmapState desiredState);

    /**
     * finds all the valid ids, and the states that are valid to load for that ID
     * @return a map where each key is a valid TecmapID, and the corresponding value is a list of valid states for that Tecmap
     */
    Map<String, List<String>> retrieveValidIdsAndStates();
}
