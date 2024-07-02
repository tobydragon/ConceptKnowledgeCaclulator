package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class ConvertToSakaiLabel implements CsvProcessor {

    public static void addPointTotalsToQuestionLabels(String[] questionLabelsRow, String[] pointTotalsRow, int gradeStartColumnIndex){
        for(int colIdx=gradeStartColumnIndex; colIdx<questionLabelsRow.length; colIdx++){ // 2 for Sakai, 4 for Canvas
            questionLabelsRow[colIdx] += " ["+pointTotalsRow[colIdx]+"]";
        }
    }

    @Override
    public boolean shouldProcessFile(String filename) {
        return true;
    }

    @Override
    public void processRows(List<String[]> rows) {
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return "";
    }

    @Override
    public void writeToFile(String filepath) throws IOException {

    }
}
