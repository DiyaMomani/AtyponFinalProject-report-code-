package com.example.worker.Synchronize;

import java.util.HashMap;

public class Locks {
    private static Locks instance;
    public static Locks getInstance(){
        if(instance==null){
            instance=new Locks();
        }
        return instance;
    }
    HashMap<String,Object> map=new HashMap<>();
    public Object getLock(String path){
        String [] locks=path.split("/");
        for (String item:locks) {
            if(map.containsKey(item)){
                return map.get(item);
            }
        }
        int size=locks.length;
        Object object= new Object();
        map.put(locks[size-1],object);
        return object;
    }
    public void deleteLock(String path){
        String [] locks=path.split("/");
        int size=locks.length;
        String word=locks[size-1];
        if(map.containsKey(word)){
            map.remove(word);
        }
    }
}
