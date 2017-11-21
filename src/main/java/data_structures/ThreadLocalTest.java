package data_structures;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangwt n 2017/11/17.
 * 实现ThreadLocal
 */
public class ThreadLocalTest<T> {

    private Map<Thread,T> container = Collections.synchronizedMap(new HashMap<Thread, T>());

    public void set(T value){
        container.put(Thread.currentThread(),value);
    }

    public T get(){
        Thread thread = Thread.currentThread();
        T value = container.get(thread);
        if(null == value && !container.containsKey(thread)){
            value = initialValue();
            container.put(thread,value);
        }
        return value;
    }

    public void remove(){
        container.remove(Thread.currentThread());
    }

    protected T initialValue(){
        return null;
    }




}
