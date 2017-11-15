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

/**
 * Created by zhangwt n 2017/11/10.
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        int port = 9090;
        if(args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值
            }
        }
        new TimeClient().connect(port, "127.0.0.1");
    }


    public void connect(int port,String host) throws Exception{
        //配置客户端NIO线程组
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new TimeClientHandler());//正常发送数据

                            //直接在TimeClientHandler之前新增LineBasedFrameDecoder和StringDecoder解码器
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler2());//模拟TCP粘包
                        }
                    });

            //发起异步连接操作
            ChannelFuture f=b.connect(host, port).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally{
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
