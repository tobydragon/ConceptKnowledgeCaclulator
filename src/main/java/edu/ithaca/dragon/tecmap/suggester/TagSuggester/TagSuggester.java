package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

import smile.nlp.SimpleCorpus;
import smile.nlp.relevance.TFIDF;

import java.util.*;

import static edu.ithaca.dragon.tecmap.suggester.TagSuggester.TagSuggesterUtil.*;

public class TagSuggester {

    /**
     *
     * @param mainDocument - document being tagged
     * @param allDocuments - list of documents to perform IDF
     * @return list of the top tags calculated with TFIDF for the mainDocument
     */
    public static List calculateTagsTFIDF(String mainDocument, List<String> allDocuments){

        TFIDF tfidf = new TFIDF();
        List<Set> documentSet = documentToSet(allDocuments);
        SimpleCorpus mainCorpus = generateMainCorpus(mainDocument);
        SimpleCorpus fullCorpus = generateFullCorpus(allDocuments);

        Iterator<String> uniqueTerms = mainCorpus.getTerms();
        List<TFIDF_Term> terms = new ArrayList<>();

        String term;
        Double rank;

        while (uniqueTerms.hasNext()){
            term = uniqueTerms.next();
            term = clearNonLetterCharacters(term);

            if (!term.isEmpty() && !term.matches("[\\d*]-") && !term.matches("[-]\\d*") && !term.matches("\\d*") && !isCommonWord(term)){
                rank = calculateTFIDF(tfidf, mainCorpus, fullCorpus, fullCorpus.getNumDocuments(), documentSet, term);
                terms.add(new TFIDF_Term(term, rank));
            }
        }

        return topTFIDFTags(terms);

    }

    /**
     *
     * @param terms - list of TFIDF__Term containing all the unique terms from the mainDocument
     * @return list of tags
     */
    public static List topTFIDFTags(List<TFIDF_Term> terms){

        int topTerms = 5;

        if (terms.size() < 5){
            topTerms = 2;
        }

        Collections.sort(terms);

        List<String> tags = new ArrayList<>();

        for (int i = 0; i < topTerms; i++) {
            tags.add(terms.get(i).getTerm());
        }

        return tags;
    }

    public static SimpleCorpus generateMainCorpus(String mainDocument){
        SimpleCorpus corpus = new SimpleCorpus();
        corpus.add("", "", mainDocument.toLowerCase());
        return corpus;
    }

    public static SimpleCorpus generateFullCorpus(List<String> documents){

        SimpleCorpus corpus = new SimpleCorpus();

        for (String document : documents){
            corpus.add("", "", document.toLowerCase());
        }

        return corpus;
    }

    public static int findNumOfOccurrences(List<Set> documents, String term){

        int occurrences = 0;

        for (Set document : documents){
            if (document.contains(term.toLowerCase())){
                occurrences++;
            }
        }

        return occurrences;
    }

    public static int findTF(SimpleCorpus corpus, String term){
        return corpus.getTermFrequency(term.toLowerCase());
    }

    public static double calculateTFIDF(TFIDF tfidf, SimpleCorpus mainCorpus, SimpleCorpus fullCorpus, int numOfDocuments, List<Set> documentsSet, String term){

        int TF = findTF(mainCorpus, term.toLowerCase());
        int MAXTF = findTF(fullCorpus, term.toLowerCase());
        int occurences = findNumOfOccurrences(documentsSet, term.toLowerCase());

        return tfidf.rank(TF, MAXTF, numOfDocuments, occurences);
    }

    public static String cleanTag(String tag){

        if (tag.replaceAll(",", "").trim().isEmpty()){
            return null;
        }

        return tag.replaceAll(",", "").trim().toLowerCase();
    }

    public static List<String> cleanTags(List<String> tags){

        List<String> cleanTags = new ArrayList<>();

        for (int i = 0; i < tags.size(); i++) {
            if (cleanTag(tags.get(i)) != null){
                cleanTags.add(cleanTag(tags.get(i)));
            }
        }

        cleanTags = removeCommonWords(cleanTags);

        return cleanTags;
    }

}

