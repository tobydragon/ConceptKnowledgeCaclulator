package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectFactory {

    public static List<LearningObject> makeSimpleLearningObject(){
        List<LearningObject> cnList = new ArrayList<>();

        cnList.add(new LearningObject("Q1"));
        cnList.add(new LearningObject("Q2"));
        cnList.add(new LearningObject("Q3"));
        cnList.add(new LearningObject("Q4"));
        cnList.add(new LearningObject("Q5"));
        cnList.add(new LearningObject("Q6"));

        cnList.get(0).addResponse(new LearningObjectResponse("student1", "Q1", 1));
        cnList.get(1).addResponse(new LearningObjectResponse("student1", "Q2", 1));
        cnList.get(2).addResponse(new LearningObjectResponse("student1", "Q3", 1));
        cnList.get(3).addResponse(new LearningObjectResponse("student1", "Q4", 1));
        cnList.get(4).addResponse(new LearningObjectResponse("student1", "Q5", 1));
        cnList.get(5).addResponse(new LearningObjectResponse("student1", "Q6", 1));


        cnList.get(0).addResponse(new LearningObjectResponse("student2", "Q1", 1));
        cnList.get(1).addResponse(new LearningObjectResponse("student2", "Q2", 1));
        cnList.get(2).addResponse(new LearningObjectResponse("student2", "Q3", 1));
        cnList.get(3).addResponse(new LearningObjectResponse("student2", "Q4", 0));
        cnList.get(4).addResponse(new LearningObjectResponse("student2", "Q5", 0));
        cnList.get(5).addResponse(new LearningObjectResponse("student2", "Q6", 0));


        cnList.get(0).addResponse(new LearningObjectResponse("student3", "Q1", 1));
        cnList.get(1).addResponse(new LearningObjectResponse("student3", "Q2", 1));
        cnList.get(2).addResponse(new LearningObjectResponse("student3", "Q3", 0));
        cnList.get(3).addResponse(new LearningObjectResponse("student3", "Q4", 0));

        return cnList;
    }

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
