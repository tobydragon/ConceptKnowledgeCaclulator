package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.TecmapAPI;

public interface TecmapDatastore {

    /**
     * @param idToRetrieve
     * @return the Tecmap associated with the idToRetrieve, or null if not found
     */
    TecmapAPI retrieveTecmapForId(String idToRetrieve);
}
