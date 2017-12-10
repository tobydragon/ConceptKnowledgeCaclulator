package edu.ithaca.dragonlab.ckc.io;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ryan on 11/29/2017.
 */
public class ReaderToolsTest {
    @Test
    public void NumbersTest(){
        String object = "total Score - 25";
        String outCome = "25";
        try {
            Assert.assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void NumbersTestReturnEmpty(){
        String object = "total Score - 25.3.2";
        String outCome = "";
        try {
            Assert.assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void NumbersTestDouble(){
        String object = "total Score - -25.34";
        String outCome = "-25.34";
        try {
            Assert.assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void NumbersTestNoNumbers(){
        String object = "total Score - N/A";
        String outCome = "";
        try {
            Assert.assertEquals(outCome,ReaderTools.pullNumber(object));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleCommasTest() {
        String titles = "this is, a title to test. this, will not work";
        List<String> myList = Arrays.asList("this is","a title to test. this","will not work");
        try {
            Assert.assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleCommasTestTwo() {
        String titles = "another test, to fail again, lets see how this goes";
        List<String> myList = Arrays.asList("another test", "to fail again", "lets see how this goes");
        try {
            Assert.assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleCommasTestThree() {
        String titles = "hello, a third test, maybe \"this one will pass\". Probably not";
        List<String> myList = Arrays.asList("hello", "a third test" , "maybe this one will pass. Probably not");
        try {
            Assert.assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleStarTest () {
        String titles = "hello, a \"fourth\" test, *Breaks here*";
        List<String> myList = Arrays.asList("hello", "a fourth test" , "*Breaks here*");
        try {
            Assert.assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleColonTest() {
        String titles = "Test 5: another break, \"maybe, maybe-not?\"";
        List<String> myList = Arrays.asList("Test 5: another break", "maybe, maybe-not?");
        try {
            Assert.assertEquals(myList, ReaderTools.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
