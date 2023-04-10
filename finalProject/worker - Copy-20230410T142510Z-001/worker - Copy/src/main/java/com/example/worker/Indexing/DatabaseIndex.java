package com.example.worker.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseIndex {
    private static DatabaseIndex instance;
    public static DatabaseIndex getInstance(){
        if(instance == null)
            instance = new DatabaseIndex();
        return instance;
    }
    HashMap<String , CollectionIndex> map = new HashMap<>();

    public boolean initIndex(String Database, String Collection, String []Properties){
        CollectionIndex index = new CollectionIndex();
        boolean ans = index.initIndexForCollection(Collection,Properties);
        map.put(Database,index);
        return ans;
    }
    public boolean addCollection(String Database, String Collection, String []Properties){
        if(!map.containsKey(Database)) return false;
        return map.get(Database).initIndexForCollection(Collection,Properties);
    }
    public boolean isIndexed(String Database){
        return map.containsKey(Database);
    }
    public boolean isIndexed(String Database,String Collection){
        if(!map.containsKey(Database))
            return false;

        return map.get(Database).isIndexed(Collection);
    }

    public void indexProperties(String Database, String Collection, String Property, String value, int idx){
        if(map.containsKey(Database))
            map.get(Database).indexProperties(Collection,Property,value,idx);
    }

    public HashMap<String, List<Integer>> getMap(String Database, String Collection, String Property) {
        return map.get(Database).getMap(Collection,Property);
    }

    public List<String> getCollections(String Database){
        if(!map.containsKey(Database))
            return new ArrayList<>();
        return map.get(Database).getCollections();
    }
    public List<String> getDatabases(){
        if(map == null)
            return new ArrayList<>();

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, CollectionIndex> entry : map.entrySet())
            list.add(entry.getKey());
        return list;
    }

    public void deleteByIdx(String Database,String Collection,int idx){
        if(!map.containsKey(Database))
            return;

        map.get(Database).deleteByIdx(Collection,idx);
    }
    public void addToValue(String Database, String Collection, String Property, String value, int idx){
        if(!map.containsKey(Database))
            return;

        map.get(Database).addToValue(Collection,Property,value,idx);
    }
    public void DropCollection(String Database, String Collection){
        if(!map.containsKey(Database))
            return;
        map.get(Database).dropCollection(Collection);
    }
    public void DropDatabase(String Database){
        if(!map.containsKey(Database))
            return;
        map.remove(Database);
    }
}
