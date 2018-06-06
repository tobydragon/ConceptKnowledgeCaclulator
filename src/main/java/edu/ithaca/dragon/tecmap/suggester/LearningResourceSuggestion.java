package edu.ithaca.dragon.tecmap.suggester;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */
public class LearningResourceSuggestion {
    private String id;
    private int pathNum;
    private String reasoning;
    private Level level;
    private int directConceptLinkCount;

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



    public void setId(String name){
        id = name;
    }

    public void setPathNum(int num){
        pathNum= num;
    }

    public void setLevel(Level lev){
        level = lev;
    }
    public void setDirectConceptLinkCount (int num ){
        directConceptLinkCount= num;
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
