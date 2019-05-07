package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;
import org.junit.jupiter.api.Test;

import java.util.*;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.*;
import static edu.ithaca.dragon.tecmap.io.reader.RetrieveLearningResource.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

class RetrieveLearningResourceTest {

    private String filePathOne = "src/test/resources/learning_resources_txt/220-04-ReadingAssignment.txt";
    private String filePathTwo = "src/test/resources/learning_resources_txt/220-13-ReadingAssignment.txt";

    @Test
    void readFileUnixTest() {

        String fileTextOne = "Reading\n" +
                "\n" +
                "From Learncpp.com: Read section 6.9 and 6.9a\n" +
                "\n" +
                "From www.cplusplus.com/doc/tutorial/: Read the \"Dynamic memory\" page.\n" +
                "\n" +
                "http://www.learncpp.com/\n" +
                "\n" +
                "http://www.cplusplus.com/doc/tutorial/dynamic/\n" +
                "Questions\n" +
                "\n" +
                "Why might allocation of memory fail when using the new operator? What aspect of a computer  (amount of hard drive space, amount of RAM, processor speed, number of processors, etc.) would make it more likely that allocation would fail?\n" +
                "\n" +
                "What is the difference between a dangling pointer and a memory leak? Why is each of them important?\n" +
                "\n" +
                "What is  the main difference between arrays on the stack and arrays on the heap? What is a good reason to make an array on the heap instead of the stack?\n" +
                "\n";

        String fileTextTwo = "Pass by reference\n" +
                "\n" +
                "Reading\n" +
                "\n" +
                "https://www.ibm.com/support/knowledgecenter/en/SSLTBW_2.3.0/com.ibm.zos.v2r3.cbclx01/cplr233.htm\n" +
                "\n" +
                "http://www.geeksforgeeks.org/passing-by-pointer-vs-passing-by-reference-in-c/\n" +
                "\n" +
                "http://www.learncpp.com/cpp-tutorial/71-function-parameters-and-arguments/\n" +
                "\n" +
                "Questions\n" +
                "\n" +
                "What is the difference between pass-by-value and pass-by-reference?\n" +
                "\n" +
                "What are advantages of using pass-by-reference over pass-by-pointer (i.e., when would you use pass-by-reference)?\n" +
                "\n" +
                "What are the disadvantages of using pass-by-reference over pass-by-pointer (i.e., when would you use pass-by-pointer)?\n" +
                "\n" +
                "Now that you see both pass-by-reference and pass-by-pointer, which would you use to pass the line counting parameter in our array library code? Why?\n";


        assertEquals("Input read was incorrect", fileTextOne, readFile(filePathOne));
        assertEquals("Input read was incorrect", fileTextTwo, readFile(filePathTwo));

    }

    @Test
    void learningResourceObjectTest(){

        List<LearningMaterial> assessmentsFileTwo = findAssessments(readFile(filePathTwo));
        List<LearningMaterial> assessmentsFileOne = findAssessments(readFile(filePathOne));
        List<LearningMaterial> informationFileTwo = findInformation(readFile(filePathTwo));
        List<LearningMaterial> informationFileOne = findInformation(readFile(filePathOne));

        Collection<LearningResourceType> assessmentType = new ArrayList<>();
        assessmentType.add(LearningResourceType.ASSESSMENT);

        Collection<LearningResourceType> informationType = new ArrayList<>();
        informationType.add(LearningResourceType.INFORMATION);

        ArrayList<String> questionsFileTwo = new ArrayList<>();
        questionsFileTwo.add("What is the difference between pass-by-value and pass-by-reference?");
        questionsFileTwo.add("What are advantages of using pass-by-reference over pass-by-pointer (i.e., when would you use pass-by-reference)?");
        questionsFileTwo.add("What are the disadvantages of using pass-by-reference over pass-by-pointer (i.e., when would you use pass-by-pointer)?");
        questionsFileTwo.add("Now that you see both pass-by-reference and pass-by-pointer, which would you use to pass the line counting parameter in our array library code? Why?");

        ArrayList<String> infoFileTwo = new ArrayList<>();
        infoFileTwo.add("https://www.ibm.com/support/knowledgecenter/en/SSLTBW_2.3.0/com.ibm.zos.v2r3.cbclx01/cplr233.htm");
        infoFileTwo.add("http://www.geeksforgeeks.org/passing-by-pointer-vs-passing-by-reference-in-c/");
        infoFileTwo.add("http://www.learncpp.com/cpp-tutorial/71-function-parameters-and-arguments/");

        ArrayList<String> questionsFileOne = new ArrayList<>();
        questionsFileOne.add("Why might allocation of memory fail when using the new operator? What aspect of a computer  (amount of hard drive space, amount of RAM, processor speed, number of processors, etc.) would make it more likely that allocation would fail?");
        questionsFileOne.add("What is the difference between a dangling pointer and a memory leak? Why is each of them important?");
        questionsFileOne.add("What is  the main difference between arrays on the stack and arrays on the heap? What is a good reason to make an array on the heap instead of the stack?");

        ArrayList<String> infoFileOne = new ArrayList<>();
        infoFileOne.add("From Learncpp.com: Read section 6.9 and 6.9a");
        infoFileOne.add("From www.cplusplus.com/doc/tutorial/: Read the \"Dynamic memory\" page.");
        infoFileOne.add("http://www.learncpp.com/");
        infoFileOne.add("http://www.cplusplus.com/doc/tutorial/dynamic/");

        for (int i = 0; i < assessmentsFileTwo.size(); i++) {
            assertEquals("Incorrect learning resource type for learning material", assessmentType, assessmentsFileTwo.get(i).getLearningResourceType());
            assertEquals("Content does not match", questionsFileTwo.get(i), assessmentsFileTwo.get(i).getContent());
            assertEquals("Incorrent URL", "", assessmentsFileTwo.get(i).getUrl());
        }

        for (int i = 0; i < informationFileTwo.size(); i++) {
            assertEquals("Incorrect learning resource type for learning material", informationType, informationFileTwo.get(i).getLearningResourceType());
            assertEquals("Content does not match", infoFileTwo.get(i), informationFileTwo.get(i).getContent());
            assertEquals("Incorrent URL", infoFileTwo.get(i), informationFileTwo.get(i).getUrl());
        }

        for (int i = 0; i < assessmentsFileOne.size(); i++) {
            assertEquals("Incorrect learning resource type for learning material", assessmentType, assessmentsFileOne.get(i).getLearningResourceType());
            assertEquals("Content does not match", questionsFileOne.get(i), assessmentsFileOne.get(i).getContent());
            assertEquals("Incorrent URL", "", assessmentsFileOne.get(i).getUrl());
        }

        for (int i = 0; i < informationFileOne.size(); i++) {
            assertEquals("Incorrect learning resource type for learning material", informationType, informationFileOne.get(i).getLearningResourceType());
            assertEquals("Content does not match", infoFileOne.get(i), informationFileOne.get(i).getContent());
            assertEquals("Incorrent URL", infoFileOne.get(i), informationFileOne.get(i).getUrl());
        }

    }

    @Test
    void newLineCharacterTest(){
        ArrayList<String> stringsFileOne = new ArrayList<>();
        stringsFileOne.add("Reading");
        stringsFileOne.add("From Learncpp.com: Read section 6.9 and 6.9a");
        stringsFileOne.add("From www.cplusplus.com/doc/tutorial/: Read the \"Dynamic memory\" page.");
        stringsFileOne.add("http://www.learncpp.com/");
        stringsFileOne.add("http://www.cplusplus.com/doc/tutorial/dynamic/");
        stringsFileOne.add("Questions");
        stringsFileOne.add("Why might allocation of memory fail when using the new operator? What aspect of a computer  (amount of hard drive space, amount of RAM, processor speed, number of processors, etc.) would make it more likely that allocation would fail?");
        stringsFileOne.add("What is the difference between a dangling pointer and a memory leak? Why is each of them important?");
        stringsFileOne.add("What is  the main difference between arrays on the stack and arrays on the heap? What is a good reason to make an array on the heap instead of the stack?");

        ArrayList<String> stringsFileTwo = new ArrayList<>();
        stringsFileTwo.add("Pass by reference");
        stringsFileTwo.add("Reading");
        stringsFileTwo.add("https://www.ibm.com/support/knowledgecenter/en/SSLTBW_2.3.0/com.ibm.zos.v2r3.cbclx01/cplr233.htm");
        stringsFileTwo.add("http://www.geeksforgeeks.org/passing-by-pointer-vs-passing-by-reference-in-c/");
        stringsFileTwo.add("http://www.learncpp.com/cpp-tutorial/71-function-parameters-and-arguments/");
        stringsFileTwo.add("Questions");
        stringsFileTwo.add("What is the difference between pass-by-value and pass-by-reference?");
        stringsFileTwo.add("What are advantages of using pass-by-reference over pass-by-pointer (i.e., when would you use pass-by-reference)?");
        stringsFileTwo.add("What are the disadvantages of using pass-by-reference over pass-by-pointer (i.e., when would you use pass-by-pointer)?");
        stringsFileTwo.add("Now that you see both pass-by-reference and pass-by-pointer, which would you use to pass the line counting parameter in our array library code? Why?");

        assertEquals("Strings don't match", stringsFileOne, buildStringsFromFile(readFile(filePathOne)));
        assertEquals("Strings don't match", stringsFileTwo, buildStringsFromFile(readFile(filePathTwo)));

    }

    @Test
    void isURLTest(){

        String url1 = "From Learncpp.com: Read section 6.9 and 6.9a";
        String url2 = "http://www.cplusplus.com/doc/tutorial/dynamic/";
        String url3 = "http://www.learncpp.com/";
        String url4 = "From www.cplusplus.com/doc/tutorial/: Read the \"Dynamic memory\" page.";
        String url5 = "https://www.ibm.com/support/knowledgecenter/en/SSLTBW_2.3.0/com.ibm.zos.v2r3.cbclx01/cplr233.htm";
        String url6 = "http://www.geeksforgeeks.org/passing-by-pointer-vs-passing-by-reference-in-c/";
        String url7 = "7.1 - 7.4: http://www.learncpp.com/cpp-tutorial/71-function-parameters-and-arguments/";

        assertEquals(false, isURL(url1));
        assertEquals(true, isURL(url2));
        assertEquals(true, isURL(url3));
        assertEquals(false, isURL(url4));
        assertEquals(true, isURL(url5));
        assertEquals(true, isURL(url6));
        assertEquals(false, isURL(url7));

    }

    @Test
    void generateIDTest(){

        String id1 = generateID( "Reading", "ASSESSMENT");
        String id2 = generateID( "Pass by reference", "ASSESSMENT");
        String id3 = generateID( "Pass by reference", "ASSESSMENT");
        String id4 = generateID( "Pass by reference", "ASSESSMENT");
        String id5 = generateID( "Pass by reference", "ASSESSMENT");
        String id6 = generateID( "Pass by reference", "ASSESSMENT");

        assertNotEquals(id1, id2);
        assertNotEquals(id3, id4);
        assertNotEquals(id5, id6);

    }

}