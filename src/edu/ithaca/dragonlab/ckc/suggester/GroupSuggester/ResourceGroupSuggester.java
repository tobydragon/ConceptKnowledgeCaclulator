package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import com.sun.tools.corba.se.idl.constExpr.ShiftLeft;
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
public class ResourceGroupSuggester extends GroupSuggester {



    public List<String> getSimilarResourceLevel2 (SuggestionResource s1, SuggestionResource s2){

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

        List<String> order = new ArrayList<>();

        String value = "0";
        String resourceName = "";



        if(incom1.size()> 0 && incom2.size()>0 &&incom1.get(0).getId().equals(incom2.get(0).getId())){
            value = String.valueOf(2);
            resourceName = incom1.get(0).getId();

        } else if(wrong1.size()>0 && wrong2.size()>0&&wrong1.get(0).getId().equals(wrong2.get(0).getId())) {
            value = String.valueOf(3);
            resourceName = wrong1.get(0).getId();

        }else if(incom1.size()>0 && wrong2.size()>0 &&incom1.get(0).getId().equals(wrong2.get(0).getId())){
            value = String.valueOf(4);
            resourceName = incom1.get(0).getId();

        } else if (wrong1.size()>0 && incom2.size()>0&&wrong1.get(0).getId().equals(incom2.get(0).getId())){
            value = String.valueOf(4);
            resourceName = wrong1.get(0).getId();
        } else if (wrong1.size()== 0&& incom1.size()==0 && wrong2.size()==0 && incom2.size()==0){
            value = String.valueOf(1);
            resourceName = "something challenging";

        }

        order.add(value);
        order.add(resourceName);
        return order;
    }



    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {

        Map <String, ConceptGraph> getMaps = getUserMap(graphs);
        Map<String, SuggestionResource> userSuggestionMap = new HashMap<>();


        for(ConceptGraph graph: getMaps.values()){
            List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(graph);
            userSuggestionMap.put(graph.getName(),new SuggestionResource(graph, nodeList) );
        }



        Map<List<String>, Integer> storedMap = new HashMap<>();
        // student 1, student 2, resource, integer

        List<String> repeats = new ArrayList<>();


//        if(choice==2){
            for(String name: userSuggestionMap.keySet()){
                SuggestionResource sugres = userSuggestionMap.get(name);

                for(String name2: userSuggestionMap.keySet()){
                    SuggestionResource sugres2 = userSuggestionMap.get(name2);

                    if(!name.equals(name2) && !(repeats.contains(name+"+"+name2) || repeats.contains(name2+"+"+name))){

                        List<String> ex = getSimilarResourceLevel2(sugres, sugres2);
                        if(!ex.get(0).equals("0")){
                            List<String> mapList = new ArrayList<>();

                            mapList.add(name);
                            mapList.add(name2);
                            mapList.add(ex.get(1));
                            Integer value = Integer.parseInt(ex.get(0));
                            storedMap.put(mapList, value);

                        }

                        repeats.add(name+"+"+name2);
                    }
                }
            }


        HashMap<List<String>, Integer> sortedUserMap = sortValues(storedMap);

        List<String> userTemp = new ArrayList<>();
        userTemp.addAll(userSuggestionMap.keySet());

        List<List<String>> groupings = new ArrayList<>();


        for(List<String> list: sortedUserMap.keySet()){
            if(userTemp.contains(list.get(0)) && userTemp.contains(list.get(1))){

                List<String> groups = new ArrayList<>();
                groups.addAll(list);
                groupings.add(groups);

                userTemp.remove(list.get(0));
                userTemp.remove(list.get(1));

            }
        }


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


            List<String> group = new ArrayList<>();
            group.addAll(userTemp);
            group.add("No other students");

            groupings.add(group);


        } else {
            //user.size()%choice != 0 && user.size()>= choice

            //there will be one group that doesn't have a full group
            //while there are still pairings left to create

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

            //for the left over students
            List<String> group = new ArrayList<>();
            group.addAll(userTemp);
            group.add("No other students");

            groupings.add(group);


        }
        if(choice==2){
            return groupings;

        }

        if(choice==3){
//            System.out.println(groupings);

            List<List<String>> trioGroup = new ArrayList<>();

            for(int i =0; i< (getMaps.size()/choice); i++){

                List<String> group = groupings.get(0);
                trioGroup.add(group);
                groupings.remove(group);
            }


            List<String> leftOver = new ArrayList<>();
            for(List<String> list: groupings){
                leftOver.addAll(list);
            }

            List<String> leftOver2 = new ArrayList<>();

            for(String in: leftOver){
                if(userSuggestionMap.containsKey(in)){
                    leftOver2.add(in);
                }
            }

            List<String> leftOver3 = new ArrayList<>();
            leftOver3.addAll(leftOver2);


            HashMap<String , Integer> addMap = new HashMap<>();

            List<String > rr = new ArrayList<>();
            for(String list: leftOver2){
                SuggestionResource sugres = userSuggestionMap.get(list);

                String leftOverName = "";
                String trioTempTemp = "";
                int index =0;


                for(int i=0; i< trioGroup.size(); i++){
                    List<String> actualList = trioGroup.get(i);
                    String name = actualList.get(0);
                    SuggestionResource sugres2 = userSuggestionMap.get(name);


                    int temp = storedMap.get(actualList);

                    if(getMaps.containsKey(list) && getMaps.containsKey(name)){

                        List<String> ex = getSimilarResourceLevel2(sugres, sugres2);
                        int value = Integer.parseInt(ex.get(0));


                        if ( value == temp && (!rr.contains(name))) {


                            leftOverName = list;
                            trioTempTemp = name;
                            index = i;



                        }
                    }
                }

                rr.add(trioTempTemp);

                leftOver3.remove(leftOverName);

                if(!addMap.containsValue(index) && (!leftOverName.equals(""))){
                    addMap.put(leftOverName, index);

                }
            }


            for(String name: addMap.keySet()){
                int index2 = addMap.get(name);

                if(trioGroup.size()>0){
                    List<String> group2 = trioGroup.get(index2);

                    if(group2.size()>2){
                        group2.add(2, name);
                    }else{
                        group2.add(name);
                    }
                }
            }


            if(leftOver3.size()>0){
                List<String> group = new ArrayList<>();
                group.addAll(leftOver3);
                if(group.size()<3){
                    while(group.size()!=3){
                        group.add("No other students");
                    }
                }

                trioGroup.add(group);
            }




                return trioGroup;
        }



        return new ArrayList<>();
    }


    private static  HashMap<List<String>, Integer> sortValues(Map<List<String>, Integer> map) {
        List hashmapList = new LinkedList(map.entrySet());

        Collections.sort(hashmapList, new Comparator() {
            public int compare(Object o1, Object o2) {

                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        }
        );

        HashMap<List<String>, Integer> sortedMap = new LinkedHashMap();
        for (Iterator itr = hashmapList.iterator(); itr.hasNext(); ) {
            Map.Entry entry = (Map.Entry) itr.next();

            List<String> key = (List<String>) entry.getKey();
            Integer value = (Integer) entry.getValue();

            sortedMap.put(key, value);
        }
        return sortedMap;
    }



}
