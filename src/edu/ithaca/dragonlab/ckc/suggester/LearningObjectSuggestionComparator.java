package edu.ithaca.dragonlab.ckc.suggester;

import java.util.Comparator;

/**
 * Created by Mia Kimmich Mitchell on 4/10/2017.
 */
public class LearningObjectSuggestionComparator implements Comparator<LearningObjectSuggestion> {

    public int compare(LearningObjectSuggestion one, LearningObjectSuggestion two){
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
