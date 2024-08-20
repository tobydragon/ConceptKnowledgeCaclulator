package edu.ithaca.dragon.tecmap.suggester;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */
public class LearningResourceSuggestion {
    private String id;
    private int pathNum; // number of paths to LR from a node
    private String reasoning;
    private Level level;
    private int directConceptLinkCount; // how many concepts LR is linked to
//    private String text;

    public enum Level{
        //used to create ordering for suggestion
        RIGHT, WRONG, INCOMPLETE;

    }

    public LearningResourceSuggestion(String id, int path, Level lev, String caused, int directConceptLinkCount) {
        this.id = id;
        this.pathNum= path;
        this.level = lev;
        this.reasoning= caused;
        this.directConceptLinkCount = directConceptLinkCount;
    }

    public String getId(){
        return id;
    }

    public int getPathNum(){
        return pathNum;
    }

    public Level getLevel(){
        return level;
    }

    public String getReasoning(){
        return reasoning;
    }

    public int getDirectConceptLinkCount(){
        return directConceptLinkCount;
    }

    public String toString(){
            return "Resource: " +id + "\t Concept it relates to: " + reasoning + "\t Importance: "+ pathNum + "\t Direct Concept Links: " + directConceptLinkCount + "\n";
    }

}
