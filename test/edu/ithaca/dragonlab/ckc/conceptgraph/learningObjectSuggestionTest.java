package edu.ithaca.dragonlab.ckc.conceptgraph;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */

import org.junit.Assert;
import org.junit.Test;

public class learningObjectSuggestionTest {

    @Test
    public void getTest(){


        learningObjectSuggestionNode test1= new learningObjectSuggestionNode("A", 2, "incomplete");

        Assert.assertEquals("A", test1.getId() );
        Assert.assertEquals(2, test1.getPathNum() );
        Assert.assertEquals("incomplete", test1.getLevel() );


    }

    @Test
    public void setTest(){

        learningObjectSuggestionNode test2= new learningObjectSuggestionNode("A", 2, "incomplete");

        test2.setId("W");
        Assert.assertEquals("W", test2.getId() );

        test2.setPathNum(10);
        Assert.assertEquals(10, test2.getPathNum() );

        test2.setLevel("complete");
        Assert.assertEquals("complete", test2.getLevel() );
    }



}