package com.example.worker.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollIndex {
    HashMap<String , PropIndex> map = new HashMap<>();


    public boolean containsKey(String Collection){
        return map.containsKey(Collection);
    }
    public void indexProperties(String Collection, String Property, String value, int idx){
        map.get(Collection).addIndex(Property,value,idx);
    }


    public boolean createCollectionIndex(String Collection, String []Properties){
        if(map.containsKey(Collection))
            return false;

        PropIndex index = new PropIndex(Properties);
        map.put(Collection, index);
        return true;
    }
    public List<String> getColl(){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, PropIndex> entry : map.entrySet())
            list.add(entry.getKey());
        return list;
    }
    public void deleteByIndex(String Collection, int idx){
        map.get(Collection).deleteIndex(idx);
    }
    public void addIndex(String Collection, String Property, String value, int idx){
        if(map.containsKey(Collection))
            map.get(Collection).addToValue(Property,value,idx);
    }
    public HashMap<String, List<Integer>> getMap(String Collection, String Property) {
        return map.get(Collection).getMap(Property);
    }
    public void deleteColl(String Collection){
        if(map.containsKey(Collection))
            map.remove(Collection);
    }

}
