package edu.ithaca.dragon.tecmap.conceptgraph;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */

import edu.ithaca.dragon.tecmap.suggester.LearningObjectSuggestion;
import org.junit.Assert;
import org.junit.Test;

public class learningObjectSuggestionTest {

    @Test
    public void getTest(){


        LearningObjectSuggestion test1= new LearningObjectSuggestion("A", 2, LearningObjectSuggestion.Level.INCOMPLETE, "Intro to CS",1);

        Assert.assertEquals("A", test1.getId() );
        Assert.assertEquals(2, test1.getPathNum() );
        Assert.assertEquals(LearningObjectSuggestion.Level.INCOMPLETE, test1.getLevel() );
        Assert.assertEquals("Intro to CS", test1.getReasoning());

    }

    @Test
    public void setTest(){

        LearningObjectSuggestion test2= new LearningObjectSuggestion("A", 2, LearningObjectSuggestion.Level.INCOMPLETE, "Intro to CS",1);

        test2.setId("W");
        Assert.assertEquals("W", test2.getId() );

        test2.setPathNum(10);
        Assert.assertEquals(10, test2.getPathNum() );

        test2.setLevel(LearningObjectSuggestion.Level.RIGHT);
        Assert.assertEquals(LearningObjectSuggestion.Level.RIGHT, test2.getLevel() );
    }



}