import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zhangwt
 * @date 2017/7/21 22:00.
 */
public class RedisPool {

    private static JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(100);//jedis的最大分配对象
        config.setMaxIdle(100);//jedis最大保存idel状态对象数
        config.setMaxWait(1000);//jedis池没有对象返回时，最大等待时间
        pool = new JedisPool(config,"127.0.0.1", 6379);
    }

    public static Jedis getJedis(){
        Jedis jedis = pool.getResource();
        jedis.auth("admin");
        return jedis;
    }

    public static void recycleJedis(Jedis jedis){
        pool.returnResource(jedis);
    }
}
