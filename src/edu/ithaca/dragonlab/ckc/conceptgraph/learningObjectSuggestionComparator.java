package edu.ithaca.dragonlab.ckc.conceptgraph;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Mia Kimmich Mitchell on 4/10/2017.
 */
public class learningObjectSuggestionComparator implements Comparator<learningObjectSuggestion> {

    public int compare(learningObjectSuggestion one, learningObjectSuggestion two){
        //have some tests
        //if levels are equal, then compare the path nums

        if (one.getLevel().compareTo(two.getLevel()) == 0) {
            if (one.getPathNum() < two.getPathNum()) {
                return 1;
            } else if (one.getPathNum() > two.getPathNum()) {
                return -1;

            } else {
                return 0;
            }
        }



        return one.getLevel().compareTo(two.getLevel());

    }

}
