import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<T> implements Iterable<T>{

    private int capacity;
    private T[] elements;
    private int size=0;
    private volatile int modCount;
    private final Object lock= new Object();

    @SuppressWarnings("unchecked")
    public MyArrayList(int capacity){
        if(capacity<=0){
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity=capacity;
        this.elements =(T[])new Object [capacity];
    }
    public MyArrayList(){
        this(100);
    }

    public void add(T val){
        synchronized (lock){
            if(size==capacity){
                resize();
            }
            elements[size++]=val;
            modCount++;
        }
    }

    public T get(int i){
        synchronized (lock){
            if(i<0 || i>=size){
                throw new IndexOutOfBoundsException();
            }
            return elements[i];
        }
    }

    public void set(int i, T val){
        synchronized (lock){
            if(i<0 || i>=size){
                throw new IndexOutOfBoundsException();
            }
            elements[i]=val;
        }
    }

    public int size(){
        synchronized (lock){
            return size;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        capacity= capacity*2;
        T[] newArrayList =(T[]) new Object [capacity];
        System.arraycopy(elements, 0, newArrayList,0, size);
        elements = newArrayList;
    }

    public void remove(int index){
        synchronized (lock){
            if(index<0|| index>=size){
                throw new IndexOutOfBoundsException();
            }
            for(int i=index;i<size-1;i++){
                elements[i] = elements[i+1];
            }

            elements[--size]=null;//help GC
            modCount++;
        }
    }

    public void insert(int index, T val){
        synchronized (lock){
            if(index<0 || index>size){
                throw new IndexOutOfBoundsException();
            }
            if(size==capacity){
                resize();
            }
            for(int i=size;i>index;i--){
                elements[i] = elements[i-1];
            }
            elements[index]=val;
            size++;
            modCount++;
        }
    }

    public boolean isEmpty(){
        return size==0;
    }

    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder("[");
        for(int i=0;i<size;i++){
            sb.append(elements[i]);
            if(i < size-1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        synchronized (lock){
            return new MyIterator();
        }
    }

    private class MyIterator implements Iterator<T>{
        private int cursor=0;
        private int expectedModCount=modCount;
        int lastReturedIndex=-1;

        @Override
        public boolean hasNext() {
            checkForModification();
            return cursor<size;
        }

        @Override
        public T next() {
            synchronized (lock){
                checkForModification();
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                lastReturedIndex=cursor;
                return elements[cursor++];
            }
        }

        private void checkForModification(){
            if(expectedModCount!=modCount){
                throw new ConcurrentModificationException();
            }
        }
        @SuppressWarnings("UnCheckedf")
        public void remove(){
            checkForModification();
            if(lastReturedIndex < 0){
                throw new IllegalStateException();
            }
            MyArrayList.this.remove(lastReturedIndex);
            cursor= lastReturedIndex;//adjust cursor
            lastReturedIndex= -1;
            expectedModCount=modCount;
        }

    }

    public static void main(String args[]){
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);

        for(Integer val : list) {
            System.out.println(val);
        }
        //Add Features of Iterator
   /*     for(Integer val : list) {
            list.add(40);
        }*/
        //without Remove
        Iterator<Integer> it = list.iterator();
        while(it.hasNext()){
            Integer val= it.next();
            if(val==20){
                it.remove();
            }
        }

    }
}

/*
Official Java Docs Say: Fail-fast behavior cannot be guaranteed and should be used only to detect bugs.
Very important line.

How CopyOnWriteArrayList Solves It: Core Idea. When you modify the list:
👉 It creates a NEW copy of the underlying array
👉 Modifies the new copy
👉 Replaces the reference
Old array remains untouched.
Example -- Thread A:
Iterator it = list.iterator();  At this moment, iterator holds reference to:
Array A

Thread B:
list.add(5);
Internally:
New Array B created Elements copied 5 added
Reference updated
Thread A is still iterating over:
Array A (unchanged)
So:
No ConcurrentModificationException
No corruption
No need for modCount
Completely safe iteration
🔥 Why It Is Thread-Safe
Because: Array reference is volatile. Writes are synchronized internally. Readers see immutable snapshot
No shared mutation.
🔥 When To Use CopyOnWriteArrayList
Use when:
Many reads
Very few writes
Example: listeners list, configuration list
Never use when:
Frequent writes
Large collections
🎯 Simple Mental Model
Fail-fast:
"If you touch it while I iterate, I crash."
CopyOnWrite:
"You modify your copy. I keep reading mine."
🔥 Interview Gold Line
Fail-fast is:
A debugging mechanism, not a concurrency control mechanism.
CopyOnWrite is:
A snapshot-based concurrency strategy.
 */