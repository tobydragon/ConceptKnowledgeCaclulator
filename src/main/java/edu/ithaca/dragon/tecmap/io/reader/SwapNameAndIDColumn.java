package edu.ithaca.dragon.tecmap.io.reader;


import java.io.IOException;
import java.util.List;

public class SwapNameAndIDColumn implements CsvProcessor {

    public static void swapNameAndIDColumn(List<String[]> rows) {
        for (String[] row : rows) {
            if ((row[0] != null && !row[0].isEmpty()) && (row[1] != null && !row[1].isEmpty())) {
                String temp = row[0];
                row[0] = row[1];
                row[1] = temp;
            }
        }
    }

    @Override
    public boolean shouldProcessFile(String filename) {
        return true;
    }

    @Override
    public void processRows(List<String[]> rows) {
        swapNameAndIDColumn(rows);
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return "";
    }

    @Override
    public void writeToFile(String filepath) throws IOException {

    }
}
