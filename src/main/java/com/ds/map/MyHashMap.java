package map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class MyHashMap {
    LinkedList<Entry>[] map;

    MyHashMap(int capacity){
        this.map= new LinkedList[capacity];
    }

    public void put(int key, int val){
        int bucket=Math.abs(key)%map.length;
        if(map[bucket]==null){
            map[bucket]= new LinkedList<>();
        }
        for(Entry entry:map[bucket]){
                if(entry.key==key){
                    entry.val=val;
                    return;
                }
        }
        map[bucket].add(new Entry(key, val));
    }
    public int get(int key){
        int bucket=Math.abs(key)%map.length;
        if(map[bucket]==null) return -1;
        for(Entry entry:map[bucket]){
            if(key==entry.key){
                return entry.val;
            }
        }
        return -1;
    }
    public void remove(int key){
        int bucket=Math.abs(key)%map.length;
        if(map[bucket]==null) return;
        Iterator<Entry> it= map[bucket].iterator();
        while(it.hasNext()){
            if(key==it.next().key){
                it.remove();
                return;
            }
        }
    }
}
class Entry{
    int key, val;
    public Entry(int key, int val){
        this.key=key;
        this.val=val;
    }
}