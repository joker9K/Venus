package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by zhangwt n 2017/12/28.
 */
public class EntityToByteEncoder extends MessageToByteEncoder<MessageEntity> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageEntity messageEntity, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(messageEntity.getMessage().getBytes());
    }
}
