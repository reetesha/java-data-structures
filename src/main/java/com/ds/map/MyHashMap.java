package map;

import java.util.Iterator;
import java.util.LinkedList;

public class MyHashMap<K,V> {
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;
    private static final double LOAD_FACTOR=0.75;

    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity){
        if(capacity<=0){
            throw new IllegalArgumentException("Capacity must be <0");
        }
        this.buckets = new LinkedList[capacity];
    }

    public MyHashMap(){
        this(16);
    }

    private int getBucketIndex(K key){
        if(key==null) return 0;
        return (key.hashCode() & 0x7fffffff)% buckets.length;
    }

    public void put(K key, V val){
        int index=getBucketIndex(key);
        if(buckets[index]==null){
            buckets[index]= new LinkedList<>();
        }
        for(Entry<K,V> entry: buckets[index]){
                if(entry.key==null ? key==null : entry.key.equals(key)){
                    entry.val=val;
                    return;
                }
        }
        //insert new
        buckets[index].add(new Entry<>(key, val));
        size++;
        //check for resize
        if(size>buckets.length*LOAD_FACTOR){
            resize();
        }

    }
    public V get(K key){
        int index=getBucketIndex(key);
        if(buckets[index]==null) return null;
        for(Entry<K,V>  entry: buckets[index]){
            if(entry.key==null ? key==null : entry.key.equals(key)){
                return entry.val;
            }
        }
        return null;
    }
    public void remove(K key){
        int index=getBucketIndex(key);
        if(buckets[index]==null) return;
        Iterator<Entry<K,V>> it= buckets[index].iterator();
        while(it.hasNext()){
            Entry<K,V> entry = it.next();
            if(entry.key ==null ? key==null : entry.key.equals(key)){
                it.remove();
                size--;
                return;
            }
        }
    }

    public int size(){
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        LinkedList<Entry<K,V>>[] oldBuckets= buckets;
        buckets= new LinkedList[oldBuckets.length*2];
        size=0;//Important : re-add everything

        for(LinkedList<Entry<K,V>> bucket:oldBuckets){
            if(bucket!=null){
                for(Entry<K,V> entry:bucket){
                    put(entry.key, entry.val);//rehash
                }
            }
        }

    }

    /* Entry Class as private static for Better encapsulation.Cleaner design.More professional.*/
    private static class Entry<K,V>{
        K key;
        V val;
        public Entry(K key, V val){
            this.key=key;
            this.val=val;
        }
    }
}
