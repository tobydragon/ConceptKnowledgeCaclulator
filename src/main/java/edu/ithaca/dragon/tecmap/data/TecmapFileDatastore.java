package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.SuggestingTecmap;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapDataFilesRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapFileDatastoreRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TecmapFileDatastore implements TecmapDatastore {
    private static final Logger logger = LogManager.getLogger(TecmapFileDatastore.class);

    Map<String, TecmapFileData> idToMap;
    String rootPath;

    public TecmapFileDatastore(TecmapFileDatastoreRecord recordIn, String rootPath){
        this.rootPath = rootPath;
        idToMap = new TreeMap<>();
        for (TecmapDataFilesRecord dataFiles : recordIn.getAllRecords()){
            idToMap.put(dataFiles.getId(), new TecmapFileData(dataFiles));
        }
    }

    @Override
    public SuggestingTecmapAPI retrieveTecmapForId(String idToRetrieve) {
        TecmapFileData files = idToMap.get(idToRetrieve);
        if (files != null) {
            return retrieveTecmapForId(idToRetrieve, files.getAvailableState());
        }
        else{
            logger.info("Map not found in fileDatastore for id: "+ idToRetrieve);
            return null;
        }
    }

    @Override
    public SuggestingTecmapAPI retrieveTecmapForId(String idToRetrieve, TecmapState desiredState) {
        TecmapFileData files = idToMap.get(idToRetrieve);
        if (files != null){
            try {
                if (desiredState == TecmapState.assessmentConnected) {
                    return new SuggestingTecmap(files.getGraphFile(), files.getResourceFiles(), files.getAssessmentFiles());
                }
                else if (desiredState == TecmapState.assessmentAdded) {
                    return new SuggestingTecmap(files.getGraphFile(), null, files.getAssessmentFiles());
                }
                else if (desiredState == TecmapState.noAssessment) {
                    return new SuggestingTecmap(files.getGraphFile(), null, null);
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

    @Override
    public Map<String, List<TecmapUserAction>> retrieveValidIdsAndActions() {
        //TODO: make functional style to allow parallelism
        Map<String, List<TecmapUserAction>> idToActions = new TreeMap<>();
        for (TecmapFileData fileData : idToMap.values()){
            idToActions.put(fileData.getId(), fileData.getAvailableState().getAvailableActions());
        }
        return idToActions;
    }

    @Override
    public String updateTecmapResources(String idToUpdate, List<LearningResourceRecord> learningResourceRecords) {
        if (idToMap.containsKey(idToUpdate)) {
            if (learningResourceRecords != null && learningResourceRecords.size() > 0) {
                try {
                    //TODO: SAVE FILE TO A NEW FILENAME
                    //Write To A New Resource File
                    String filename = rootPath + "resources/datastore/" + idToUpdate + "/" + idToUpdate + "Resources.json";
                    LearningResourceRecord.resourceRecordsToJSON(learningResourceRecords, filename);
                    idToMap.get(idToUpdate).addResourceFiles(filename);

                    //TODO: FIND A WAY TO UPDATE THE PERMANENT DATASTORE
                    return filename;
                } catch (IOException exception) {
                    return null;
                }
            }
        }
        return null;
    }

    public static TecmapFileDatastore buildFromJsonFile(String filename, String rootPath) throws IOException {
        return new TecmapFileDatastore(Json.fromJsonString(filename, TecmapFileDatastoreRecord.class), rootPath);
    }
}
