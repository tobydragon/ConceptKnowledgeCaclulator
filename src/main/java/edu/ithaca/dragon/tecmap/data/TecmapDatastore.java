package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

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
}
