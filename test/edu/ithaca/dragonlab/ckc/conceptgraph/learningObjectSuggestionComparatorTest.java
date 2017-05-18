package edu.ithaca.dragonlab.ckc.conceptgraph;

/**
 * Created by Mia Kimmich Mitchell on 4/25/2017.
 */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class learningObjectSuggestionComparatorTest {
    static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

    //order them: right, wrong, incomplete
    //then by pathNum val

    @Test
    public void equalTest(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestList.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );

        new learningObjectSuggestionComparator();
        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );

        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }

    @Test
    public void SecondPathHigher(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.WRONG) );
        suggestList.add(new learningObjectSuggestion("Q4", 2,learningObjectSuggestion.Level.WRONG) );

        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q4", 2,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.WRONG) );

        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }

    @Test
    public void secondPathLower(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 5,learningObjectSuggestion.Level.RIGHT) );
        suggestList.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );

        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q1", 5,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );


        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }

    @Test
    public void firstLevelLower(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.INCOMPLETE) );
        suggestList.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );

        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.INCOMPLETE) );


        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }

    @Test
    public void firstLevelHigher(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestList.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.INCOMPLETE) );

        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.INCOMPLETE) );


        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }

    @Test
    public void firstLevelLower2(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.WRONG) );
        suggestList.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.INCOMPLETE) );

        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q4", 1,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.INCOMPLETE) );


        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }



    @Test
    public void LevelsAreDiff(){
        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.WRONG) );
        suggestList.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.INCOMPLETE) );
        suggestList.add(new learningObjectSuggestion("Q3", 1,learningObjectSuggestion.Level.RIGHT) );


        Collections.sort(suggestList, new learningObjectSuggestionComparator());

        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q3", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.INCOMPLETE) );



        for (int i =0; i<suggestList.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }




    @Test
    public void suggestionComparatorTest(){

        List<learningObjectSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new learningObjectSuggestion("Q1", 2,learningObjectSuggestion.Level.INCOMPLETE) );
        suggestList.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.WRONG) );
        Collections.sort(suggestList, new learningObjectSuggestionComparator());


        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 2,learningObjectSuggestion.Level.INCOMPLETE) );


        for (int i =0; i<suggestListTest.size(); i++){

            Assert.assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
//
//        suggestListTest.add(new learningObjectSuggestion("Q3", 2,learningObjectSuggestion.Level.INCOMPLETE) );
//        suggestListTest.add(new learningObjectSuggestion("Q4", 3,learningObjectSuggestion.Level.INCOMPLETE) );

    }
}

