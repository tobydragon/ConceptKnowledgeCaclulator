package edu.ithaca.dragon.tecmap.suggester;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */

import org.junit.Assert;
import org.junit.Test;

public class LearningObjectSuggestionTest {

    @Test
    public void getTest(){


        LearningResourceSuggestion test1= new LearningResourceSuggestion("A", 2, LearningResourceSuggestion.Level.INCOMPLETE, "Intro to CS",1);

        Assert.assertEquals("A", test1.getId() );
        Assert.assertEquals(2, test1.getPathNum() );
        Assert.assertEquals(LearningResourceSuggestion.Level.INCOMPLETE, test1.getLevel() );
        Assert.assertEquals("Intro to CS", test1.getReasoning());

    }

    @Test
    public void setTest(){

        LearningResourceSuggestion test2= new LearningResourceSuggestion("A", 2, LearningResourceSuggestion.Level.INCOMPLETE, "Intro to CS",1);

        test2.setId("W");
        Assert.assertEquals("W", test2.getId() );

        test2.setPathNum(10);
        Assert.assertEquals(10, test2.getPathNum() );

        test2.setLevel(LearningResourceSuggestion.Level.RIGHT);
        Assert.assertEquals(LearningResourceSuggestion.Level.RIGHT, test2.getLevel() );
    }



}