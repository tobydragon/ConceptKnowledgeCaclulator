package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.data.TecmapDataFiles;

import java.util.List;

public class TecmapFileDatastoreRecord {

    private List<TecmapDataFilesRecord> allRecords;

    public TecmapFileDatastoreRecord() {
    }

    public TecmapFileDatastoreRecord(List<TecmapDataFilesRecord> allRecords) {
        this.allRecords = allRecords;
    }

    public List<TecmapDataFilesRecord> getAllRecords() {
        return allRecords;
    }

    public void setAllRecords(List<TecmapDataFilesRecord> allRecords) {
        this.allRecords = allRecords;
    }
}
