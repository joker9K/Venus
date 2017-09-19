package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author zhangwt
 * @date 2017/9/7 19:35.
 * 实现linkedList
 */
public class MyLinkedList<T> implements Iterable<T>{

    private int theSize;
    private int modCount = 0;//记录修改的次数
    private Node<T> beginMarker;
    private Node<T> endMarker;

    private static class Node<T>{
        public T data;
        public Node<T> pre;
        public Node<T> next;

        public Node(T data, Node<T> pre, Node<T> next) {
            this.data = data;
            this.pre = pre;
            this.next = next;
        }
    }

    public MyLinkedList() {
        clear();
    }

    public void clear(){
        doClear();
    }

    public void doClear(){
        beginMarker = new Node<>(null,null,null);
        endMarker = new Node<>(null,null,null);
        beginMarker.next = endMarker;
        theSize = 0;
        modCount = 0;
    }

    public int size(){
        return theSize;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public boolean add(T x){
        add(size(),x);
        return true;
    }

    public Node<T> getNode(int idx){
        return getNode(idx,0,size()-1);
    }

    private Node<T> getNode(int idx,int lower,int upper){
        Node<T> p;
        if(idx< lower || idx> upper){
            throw  new IndexOutOfBoundsException();
        }
        if(idx < size()/2){
            p = beginMarker.next;
            for(int i=0;i<idx;i++){
                p =p.next;
            }
        }else {
            p = endMarker;
            for(int i=size();i>idx;i--){
                p = p.pre;
            }
        }
        return p;
    }

    private void addBefore(Node<T> p,T x){
        Node<T> newNode = new Node<>(x,p.pre,p);
        newNode.pre.next = newNode;
        p.pre = newNode;
        theSize++;
        modCount++;
    }

    public void add(int idx,T x){
        addBefore(getNode(idx,0,size()),x);
    }

    public T get(int idx){
        return getNode(idx).data;
    }

    public T set(int idx,T newVal){
        Node<T> p = getNode(idx);
        T oldVal = p.data;
        p.data = newVal;
        return oldVal;
    }

    public T remove(int idx){
        return remove(getNode(idx));
    }

    public T remove(Node<T> p){
        p.next.pre = p.pre;
        p.pre.next = p.next;
        theSize--;
        modCount++;
        return p.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T>{
        private Node<T> current = beginMarker.next;
        private int expectModCount = modCount;
        private boolean okToRemove = false;

        @Override
        public void remove() {
            if(modCount != expectModCount){
                throw new ConcurrentModificationException();
            }
            if(!okToRemove){
                throw new IllegalStateException();
            }
            MyLinkedList.this.remove(current.pre);
            expectModCount++;
            okToRemove=false;
        }

        @Override
        public T next() {

            if(modCount != expectModCount){
                throw new ConcurrentModificationException();
            }
            if(!hasNext()){
                throw  new NoSuchElementException();
            }
            T nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }

        @Override
        public boolean hasNext() {
            return current != endMarker;
        }
    }
}
