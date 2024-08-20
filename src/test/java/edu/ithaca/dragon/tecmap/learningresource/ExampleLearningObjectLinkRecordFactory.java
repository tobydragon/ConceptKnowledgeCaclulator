package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectLinkRecordFactory {

    public static List<LearningResourceRecord> makeSimpleLOLRecords(){
        List<LearningResourceRecord> lrrList = new ArrayList<>();

        lrrList.add(new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("B"),1 , 1, "Q1"));
        lrrList.add(new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("B"),1 , 1, "Q2"));
        lrrList.add(new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1, "Q3"));
        lrrList.add(new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1, "Q4"));
        lrrList.add(new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1, "Q5"));
        lrrList.add(new LearningResourceRecord(Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1, "Q6"));

        return lrrList;
    }


}
