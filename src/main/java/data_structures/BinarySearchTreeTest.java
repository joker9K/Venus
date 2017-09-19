package data_structures;

import java.util.EmptyStackException;

/**
 * @author zhangwt
 * @date 2017/9/11 21:39.
 */
public class BinarySearchTreeTest<T extends Comparable<? super T>> {
    private static  class BinaryNode<T>{

        public BinaryNode(T element, BinaryNode<T> left, BinaryNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }

        T element;
        BinaryNode<T> left;
        BinaryNode<T> right;
    }


    private BinaryNode<T> root;

    public BinarySearchTreeTest(){
        root = null;
    }

    public void makeEmpty(){
        root = null;
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean contains(T x){
        return contains(x,root);
    }

    //递归查找是否存在该节点
    private boolean contains(T x,BinaryNode<T> t){
        if(t == null){
            return false;
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            return contains(x,t.left);
        }else if(compareResult > 0){
            return contains(x,t.right);
        }else {
            return true;
        }
    }

    public T findMin(){
        if(isEmpty()){
            throw new UnderFlowException("empty tree");
        }
        return findMin(root).element;
    }


    private BinaryNode<T> findMin(BinaryNode<T> t){
        if(t == null){
            return null;
        }else if(t.left == null){
            return t;
        }else {
            return findMin(t.left);
        }
    }

    public T findMax(){
        if(isEmpty()){
            throw new UnderFlowException("empty tree");
        }
        return findMax(root).element;
    }

    private BinaryNode<T> findMax(BinaryNode<T> t){
        if(t == null){
            return null;
        }else if(t.right == null){
            return t;
        }else {
            return findMin(t.right);
        }
    }

    public void insert(T x){
        root = insert(x,root);
    }


    private BinaryNode<T> insert(T x,BinaryNode<T> t){
        if(t == null){
            return new BinaryNode<>(x,null,null);
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = insert(x,t.left);
        }else if(compareResult > 0){
            t.right = insert(x,t.right);
        }else {

        }
        return t;
    }

    public  void remove(T x){
        root = remove(x,root);
    }

    private BinaryNode<T> remove(T x,BinaryNode<T> t){
        if(t == null){
            return t;
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = remove(x,t.left);
        }else if(compareResult > 0){
            t.right = remove(x,t.right);
        }else if(t.left != null && t.right != null){
            t.element = findMin(t.right).element;
            t.right = remove(t.element,t.right);
        }else {
            t = (t.left != null)?t.left:t.right;
        }
        return t;
    }

    public void printTree(){
        if(isEmpty()){
            System.out.println("Empty tree");
        }else {
            printTree(root);
        }
    }

    private void printTree(BinaryNode<T> t){
        if(t != null){
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

}
class UnderFlowException extends RuntimeException{

    public UnderFlowException(String message) {
        super(message);
    }
}

