package edu.ithaca.dragonlab.ckc.conceptgraph;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */

import org.junit.Assert;
import org.junit.Test;

public class learningObjectSuggestionTest {

    @Test
    public void getTest(){


        learningObjectSuggestion test1= new learningObjectSuggestion("A", 2, learningObjectSuggestion.Level.INCOMPLETE);

        Assert.assertEquals("A", test1.getId() );
        Assert.assertEquals(2, test1.getPathNum() );
        Assert.assertEquals(learningObjectSuggestion.Level.INCOMPLETE, test1.getLevel() );


    }

    @Test
    public void setTest(){

        learningObjectSuggestion test2= new learningObjectSuggestion("A", 2, learningObjectSuggestion.Level.INCOMPLETE);

        test2.setId("W");
        Assert.assertEquals("W", test2.getId() );

        test2.setPathNum(10);
        Assert.assertEquals(10, test2.getPathNum() );

        test2.setLevel(learningObjectSuggestion.Level.RIGHT);
        Assert.assertEquals(learningObjectSuggestion.Level.RIGHT, test2.getLevel() );
    }



}