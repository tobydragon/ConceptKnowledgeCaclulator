package edu.ithaca.dragon.tecmap.analysis;

public class RSettings {
    private static String R_SCRIPT_PATH = "/usr/local/Cellar/r/3.5.2_2/bin/Rscript";
    private static String R_CODE_ROOT = "/Users/home/IdeaProjects/tecmap/src/main/r/";

    public static String getRScriptPath() {
        return R_SCRIPT_PATH;
    }

    public static String getRCodeRoot() {
        return R_CODE_ROOT;
    }
}
