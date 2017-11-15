package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhangwt n 2017/11/10.
 */
@Slf4j
public class TimeClientHandler2 extends ChannelInboundHandlerAdapter{
    private int counter;
    private byte[] req;

    public TimeClientHandler2() {
        req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    /**
     * 连接服务时被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message;
        for(int i=0;i<100;i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    /**
     * 接受到服务端数据时被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //拿到的msg已经是解码成字符串的应答消息了
        String body = (String)msg;
        System.out.println("NOW is :"+body+" ; the counter is :"+ ++counter);
    }


    /**
     * 捕获到异常时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        log.warn("Unexpected exception from downstream : "+cause.getMessage());
        ctx.close();
    }
}
