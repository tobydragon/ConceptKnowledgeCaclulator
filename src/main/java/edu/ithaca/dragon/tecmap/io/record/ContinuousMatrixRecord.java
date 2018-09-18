package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.util.List;

/**
 * Created by Benjamin on 9/18/2018.
 * This class represents all information that can be held as a matrix of doubles
 */
public class ContinuousMatrixRecord {

    private List<AssessmentItem> columnItems;
    private List<String> columnIds;
    private List<String> rowIds;
    private double[][] matrix;

    public ContinuousMatrixRecord(List<AssessmentItem> columnItemsIn, List<String> columnIdsIn, List<String> rowIdsIn, double[][] matrixIn){
        this.columnItems = columnItemsIn;
        this.columnIds = columnIdsIn;
        this.rowIds = rowIdsIn;
        this.matrix = matrixIn;
    }

    public List<AssessmentItem> getColumnItems(){return columnItems;}

    public List<String> getColumnIds(){return columnIds;}

    public List<String> getRowIds() {return rowIds;}

    public double[][] getMatrix(){return matrix;}
}
