import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author zhangwt
 * @date 2017/7/22 15:46.
 */
public class RedisPubAndSubTest {


    public static void main(String[] args) {
        Jedis jedis1 = RedisPool.getJedis();
        Jedis jedis2 = RedisPool.getJedis();
        MyListener listener = new MyListener();
        Publisher pub = new Publisher();
        pub.publish(jedis2);

        Subscriber sub = new Subscriber();
        sub.psub(jedis1,listener);
    }
}

class MyListener extends JedisPubSub{

    // 取得订阅的消息后的处理
    @Override
    public void onMessage(String channel, String message) {
        System.out.println(channel+"="+message);
    }

    // 取得按表达式的方式订阅的消息后的处理
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(pattern + "=" + channel + "=" + message);
    }

    // 初始化订阅时候的处理
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(channel + "=" + subscribedChannels+"#########");
    }

    // 取消订阅时候的处理
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(channel + "=" + subscribedChannels+"#########");
    }

    // 取消按表达式的方式订阅时候的处理
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println(pattern + "=" + subscribedChannels+"@@@@@@@@@");
    }

    // 初始化按表达式的方式订阅时候的处理
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println(pattern + "=" + subscribedChannels+"!!!!!!!");
    }
}


class Publisher {

    public void publish(final Jedis redisClient) {

        new Thread(() -> {
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("发布：news.share");
            redisClient.publish("news.share", "ok");
            redisClient.publish("news.share", "hello word");
        }).start();
    }
}


class Subscriber{
    public void psub(final Jedis redisClient, final MyListener listener) {

        new Thread(() -> {
            System.out.println("订阅：news.share");
            // 订阅得到信息在lister的onMessage(...)方法中进行处理
            // 订阅多个频道
            // redisClient.subscribe(listener, "news.share", "news.log");
            //redisClient.subscribe(listener, new String[]{"news.share","news.log"});
            redisClient.psubscribe(listener, "news.share");// 使用模式匹配的方式设置频道
        }).start();
    }
}