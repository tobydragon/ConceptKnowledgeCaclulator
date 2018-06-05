package edu.ithaca.dragon.tecmap;

public enum TecmapAction {

    structureTree("/structureTree"),
    cohortTree("/cohortTree");


    private String mappingString;

    TecmapAction(String mappingString){
        this.mappingString = mappingString;
    }

    public String getMappingString(){
        return mappingString;
    }


}
