package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class CanvasConverter implements CsvProcessor {

    public static void canvasConverter(List<String[]> rows) {
        CreateMaxScoreRow.createMaxScoreRow(rows);
        SwapNameAndIDColumn.swapNameAndIDColumn(rows);
        MoveGradeStartColumnIndex.moveGradeStartColumnIndex(rows);
    }
    @Override
    public boolean shouldProcessFile(String filename) {
        return true;
    }

    @Override
    public void processRows(List<String[]> rows) {
        canvasConverter(rows);
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return "";
    }

    @Override
    public void writeToFile(String filepath) throws IOException {

    }
}
