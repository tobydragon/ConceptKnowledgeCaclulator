package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import edu.ithaca.dragon.tecmap.io.reader.RetrieveLearningResource;
import edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord;
import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord.learningMaterialRecordsToJson;
import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.LearnCPPUtil.findTagsFromBoldWords;
import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.LearnCPPUtil.isLearnCPPURL;
import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggester.calculateTagsTFIDF;
import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.*;

public class TagLearningResources {

    public static void main(String[] args) throws IOException {

        writeMaterialsToJson();

    }

    public static void writeMaterialsToJson() throws IOException {

        Scanner scan = new Scanner(System.in);

        System.out.println("What directory would you like to use?");
        String directory = scan.nextLine();

        System.out.println("What is the destination?");
        String dest = scan.nextLine();

        File[] files = new File(directory).listFiles();
        List<LearningMaterial> learningMaterials = tagLearningMaterials(findLearningResources(files));
        List<LearningMaterialRecord> learningMaterialRecords = new ArrayList<>();


        for (LearningMaterial learningMaterial : learningMaterials){
            learningMaterialRecords.add(new LearningMaterialRecord(learningMaterial));
        }

        learningMaterialRecordsToJson(learningMaterialRecords, dest);
    }

    /**
     * Tags LearningMaterials based on their properties. Learncpp.com urls
     * will get tagged by either TFIDF or with bold words. Strings will get tagged by TFIDF.
     * NOTE: For TFIDF specifically, a list of more than 1 LearningMaterial should be provided for better results
     * @param learningMaterials - list of learningMaterials to be tagged
     * @return the same learningMaterial objects that were passed in, but with additional tags
     */
    public static List<LearningMaterial> tagLearningMaterials(List<LearningMaterial> learningMaterials) throws IOException {

        //List<String> learnCPPDocuments = generateDocumentsFromIndex();
        List<String> assessmentDocuments = generateAssessmentDocuments(learningMaterials);

        for (LearningMaterial learningMaterial : learningMaterials){

            if (isLearnCPPURL(learningMaterial.getUrl()) && learningMaterial.getLearningResourceType().contains(LearningResourceType.INFORMATION)){
                //learningMaterial.addTagsMap(calculateTagsTFIDF(findParagraph(learningMaterial.getUrl()), learnCPPDocuments));
                Document doc = Jsoup.connect(learningMaterial.getUrl()).timeout(10000).validateTLSCertificates(false).get();
                learningMaterial.addTagsMap(findTagsFromBoldWords(doc));
            }

            else if (learningMaterial.getLearningResourceType().contains(LearningResourceType.ASSESSMENT)){
                learningMaterial.addTagsMap(calculateTagsTFIDF(learningMaterial.getContent(), assessmentDocuments));
            }

        }
        return learningMaterials;
    }


    /**
     * Creates LearningMaterials from .txt files provided with the correct format.
     * @param files -  array of files (or directory) containing the LearningMaterials
     * @return - list of LearningMaterials
     */
    public static List<LearningMaterial> findLearningResources(File[] files) {

        final String jsonFile = "src/test/resources/learningMaterialRecordsTags.json";
        int startingNum = learningMaterialsCount(jsonFile);

        List<LearningMaterial> learningMaterials = new ArrayList<>();
        RetrieveLearningResource retrieveLearningResource = new RetrieveLearningResource(startingNum);

        for (File file : files) {
            learningMaterials.addAll(retrieveLearningResource.findLearningMaterial(readFile(file.getPath())));
        }

        return learningMaterials;

    }



}
