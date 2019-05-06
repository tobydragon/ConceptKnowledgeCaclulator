package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.buildStringsFromFile;

/**
 * RetrieveLearningResource creates LearningMaterials that are either assessements or information
 * from a provided string of text
 * @author Fernando Barreto
 * 09/11/18
 */

public class RetrieveLearningResource {

    private static AtomicInteger count = new AtomicInteger(0);

    public RetrieveLearningResource(int startingIndex){
        count = new AtomicInteger(startingIndex);
    }

    /**
     * Generates learning materials of type "INFORMATION"
     * @param text - file text
     * @return list of learning materials generated from the file
     */
    public static List<LearningMaterial> findInformation(String text) {
        String information = "reading";
        String endingWord = "questions";
        return findLearningMaterial(buildStringsFromFile(text), information, endingWord);
    }

    /**
     * Generates learning materials of type "ASSESSMENT"
     * @param text - file text
     * @return list of learning materials generated from the file
     */
    public static List<LearningMaterial> findAssessments(String text) {
        String information = "questions";
        String endingWord = "reading";
        return findLearningMaterial(buildStringsFromFile(text), information, endingWord);
    }

    /**
     * Checks whether or not a string of text is a URL
     * @param text - text to check
     * @return true if text is URL, false if not
     */
    public static Boolean isURL(String text){

        Pattern urlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        return text.matches(urlPattern.pattern());
    }

    public static List<LearningMaterial> findLearningMaterial(String text){

        List<LearningMaterial> learningMaterials = new ArrayList<>();
        learningMaterials.addAll(findAssessments(text));
        learningMaterials.addAll(findInformation(text));

        return learningMaterials;

    }

    public static String generateID(String tags, String types){
        return types + " " + count.incrementAndGet() + " " + tags;
    }

    /**
     * Extracts learning materials and creates an arraylist containing the learning materials
     * @param lines arraylist of strings representing every line from the text file
     * @param learningMaterialToFind string determining if user is looking for information or assessments
     * @param endingWord - string with the opposite learningMaterialToFind
     * @return arraylist populated with learning materials created from text file
     */
    public static List<LearningMaterial> findLearningMaterial(List<String> lines, String learningMaterialToFind, String endingWord){
        List<LearningMaterial> learningMaterials = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        List<LearningResourceType> types = new ArrayList<>();
        List<String> lowerCaseLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            lowerCaseLines.add(lines.get(i).toLowerCase());
        }

        int start = lowerCaseLines.indexOf(learningMaterialToFind) + 1;
        int end = lowerCaseLines.indexOf(endingWord);
        tags.add(lines.get(0));

        if (start > end){
            end = lines.size();
        }

        if (learningMaterialToFind.equals("questions")) {
            types.add(LearningResourceType.ASSESSMENT);
            for (int i = start; i < end; i++) {
                learningMaterials.add(new LearningMaterial(generateID(tags.toString(), types.toString()), types, lines.get(i), tags));
            }
        } else {
            types.add(LearningResourceType.INFORMATION);
            for (int i = start; i < end; i++) {
                learningMaterials.add(new LearningMaterial(generateID(tags.toString(), types.toString()), types, lines.get(i), tags, lines.get(i)));
            }
        }

        return learningMaterials;

    }

}