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
        List<LearningResourceRecord> clList = new ArrayList<>();

        clList.add(new LearningResourceRecord("Q1", Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("B"),1 , 1));
        clList.add(new LearningResourceRecord("Q2", Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("B"),1 , 1));
        clList.add(new LearningResourceRecord("Q3", Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1));
        clList.add(new LearningResourceRecord("Q4", Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1));
        clList.add(new LearningResourceRecord("Q5", Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1));
        clList.add(new LearningResourceRecord("Q6", Arrays.asList(LearningResourceType.ASSESSMENT, LearningResourceType.PRACTICE), Arrays.asList("C"),1 , 1));

        return clList;
    }


}
