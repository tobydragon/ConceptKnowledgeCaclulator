package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmap;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileData;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.reader.ReaderTools;
import edu.ithaca.dragon.tecmap.io.record.*;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static List<LearningResourceRecord> learningResourcesFromExploratoryFactorMatrixRecord(ContinuousMatrixRecord factorMatrixRecord){

        List<LearningResourceRecord> learningResourceRecordList = new ArrayList<>();
        List<LearningResourceType> resourceTypeList = new ArrayList<>();
        resourceTypeList.add(resourceType);

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

    public static ConceptGraphRecord conceptGraphFromExploratoryMatrixRecord(ContinuousMatrixRecord factorMatrixRecord, String conceptGraphRecordName){
        List<AssessmentItem> assessmentItems = factorMatrixRecord.getAssessmentItems();
        double[][] dataMatrix = factorMatrixRecord.getDataMatrix();
        //Create List<ConceptRecord>
        List<ConceptRecord> conceptRecordList = new ArrayList<>();

        //holds onto the index of the assessment to match with the dataMatrix. Increments to the next index once all factors are checked within an assessment
        int matrixColumnIndexIterator = 0;
        List<String> factorList = factorMatrixRecord.getRowIds();
        //Create List<LinkRecord>
        List<LinkRecord> linksList = new ArrayList<>();


        for(String factor : factorList){
            ConceptRecord conceptRecord = new ConceptRecord(factor);
            conceptRecordList.add(conceptRecord);
        }

        ConceptGraphRecord conceptGraphRecord = new ConceptGraphRecord(conceptGraphRecordName, conceptRecordList, linksList);
        return conceptGraphRecord;
    }


    public static SuggestingTecmap createGraphAndResourceFromAssessment(List <AssessmentItem> assessmentItems) {
        //Get List of all ItemResponses connected to AssessmentItems for SuggestingTecmap
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItem.getItemResponsesFromAssessmentList(assessmentItems);
        //Make AssessmentItems list copy that does not have connections to its itemResponses
        List<AssessmentItem> assessmentItemsWithoutResponses = AssessmentItem.getAssessmentCopyWithoutResponses(assessmentItems);
        ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);
        ContinuousMatrixRecord factorMatrix = FactorAnalysis.calculateExploratoryMatrix(assessmentMatrix);

        List<LearningResourceRecord> lrrList = learningResourcesFromExploratoryFactorMatrixRecord(factorMatrix);
        ConceptGraphRecord conceptGraphRecord = conceptGraphFromExploratoryMatrixRecord(factorMatrix, "newConceptGraphRecord");

        return new SuggestingTecmap(new ConceptGraph(conceptGraphRecord), lrrList, assessmentItemsWithoutResponses, assessmentItemResponses);
        /*
        //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
        ReaderTools.learningObjectsFromCSVList(2, files.getAssessmentFiles()),
        AssessmentItemResponse.createAssessmentItemResponses(files.getAssessmentFiles()));
        */
    }
}
