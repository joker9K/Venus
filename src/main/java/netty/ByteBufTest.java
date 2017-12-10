package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * Created by zhangwt n 2017/12/10.
 */
public class ByteBufTest {

    public static void readAndWrite1(){
        ByteBuf buffer = Unpooled.copiedBuffer("do one's best and leave the rest to Heaven".getBytes());
        while (buffer.isReadable()){
            System.out.print((char) buffer.readByte());
            if(buffer.readerIndex() == 20){
                break;
            }
        }
        System.out.println();
        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());
        buffer.discardReadBytes();
        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());
        while (buffer.isReadable()){
            System.out.print((char) buffer.readByte());
        }
    }

    //派生缓冲区，创建的ByteBuf是共享的(即创建一个引用)
    public static void readAndWrite2(){
        Charset utf8 = Charset.forName("utf-8");
        ByteBuf buffer = Unpooled.copiedBuffer("do one's best and leave the rest to Heaven",utf8);
        ByteBuf sliced = buffer.slice(0,15);
        System.out.println(sliced.toString(utf8));
        sliced.setByte(0,(byte)'J');
        System.out.println(buffer.toString(utf8));
    }

    //复制ByteBuf,拷贝一份备份
    public static void readAndWrite3(){
        Charset utf8 = Charset.forName("utf-8");
        ByteBuf buffer = Unpooled.copiedBuffer("do one's best and leave the rest to Heaven",utf8);
        ByteBuf sliced = buffer.copy(0,15);
        System.out.println(sliced.toString(utf8));
        sliced.setByte(0,(byte)'J');
        System.out.println(buffer.toString(utf8));
    }


    public static void main(String[] args) {
        readAndWrite3();
    }
}
