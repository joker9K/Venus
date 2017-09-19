package redis;

import redis.RedisPool;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhangwt
 * @date 2017/7/21 21:32.
 * 本程序主要是为了测试reids并发的问题
 */
public class RedisConcurrentTest {


    public static void main(String[] args) throws InterruptedException {
        Jedis jedis1 = RedisPool.getJedis();
        System.out.println(jedis1.get("name"));
        CountDownLatch latch = new CountDownLatch(2);
        for (int i =0 ;i<2;i++) {
            new Thread(() -> {
                Jedis jedis = RedisPool.getJedis();
                for (int j = 0; j < 1000; j++) {
//                    jedis.incr("name");//原子操作是线程安全的
                    int p = Integer.valueOf(jedis.get("name"));
                    jedis.set("name",String.valueOf(p+1));
                }
                RedisPool.recycleJedis(jedis);
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println(jedis1.get("name"));
    }
}
