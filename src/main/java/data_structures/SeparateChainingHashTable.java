package data_structures;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangwt
 * @date 2017/10/16 12:44.
 * 链表法解决hash冲突
 */
public class SeparateChainingHashTable<T> {

    private static final int DEFAULT_TABLE_SIZE = 101;
    private List<T>[] theLists;
    private int currentSize;

    public SeparateChainingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    @SuppressWarnings("unchecked")
    public SeparateChainingHashTable(int size) {
        theLists = new LinkedList[nextPrime(size)];
        for (int i = 0; i < theLists.length; i++) {
            theLists[i] = new LinkedList<T>();
        }
    }

    private int myhash(T x) {
        int hashVal = x.hashCode();
        hashVal %= theLists.length;
        if (hashVal < 0) {
            hashVal += theLists.length;
        }
        return hashVal;
    }

    public void insert(T x) {
        List<T> whichList = theLists[myhash(x)];
        if (!whichList.contains(x)) {
            whichList.add(x);
            //rehash
            if (++currentSize > theLists.length) {
                rehash();
            }
        }
    }

    public void remove(T x) {
        List<T> whichList = theLists[myhash(x)];
        if (whichList.contains(x)) {
            whichList.remove(x);
            currentSize--;
        }
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        List<T>[] oldArray = theLists;
        //create new double-sized,empty table
        theLists = new List[nextPrime(2 * theLists.length + 1)];
        for (int j = 0; j < theLists.length; j++) {
            theLists[j] = new LinkedList<>();
        }

        //copy table over
        currentSize = 0;
        for (int i = 0; i < oldArray.length; i++) {
            for (T item : oldArray[i]) {
                insert(item);
            }
        }
    }


    private static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }else {
            n+=2;
        }
        for (; ; n += 2) {
            boolean isPrime = true;
            for (int i = 3; i * i <= n; i+=2) {
                if (n % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                return n;
            }
        }
    }


    public static void main(String[] args) {
        nextPrime(2);
    }
}

