package edu.ithaca.dragon.tecmap.suggester;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */

public class LearningResourceSuggestionTest {

    @Test
    public void getTest(){
        LearningResourceSuggestion test1= new LearningResourceSuggestion("A", 2, LearningResourceSuggestion.Level.INCOMPLETE, "Intro to CS",1);

        assertEquals("A", test1.getId() );
        assertEquals(2, test1.getPathNum() );
        assertEquals(LearningResourceSuggestion.Level.INCOMPLETE, test1.getLevel() );
        assertEquals("Intro to CS", test1.getReasoning());
    }
}