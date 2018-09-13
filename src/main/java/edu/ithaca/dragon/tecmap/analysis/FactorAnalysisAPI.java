package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;

/**
 * Created by Benjamin on 9/13/2018.
 */
public interface FactorAnalysisAPI {

    /**
     * Prints out a factor matrix connecting learning objects to similar higher up objects
     * @param assessmentMatrix KnowledgeEstimateMatrix object will use the RMatrix data member
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    void getExploratoryGraph(ContinuousAssessmentMatrix assessmentMatrix)throws Exception;

    /**
     * creates a matrix of factors in java (factors=rows, LearningObjects=columns)
     * @param assessmentMatrix
     * @return statsMatrix the matrix of strengths between a factor and AssessmentItem
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    double[][] returnExploratoryMatrix(ContinuousAssessmentMatrix assessmentMatrix)throws Exception;

    /**
     * Prints out a factor matrix providing strengths for the given connections from the user
     * @param assessmentMatrix
     * @param ccg
     */
    void getConfirmatoryGraph(ContinuousAssessmentMatrix assessmentMatrix, CohortConceptGraphs ccg);

    double[][] returnConfirmatoryMatrix(ContinuousAssessmentMatrix assessmentMatrix, CohortConceptGraphs ccg);

}
