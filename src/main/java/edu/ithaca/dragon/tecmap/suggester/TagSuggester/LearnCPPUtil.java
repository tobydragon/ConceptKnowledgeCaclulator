package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.*;

public class LearnCPPUtil {

    /**
     * @param doc - Jsoup HTML document
     * @return list of all the bold words from the html page
     */
    public static List findTagsFromBoldWords(Document doc) {

        String bQuery = "div.post-bodycopy.clearfix > p > strong, .cpp-definition";
        List<String> tags = new ArrayList<>();
        List<String> cleanTags = new ArrayList<>();

        try {
            Element body = doc.body();
            for (Element row : body.select(bQuery)) {
                if (!row.wholeText().equals("") || !row.wholeText().equals(" ")) {
                    tags.add(row.wholeText().trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String tag : tags){
            if (tag.contains(" ")){
                String[] split = tag.toLowerCase().split(" ");
                cleanTags.addAll(Arrays.asList(split));
            } else {
                cleanTags.add(clearNonLetterCharacters(tag.toLowerCase()));
            }
        }

        return removeCommonWords(cleanTags);
    }

    /**
     * @param doc - Jsoup HTML document
     * @return list of all the words enclosed with a <title> tag from the url
     */
    public static List findTagsFromTitle(Document doc) {

        String tQuery = "div.post-headline";
        String title = "";
        List<String> tags = new ArrayList<>();

        try {
            Element body = doc.body();
            for (Element row : body.select(tQuery)) {
                if (!row.wholeText().equals("") || !row.wholeText().equals(" ")) {
                    title += row.wholeText().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] splitTitle = title.split(" ");

        for (String word : splitTitle) {
            if (!isCommonWord(word)){
                tags.add(word);
            }
        }

        return tags;

    }

    /**
     * @param doc - Jsoup HTML document
     * @return string of everything enclosed in a <p> tag
     */
    public static String findParagraph(Document doc) {

        String pQuery = "div.post-bodycopy.clearfix > p";
        String urlText = "";

        try {
            Element body = doc.body();

            for (Element row : body.select(pQuery)) {
                if (!row.ownText().equals("") || !row.ownText().equals(" ")) {
                    urlText += row.ownText().trim();
                    urlText += " ";
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        urlText = urlText.trim();
        return urlText;

    }

    /**
     * @return list of strings containing content from all the learncpp.com chapters
     */
    public static List<String> generateDocumentsFromIndex() throws IOException {
        List<String> urls = findURLSFromIndex();
        List<String> documents = new ArrayList<>();

        for (String url : urls){
            documents.add(findParagraph(Jsoup.connect(url).timeout(10000).validateTLSCertificates(false).get()));
        }

        return documents;
    }

    /**
     * @return list of urls for all the learncpp.com chapters
     */
    public static List findURLSFromIndex(){

        String aQuery = "div.post-bodycopy.clearfix > table > tbody > tr > td > a";
        String index = "https://www.learncpp.com";
        List<String> urls = new ArrayList<>();

        try {
            final Document document = Jsoup.connect(index).timeout(10000).validateTLSCertificates(false).get();
            Element body = document.body();

            for (Element row : body.select(aQuery)) {
                if (!row.wholeText().equals("") || !row.wholeText().equals(" ")) {
                    urls.add(row.attr("href"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertPathsToLinks(urls);
    }

    public static List convertPathsToLinks(List<String> urls){
        for (int i = 0; i < urls.size(); i++) {
            if (!urls.get(i).contains(".com")){
                urls.set(i, convertPathToLink(urls.get(i)));
            }
        }

        return urls;
    }

    public static String convertPathToLink(String path){
        String index = "https://www.learncpp.com";
        return index + path;
    }

    public static boolean isLearnCPPURL(String url){
        return url.matches(".+learncpp\\.com/.+");
    }
}
