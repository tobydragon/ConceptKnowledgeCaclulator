package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class CreateMaxScoreRow implements CsvProcessor {
    // turn [] to max score row, change tecmap reader to read numbers in row
    // build a new row of point possible (canvas format)
    public static void createMaxScoreRow(List<String[]> rows) {
        String[] newRow = new String[rows.get(0).length];
        newRow[0] = "Points Possible";
        for (int i = 0; i < rows.get(0).length; i++) {
            String label = rows.get(0)[i];
            int begin = label.lastIndexOf('[');
            int end = label.lastIndexOf(']');
            if (begin >= 0 && end >= 0) {
                String maxScoreStr = label.substring(begin + 1, end);
                newRow[i] = maxScoreStr;
            }
        }
        // Fill the empty spots ""
        for (int i = 1; i < newRow.length; i++) {
            if (newRow[i] == null || newRow[i].isEmpty()) {
                newRow[i] = "";
            }
        }
        rows.add(2, newRow);
    }

    @Override
    public boolean shouldProcessFile(String filename) {
        return true;
    }

    @Override
    public void processRows(List<String[]> rows) {
        createMaxScoreRow(rows);
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return "";
    }

    @Override
    public void writeToFile(String filepath) throws IOException {

    }
}
