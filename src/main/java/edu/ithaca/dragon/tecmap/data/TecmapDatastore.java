package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapFileDatastoreRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;

import java.util.List;
import java.util.Map;

public interface TecmapDatastore {

    /**
     * finds and builds the tecmap in the most advanced state possible
     * from the data associated with the given ID
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
     * updates the resource files for a tecmap with a new file from the learning resource records
     * being added
     * @param idToUpdate
     * @param learningResourceRecords
     * @return the json filename if update successful, or null if fail
     */
    String updateTecmapResources(String idToUpdate, List<LearningResourceRecord> learningResourceRecords);

    /**
     * finds all the valid ids, and the states that are valid to load for that ID
     * @return a map where each key is a valid TecmapID, and the corresponding value is a list of valid states for that Tecmap
     */
    Map<String, List<TecmapUserAction>> retrieveValidIdsAndActions();

    /**
     * creates a TecmapFileDatastoreRecord from the Datastore object
     * @return
     */
    TecmapFileDatastoreRecord createTecmapFileDatastoreRecord();
}
