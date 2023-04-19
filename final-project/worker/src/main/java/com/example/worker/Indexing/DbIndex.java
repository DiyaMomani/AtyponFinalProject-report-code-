package com.example.worker.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbIndex {
    private static DbIndex instance;
    public static DbIndex getInstance(){
        if(instance == null)
            instance = new DbIndex();
        return instance;
    }
    HashMap<String , CollIndex> map = new HashMap<>();

    public boolean createIndex(String Database, String Collection, String []Properties){
        CollIndex index = new CollIndex();
        boolean ans = index.createCollectionIndex(Collection,Properties);
        map.put(Database,index);
        return ans;
    }
    public boolean addColl(String Database, String Collection, String []Properties){
        if(!map.containsKey(Database)) return false;
        return map.get(Database).createCollectionIndex(Collection,Properties);
    }
    public boolean containsKey(String Database){
        return map.containsKey(Database);
    }
    public boolean containsKey(String Database, String Collection){
        if(!map.containsKey(Database))
            return false;

        return map.get(Database).containsKey(Collection);
    }
    public List<String> getDatabase(){
        if(map == null)
            return new ArrayList<>();

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, CollIndex> entry : map.entrySet())
            list.add(entry.getKey());
        return list;
    }
    public void indexProperties(String Database, String Collection, String Property, String value, int idx){
        if(map.containsKey(Database))
            map.get(Database).indexProperties(Collection,Property,value,idx);
    }

    public HashMap<String, List<Integer>> getMap(String Database, String Collection, String Property) {
        return map.get(Database).getMap(Collection,Property);
    }
    public void deleteByIndex(String Database, String Collection, int idx){
        if(!map.containsKey(Database))
            return;

        map.get(Database).deleteByIndex(Collection,idx);
    }
    public List<String> getColl(String Database){

        if(!map.containsKey(Database))
            return new ArrayList<>();
        return map.get(Database).getColl();
    }
    public void deleteDatabase(String Database){
        if(!map.containsKey(Database))
            return;
        map.remove(Database);
    }

    public void deleteCollection(String Database, String Collection){
        if(!map.containsKey(Database))
            return;
        map.get(Database).deleteColl(Collection);
    }
    public void addIndex(String Database, String Collection, String Property, String value, int idx){
        if(!map.containsKey(Database))
            return;

        map.get(Database).addIndex(Collection,Property,value,idx);
    }


}
