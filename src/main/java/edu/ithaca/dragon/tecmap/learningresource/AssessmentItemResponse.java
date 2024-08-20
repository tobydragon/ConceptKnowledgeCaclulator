package edu.ithaca.dragon.tecmap.learningresource;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.io.reader.*;
import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author tdragon
 * 2/14/17.
 */
public class AssessmentItemResponse {

    private String userId;
    private String assessmentItemId;
    private String assessmentItemText;
    private double knowledgeEstimate;

    public AssessmentItemResponse(String userId, String assessmentItemId, String assessmentItemText, double knowledgeEstimate) {
        this.userId = userId;
        this.assessmentItemId = assessmentItemId;
        this.assessmentItemText = assessmentItemText;
        this.knowledgeEstimate = knowledgeEstimate;
    }

    public AssessmentItemResponse(AssessmentItemResponse other){
        this.userId = other.userId;
        this.assessmentItemId = other.assessmentItemId;
        this.assessmentItemText = other.assessmentItemText;
        this.knowledgeEstimate = other.knowledgeEstimate;
    }

    //TODO: this needs to be tested
    public static Map<String, List<AssessmentItemResponse>> getUserResponseMap(List<AssessmentItemResponse> responses) {
        Map<String, List<AssessmentItemResponse>> userIdToResponses = new TreeMap<>();
        for(AssessmentItemResponse response : responses){
            List<AssessmentItemResponse> userResponses = userIdToResponses.get(response.getUserId());
            //If nothing has been added for this user, add a new map entry for that user
            if (userResponses == null){
                userResponses = new ArrayList<>();
                userIdToResponses.put(response.getUserId(), userResponses);
            }
            //add the response to the list (
            userResponses.add(response);
        }

        return userIdToResponses;
    }

    public String getAssessmentItemId(){ return assessmentItemId; }

    public double calcKnowledgeEstimate(){
        return knowledgeEstimate;
    }

    public String getUserId(){
        return userId;
    }

    public String getAssessmentItemText(){ return assessmentItemText; }

    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!AssessmentItemResponse.class.isAssignableFrom(other.getClass())){
            return false;
        }
        AssessmentItemResponse otherNode = (AssessmentItemResponse) other;
        if(this.userId.equals(otherNode.userId) && DataUtil.equalsDoubles(this.knowledgeEstimate, otherNode.knowledgeEstimate)
                && this.assessmentItemId.equals(otherNode.assessmentItemId)){
            return true;
        } else {
            return false;
        }
    }

    public String toString(){
        return getAssessmentItemText() + "\tuser: "+ getUserId() + "\t est: "+ calcKnowledgeEstimate();
    }

    public static List<AssessmentItemResponse> createAssessmentItemResponses(List<String> assessmentFiles) throws IOException, CsvException {
        List<AssessmentItemResponse> assessments = new ArrayList<>();
        for (String aname: assessmentFiles){
            List<String[]> rows = CsvFileLibrary.parseRowsFromFile(aname);
            List<CsvProcessor> processors = new ArrayList<>();
            processors.add(new CanvasConverter());
            TecmapCSVReader tecmapCsvReader = new CanvasReader(rows, processors);
            List<AssessmentItemResponse> temp = tecmapCsvReader.getManualGradedResponses();
            assessments.addAll(temp);
        }
        return assessments;
    }
}
