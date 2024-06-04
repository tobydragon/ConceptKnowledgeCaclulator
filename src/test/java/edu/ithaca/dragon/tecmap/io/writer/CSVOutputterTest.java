package edu.ithaca.dragon.tecmap.io.writer;

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ExampleLearningObjectResponseFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by willsuchanek on 2/28/17.
 */
public class CSVOutputterTest {

    @Test
    public void questionsToSortedSetTest(){
        List<AssessmentItemResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        CSVOutputter outputter = new CSVOutputter(responses);

        SortedSet<String> testSet = new TreeSet<String>();

        testSet.add("Q1");
        testSet.add("Q5");
        testSet.add("Q3");
        testSet.add("Q2");
        testSet.add("Q6");
        testSet.add("Q4");

        SortedSet<String> outputterSet = CSVOutputter.questionsToSortedSet(outputter.studentsToQuestions);

        assertEquals(testSet.first(),outputterSet.first());
        assertEquals(testSet.last(),outputterSet.last());
        assertEquals(testSet, outputterSet);

    }

    @Test
    public void makeCSVTest(){
        List<AssessmentItemResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        CSVOutputter outputter = new CSVOutputter(responses);

        String testString = ",Q1,Q2,Q3,Q4,Q5,Q6,\n"
                +"student1,1,1,1,1,1,1,\n"
                +"student2,1,1,1,0,0,0,\n"
                +"student3,1,1,0,0,,,\n";

        assertEquals(testString,outputter.makeCSV());
    }

}