package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Tecmap;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.TecmapDataFilesRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapFileDatastoreRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class TecmapFileDatastore implements TecmapDatastore {
    private static final Logger logger = LogManager.getLogger(TecmapFileDatastore.class);

    Map<String, TecmapFileData> idToMap;

    public TecmapFileDatastore(TecmapFileDatastoreRecord recordIn){
        idToMap = new TreeMap<>();
        for (TecmapDataFilesRecord dataFiles : recordIn.getAllRecords()){
            idToMap.put(dataFiles.getId(), new TecmapFileData(dataFiles));
        }
    }

    @Override
    public TecmapAPI retrieveTecmapForId(String idToRetrieve) {
        return retrieveTecmapForId(idToRetrieve, TecmapState.assessmentConnected);
    }

    @Override
    public TecmapAPI retrieveTecmapForId(String idToRetrieve, TecmapState desiredState) {
        TecmapFileData files = idToMap.get(idToRetrieve);
        if (files != null){
            try {
                if (desiredState == TecmapState.assessmentConnected) {
                    return new Tecmap(files.getGraphFile(), files.getResourceFiles(), files.getAssessmentFiles());
                }
                else if (desiredState == TecmapState.assessmentAdded) {
                    return new Tecmap(files.getGraphFile(), null, files.getAssessmentFiles());
                }
                else if (desiredState == TecmapState.noAssessment) {
                    return new Tecmap(files.getGraphFile(), null, null);
                }
                else {
                    throw new RuntimeException("Unrecognized state desired, can't retrieve tecmap");
                }
            }
            catch (IOException e){
                logger.info("IOException when trying to create map for id: "+ idToRetrieve +"\tError:", e);
                return null;
            }
        }
        else {
            logger.info("Map not found in fileDatastore for id: "+ idToRetrieve);
            return null;
        }
    }

    public static TecmapFileDatastore buildFromJsonFile(String filename) throws IOException {
        return new TecmapFileDatastore(Json.fromJsonString(filename, TecmapFileDatastoreRecord.class));
    }
}
