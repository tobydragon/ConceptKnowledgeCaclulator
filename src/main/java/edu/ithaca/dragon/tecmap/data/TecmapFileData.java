package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.io.record.TecmapDataFilesRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.util.List;

public class TecmapFileData {

    private String id;
    private TecmapState availableState;
    private String graphFile;
    private List<String> resourceFiles;
    private List<String> assessmentFiles;

    private List<String> learningMaterialRecordFiles;

    public TecmapFileData(String id, String graphFile, List<String> resourceFiles, List<String> assessmentFiles) {
        this.id = id;
        this.graphFile = graphFile;
        this.resourceFiles = resourceFiles;
        this.assessmentFiles = assessmentFiles;
        this.availableState = TecmapState.checkAvailableState(resourceFiles, assessmentFiles);
    }

    public TecmapFileData(String id, String graphFile, List<String> resourceFiles, List<String> assessmentFiles, List<String> learningMaterialRecordFiles) {
        this.id = id;
        this.graphFile = graphFile;
        this.resourceFiles = resourceFiles;
        this.assessmentFiles = assessmentFiles;
        this.availableState = TecmapState.checkAvailableState(resourceFiles, assessmentFiles);
        this.learningMaterialRecordFiles = learningMaterialRecordFiles;
    }

    public TecmapFileData(TecmapDataFilesRecord recordIn){
        this(recordIn.getId(), recordIn.getGraphFile(), recordIn.getResourceFiles(), recordIn.getAssessmentFiles());
    }



    public String getId() {
        return id;
    }

    public String getGraphFile() {
        return graphFile;
    }

    public List<String> getResourceFiles() {
        return resourceFiles;
    }

    public List<String> getAssessmentFiles() {
        return assessmentFiles;
    }

    public TecmapState getAvailableState(){
        return availableState;
    }

    public List<String> getLearningMaterialRecordFiles() { return learningMaterialRecordFiles; }

    public void updateResourceFiles(String filename) {
        resourceFiles.clear();
        resourceFiles.add(filename);
        this.availableState = TecmapState.checkAvailableState(resourceFiles, assessmentFiles);
    }

    public TecmapDataFilesRecord createTecmapDataFilesRecord() {
        return new TecmapDataFilesRecord(id, graphFile, resourceFiles, assessmentFiles);
    }
}
