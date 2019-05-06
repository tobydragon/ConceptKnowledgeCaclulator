package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import smile.nlp.SimpleCorpus;
import smile.nlp.relevance.TFIDF;

import java.util.ArrayList;
import java.util.List;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggester.*;
import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.documentToSet;
import static org.junit.Assert.assertEquals;


public class TagSuggesterTest {

    private static List<String> documents;
    private static SimpleCorpus mainCorpus;
    private static SimpleCorpus fullCorpus;

    @BeforeEach
    void initializeDocuments(){

        documents = new ArrayList<>();
        documents.add("Starting to release music under his own name in 2010, Robinson released a variety of original singles on Glamara Records and Big Fish Recordings.");
        documents.add("During Robinson's \"Language Tour\", he continually began to grow tired of the current dance music scene, centered around formulaic songs with timed builds and beat drops, designed to excite people at festivals and clubs.");
        documents.add("The more I forced myself to work within those DJ-friendly limits, the more I resented the genre.");
        documents.add("Robinson was born in Atlanta, Georgia, and currently resides in Chapel Hill, North Carolina. He was accepted into UNC, where both of his parents are alumni, but did not himself attend due to his newly launched music career.");
        documents.add("A music video was also released on Robinson's YouTube channel, containing abstract 3-Dimensional art and cryptic messages seemingly focusing on the words \"angel\", \"virtual\", \"void\", and \"utopia\".");
        documents.add("A remix album of Worlds titled Worlds Remixed was released on October 2, 2015. It included remixes by electronic artists Odesza, San Holo, Mat Zo, Electric Mantis, Galimatias, Last Island, Chrome Sparks, Deon Custom, Rob Mayth, Point Point, Sleepy Tom, and Slumberjack.");
        documents.add("Robinson, Zedd, and Skrillex performing at South by Southwest (SXSW), 2012\n" +
                "In 2011, he signed a one-EP deal with OWSLA, then a new label operated by Skrillex, to release the eleven-track Spitfire. As the first release on OWSLA, it topped iTunes Dance chart and Beatport's overall chart, crashing the latter's servers upon release.");
        documents.add("Porter Weston Robinson (born July 15, 1992) is an American DJ, record producer and musician from Chapel Hill, North Carolina. He has released multiple number one singles across different electronic genres. His debut full-length studio album, Worlds, was released on August 12, 2014.");

        mainCorpus = generateMainCorpus(documents.get(0));
        fullCorpus = generateFullCorpus(documents);

    }

    @Test
    void findNumOfOccurrencesTest() {

        String term = "Video";
        assertEquals("Term appeared either more or less than expected", 1, findNumOfOccurrences(documentToSet(documents), term));

        term = "Released";
        assertEquals("Term appeared either more or less than expected", 4, findNumOfOccurrences(documentToSet(documents), term));
    }

    @Test
    void findTFTest() {

        String term = "music";
        assertEquals("Term frequency does not match", 4, findTF(fullCorpus, term));

        term = "Singles";
        assertEquals("Term frequency does not match", 2, findTF(fullCorpus, term));

        term = "Released";
        assertEquals("Term frequency does not match", 5, findTF(fullCorpus, term));
    }

    @Test
    void calculateTFIDFTest() {

        TFIDF tfidf = new TFIDF();
        String term = "singles";

        double result = 0.9704060528;
        assertEquals(result, calculateTFIDF(tfidf, mainCorpus, fullCorpus, fullCorpus.getNumDocuments(), documentToSet(documents), term), 0.001);

        term = "Music";
        result = 0.3812309493;

        assertEquals(result, calculateTFIDF(tfidf, mainCorpus, fullCorpus, fullCorpus.getNumDocuments(), documentToSet(documents), term), 0.001);

    }

    @Test
    void cleanTagTest(){

        String TAG1 = "";
        String TAG2 = "POINTER,,";
        String TAG3 = " ";
        String TAG4 = "ad";

        assertEquals("Tag was not clean", null, cleanTag(TAG1));
        assertEquals("Tag was not clean", "pointer", cleanTag(TAG2));
        assertEquals("Tag was not clean", null, cleanTag(TAG3));
        assertEquals("Tag was not clean", "ad", cleanTag(TAG4));

    }

    @Test
    void cleanTagsTest(){

        List<String> unclean_tags = new ArrayList<>();
        unclean_tags.add(",");
        unclean_tags.add(" ");
        unclean_tags.add("POINTER,,,");
        unclean_tags.add("act");
        unclean_tags.add("ad");

        List<String> clean_tags = new ArrayList<>();
        clean_tags.add("pointer");

        unclean_tags = cleanTags(unclean_tags);

        assertEquals("Tags don't match", clean_tags, unclean_tags);

    }
}

