package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import java.util.List;

/**
 * Created by mkimmitchell on 10/26/17.
 */
public abstract class Suggester {


    public abstract List<Group> suggestGroup(Group groupSoFar, Group extraMembers);


}
