package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import edu.ithaca.dragon.tecmap.prediction.predictionsetselector.PredictionSetSelector;
import edu.ithaca.dragon.tecmap.prediction.predictor.LearningPredictor;
import edu.ithaca.dragon.tecmap.prediction.predictor.Predictor;
import io.vavr.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionController {

    private Predictor predictor;
    private PredictionSetSelector predictionSetSelector;

    public PredictionController(Predictor predictor, PredictionSetSelector predictionSetSelector) {
        this.predictor = predictor;
        this.predictionSetSelector = predictionSetSelector;
    }

    /**
     * Gets graph from the main datastore assuming:
     * assessment files are all in a private subfolder,
     * resources files are all in the main folder and contain "Resources" in their name,
     * there is one graph file and contains "Graph" in its name
     * @param courseName the name of the directory in the datastore for the course
     * @return ConceptGraph built from the graph file, the assessment files and resource files
     */
    static ConceptGraph getConceptGraph(String courseName, String datastorePath) throws IOException {
        //Get assessment filenames
        TecmapFileDatastore courseDatastore = TecmapFileDatastore.buildFromJsonFile(datastorePath);
        SuggestingTecmapAPI courseTecmap = courseDatastore.retrieveTecmapForId(courseName);
        return courseTecmap.getAverageConceptGraph();
    }

    /**
     * Splits the given matrix into two matrices, first has the given ratio size, second has (1-ratio) size
     * Assumes that all students have all assessments with responses
     * @param originalMatrix
     * @param ratio
     * @return Tuple2 following the given first(_1) has the given ratio size, second(_2) has (1-ratio) size
     */
    static Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix(ContinuousAssessmentMatrix originalMatrix, Double ratio) {
        int ratioSize = (int) Math.ceil(originalMatrix.getStudentIds().size()*ratio);

        List<String> ratioUserIds = new ArrayList<>();
        for (int i = 0; i < originalMatrix.getStudentIds().size(); i++) {
            if (i < ratioSize) {
                ratioUserIds.add(originalMatrix.getStudentIds().get(i));
            }
        }

        //For ratioMatrix
        List<AssessmentItem> ratioAssessments = new ArrayList<>();
        //For nonRatioMatrix
        List<AssessmentItem> nonRatioAssessments = new ArrayList<>();

        List<AssessmentItem> allAssessments = originalMatrix.getColumnItems();
        for (AssessmentItem item : allAssessments) {
            AssessmentItem ratioItem = new AssessmentItem(item.getId(), item.getMaxPossibleKnowledgeEstimate());
            AssessmentItem nonRatioItem = new AssessmentItem(item.getId(), item.getMaxPossibleKnowledgeEstimate());
            for (int i = 0; i < item.getResponses().size(); i++) {
                AssessmentItemResponse currResponse = item.getResponses().get(i);
                //Keeps the users between the two matrices constant instead of by index
                if (ratioUserIds.contains(currResponse.getUserId())) {
                    ratioItem.addResponse(currResponse);
                } else {
                    nonRatioItem.addResponse(currResponse);
                }
            }
            ratioAssessments.add(ratioItem);
            nonRatioAssessments.add(nonRatioItem);
        }
        ContinuousAssessmentMatrix ratioMatrix = new ContinuousAssessmentMatrix(ratioAssessments);
        ContinuousAssessmentMatrix nonRatioMatrix = new ContinuousAssessmentMatrix(nonRatioAssessments);

        return new Tuple2<>(ratioMatrix, nonRatioMatrix);
    }

    /**
     * Finds the students that have responses to all of the assessments to be included
     * @param allAssessments
     * @param assessmentsToInclude
     * @return all of the assessmentItemResponses for the students that are to be included
     */
    static List<AssessmentItem> getAssessmentsForStudentsWithAllResponses(List<AssessmentItem> allAssessments, List<String> assessmentsToInclude) {
        Map<String, List<AssessmentItemResponse>> studentResponses = new HashMap<>();
        Map<String, Double> originalMaxKnowledgeEstimates = new HashMap<>();
        for (AssessmentItem columnItem : allAssessments) {
            if (assessmentsToInclude.contains(columnItem.getId())) {
                originalMaxKnowledgeEstimates.put(columnItem.getId(), columnItem.getMaxPossibleKnowledgeEstimate());
                for (AssessmentItemResponse response : columnItem.getResponses()) {
                    if (studentResponses.containsKey(response.getUserId())) {
                        studentResponses.get(response.getUserId()).add(response);
                    } else {
                        List<AssessmentItemResponse> responseList = new ArrayList<>();
                        responseList.add(response);
                        studentResponses.put(response.getUserId(), responseList);
                    }
                }
            }
        }
        List<AssessmentItemResponse> studentsWithAllAssessmentsToInclude = new ArrayList<>();
        for (Map.Entry<String, List<AssessmentItemResponse>> entry : studentResponses.entrySet()) {
            if (entry.getValue().size() == assessmentsToInclude.size()) {
                studentsWithAllAssessmentsToInclude.addAll(entry.getValue());
            }
        }
        return AssessmentItem.buildListFromAssessmentItemResponses(studentsWithAllAssessmentsToInclude, originalMaxKnowledgeEstimates);
    }

    /**
     * Gets the a matrix to use for prediction by removing students that don't have responses to the assessments in the prediction set
     * @param allAssessments
     * @param predictionSet
     * @return
     */
    public ContinuousAssessmentMatrix getMatrix(List<AssessmentItem> allAssessments, List<String> predictionSet) {
        List<AssessmentItem> columnItemsWithValidStudents = getAssessmentsForStudentsWithAllResponses(allAssessments, predictionSet);
        return new ContinuousAssessmentMatrix(columnItemsWithValidStudents);
    }

    /**
     * Gets the predictionSet of assessment ids for future prediction
     * @param allAssessments
     * @param studentId
     * @param assessmentToPredict
     * @return
     */
    public List<String> getPredictionSet(List<AssessmentItem> allAssessments, String studentId, String assessmentToPredict) {
        return predictionSetSelector.getPredictionSetForGivenStudent(allAssessments, studentId, assessmentToPredict);
    }

    /**
     * Gets the classifications given a testing matrix and a set to predict
     * @return
     */
    public Map<String, String> predict(ContinuousAssessmentMatrix testingMatrix, List<String> testingAssessments) {
        return predictor.classifySet(testingMatrix, testingAssessments);
    }

    /**
     * What to call with the TecmapService
     * @param allAssessments
     * @param assessmentToPredict
     */
    public void getPredictions(List<AssessmentItem> allAssessments, String assessmentToPredict) {
        List<String> predictionSet = getPredictionSet(allAssessments, allAssessments.get(0).getResponses().get(0).getUserId(), assessmentToPredict);
        ContinuousAssessmentMatrix matrix = getMatrix(allAssessments, predictionSet);
        if (this.predictor instanceof LearningPredictor) {
            Tuple2<ContinuousAssessmentMatrix, ContinuousAssessmentMatrix> splitMatrix = splitMatrix(matrix, 0.5);
            ((LearningPredictor) predictor).learnSet(splitMatrix._1, assessmentToPredict, predictionSet);
            System.out.println(predict(splitMatrix._2, predictionSet));
        } else {
            System.out.println(predict(matrix, predictionSet));
        }
    }

}
