package edu.ithaca.dragon.tecmap.suggester;

/**
 * Created by Mia Kimmich Mitchell on 4/25/2017.
 */

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraphTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LearningResourceSuggestionComparatorTest {
    static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

    //order them: right, wrong, incomplete
    //then by pathNum val

    @Test
    public void equalTest(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestList.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );

        new LearningResourceSuggestionComparator();
        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );

        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }

    @Test
    public void SecondPathHigher(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.WRONG, "A",4) );
        suggestList.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.WRONG, "A",3) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.WRONG, "A",3) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.WRONG, "A",4) );

        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }

    @Test
    public void secondPathLower(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 5, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestList.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q1", 5, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );


        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }

    @Test
    public void firstLevelLower(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",2) );
        suggestList.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",2) );


        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }

    @Test
    public void firstLevelHigher(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestList.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",1) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",1) );


        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }

    @Test
    public void firstLevelLower2(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.WRONG, "A",1) );
        suggestList.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",5) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q4", 1, LearningResourceSuggestion.Level.WRONG, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",5) );


        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }



    @Test
    public void LevelsAreDiff(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.WRONG, "A",1) );
        suggestList.add(new LearningResourceSuggestion("Q2", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",3) );
        suggestList.add(new LearningResourceSuggestion("Q3", 1, LearningResourceSuggestion.Level.RIGHT, "A",2) );


        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q3", 1, LearningResourceSuggestion.Level.RIGHT, "A",2) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 1, LearningResourceSuggestion.Level.WRONG, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q2", 1, LearningResourceSuggestion.Level.INCOMPLETE, "A",3) );



        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }




    @Test
    public void suggestionComparatorTest(){

        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.INCOMPLETE, "A",3) );
        suggestList.add(new LearningResourceSuggestion("Q2", 1, LearningResourceSuggestion.Level.WRONG, "A",1) );
        Collections.sort(suggestList, new LearningResourceSuggestionComparator());


        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q2", 1, LearningResourceSuggestion.Level.WRONG, "A",1 ));
        suggestListTest.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.INCOMPLETE, "A",3) );


        for (int i =0; i<suggestListTest.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }






    @Test
    public void FirstLinkHigher(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.INCOMPLETE, "A",5) );
        suggestList.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.INCOMPLETE, "A",1) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.INCOMPLETE, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.INCOMPLETE, "A",5) );

        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }

    @Test
    public void secondLinkHigher(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.INCOMPLETE, "C",3) );
        suggestList.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.INCOMPLETE, "C",6) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.INCOMPLETE, "C",3) );
        suggestListTest.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.INCOMPLETE, "C",6) );

        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }


    @Test
    public void LinkTheSame(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 4, LearningResourceSuggestion.Level.INCOMPLETE, "A",4) );
        suggestList.add(new LearningResourceSuggestion("Q4", 4, LearningResourceSuggestion.Level.INCOMPLETE, "A",4) );

        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q1", 4, LearningResourceSuggestion.Level.INCOMPLETE, "A",4) );
        suggestListTest.add(new LearningResourceSuggestion("Q4", 4, LearningResourceSuggestion.Level.INCOMPLETE, "A",4) );

        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }
    }


    @Test
    public void LinksHIgher(){
        List<LearningResourceSuggestion> suggestList = new ArrayList<>();
        suggestList.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.RIGHT, "A",6) );
        suggestList.add(new LearningResourceSuggestion("Q3", 1, LearningResourceSuggestion.Level.RIGHT, "A",3) );
        suggestList.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.RIGHT, "A",2) );
        suggestList.add(new LearningResourceSuggestion("Q5", 3, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestList.add(new LearningResourceSuggestion("Q6", 3, LearningResourceSuggestion.Level.RIGHT, "A",5) );
        suggestList.add(new LearningResourceSuggestion("Q2", 3, LearningResourceSuggestion.Level.RIGHT, "A",4) );



        new LearningResourceSuggestionComparator();
        Collections.sort(suggestList, new LearningResourceSuggestionComparator());

        List<LearningResourceSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningResourceSuggestion("Q5", 3, LearningResourceSuggestion.Level.RIGHT, "A",1) );
        suggestListTest.add(new LearningResourceSuggestion("Q2", 3, LearningResourceSuggestion.Level.RIGHT, "A",4) );
        suggestListTest.add(new LearningResourceSuggestion("Q6", 3, LearningResourceSuggestion.Level.RIGHT, "A",5) );
        suggestListTest.add(new LearningResourceSuggestion("Q4", 2, LearningResourceSuggestion.Level.RIGHT, "A",2) );
        suggestListTest.add(new LearningResourceSuggestion("Q1", 2, LearningResourceSuggestion.Level.RIGHT, "A",6) );
        suggestListTest.add(new LearningResourceSuggestion("Q3", 1, LearningResourceSuggestion.Level.RIGHT, "A",3) );



        for (int i =0; i<suggestList.size(); i++){

            assertEquals(suggestList.get(i).getId(),suggestListTest.get(i).getId());
            assertEquals(suggestList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            assertEquals(suggestList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }

}

