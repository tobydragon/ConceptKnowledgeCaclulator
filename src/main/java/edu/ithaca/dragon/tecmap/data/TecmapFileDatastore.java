package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Tecmap;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.TecmapDataFilesRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapFileDatastoreRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class TecmapFileDatastore implements TecmapDatastore {
    private static final Logger logger = LogManager.getLogger(TecmapFileDatastore.class);

    Map<String, TecmapDataFiles> idToMap;

    public TecmapFileDatastore(TecmapFileDatastoreRecord recordIn){
        idToMap = new TreeMap<>();
        for (TecmapDataFilesRecord dataFiles : recordIn.getAllRecords()){
            idToMap.put(dataFiles.getId(), new TecmapDataFiles(dataFiles));
        }
    }

    @Override
    public Tecmap retrieveTecmapForId(String idToRetrieve) {
        TecmapDataFiles files = idToMap.get(idToRetrieve);
        if (files != null){
            try {
                return new Tecmap(files.getGraphFile(), files.getResourceFiles(), files.getAssessmentFiles());
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
