import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * @author zhangwt
 * @date 2017/7/21 22:47.
 */
public class RedisTransactionTest {

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        Transaction tx = jedis.multi();
        tx.set("t1","1");
        tx.set("t2","2");
        tx.exec();
        System.out.println(jedis.get("t1"));
    }
}
