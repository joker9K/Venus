package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by zhangwt n 2017/11/10.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter{
    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req="QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);

    }


    /**
     * 当客户端和服务端TCP链路建立成功之后,netty的NIO线程会调用channelActive方法,
     * 发送查询时间的指令给服务端
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将请求信息发送给客户端
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("NOW is :"+body);
    }

    /**
     * 发生异常时,打印异常日志,释放客户端资源
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        System.out.println("Unexpected exception from downstream : "+cause.getMessage());
        ctx.close();
    }
}
