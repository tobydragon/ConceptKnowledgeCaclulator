package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.isCommonWord;
import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.removeCommonWords;
import static org.junit.Assert.assertEquals;


class TagSuggesterUtilTest {

    @Test
    void removeCommonWordsTest() {
        List<String> containsCommonWords = new ArrayList();
        containsCommonWords.add("a");
        containsCommonWords.add("plane");
        containsCommonWords.add("photoshop");

        List<String> notContainsCommonWords = new ArrayList();
        notContainsCommonWords.add("photoshop");


        assertEquals("String contains common words", notContainsCommonWords, removeCommonWords(containsCommonWords));
    }

    @Test
    void isCommonWordTest(){
        assertEquals("Word is a common word", true, isCommonWord("plane"));
        assertEquals("Word is not a common word", false, isCommonWord("photoshop"));
    }

}