package edu.ithaca.dragon.tecmap.analysis;

import java.util.ArrayList;
import java.util.List;

public class MatrixTransforms {
    /**
     * Creates a new matrix without useless all 0 rows
     * @param confirmatoryMatrix
     * @return double[][]
     */
    public static double[][] removeEmptyRowsFromMatrix(double[][] confirmatoryMatrix){
        List<Integer> indicesToAdd = new ArrayList<>();
        for(int i=0;i<confirmatoryMatrix.length; i++){
            for(int j=0;j<confirmatoryMatrix[0].length; j++){
                if(confirmatoryMatrix[i][j] != 0){
                    if(false == indicesToAdd.contains(i)){
                        indicesToAdd.add(i);
                    }

                }
            }
        }
        double[][] cleanedMatrix = new double[indicesToAdd.size()][confirmatoryMatrix[0].length];

        int newIndex = 0;
        for(int rowIndex :indicesToAdd){
            for (int j = 0; j < confirmatoryMatrix[0].length; j++) {
                cleanedMatrix[newIndex][j] = confirmatoryMatrix[rowIndex][j];
            }
            newIndex++;
        }
        return cleanedMatrix;
    }

    /**
     * Transposes a matrix of doubles [row][column] -> [column][row]
     * @param originalMatrix
     * @return
     */
    public static double[][] transposeMatrix(double[][] originalMatrix){
        double[][] transposedMatrix = new double[originalMatrix[0].length][originalMatrix.length];
        for (int i = 0; i < originalMatrix.length; i++) {
            for (int j = 0; j < originalMatrix[0].length; j++) {
                transposedMatrix[j][i] = originalMatrix[i][j];
            }

        }
        return transposedMatrix;
    }
}
