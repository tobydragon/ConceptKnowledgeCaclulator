package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmap;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.reader.ReaderTools;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapDataFilesRecord;
import edu.ithaca.dragon.tecmap.io.record.TecmapFileDatastoreRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TecmapFileDatastore implements TecmapDatastore {
    private static final Logger logger = LogManager.getLogger(TecmapFileDatastore.class);

    Map<String, TecmapFileData> idToMap;
    String rootPath;

    public TecmapFileDatastore(TecmapFileDatastoreRecord recordIn, String rootPath){
        this.rootPath = rootPath;
        idToMap = new TreeMap<>();
        for (TecmapDataFilesRecord dataFiles : recordIn.getAllRecords()){
            boolean graphValid = true;
            boolean resourceValid = true;
            boolean assessmentValid = true;
            Path path;
            for (String assessmentFilename : dataFiles.getAssessmentFiles()) {
                path = Paths.get(assessmentFilename);
                if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                    assessmentValid = false;
                }
            }
            for (String resourceFilename : dataFiles.getResourceFiles()) {
                path = Paths.get(resourceFilename);
                if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                    resourceValid = false;
                }
            }
            path = Paths.get(dataFiles.getGraphFile());
            if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                graphValid = false;
            }

            if (graphValid && assessmentValid && resourceValid) { //Assessment Connected
                idToMap.put(dataFiles.getId(), new TecmapFileData(dataFiles));
            } else if (graphValid && assessmentValid) { //Assessment Added
                idToMap.put(dataFiles.getId(), new TecmapFileData(dataFiles.getId(), dataFiles.getGraphFile(), new ArrayList<String>(), dataFiles.getAssessmentFiles()));
            } else if (graphValid && resourceValid) { //No state for this, but keeps the resource files
                idToMap.put(dataFiles.getId(), new TecmapFileData(dataFiles.getId(), dataFiles.getGraphFile(), dataFiles.getResourceFiles(), new ArrayList<String>()));
            } else if (graphValid) { //No Assessment
                idToMap.put(dataFiles.getId(), new TecmapFileData(dataFiles.getId(), dataFiles.getGraphFile(), new ArrayList<String>(), new ArrayList<String>()));
            }
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
                    return new SuggestingTecmap(new ConceptGraph(ConceptGraphRecord.buildFromJson(files.getGraphFile())),
                            LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(files.getResourceFiles()),
                            //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
                            ReaderTools.learningObjectsFromCSVList(2, files.getAssessmentFiles()),
                            AssessmentItemResponse.createAssessmentItemResponses(files.getAssessmentFiles())
                    );
                }
                else if (desiredState == TecmapState.assessmentAdded) {

                    return new SuggestingTecmap(new ConceptGraph(ConceptGraphRecord.buildFromJson(files.getGraphFile())),
                            null,
                            //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
                            ReaderTools.learningObjectsFromCSVList(2, files.getAssessmentFiles()),
                            AssessmentItemResponse.createAssessmentItemResponses(files.getAssessmentFiles())
                    );
                }
                else if (desiredState == TecmapState.resourcesNoAssessment){
                    return new SuggestingTecmap(new ConceptGraph(ConceptGraphRecord.buildFromJson(files.getGraphFile())),
                            LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(files.getResourceFiles()),
                            null,
                            null
                    );
                }
                else if (desiredState == TecmapState.noAssessment) {
                    return new SuggestingTecmap(new ConceptGraph(ConceptGraphRecord.buildFromJson(files.getGraphFile())), null,null, null);
                }
                else {
                    throw new RuntimeException("Unrecognized state desired, can't retrieve tecmap");
                }
            }
            catch (IOException e){
                logger.warn("IOException when trying to create map for id: "+ idToRetrieve +"\tError:", e);
                return null;
            }
        }
        else {
            logger.warn("Map not found in fileDatastore for id: "+ idToRetrieve);
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
    //ALWAYS WRITES TO THE SAME DEFAULT FILENAME, COPIES TO A DIFFERENT FILENAME
    public String updateTecmapResources(String idToUpdate, List<LearningResourceRecord> learningResourceRecords) {
        if (idToMap.containsKey(idToUpdate)) {
            if (learningResourceRecords != null && learningResourceRecords.size() > 0) {
                try {
                    //Copies old file with defaultFilename (if exists) to a new backup and overwrites the default
                    int i = 0;
                    String defaultFilename = rootPath + idToUpdate + "/" + idToUpdate + "Resources.json";
                    FileCheck.backup(defaultFilename);
                    Json.toJsonFile(defaultFilename, learningResourceRecords);
                    idToMap.get(idToUpdate).updateResourceFiles(defaultFilename);

                    //Copies old datastore with default filename to a new backup and overwrites the default
                    String defaultDatastoreFilename = rootPath + Settings.DEFAULT_DATASTORE_FILENAME;
                    FileCheck.backup(defaultDatastoreFilename);
                    Json.toJsonFile(defaultDatastoreFilename, createTecmapFileDatastoreRecord());

                    return defaultFilename;
                } catch (IOException exception) {
                    return null;
                }
            }
        }
        return null;
    }

    public static TecmapFileDatastore buildFromJsonFile(String rootPath) throws IOException {
        String filename = rootPath + Settings.DEFAULT_DATASTORE_FILENAME;
        return new TecmapFileDatastore(Json.fromJsonFile(filename, TecmapFileDatastoreRecord.class), rootPath);
    }


    public TecmapFileDatastoreRecord createTecmapFileDatastoreRecord() {
        Collection<TecmapFileData> allData = idToMap.values();
        List<TecmapDataFilesRecord> allDataRecords = new ArrayList<>();
        for (TecmapFileData data: allData) {
            allDataRecords.add(new TecmapDataFilesRecord(data.getId(), data.getGraphFile(), data.getResourceFiles(), data.getAssessmentFiles()));
        }
        return new TecmapFileDatastoreRecord(allDataRecords);
    }

}
