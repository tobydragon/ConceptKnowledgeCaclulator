package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ContinuousMatrixRecord;
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
    void exploratoryGraph(ContinuousMatrixRecord assessmentMatrix)throws Exception;

    /**
     * creates a matrix of factors in java using assessment matrix(factors=rows, LearningObjects=columns)
     * @param assessmentMatrix
     * @return statsMatrix the matrix of strengths between a factor and AssessmentItem
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    ContinuousMatrixRecord exploratoryMatrix(ContinuousMatrixRecord assessmentMatrix)throws Exception;

    /**
     * displays a graph with connections (given by user) between assessments and higher up objects with strengths in connection
     * @param assessmentMatrix
     * @param acg
     */
    void displayConfirmatoryGraph(ContinuousMatrixRecord assessmentMatrix, ConceptGraph acg);


    /**
     * creates a matrix with values measuring the strength of the connection between a learningResource and factor given by a resource.json file
     * @param acg
     * @return
     */
    ContinuousMatrixRecord confirmatoryMatrix(ConceptGraph acg)throws Exception;

}
