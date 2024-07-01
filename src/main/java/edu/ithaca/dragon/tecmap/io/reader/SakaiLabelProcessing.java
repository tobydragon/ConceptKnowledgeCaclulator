package edu.ithaca.dragon.tecmap.io.reader;

public class SakaiLabelProcessing {

    public static void addPointTotalsToQuestionLabels(String[] questionLabelsRow, String[] pointTotalsRow, int gradeStartColumnIndex){
        for(int colIdx=gradeStartColumnIndex; colIdx<questionLabelsRow.length; colIdx++){ // 2 for Sakai, 4 for Canvas
            questionLabelsRow[colIdx] += " ["+pointTotalsRow[colIdx]+"]";
        }
    }
}
