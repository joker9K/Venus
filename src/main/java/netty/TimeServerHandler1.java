package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created by zhangwt n 2017/11/10.
 * 用于对网络事件进行读写操作
 */
public class TimeServerHandler1 extends ChannelInboundHandlerAdapter{
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ByteBuf类似于JDK中的java.nio.ByteBuffer对象,不过它提供了更加强大和灵活的功能
        String body=(String) msg;
        System.out.println("The time server receive order : "+body+" ;the counter is : "+ ++counter);
        String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body) ? new
                Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime    =currentTime+System.getProperty("line.separator");
        ByteBuf resp=Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         * ctx.flush();将消息发送队列中的消息写入到SocketChannel中发送给对方。
         * 从性能角度考虑,为了防止频繁地唤醒Selector进行消息发送,Netty的write方法并不直接将消息写入SocketChannel中,
         * 调用write方法只是把待发送的消息发送到发送缓存组中,再通过flush方法,将发送缓存区中的消息全部写到SocketChannel中。
         */
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
