package edu.ithaca.dragon.tecmap.io.record;

import java.util.List;

public class TecmapDataFilesRecord {

    private String id;
    private String graphFile;
    private List<String> resourceFiles;
    private List<String> assessmentFiles;

    //used for json import
    public TecmapDataFilesRecord(){}

    public TecmapDataFilesRecord(String id, String graphFile, List<String> resourceFiles, List<String> assessmentFiles) {
        this.id = id;
        this.graphFile = graphFile;
        this.resourceFiles = resourceFiles;
        this.assessmentFiles = assessmentFiles;
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
}
