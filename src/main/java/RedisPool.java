import redis.clients.jedis.Jedis;

/**
 * @author zhangwt
 * @date 2017/7/13 22:28.
 */
public class RedisPool {

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
    }
}
