package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zhangwt n 2017/11/10.
 * netty服务端程序
 */
public class TimeServer {
    public static void main(String[] args) throws Exception {
        int port = 9090;
        if(args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值9090端口
            }
        }
        new TimeServer().bind(port);
    }

    public void bind(int port)throws Exception{
        /**
         * 配置服务端的NIO线程组,它包含了一组NIO线程组,专门用于网络事件的处理,实际上它们就是reactor线程组。
         * 这里创建两个的原因:一个用于服务端接受客户端的连接,
         * 另一个用于进行SocketChannel的网络读写
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //ServerBootstrap对象,netty用于启动NIO服务端的辅助启动类,目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    /**
                     * 绑定I/O事件的处理类ChildChannelHandler,它的作用类似于Reactor模式中的handler类,
                     * 主要用于处理网络I/O事件,例如:记录日志、对消息进行编解码等。
                     */
                    .childHandler(new ChildChannelHandler());
            /**
             * 绑定端口,同步等待成功(调用它的bind方法监听端口,随后,调用它的同步阻塞sync等待绑定操作完成。
             * 完成之后netty会返回一个ChannelFuture,它的功能类似于JDK的java.util.concurrent.Future,
             * 主要用于异步操作的通知回调)
             */
            ChannelFuture f = b.bind(port).sync();

            //等待服务端监听端口关闭(f.channel().closeFuture().sync()方法进行阻塞,等待服务端链路关闭之后main函数才推出。)
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            //arg0.pipeline().addLast(new TimeServerHandler());//正常模拟netty通信

            //处理TCP粘包的现象
            arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//LineBasedFrameDecoder是以换行符为界限的解码器,1024为最大长度。如果连续读取到最大长度后没有发现换行符，就会抛出异常，同时忽略掉之前读到的异常码流。
            arg0.pipeline().addLast(new StringDecoder());//解码

            arg0.pipeline().addLast(new TimeServerHandler1());
        }
    }

}
