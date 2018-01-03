package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * Created by zhangwt n 2018/1/2.
 */
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress inetSocketAddress, File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST,true).handler(new LogEventEncoder(inetSocketAddress));
        this.file = file;
    }

    public void run()throws Exception{
        Channel ch = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        while(true){
            long len = file.length();
            if(len < pointer){
                pointer = len;
            }else if(len > pointer){
                RandomAccessFile raf = new RandomAccessFile(file,"r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null){
                    ch.writeAndFlush(new LogEvent(null,-1,file.getAbsolutePath(),line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args)throws Exception {
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("127.0.0.1",8888),new File("/private/tmp/alipayfile/4/jiaofei_bill_water_4010785.json"));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }

    }
}
