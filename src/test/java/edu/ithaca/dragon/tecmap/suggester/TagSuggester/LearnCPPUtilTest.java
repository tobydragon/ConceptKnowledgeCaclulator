package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.LearnCPPUtil.*;
import static org.junit.Assert.assertEquals;

class LearnCPPUtilTest {

    private final File learnCpp1 = new File("src/test/resources/LearnCPPHTMLs/learnCpp1.html");
    private final File learnCpp2 = new File("src/test/resources/LearnCPPHTMLs/learnCpp2.html");

    @Test
    void getTagsFromBoldWordsTest() throws IOException {
        List<String> boldTags_VARIABLES = new ArrayList<>();
        boldTags_VARIABLES.add("data".toLowerCase());
        boldTags_VARIABLES.add("RAM".toLowerCase());
        boldTags_VARIABLES.add("value".toLowerCase());
        boldTags_VARIABLES.add("object".toLowerCase());
        boldTags_VARIABLES.add("variable".toLowerCase());
        boldTags_VARIABLES.add("identifier".toLowerCase());
        boldTags_VARIABLES.add("definition".toLowerCase());
        boldTags_VARIABLES.add("runtime".toLowerCase());
        boldTags_VARIABLES.add("Instantiation".toLowerCase());
        boldTags_VARIABLES.add("instance".toLowerCase());
        boldTags_VARIABLES.add("data type".toLowerCase());
        boldTags_VARIABLES.add("type".toLowerCase());
        boldTags_VARIABLES.add("integer".toLowerCase());
        boldTags_VARIABLES.add("compile-time".toLowerCase());


        List tagsFromBoldWords = findTagsFromBoldWords(Jsoup.parse(learnCpp1, "UTF-8"));

        for (int i = 0; i < tagsFromBoldWords.size(); i++) {
            assertEquals(true, boldTags_VARIABLES.contains(tagsFromBoldWords.get(i)));
        }

        boldTags_VARIABLES.clear();
        boldTags_VARIABLES.add("recursive".toLowerCase());
        boldTags_VARIABLES.add("function".toLowerCase());
        boldTags_VARIABLES.add("conditions".toLowerCase());
        boldTags_VARIABLES.add("termination".toLowerCase());
        boldTags_VARIABLES.add("algorithms".toLowerCase());
        boldTags_VARIABLES.add("base".toLowerCase());
        boldTags_VARIABLES.add("case".toLowerCase());
        boldTags_VARIABLES.add("Fibonacci".toLowerCase());
        boldTags_VARIABLES.add("numbers".toLowerCase());
        boldTags_VARIABLES.add("iterative".toLowerCase());
        boldTags_VARIABLES.add("Quiz".toLowerCase());
        boldTags_VARIABLES.add("time".toLowerCase());

        tagsFromBoldWords = findTagsFromBoldWords(Jsoup.parse(learnCpp2, "UTF-8"));

        for (int i = 0; i < tagsFromBoldWords.size(); i++) {
            assertEquals(true, boldTags_VARIABLES.contains(tagsFromBoldWords.get(i)));
        }


    }

    @Test
    void getTagsFromTitleTest() throws IOException {
        List<String> titleTags_VARIABLES = new ArrayList<>();
        titleTags_VARIABLES.add("1.3");
        titleTags_VARIABLES.add("—");
        titleTags_VARIABLES.add("Introduction");
        titleTags_VARIABLES.add("variables");

        assertEquals(titleTags_VARIABLES, findTagsFromTitle(Jsoup.parse(learnCpp1, "UTF-8")));

    }

    @Test
    void getURLSFromIndexTest() {
        List<String> urls = new ArrayList<>();
        urls.add("https://www.learncpp.com/cpp-tutorial/introduction-to-these-tutorials/");
        urls.add("https://www.learncpp.com/cpp-tutorial/introduction-to-cpp-development/");
        urls.add("https://www.learncpp.com/cpp-tutorial/appendix-c-the-end/");
        urls.add("https://www.learncpp.com/cpp-tutorial/36-logical-operators/");

        for (String url : urls){
            assertEquals(url + " is not present", true, findURLSFromIndex().contains(url));
        }
    }

    @Test
    void convertPathToLinkTest() {
        String path = "/index";
        String link = "https://www.learncpp.com/index";
        assertEquals(link, convertPathToLink(path));
    }

    @Test
    void isCPPUrlTest() {
        String link = "https://www.learncpp.com/";
        assertEquals(false, isLearnCPPURL(link));

        link = "http://www.learncpp.com/";
        assertEquals(false, isLearnCPPURL(link));

        link = "www.learncpp.com/";
        assertEquals( false, isLearnCPPURL(link));

        link = "https://www.learncpp.com/cpp-tutorial/appendix-c-the-end/";
        assertEquals(true, isLearnCPPURL(link));

        link = "http://www.learncpp.com/cpp-tutorial/appendix-c-the-end/";
        assertEquals( true, isLearnCPPURL(link));

        link = "https://medium.com/factory-mind/regex-tutorial-a-simple-cheatsheet-by-examples-649dc1c3f285";
        assertEquals( false, isLearnCPPURL(link));

    }
}