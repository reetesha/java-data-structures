package cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LRU<K,V> {

    private int capacity;

    private Node<K,V> head;
    private Node<K,V> tail;

    private Map<K,Node<K,V>> map= new HashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    LRU(int capacity){
        if(capacity<=0){
            throw new IllegalArgumentException("Capacity must be >0");
        }
        this.capacity=capacity;
        this.head = new Node(null, null);
        this.tail = new Node(null, null);
        this.head.next=tail;
        this.tail.prev=head;
    }

    public void put(K key, V val){
        lock.lock();
        try{
            Node<K,V> node=map.get(key);
            if(node!=null){
                node.val=val;
                moveToHead(node);
            }else{
                Node<K,V> newNode = new Node(key, val);
                map.put(key,newNode);
                addToHead(newNode);
                if(map.size()>capacity){
                    Node<K,V> lru=tail.prev;
                    remove(lru);
                    map.remove(lru.key);
                }
            }
        }
        finally {
            lock.unlock();
        }
    }
    public V get(K key){
        lock.lock();
        try{
            Node<K,V> node =map.get(key);
            if(node!=null){
                moveToHead(node);
                return node.val;
            }else{
                return null;
            }
        }
        finally {
            lock.unlock();
        }
    }

    private void remove(Node<K,V> node){
        node.prev.next=node.next;
        node.next.prev=node.prev;
    }

    private void addToHead(Node<K,V> node){
        node.next=head.next;
        node.prev=head;
        head.next.prev=node;
        head.next=node;
    }

    private void moveToHead(Node<K,V> node){
        remove(node);
        addToHead(node);
    }

    static class Node<K,V>{
        K key;
        V val;
        Node<K,V> prev, next;
        Node(K key,V val){
            this.key=key;
            this.val=val;
        }
    }
/*    public static void main(String args[]){

    }*/
}
