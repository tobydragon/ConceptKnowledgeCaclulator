package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.*;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import io.vavr.Tuple2;
import io.vavr.collection.Iterator;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.DoubleStream;

public class BayesPredictor implements LearningPredictor {

    private Classifier<Double, String> bayesClassifier;

    /**
     * Default constructor
     * Sets memory capacity to 500 as a default
     */
    public BayesPredictor() {
        bayesClassifier = new BayesClassifier<>();
        bayesClassifier.setMemoryCapacity(500);
    }

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
     * Converts KnowledgeEstimateMatrix into a Dataframe
     * @param rawData
     * @param columnsToInclude list of columns (assessment Ids) to be included in the dataframe
     * @return DataFrame that is easier to use (IN ORDER TO GET VALUES, YOU MUST USE THE COLUMNID TYPE THAT IS DICTATED BY THE DATAFRAME
     */
    public static DataFrame toDataFrame(KnowledgeEstimateMatrix rawData, List<String> columnsToInclude) {
        if (rawData != null) {
            //Column for Student IDs
            List<String> studentIDs = rawData.getUserIdList();
            StringColumn studentIDCol = StringColumn.ofAll(ColumnIds.StringColumnId.of("Students"), studentIDs);

            //List of Columns for Each Assessment
            List<DoubleColumn> assessmentCols = new ArrayList<>();
            List<String> assessmentIDs = new ArrayList<>(); //list of IDs for each assessment
            for (AssessmentItem assessment : rawData.getObjList()) {
                assessmentIDs.add(assessment.getId());
            }

            double[][] assessmentMatrix = rawData.getStudentKnowledgeEstimates();
            for (int i = 0; i < assessmentMatrix.length; i++) {
                String currAssessmentId = assessmentIDs.get(i);
                if (columnsToInclude.contains(currAssessmentId)) {
                    DoubleColumn assessmentCol = DoubleColumn.ofAll(ColumnIds.DoubleColumnId.of(currAssessmentId), DoubleStream.of(assessmentMatrix[i]));
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
     * Discretizes a column for an assignment (type double) and gives back a dataframe with that column replaced by a
     * categorical variable
     * @param original
     * @param assessmentId
     * @return new DataFrame with a category column replacing the double column, null if assessmentId doesn't match any column
     */
    public static DataFrame discretizeGradeColumn(@NotNull DataFrame original, String assessmentId) {
        Iterator<ColumnId> columnIds = original.getColumnIds().iterator();
        ColumnId assessmentColumnId = null;
        assessmentColumnId = getColumnId(original, assessmentId);
        if (assessmentColumnId == null || assessmentColumnId.getType() != ColumnType.DOUBLE) {
            return null;
        } else {
            DoubleColumn assessmentColumn = (DoubleColumn) original.getColumn(assessmentColumnId);
            List<String> discreteValues = new ArrayList<>();
            for (Double grade : assessmentColumn.valueStream().toArray()) {
                if (grade <= Predictor.ESTIMATE_THRESHOLD) {
                    discreteValues.add("AT-RISK");
                } else {
                    discreteValues.add("OK");
                }
            }
            CategoryColumn discreteColumn = CategoryColumn.ofAll(ColumnIds.CategoryColumnId.of(assessmentId), discreteValues);

            List<Column<?>> allCols = new ArrayList<>();
            for (Column<?> column : original.iterator()) {
                if (column.getId() != assessmentColumnId) {
                    allCols.add(column);
                }
            }
            allCols.add(discreteColumn);

            return DataFrame.ofAll(allCols);
        }
    }

    /**
     * Gets a collection of all of the double collections for a given row
     * @param dataFrame
     * @param rowIndex
     * @return collection of grades as doubles
     */
    public static Collection<Double> getGrades(@NotNull DataFrame dataFrame, int rowIndex) {
        Collection<Double> grades = new ArrayList<>();
        List<ColumnIds.DoubleColumnId> columnIds = new ArrayList<>();
        for (ColumnId columnId : dataFrame.getColumnIds()) {
            if (columnId.getType() == ColumnType.DOUBLE) {
                columnIds.add((ColumnIds.DoubleColumnId) columnId);
            }
        }
        for (ColumnIds.DoubleColumnId columnId : columnIds) {
            grades.add(dataFrame.getValueAt(rowIndex, columnId));
        }
        return grades;
    }

    /**
     * Gets rows from the dataframe for learning
     * @param learningData Dataframe to be parsed
     * @param discreteColumn The column that is discretized
     * @return a Map of studentId -> (a tuple pair of category and grades)
     */
    public static Map<String, Tuple2<String, Collection<Double>>> getRowsToLearn(@NotNull DataFrame learningData, String discreteColumn) {
        Map<String, Tuple2<String, Collection<Double>>> learningRows = new HashMap<>();
        for (int i = 0; i < learningData.getRowCount(); i++) {
            //Get the student ID for the first string in the map
            ColumnIds.StringColumnId studentIdColumn = (ColumnIds.StringColumnId) getColumnId(learningData, "Students");
            String studentId = learningData.getValueAt(i, studentIdColumn);

            //Get the category string for the currentRow map
            ColumnIds.CategoryColumnId discreteColumnId = (ColumnIds.CategoryColumnId) getColumnId(learningData, discreteColumn);
            String currentCategory = learningData.getValueAt(i, discreteColumnId);

            //Get the rest of the grades
            Collection<Double> grades = getGrades(learningData, i);

            Tuple2<String, Collection<Double>> currentRow = new Tuple2<>(currentCategory, grades);
            learningRows.put(studentId, currentRow);
        }

        return learningRows;
    }

    /**
     * Get rows from dataframe for testing
     * @param testingData
     * @return
     */
    public static Map<String, Collection<Double>> getRowsToTest(@NotNull DataFrame testingData) {
        Map<String, Collection<Double>> testingRows = new HashMap<>();
        for (int i = 0; i < testingData.getRowCount(); i++) {
            //Get student ID
            ColumnIds.StringColumnId studentIdColumn = (ColumnIds.StringColumnId) getColumnId(testingData, "Students");
            String studentId = testingData.getValueAt(i, studentIdColumn);

            //Get grades
            Collection<Double> grades = getGrades(testingData, i);

            testingRows.put(studentId, grades);
        }
        return testingRows;
    }

    /**
     * Resets and unlearns everything, resets category count
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     */
    public void reset() {
        bayesClassifier.reset();
        bayesClassifier.setMemoryCapacity(500);
    }

    /**
     * Learns given the necessary data, category that assignment belongs to based on features given
     * @param category
     * @param grades
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     */
    private void learn(String category, Collection<Double> grades) {
        bayesClassifier.learn(category, grades);
    }

    /**
     * Trains the data based on the raw data matrix you give it
     * @param rawTrainingData in the form of KnowledgeEstimateMatrix
     * @param assessmentToLearn string of the assessment you want to learn on (will be the categorical variable), should have doubles as grade type
     * @param assignmentsToLearnWith list of what columns should be used in learning (must include the assessmentToLearn)
     * TRAINING DATA MUST BE MANIPULATED IN ORDER TO USE THE BAYES LEARN METHOD
     */
    public void learnSet(KnowledgeEstimateMatrix rawTrainingData, String assessmentToLearn, List<String> assignmentsToLearnWith) {
        if (!assignmentsToLearnWith.contains(assessmentToLearn)) {
            throw new RuntimeException("Assignments to Learn Must include assessmentToLearn");
        }
        //Get a dataframe from the raw training data
        DataFrame rawTrainingDataframe = toDataFrame(rawTrainingData, assignmentsToLearnWith);

        //Get discretized column using assessmentToLearn param
        DataFrame discretizedTrainingDataframe = discretizeGradeColumn(rawTrainingDataframe, assessmentToLearn);

        //Get rows from the discretized dataframe
        Map<String, Tuple2<String, Collection<Double>>> trainingRows = getRowsToLearn(discretizedTrainingDataframe, assessmentToLearn);

        //For each row, call learn
        for (Tuple2<String, Collection<Double>> row: trainingRows.values()) {
            learn(row._1, row._2);
        }
    }

    /**
     * classifies a single "row" of data in the form of a collection
     * @param testData a "row" of doubles(grades) for a single student
     * Uses Bayes Classify Method
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     * @return String classification for a student
     */
    public String classify(Collection<Double> testData) {
        return bayesClassifier.classify(testData).getCategory();
    }

    /**
     * Classifies the data based on the raw testing matrix you give it, should not have a grade for what you are predicting
     * @param rawTestingData in the form of KnowledgeEstimateMatrix
     * @param assignmentsForClassification list of what assessment columns should be used in learning (should all be doubles, not the categorical variable)
     * TESTING DATA MUST BE MANIPULATED IN ORDER TO GET ROWS FOR THE BAYES CLASSIFY METHOD
     * @return Map of String to String (Student id -> Classification) MAY CHANGE
     */
    public Map<String, String> classifySet(KnowledgeEstimateMatrix rawTestingData, List<String> assignmentsForClassification) {
        //Get a dataframe from the raw testing data
        DataFrame rawTestingDataframe = toDataFrame(rawTestingData, assignmentsForClassification);

        //Get rows from the dataframe (studentId -> grades)
        Map<String, Collection<Double>> testingRows = getRowsToTest(rawTestingDataframe);

        //Get classifications for data
        Map<String, String> classifications = new HashMap<>();

        //For each row, call classify
        for (String studentId : testingRows.keySet()) {
            String classification = classify(testingRows.get(studentId));
            classifications.put(studentId, classification);
        }

        return classifications;
    }
}
