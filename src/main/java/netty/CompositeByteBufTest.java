package netty;

import io.netty.buffer.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

/**
 * Created by zhangwt n 2017/12/10.
 */
public class CompositeByteBufTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        CompositeByteBuf message = Unpooled.compositeBuffer();
        ByteBuf headBuf = Unpooled.copiedBuffer("time:".getBytes());
        ByteBuf bodyBuf = Unpooled.copiedBuffer(LocalDate.now().toString().getBytes());
        message.addComponents(headBuf,bodyBuf);
        System.out.println(headBuf.hasArray());
        for (ByteBuf buf:message){
            System.out.println(buf.toString());
        }
        message.removeComponent(0);
        System.out.println("------------");
        for(ByteBuf buf : message){
            System.out.println(buf.toString());
        }
    }
}
