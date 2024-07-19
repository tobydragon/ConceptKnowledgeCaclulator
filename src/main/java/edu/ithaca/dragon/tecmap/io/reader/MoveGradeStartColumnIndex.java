package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class MoveGradeStartColumnIndex implements CsvProcessor {

    public static void moveGradeStartColumnIndex(List<String[]> rows) {
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            String[] newRow = new String[row.length + 2];
            if (row.length > 1) {
                // copy elements before index 2
                System.arraycopy(row, 0, newRow, 0, 2);
                // insert empty string at position of 2 and 3
                for (int j = 2; j < 4; j++) {
                    newRow[j] = "";
                }
                // copy elements after GradeStartColumnIndex 2
                System.arraycopy(row, 2, newRow, 4, row.length - 2);
                // Replace old row with the new row
                rows.set(i, newRow);
            }
        }
    }

    @Override
    public boolean shouldProcessFile(String filename) {
        return true;
    }

    @Override
    public void processRows(List<String[]> rows) {
        moveGradeStartColumnIndex(rows);
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return "";
    }

    @Override
    public void writeToFile(String filepath) throws IOException {

    }
}
