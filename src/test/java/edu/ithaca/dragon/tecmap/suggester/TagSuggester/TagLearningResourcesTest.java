package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagLearningResourcesMain.findLearningResources;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TagLearningResourcesTest {

    private final String directory = "src/test/resources/learning_resources_txt";
    private final String cs1Example = "src/test/resources/learning_resources_txt/Cs1ExampleLearningMaterial.txt";

    @Test
    void findLearningResourcesTest() {
        File[] files = new File(directory).listFiles();
        try {
            findLearningResources(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(34, findLearningResources(files).size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void cs1LearningMaterialTest(){
        File[] file = new File[] {new File(cs1Example)};
        try {
            assertEquals(6, findLearningResources(file).size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}