package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.util.*;

/**
 * Created by mkimmitchell on 8/1/17.
 */
public class suggestionGroupSuggestion extends GroupSuggester {

    public String getSimilarResourceLevel (SuggestionResource s1, SuggestionResource s2){

        List<LearningObjectSuggestion> incom1 = s1.incompleteList;

        List<LearningObjectSuggestion> incom2 = s2.incompleteList;

        List<LearningObjectSuggestion> wrong1 = s1.wrongList;
        List<LearningObjectSuggestion> wrong2 = s2.wrongList;

        /*
        student1   student2
        incomplete  ==true  2
        wrong == true       3
        incomplete wrong    4
        wrong incomplete    4


        empty empty         1


         */


            if(incom1.size()> 0 && incom2.size()>0 &&incom1.get(0).getId().equals(incom2.get(0).getId())){
                return 2 + "\n" + incom1.get(0).getId();
            }

            else if(wrong1.size()>0 && wrong2.size()>0&&wrong1.get(0).getId().equals(wrong2.get(0).getId())) {
                return 3 + "\n" + wrong1.get(0).getId();

            }else if(incom1.size()>0 && wrong2.size()>0 &&incom1.get(0).getId().equals(wrong2.get(0).getId())){
                return 4 + "\n"+ incom1.get(0).getId();
            } else if (wrong1.size()>0 && incom2.size()>0&&wrong1.get(0).getId().equals(incom2.get(0).getId())){
                return 4+ "\n" + wrong1.get(0).getId();
            }

            else if (wrong1.size()== 0&& incom1.size()==0 && wrong2.size()==0 && incom2.size()==0){
                    return 1+ "\n"+ "something challenging";
            } else{
//                System.out.println("Something else " + "incom1 " + incom1.size() + " incom2 " +incom2.size() + " wrong1 " + wrong1.size() + " wrong2 "+ wrong2.size());
                return 0+"\n";

            }


        }



        public void makeGroups(List<List<String>> num, List<String> userTemp, List<List<String>> groupings){

            for(List<String> temp: num){
                boolean flag = false;


                for(int i=0; i<temp.size()-2; i++){
                    String name = temp.get(i);

                    if(userTemp.contains(name)){

                    } else{
                        flag=true;
                        break;
                    }
                }

                if(!flag){
                    List<String> groupRe = new ArrayList<>();
                    groupRe.addAll(temp);
//                    groupRe.add();
                    groupings.add(groupRe);
                    userTemp.remove(temp.get(0));
                    userTemp.remove(temp.get(1));

                }

            }



        }


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {

        Map <String, ConceptGraph> getMaps = getUserMap(graphs);

        List<ConceptGraph> graphList = new ArrayList<>();
        graphList.addAll(getMaps.values());


        Map<String, SuggestionResource> userMap = new HashMap<>();


        for(ConceptGraph graph: graphList){
            List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(graph);

            userMap.put(graph.getName(),new SuggestionResource(graph, nodeList) );

        }
        List<List<String>> zero = new ArrayList<>();

        List<List<String>> one = new ArrayList<>();
        List<List<String>> two = new ArrayList<>();
        List<List<String>> three = new ArrayList<>();
        List<List<String>> four = new ArrayList<>();

        List<String> repeats = new ArrayList<>();

        for(String name: userMap.keySet()){
            SuggestionResource sugres = userMap.get(name);

            for(String name2: userMap.keySet()){
                SuggestionResource sugres2 = userMap.get(name2);

                if(!name.equals(name2) && !(repeats.contains(name+"+"+name2) || repeats.contains(name2+"+"+name))){

                    String ex = getSimilarResourceLevel(sugres, sugres2);

                    List<String> groups = new ArrayList<>();
                    groups.add(name);
                    groups.add(name2);

                    if(ex.substring(0,1).equals("1")){
                        groups.add(ex.substring(2,ex.length()));
                        one.add(groups);
                    }else if(ex.substring(0,1).equals("2")){
                        groups.add(ex.substring(2,ex.length()));
                        two.add(groups);
                    }else if(ex.substring(0,1).equals("3")){
                        groups.add(ex.substring(2,ex.length()));
                        three.add(groups);
                    }else if (ex.substring(0,1).equals("4")){
                        groups.add(ex.substring(2,ex.length()));
                        four.add(groups);
                    }

                    repeats.add(name+"+"+name2);

                }
            }
        }

//
//        System.out.println("one");
//        System.out.println(one);
//
//        System.out.println("two");
//        System.out.println(two);
//
//        System.out.println("three");
//        System.out.println(three);
//
//        System.out.println("four");
//        System.out.println(four);
//
//        System.out.println("\n");


        List<String> userTemp = new ArrayList<>();
        userTemp.addAll(userMap.keySet());

        List<List<String>> groupings = new ArrayList<>();

        if(one.size()>0){
            makeGroups(one, userTemp, groupings);

        }
        if(two.size()>0){
            makeGroups(two, userTemp, groupings);

        }
        if(three.size()>0){
            makeGroups(three, userTemp, groupings);

        }
        if(four.size()>0){
            makeGroups(four, userTemp, groupings);

        }

//        System.out.println("left over");
//
//        System.out.println(userTemp.size());
//        System.out.println(choice);

        if(userTemp.size() % choice == 0 && userTemp.size() >= choice){

            int whileNum = userTemp.size();
            while(whileNum>0) {

                List<String> group = new ArrayList<>();

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userTemp.get(choices-1));
                    userTemp.remove(userTemp.get(choices-1));
                    choices--;
                }

                group.add("Random pairing");
                groupings.add(group);

                whileNum= whileNum-choice;
            }


        }else if (userTemp.size() < choice && userTemp.size() % choice != 0){


            List<String> groupRe = new ArrayList<>();
            groupRe.addAll(userTemp);
            groupRe.add("Random paring");

            groupings.add(groupRe);


        } else {
            //user.size()%choice != 0 && user.size()>= choice

            //there will be one group that doesn't have a full group

            for(int i = userTemp.size()/choice; i> 0 ; i--){

                List<String> group = new ArrayList<>();

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userTemp.get(choices-1));
                    userTemp.remove(userTemp.get(choices-1));
                    choices--;
                }

                group.add("Random pairing");
                groupings.add(group);

            }

            List<String> group = new ArrayList<>();
            group.addAll(userTemp);

            groupings.add(group);


        }







        return groupings;
    }


}
