package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.*;
import edu.ithaca.dragon.tecmap.learningresource.DiscreteAssessmentMatrix;
import io.vavr.Tuple2;

import javax.validation.constraints.NotNull;
import java.util.*;

public class DiscreteDataFrameFunctions {

    /**
     * Gets columnId based on string, assumes unique names for columns
     * @param dataFrame
     * @param columnId
     * @return
     */
    private static ColumnId getColumnId(DataFrame dataFrame, String columnId) {
        for (ColumnId colId : dataFrame.getColumnIds()) {
            if (colId.getName().equals(columnId)) {
                return colId;
            }
        }
        return null;
    }

    /**
     * Converts DiscreteAssessmentMatrix into a Dataframe
     * @param rawData
     * @param columnsToInclude list of columns (assessment Ids) to be included in the dataframe
     * @return DataFrame that is easier to use (IN ORDER TO GET VALUES, YOU MUST USE THE COLUMNID TYPE THAT IS DICTATED BY THE DATAFRAME
     */
    public static DataFrame toDataFrame(DiscreteAssessmentMatrix rawData, List<String> columnsToInclude) {
        if (rawData != null) {
            //Column for Student IDs
            List<String> studentIDs = rawData.getStudentIds();
            StringColumn studentIDCol = StringColumn.ofAll(ColumnIds.StringColumnId.of("Students"), studentIDs);

            //List of Columns for Each Assessment
            List<CategoryColumn> assessmentCols = new ArrayList<>();
            List<String> assessmentIDs = rawData.getAssessmentIds(); //list of IDs for each assessment

            String[][] assessmentMatrix = rawData.getStudentAssessmentGrades();
            for (int i = 0; i < assessmentMatrix.length; i++) {
                String currAssessmentId = assessmentIDs.get(i);
                if (columnsToInclude.contains(currAssessmentId)) {
                    CategoryColumn assessmentCol = CategoryColumn.ofAll(ColumnIds.CategoryColumnId.of(currAssessmentId), assessmentMatrix[i]);
                    assessmentCols.add(assessmentCol);
                }
            }

            List<Column<?>> allCols = new ArrayList<>();
            allCols.add(studentIDCol);
            allCols.addAll(assessmentCols);

            return DataFrame.ofAll(allCols);

        } else {
            return null;
        }
    }

    /**
     * Gets a collection of all of the double collections for a given row
     * @param dataFrame
     * @param rowIndex
     * @return collection of grades as doubles
     */
    public static Collection<String> getGrades(@NotNull DataFrame dataFrame, List<String> assessmentsToInclude, int rowIndex) {
        Collection<String> grades = new ArrayList<>();
        List<ColumnIds.CategoryColumnId> columnIds = new ArrayList<>();
        for (ColumnId columnId : dataFrame.getColumnIds()) {
            if (columnId.getType() == ColumnType.CATEGORY && assessmentsToInclude.contains(columnId.getName())) {
                columnIds.add((ColumnIds.CategoryColumnId) columnId);
            }
        }
        for (ColumnIds.CategoryColumnId columnId : columnIds) {
            grades.add(dataFrame.getValueAt(rowIndex, columnId));
        }
        return grades;
    }

    /**
     * Gets rows from the dataframe for learning
     * @param learningData Dataframe to be parsed
     * @param columnToLearn The column that is discretized
     * @return a Map of studentId -> (a tuple pair of category and grades)
     */
    public static Map<String, Tuple2<String, Collection<String>>> getRowsToLearn(@NotNull DataFrame learningData, List<String> assessmentsToLearnWith, String columnToLearn) {
        Map<String, Tuple2<String, Collection<String>>> learningRows = new HashMap<>();
        //Get just the columns to learn from
        assessmentsToLearnWith.remove(columnToLearn);
        for (int i = 0; i < learningData.getRowCount(); i++) {
            //Get the student ID for the first string in the map
            ColumnIds.StringColumnId studentIdColumn = (ColumnIds.StringColumnId) getColumnId(learningData, "Students");
            String studentId = learningData.getValueAt(i, studentIdColumn);

            //Get the category string for the currentRow map
            ColumnIds.CategoryColumnId discreteColumnId = (ColumnIds.CategoryColumnId) getColumnId(learningData, columnToLearn);
            String currentCategory = learningData.getValueAt(i, discreteColumnId);

            //Get the rest of the grades
            Collection<String> grades = getGrades(learningData, assessmentsToLearnWith, i);


            Tuple2<String, Collection<String>> currentRow = new Tuple2<>(currentCategory, grades);
            learningRows.put(studentId, currentRow);
        }
        //Don't have the method have side effects
        assessmentsToLearnWith.add(columnToLearn);
        return learningRows;
    }

    /**
     * Get rows from dataframe for testing
     * @param testingData
     * @return
     */
    public static Map<String, Collection<String>> getRowsToTest(@NotNull DataFrame testingData, List<String> assessmentsToClassifyWith) {
        Map<String, Collection<String>> testingRows = new HashMap<>();
        for (int i = 0; i < testingData.getRowCount(); i++) {
            //Get student ID
            ColumnIds.StringColumnId studentIdColumn = (ColumnIds.StringColumnId) getColumnId(testingData, "Students");
            String studentId = testingData.getValueAt(i, studentIdColumn);

            //Get grades
            Collection<String> grades = getGrades(testingData, assessmentsToClassifyWith, i);

            testingRows.put(studentId, grades);
        }
        return testingRows;
    }

}
