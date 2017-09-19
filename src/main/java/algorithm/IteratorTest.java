package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhangwt
 * @date 2017/9/6 21:31.
 */
public class IteratorTest {

    //illegal
    public static void test1(List<String> list){
        //对正在迭代的集合进行结构上的改变,就会抛出ConcurrentModificationException异常
        for(String string:list){
            if ("2".equals(string)) {
                list.remove(string);
            }
        }
        list.forEach(System.out::println);
    }

    //legal
    public static void test2(List<String> list){
        Iterator<String> iterator = list.iterator();
        //使用迭代器进行remove是合法的
        while (iterator.hasNext()){
            if("2".equals(iterator.next())){
                iterator.remove();
            }
        }
        list.forEach(System.out::println);
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("2","e","r","4"));
        test1(list);//illegal
        test2(list);//legal
//        List<Integer> list = new ArrayList<>();
//        for(int i=0;1<1000;i++){
//            list.add(i);
//        }
    }
}
