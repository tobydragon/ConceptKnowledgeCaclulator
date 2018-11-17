package edu.ithaca.dragon.tecmap.io.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface CsvProcessor {

    boolean shouldProcessFile(String filename);
    void processRows(List<String[]> rows);
    String createProcessedFilename(String origFilename);
    void writeToFile(String filepath) throws IOException;


    static void processFilesInDirectory(CsvProcessor processor, String directoryToProcess) throws IOException {
        for (String filename : allCsvFilesInDirectory(directoryToProcess)) {
            if (processor.shouldProcessFile(filename)) {
                List<String[]> rows = CsvRepresentation.parseRowsFromFile(directoryToProcess+filename);
                processor.processRows(rows);
                CsvRepresentation.writeRowsToFile(rows, directoryToProcess+processor.createProcessedFilename(filename));
            }
        }
    }

    static List<String> allCsvFilesInDirectory(String directory) {
        List<String> textFiles = new ArrayList<>();
        File dir = new File(directory);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith((".csv"))) {
                    textFiles.add(file.getName());
                }
            }
        }
        return textFiles;
    }
}
