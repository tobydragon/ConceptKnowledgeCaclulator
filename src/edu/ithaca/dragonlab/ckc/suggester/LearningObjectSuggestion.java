package edu.ithaca.dragonlab.ckc.suggester;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */
public class LearningObjectSuggestion {
    private String id;
    private int pathNum;
    private Level level;

    public enum Level{
        //used to create ordering for suggestion
        RIGHT, WRONG, INCOMPLETE;

    }


    public LearningObjectSuggestion() {
        this.id = null;
        this.pathNum= 0;
        this.level = Level.INCOMPLETE;

    }

    public LearningObjectSuggestion(String id, int path, Level lev) {
        this();
        this.id = id;
        this.pathNum= path;
        this.level = lev;
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


    public String toString(){
            return id + " " + level + " "+ pathNum;
    }


}
