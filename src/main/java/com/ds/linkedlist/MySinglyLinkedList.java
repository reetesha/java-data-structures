
public class MySinglyLinkedList<T> {
    Node head;
    Node tail;
    MySinglyLinkedList(){
        this.head= null;
        this.tail= null;
    }

    void addFirst(T val){
        Node newNode = new Node(val);
        newNode.next=head;
        head = newNode;
    }

    void addLast(T val){
        Node newNode = new Node(val);
        if(head==null){
            head=tail=newNode;
        } else{
            tail.next=newNode;
            tail=newNode;
        }

    }

    void removeFirst(){
        head=head.next;
    }
/* 🧠 Important Insight. Even with tail pointer: removeLast = O(n)
Because you must find second-last node. Singly LinkedList limitation. To make removeLast O(1), you need:
👉 Doubly LinkedList
🎯 Mental Model
To remove last in singly list: Find node whose next is last.
🔥 Interview Insight
If interviewer asks:Why is removeLast O(n) in singly LinkedList even with tail pointer?
Answer:
Because we need previous node to update its next to null, and singly list doesn’t store backward links.
*/

    void removeLast(){
        if(head==null){
            return;
        }
        if(head.next==null){
            head=tail=null;
            return;
        }
        Node temp=head;
        while(temp.next.next!=null){
            temp=temp.next;
        }
        temp.next=null;
        tail=temp;
    }

    int size(){
        if(head==null){
            return 0;
        }
        int size=0;
        Node temp=head;
        while(temp!=null){
            temp=temp.next;
            size++;
        }
        return size;
    }

    @Override
    public String toString(){
        StringBuffer sb= new StringBuffer("[");
        Node temp=head;
        while(temp!=null){
            sb.append(temp.val);
            if(temp.next!=null){
                sb.append(temp.val+", ");
            }
            temp=temp.next;
        }
        sb.append("]");
        return sb.toString();

    }
    void revers(){

    }
}

class Node<T>{
    T val;
    Node next;
    Node(T val){
        this.val=val;
    }
}
