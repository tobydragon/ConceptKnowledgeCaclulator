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
     * displays a graph connecting learning objects to similar higher up objects
     * @param assessmentMatrix KnowledgeEstimateMatrix object will use the RMatrix data member
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    void displayExploratoryGraph(ContinuousAssessmentMatrix assessmentMatrix)throws Exception;

    /**
     * creates a matrix of factors in java using assessment matrix(factors=rows, LearningObjects=columns)
     * @param assessmentMatrix
     * @return statsMatrix the matrix of strengths between a factor and AssessmentItem
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    double[][] calculateExploratoryMatrix(ContinuousAssessmentMatrix assessmentMatrix)throws Exception;

    /**
     * displays a graph with connections (given by user) between assessments and higher up objects with strengths in connection
     * @param assessmentMatrix
     * @param ccg
     */
    void displayConfirmatoryGraph(ContinuousAssessmentMatrix assessmentMatrix, CohortConceptGraphs ccg);


    /**
     * creates a matrix of factors in java using assessment matrix and connections given by user(factors=rows, LearningObjects=columns)
     * @param assessmentMatrix
     * @param ccg
     * @return
     */
    double[][] calculateConfirmatoryMatrix(ContinuousAssessmentMatrix assessmentMatrix, CohortConceptGraphs ccg);

}
