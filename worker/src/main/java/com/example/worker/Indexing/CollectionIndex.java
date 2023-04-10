package com.example.worker.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionIndex {
    HashMap<String , PropertyIndex> map = new HashMap<>();

    public boolean initIndexForCollection(String Collection, String []Properties){
        if(map.containsKey(Collection))
            return false;

        PropertyIndex index = new PropertyIndex(Properties);
        map.put(Collection, index);
        return true;
    }
    public boolean isIndexed(String Collection){
        return map.containsKey(Collection);
    }
    public void indexProperties(String Collection, String Property, String value, int idx){
        map.get(Collection).add(Property,value,idx);
    }

    public HashMap<String, List<Integer>> getMap(String Collection, String Property) {
        return map.get(Collection).getMap(Property);
    }
    public void deleteByIdx(String Collection,int idx){
        map.get(Collection).removeIndex(idx);
    }
    public void addToValue(String Collection, String Property, String value, int idx){
        if(map.containsKey(Collection))
            map.get(Collection).addToValue(Property,value,idx);
    }
    public void dropCollection(String Collection){
        if(map.containsKey(Collection))
            map.remove(Collection);
    }
    public List<String> getCollections(){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, PropertyIndex> entry : map.entrySet())
            list.add(entry.getKey());
        return list;
    }
}
