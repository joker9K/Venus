package data_structures;

/**
 * @author zhangwt
 * @date 2017/9/18 21:46.
 * AVL树是其每个节点的左子树和右子树的高度最多差1的二叉查找树
 * 1.对α的左儿子的左子树进行一次插入。LL->单旋转
 * 2.对α的左儿子的右子树进行一次插入。LR->双旋转(先经过RR旋转,再经过LL旋转)
 * 3.对α的右儿子的左子树进行一次插入。RL->双旋转(先经过LL旋转,再经过RR旋转)
 * 4.对α的右儿子的右子树进行一次插入。RR->单旋转
 * 平衡因子 = 右子树的高度 - 左子树的高度
 */
public class AVLTreeTest<T extends Comparable<? super T>> {
    private static  class AVLNode<T>{
        public AVLNode(T theElement){
            this(theElement,null,null);
        }

        public AVLNode(T element, AVLNode<T> left, AVLNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.height = 0;
        }

        T element;
        AVLNode<T> left;
        AVLNode<T> right;
        int height;//节点高度
    }

    private AVLNode<T> root;

    private int height(AVLNode<T> t){
        if(t == null){
            return -1;
        }else {
            return t.height;
        }
    }




    private AVLNode<T> findMin(AVLNode<T> t){
        if(t == null){
            return null;
        }else if(t.left == null){
            return t;
        }else {
            return findMin(t.left);
        }
    }

    public  void remove(T x){
        root = remove(x,root);
    }

    private AVLNode<T> remove(T x, AVLNode<T> t){
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
        return balance(t);
    }


    public void insert(T x){
        root = insert(x,root);
    }

    private AVLNode<T> insert(T x,AVLNode<T> t){
        if(t== null){
            return new AVLNode<T>(x);
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = insert(x,t.left);
        }else if(compareResult > 0){
            t.right = insert(x,t.right);
        }else {

        }
        return balance(t);
    }

    private static  final int ALLOWED_IMBALANCE = 1;

    private AVLNode<T> balance(AVLNode<T> t){
        if(t == null){
            return t;
        }
        if(height(t.left)-height(t.right) > ALLOWED_IMBALANCE){
            if(height(t.left.left) >= height(t.left.right)){
                t = rotateWithLeftChild(t);
            }else {
                t = doubleWithLeftRightChild(t);
            }
        }else if(height(t.right)-height(t.left) > ALLOWED_IMBALANCE){
            if(height(t.right.right) >= height(t.right.left)){
                t = rotateWithRightChild(t);
            }else {
                t = doubleWithRightLeftChild(t);
            }
        }
        t.height = Math.max(height(t.left),height(t.right))+1;
        return t;
    }


    //LL
    private AVLNode<T> rotateWithLeftChild(AVLNode<T> k2){
        AVLNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left),height(k2.right))+1;
        k1.height = Math.max(height(k1.left),height(k2))+1;
        return k1;
    }

    //RR
    private AVLNode<T> rotateWithRightChild(AVLNode<T> k2){
        AVLNode<T> k1 = k2.right;
        k2.right = k1.left;
        k1.left = k2;
        k2.height = Math.max(height(k2.left),height(k2.right))+1;
        k1.height = Math.max(height(k1.right),height(k2))+1;
        return k1;
    }


    //LR
    private AVLNode<T> doubleWithLeftRightChild(AVLNode<T> k3){
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    //RL
    private AVLNode<T> doubleWithRightLeftChild(AVLNode<T> k3){
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }
}
