package edu.ithaca.dragonlab.ckc.conceptgraph;

/**
 * Created by Mia Kimmich Mitchell on 3/28/2017.
 */

public class learningObjectSuggestionNode {
    String id;
    int pathNum;
    String level;
    //right, wrong, unanswered

    public learningObjectSuggestionNode() {
        this.id = null;
        this.pathNum= 0;
        this.level = null;

    }

    public learningObjectSuggestionNode(String id, int path, String level) {
        this();
        this.id = id;
        this.pathNum= path;
        this.level = level;
    }



    public void setId(String name){
        id = name;
    }

    public void setPathNum(int num){
        pathNum= num;
    }

    public void setLevel(String lev){
        level = lev;
    }


    public String getId(){
        return id;
    }

    public int getPathNum(){
        return pathNum;
    }

    public String getLevel(){
        return level;
    }


}
