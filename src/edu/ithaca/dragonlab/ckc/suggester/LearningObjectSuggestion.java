package edu.ithaca.dragonlab.ckc.suggester;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */
public class LearningObjectSuggestion {
    private String id;
    private int pathNum;
    private String reasoning;
    private Level level;

    public enum Level{
        //used to create ordering for suggestion
        RIGHT, WRONG, INCOMPLETE;

    }


    public LearningObjectSuggestion() {
        this.id = null;
        this.pathNum= 0;
        this.reasoning= null;
        this.level = Level.INCOMPLETE;

    }

    public LearningObjectSuggestion(String id, int path, Level lev, String caused) {
        this();
        this.id = id;
        this.pathNum= path;
        this.level = lev;
        this.reasoning= caused;
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


    public String toString(){
            return "Resource: " +id + "\t Concept it relates to: " + reasoning + "\t Importance: "+ pathNum + "\n";
    }


}
