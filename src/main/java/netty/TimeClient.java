package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangwt n 2017/11/10.
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        int port = 9090;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值
            }
        }
        new TimeClient().connect(port, "127.0.0.1");
    }


    public void connect(int port, String host) throws Exception {
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            //设置用于处理Channel所有事件的EventLoopGroup
            b.group(group)
                    //channel()方法指定了Channel的实现类。
                    .channel(NioSocketChannel.class)
                    //设置ChannelOption，其将被应用到每个新创建的Channel的ChannelConfig。
                    .option(ChannelOption.TCP_NODELAY, true)
                    //设置将被添加到ChannelPipeline以接收事件通知的ChannelHandler
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new TimeClientHandler());//正常发送数据

                            //ChannelOutboundHandler，逆序执行
                            ch.pipeline().addLast(new EntityToByteEncoder());
                            ch.pipeline().addLast(new StringToEntityEncoder());

                            //ChannelIntboundHandler，顺序执行
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));//直接在TimeClientHandler2之前新增LineBasedFrameDecoder和StringDecoder解码器
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler2());//模拟TCP粘包

                            //发送心跳(IdleStateHandler:如果10秒之内没有接收到或者发送任何的数据，则关闭连接),ReadTimeoutHandler是指间隔时间内没有收到任何的入站数据
                            //WriteTimeoutHandler是间隔时间内没有任何的出站数据
                            ch.pipeline().addLast(new IdleStateHandler(0, 0, 10, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new HeartbeatHandler());

                        }
                    });

            //发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
