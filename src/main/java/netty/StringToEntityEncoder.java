package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by zhangwt n 2017/12/28.
 * 自定义编码器
 */
public class StringToEntityEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        MessageEntity entity = new MessageEntity();
        entity.setMessage(s);
        list.add(entity);
    }
}
