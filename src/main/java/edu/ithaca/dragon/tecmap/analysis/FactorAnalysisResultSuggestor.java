package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ContinuousMatrixRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.ithaca.dragon.tecmap.learningresource.LearningResourceType.ASSESSMENT;

public class FactorAnalysisResultSuggestor {

    public static LearningResourceType resourceType = ASSESSMENT;

    /**
     * Between 0-1: 0 includes all; 1 is very exclusive
     * Determines what values in the matrix are large enough to connect a factor to the AssessmentItem
     */
    public static double connectionThreshold = 0.3;


    /**
     * Creates a list of learningResourceRecords using a ContinuousMatrixRecord
     * connecting assessments to factors using factor analysis to and a threshold to determine connections
     * @param factorMatrixRecord
     * @return List<LearningResourceRecord> LearningResourceRecordList
     */
    public static List<LearningResourceRecord> learningResourceFromExploratoryFactorMatrixRecord(ContinuousMatrixRecord factorMatrixRecord){

        List<LearningResourceRecord> learningResourceRecordList = new ArrayList<>();
        List<LearningResourceType> resourceTypeList = new ArrayList<>();
        resourceTypeList.add(resourceType);

        List<LearningResourceRecord> resourceList = new ArrayList<>();
        double[][] dataMatrix = factorMatrixRecord.getDataMatrix();

        //holds onto the index of the assessment to match with the dataMatrix. Increments to the next index once all factors are checked within an assessment
        int matrixColumnIndexIterator = 0;

        List<String> factorList = factorMatrixRecord.getRowIds();
        //Look through each assessment in the ContinuousMatrixRecord
        for(AssessmentItem assessment : factorMatrixRecord.getAssessmentItems()){
            //Look through each value connected with the assessment
            //If that value is greater than the connectionThreshold, add it to a list of connected concepts
            Collection<String> viableFactors = new ArrayList<>();
            for (int row = 0; row < factorList.size(); row++) {
                if(dataMatrix[row][matrixColumnIndexIterator] > connectionThreshold){
                    viableFactors.add(factorList.get(row));
                }
            }
            LearningResourceRecord learningResourceRecord = new LearningResourceRecord(assessment.getId(), resourceTypeList, viableFactors, assessment.getMaxPossibleKnowledgeEstimate(), 1.0);
            learningResourceRecordList.add(learningResourceRecord);

            matrixColumnIndexIterator++;
        }
        return learningResourceRecordList;
    }
/*
    public static ConceptGraph conceptGraphFromExploratoryMatrixRecord(ContinuousMatrixRecord factorMatrixRecord){

    }
    */
}
