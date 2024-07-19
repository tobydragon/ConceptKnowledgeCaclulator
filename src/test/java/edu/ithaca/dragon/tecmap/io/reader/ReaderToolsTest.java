package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by Ryan on 11/29/2017.
 */
public class ReaderToolsTest {
    @Test
    public void NumbersTest(){
        String object = "total Score - 25";
        String outCome = "25";
        try {
            assertEquals(outCome, ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void NumbersTestReturnEmpty(){
        String object = "total Score - 25.3.2";
        String outCome = "";
        try {
            assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void NumbersTestDouble(){
        String object = "total Score - -25.34";
        String outCome = "-25.34";
        try {
            assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void NumbersTestNoNumbers(){
        String object = "total Score - N/A";
        String outCome = "";
        try {
            assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void titleCommasTest() {
        String titles = "this is, a title to test. this, will not work";
        List<String> myList = Arrays.asList("this is","a title to test. this","will not work");
        try {
            assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void titleCommasTestTwo() {
        String titles = "another test, to fail again, lets see how this goes";
        List<String> myList = Arrays.asList("another test", "to fail again", "lets see how this goes");
        try {
            assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void titleCommasTestThree() {
        String titles = "hello, a third test, maybe \"this one will pass\". Probably not";
        List<String> myList = Arrays.asList("hello", "a third test" , "maybe this one will pass. Probably not");
        try {
            assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void titleStarTest () {
        String titles = "hello, a \"fourth\" test, *Breaks here*";
        List<String> myList = Arrays.asList("hello", "a fourth test" , "*Breaks here*");
        try {
            assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void titleColonTest() {
        String titles = "Test 5: another break, \"maybe, maybe-not?\"";
        List<String> myList = Arrays.asList("Test 5: another break", "maybe, maybe-not?");
        try {
            assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void assessmentItemsFromListTest() throws IOException, CsvException {
        List<String[]> rows = CsvFileLibrary.parseRowsFromFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/assessmentGrades.csv");
        // convert sakai file to canvas format
        CanvasConverter.canvasConverter(rows);
        List<AssessmentItem> aiList = ReaderTools.assessmentItemsFromList(rows);
        List<String> ai = aiList.stream().map(AssessmentItem::getId).toList();
        List<String> expectedAIList = Arrays.asList("Q1","Q2","Q3","Q4","Q5","Q6","Q7");
        assertEquals(expectedAIList, ai);
    }
}
