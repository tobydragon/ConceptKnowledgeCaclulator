package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.ReaderTools;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssessmentAddedState extends NoAssessmentState {

    private List<AssessmentItem> assessmentItems;
    //These items and responses not linked together, because we don't know about the links to the graph yet
    protected List<AssessmentItemResponse> assessmentItemResponses;

    public AssessmentAddedState(String structureFile, List<String> assessmentFiles) throws IOException {
        super(structureFile);
        //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
        assessmentItems = ReaderTools.learningObjectsFromCSVList(2, assessmentFiles);
        assessmentItemResponses = createAssessmentItemResponses(assessmentFiles);
    }

    public List<LearningResourceRecord> createBlankLearningResourceRecordsFromAssessment() {
        return LearningResourceRecord.createLRecordsFromAssessments(assessmentItems);
    }

    private static List<AssessmentItemResponse> createAssessmentItemResponses(List<String> assessmentFiles) throws IOException{
        List<AssessmentItemResponse> assessments = new ArrayList<>();
        for (String aname: assessmentFiles){
            CSVReader csvReader = new SakaiReader(aname);
            List<AssessmentItemResponse> temp = csvReader.getManualGradedResponses();
            assessments.addAll(temp);
        }
        return assessments;
    }

}
