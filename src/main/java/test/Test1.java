package test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangwt
 * @date 2017/7/26 17:23.
 */

class User{
    boolean age;

    public boolean isAge() {
        return age;
    }

    public void setAge(boolean age) {
        this.age = age;
    }
}
public class Test1 {
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        putData(map);
        System.out.println(map.get("1"));
    }

    public static void putData(Map<String,Object> map){
        map.put("1",1);
        map.put("3",2);
    }
}
