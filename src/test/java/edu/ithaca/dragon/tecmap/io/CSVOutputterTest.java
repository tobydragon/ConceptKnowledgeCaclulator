package edu.ithaca.dragon.tecmap.io;

import edu.ithaca.dragon.tecmap.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragon.tecmap.learningobject.LearningObjectResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by willsuchanek on 2/28/17.
 */
public class CSVOutputterTest {

    @Test
    public void questionsToSortedSetTest(){
        List<LearningObjectResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        CSVOutputter outputter = new CSVOutputter(responses);

        SortedSet<String> testSet = new TreeSet<String>();

        testSet.add("Q1");
        testSet.add("Q5");
        testSet.add("Q3");
        testSet.add("Q2");
        testSet.add("Q6");
        testSet.add("Q4");

        SortedSet<String> outputterSet = CSVOutputter.questionsToSortedSet(outputter.studentsToQuestions);

        Assert.assertEquals(testSet.first(),outputterSet.first());
        Assert.assertEquals(testSet.last(),outputterSet.last());
        Assert.assertEquals(testSet, outputterSet);

    }

    @Test
    public void makeCSVTest(){
        List<LearningObjectResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        CSVOutputter outputter = new CSVOutputter(responses);

        String testString = ",Q1,Q2,Q3,Q4,Q5,Q6,\n"
                +"student1,1,1,1,1,1,1,\n"
                +"student2,1,1,1,0,0,0,\n"
                +"student3,1,1,0,0,,,\n";

        Assert.assertEquals(testString,outputter.makeCSV());
    }

}