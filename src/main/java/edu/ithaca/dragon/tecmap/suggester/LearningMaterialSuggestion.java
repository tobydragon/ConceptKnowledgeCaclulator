package edu.ithaca.dragon.tecmap.suggester;

import lombok.Getter;

@Getter
public class LearningMaterialSuggestion {
    private String id;
    private String reasoning;
    private String text;


    public LearningMaterialSuggestion(String id, String caused, String text) {
        this.id = id;
        this.reasoning= caused;
        this.text = text;
    }

    public String toString(){
        return "Resource: " +id + "\t Concept it relates to: " + reasoning + "\n";
    }
}
