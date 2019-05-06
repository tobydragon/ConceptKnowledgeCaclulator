package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;
import smile.nlp.tokenizer.SimpleTokenizer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord.jsonToLearningMaterialRecords;

public class TagSuggesterUtil {

    private static final String COMMON_WORDS_FILE = "src/main/resources/COMMON_WORDS.txt";
    private static final Set<String> COMMON_WORDS = textToHashSetNewLine(readFile(COMMON_WORDS_FILE));

    public static List documentToSet(List<String> documents){
        ArrayList<Set> wordMaps = new ArrayList<>();

        for (String document : documents){
            wordMaps.add(wordsToHashSet(document));
        }

        return wordMaps;

    }

    /**
     * Converts a string into a hashset by separating each term with the "\n" character
     * Hashset used for O(1) lookup
     * @param text - string being converted
     * @return hashset
     */
    public static Set<String> textToHashSetNewLine(String text){
        Set<String> hashSetText = new HashSet<>();
        hashSetText.addAll(Arrays.asList(text.split("\n")));
        return hashSetText;
    }

    /**
     * Converts a string into a hashset by separating each term with the a SimpleTokenizer
     * @param text - string being converted
     * @return hashset
     */
    public static Set<String> wordsToHashSet(String text){
        Set<String> hashSetText = new HashSet<>();
        hashSetText.addAll(Arrays.asList(new SimpleTokenizer().split(text.toLowerCase())));
        return hashSetText;
    }

    public static boolean isCommonWord(String word){
        return COMMON_WORDS.contains(word);
    }

    public static List<String> removeCommonWords(List<String> tags){
        List<String> FOUND_WORDS = new ArrayList<>();

        for (int i = 0; i < tags.size(); i++) {
            if (COMMON_WORDS.contains(tags.get(i))){
                FOUND_WORDS.add(tags.get(i));
            }
        }

        tags.removeAll(FOUND_WORDS);

        return tags;
    }

    public static String clearNonLetterCharacters(String text){
        return text.replaceAll("[^A-Za-z0-9-]", "");
    }

    /**
     * Converts content of a .txt file into a string without "\r" characters but still retaining "\n"
     * @param filepath - file being read
     * @return - string of the contents of the file
     */
    public static String readFile(String filepath) {

        String fileText = "";
        Pattern delims = Pattern.compile(".*\\R|.+\\z");

        try (Scanner scan = new Scanner(Paths.get(filepath), "UTF-8")) {
            String line;
            while ((line = scan.findWithinHorizon(delims, 0)) != null) {
                fileText += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileText.replaceAll("\r", "");

    }

    /**
     * Takes in string of text with newlines and builds arraylist from it. Each newline will signify a new element in the list
     * @param text - string containing file contents with correct format
     * @return arraylist of the contents in the text
     */
    public static List<String> buildStringsFromFile(String text){

        List<String> fileContent = new ArrayList<>(Arrays.asList(text.split("\n")));

        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals("")){
                fileContent.remove(i);
            }
        }

        return fileContent;
    }

    /**
     * Used to generate unique ID's when creating LearningMaterials
     * @param learningMaterialsJSON - json file containing LearningMaterials
     * @return integer representing the number of LearningMaterials in a given json file
     */
    public static int learningMaterialsCount(String learningMaterialsJSON) throws IOException {

        return jsonToLearningMaterialRecords(learningMaterialsJSON).size();
    }

    public static List<String> generateAssessmentDocuments(List<LearningMaterial> learningMaterials){

        List<String> documents = new ArrayList<>();

        for (LearningMaterial learningMaterial : learningMaterials){
            if (learningMaterial.getLearningResourceType().contains(LearningResourceType.ASSESSMENT)){
                documents.add(learningMaterial.getContent());
            }
        }

        return documents;
    }

}
