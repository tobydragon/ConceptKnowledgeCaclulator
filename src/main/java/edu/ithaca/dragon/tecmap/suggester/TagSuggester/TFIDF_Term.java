package edu.ithaca.dragon.tecmap.suggester.TagSuggester;

public class TFIDF_Term implements Comparable {

    private String term;
    private Double rank;

    public TFIDF_Term(String term, Double rank){
        this.term = term;
        this.rank = rank;
    }

    public String getTerm() {
        return term;
    }

    public Double getRank() {
        return rank;
    }

    public String toString() {
        return "TFIDF_Term{" +
                "term='" + term + '\'' +
                ", rank=" + rank +
                '}';
    }

    @Override
    public int compareTo(Object t) {
        double compareRank =((TFIDF_Term)t).getRank();
        return Double.compare(compareRank, this.rank);
    }
}
