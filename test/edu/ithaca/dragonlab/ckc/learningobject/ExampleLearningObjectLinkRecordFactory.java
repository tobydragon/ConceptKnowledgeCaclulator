package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectLinkRecordFactory {

    public static List<LearningObjectLinkRecord> makeSimpleLOLRecords(){
        List<LearningObjectLinkRecord> clList = new ArrayList<>();

        clList.add(new LearningObjectLinkRecord("Q1", Arrays.asList("B")));
        clList.add(new LearningObjectLinkRecord("Q2", Arrays.asList("B")));
        clList.add(new LearningObjectLinkRecord("Q3", Arrays.asList("C")));
        clList.add(new LearningObjectLinkRecord("Q4", Arrays.asList("C")));
        clList.add(new LearningObjectLinkRecord("Q5", Arrays.asList("C")));
        clList.add(new LearningObjectLinkRecord("Q6", Arrays.asList("C")));

        return clList;
    }


}
