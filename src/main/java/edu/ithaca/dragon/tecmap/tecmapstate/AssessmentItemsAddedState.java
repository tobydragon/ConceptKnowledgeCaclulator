package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.io.reader.ReaderTools;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.IOException;
import java.util.List;

public class AssessmentItemsAddedState extends BaseState {

    private List<AssessmentItem> assessmentItems;

    //TODO: should probably create assessment items, not just hold filenames
    public AssessmentItemsAddedState(String structureFile, List<String> assessmentFiles) throws IOException {
        super(structureFile);
        //TODO: hardcoded to sakai csv, need to hold a list of CSVReaders, or the information about which kind of reader it is...
        assessmentItems = ReaderTools.learningObjectsFromCSVList(2, assessmentFiles);
    }

    public List<LearningResourceRecord> createBlankLearningResourceRecordsFromAssessment() {
        return LearningResourceRecord.createLRecordsFromAssessments(assessmentItems);
    }

}
