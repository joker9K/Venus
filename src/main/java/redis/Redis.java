package redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author zhangwt
 * @date 2017/7/13 22:28.
 */

public class Redis {

    private static Jedis jedis;

    static {
        //连接redis服务器，192.168.0.100:6379
        jedis = new Jedis("127.0.0.1", 6379);
        //权限认证
        jedis.auth("admin");
    }

    public static Jedis getInstance(){
        return jedis;
    }

    public static void main(String[] args) {
        Jedis jedis = getInstance();
        System.out.println(jedis.get("name"));
        System.out.println(jedis.hgetAll("zhangwt"));


        List<String> list = jedis.lrange("one",0,-1);
        list.forEach(System.out::println);


        Set<String> keys = jedis.keys("*");
        keys.forEach(t-> System.out.println("key:"+t+" type:"+jedis.type(t)));
        System.out.println(jedis.zrevrangeWithScores("three",0,-1));

    }
}
