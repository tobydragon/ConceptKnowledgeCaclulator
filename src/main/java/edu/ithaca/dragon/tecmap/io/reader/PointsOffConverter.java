package edu.ithaca.dragon.tecmap.io.reader;

import java.util.List;

public class PointsOffConverter {

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
                try {
                    studentRow[colIdx] = String.valueOf(Integer.valueOf(pointTotalsRow[colIdx])-Integer.valueOf(studentRow[colIdx]));
                }
                catch (NumberFormatException numberFormatException){
                    studentRow[colIdx] = String.valueOf(Double.valueOf(pointTotalsRow[colIdx])-Double.valueOf(studentRow[colIdx]));
                }
            }
        }
        String[] newBlankRow = new String[1];
        newBlankRow[0]="";
        rows.set(1, newBlankRow );
    }
}
