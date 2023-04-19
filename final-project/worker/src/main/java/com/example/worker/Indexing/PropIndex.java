package com.example.worker.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropIndex {
    private HashMap<String , HashMap<String,List<Integer>> > map = new HashMap<>();




    public HashMap<String, List<Integer>> getMap(String value) {
        return map.get(value);
    }
    public void deleteIndex(int idx) {
        // Iterate through all properties in the map
        for (Map.Entry<String, HashMap<String, List<Integer>>> propertyEntry : map.entrySet()) {
            String property = propertyEntry.getKey();
            HashMap<String, List<Integer>> valueMap = propertyEntry.getValue();

            // Iterate through all property values in the value map
            for (Map.Entry<String, List<Integer>> valueEntry : valueMap.entrySet()) {
                String value = valueEntry.getKey();
                List<Integer> indexList = valueEntry.getValue();

                // Check if the index exists in the index list
                int indexToRemove = indexList.indexOf(idx);
                if (indexToRemove != -1) {
                    // Remove the index from the list
                    indexList.remove(indexToRemove);

                    // Decrease all indexes greater than idx by 1
                    for (int i = indexToRemove; i < indexList.size(); i++) {
                        indexList.set(i, indexList.get(i) - 1);
                    }
                }
            }
        }
    }
    public void addIndex(String Property, String value, int idx){
        if(map.containsKey(Property)){
            if(!map.get(Property).containsKey(value))
                map.get(Property).put(value,new ArrayList<>());

            List<Integer> list = map.get(Property).get(value);
            list.add(idx);
        }
    }
    public PropIndex(String []Properties){
        for(String property: Properties)
            map.put(property,new HashMap<>());
    }
    public void addToValue(String Property, String value, int idx){
        if(map.containsKey(Property)){
            HashMap<String, List<Integer>> map1 = map.get(Property);
            if(map1.containsKey(value))
                map1.get(value).add(idx);
            else{
                List<Integer> list = new ArrayList<>();
                list.add(idx);
                map1.put(value,list);
            }
        }
    }


}
