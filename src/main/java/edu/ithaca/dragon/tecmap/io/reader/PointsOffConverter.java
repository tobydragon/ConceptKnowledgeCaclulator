package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class PointsOffConverter implements CsvProcessor {

    public static void addPointTotalsToQuestionLabels(String[] questionLabelsRow, String[] pointTotalsRow){
        for(int colIdx=2; colIdx<questionLabelsRow.length; colIdx++){
            questionLabelsRow[colIdx] += " ["+pointTotalsRow[colIdx]+"]";
        }
    }

    /**
     *
     * @throws NumberFormatException if cells can't be converted to ints or doubles
     */
    public static void convertFromPointsOffToTotalPoints(List<String[]> rows){
        addPointTotalsToQuestionLabels(rows.get(0), rows.get(1));
        String[] pointTotalsRow = rows.get(1);
        for (String[] studentRow : rows.subList(2, rows.size())){
            for(int colIdx=2; colIdx<studentRow.length; colIdx++){
                if (!studentRow[colIdx].equals("")) {
                    try {
                        studentRow[colIdx] = String.valueOf(Integer.valueOf(pointTotalsRow[colIdx]) - Integer.valueOf(studentRow[colIdx]));
                    } catch (NumberFormatException numberFormatException) {
                        studentRow[colIdx] = String.valueOf(Double.valueOf(pointTotalsRow[colIdx]) - Double.valueOf(studentRow[colIdx]));
                    }
                }
            }
        }
        String[] newBlankRow = new String[1];
        newBlankRow[0]="";
        rows.set(1, newBlankRow );
    }

    @Override
    public boolean shouldProcessFile(String filename) {
        return filename.contains("pointsOff");
    }

    @Override
    public void processRows(List<String[]> rows) {
        convertFromPointsOffToTotalPoints(rows);
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return origFilename.replace("pointsOff", "totalPoints");
    }

    @Override
    public void writeToFile(String filepath) throws IOException {

    }
}
