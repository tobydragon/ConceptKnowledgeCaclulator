package edu.ithaca.dragonlab.ckc.util;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.conceptgraph.ExampleConceptGraphFactory;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import edu.ithaca.dragonlab.ckc.ui.ConsoleUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mia Kimmich Mitchell on 6/9/2017.
 */
public class ConceptKnowledgeCalculatorTest {

    static Logger logger = LogManager.getLogger(ConceptKnowledgeCalculator.class);

    @Test
    public void calcIndividualConceptNodesSuggestionsTest(){

        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
                System.out.println("Unable to load default files, please choose files manually.");
                ckc = new ConceptKnowledgeCalculator();
        }

        List<ConceptNode> concepts = ckc.calcIndividualConceptNodesSuggestions("bspinache1");
        System.out.println(concepts.size());


        Assert.assertEquals(concepts.size(), 2);
        Assert.assertEquals(concepts.get(0).getID(), "If Statement");
        Assert.assertEquals(concepts.get(1).getID(), "While Loop");

    }





    @Test
    public void  calcIndividualGraphSuggestionsTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }

        SuggestionResource res = ckc.calcIndividualGraphSuggestions("bspinache1");
        List<LearningObjectSuggestion> incomTest = res.incompleteList;
        List<LearningObjectSuggestion> wrongTest = res.wrongList;

        Assert.assertEquals(incomTest.size(),5);
        Assert.assertEquals(incomTest.get(0).getId(),"Q10");
        Assert.assertEquals(incomTest.get(1).getId(),"Q10");
        Assert.assertEquals(incomTest.get(2).getId(),"Q3");
        Assert.assertEquals(incomTest.get(3).getId(),"Q6");
        Assert.assertEquals(incomTest.get(4).getId(),"Q6");

        Assert.assertEquals(wrongTest.size(),3);
        Assert.assertEquals(wrongTest.get(0).getId(), "Q9");
        Assert.assertEquals(wrongTest.get(1).getId(), "Q9");
        Assert.assertEquals(wrongTest.get(2).getId(), "Q1");

    }


    @Test
    public void calcIndividualSpecificConceptSuggestionsTest(){

        ConceptKnowledgeCalculatorAPI ckc = null;
        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/basicRealisticExampleConceptGraphOneStudent.json", "test/testresources/basicRealisticExampleLOLRecordOneStudent.json", "test/testresources/basicRealisticExampleGradeBook2.csv");
        } catch (IOException e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();

        }


//        List<ConceptNode> concepts = ckc.calcIndividualSpecificConceptSuggestions("bspinache1");
//        System.out.println(concepts.size());
//
//        Assert.assertEquals(concepts.size(), 2);
//        Assert.assertEquals(concepts.get(0).getID(), "If Statement");
//        Assert.assertEquals(concepts.get(1).getID(), "While Loop");

    }

}
