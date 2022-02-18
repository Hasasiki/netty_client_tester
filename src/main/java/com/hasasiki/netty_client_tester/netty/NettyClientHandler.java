package com.hasasiki.netty_client_tester.netty;

import com.hasasiki.netty_client_tester.utils.ByteArraySearch;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CLIENT_MAP = new ConcurrentHashMap<>();

    private static final byte[] pattern = new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80};
    private static int count = 0;
    public static byte[] incomingData;
    //todo add the binary file path
    public static final String path = "";

    static {
        try {
            incomingData = readFromByteFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int[] location = ByteArraySearch.Locate(incomingData, pattern);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CLIENT_MAP.put(ctx.channel().id(), ctx);
        log.info("client{} handler active!", ctx.channel().id());
        ByteBuf bf = Unpooled.wrappedBuffer(incomingData);

        InputStream is = Files.newInputStream(Path.of(""));
        ctx.channel().eventLoop().scheduleAtFixedRate(() -> {

            int length = location[count + 1] - location[count];
            ByteBuf calcBuf = bf.readRetainedSlice(length);
            log.debug("count{} buf size{} ", count, calcBuf.readableBytes());
            ctx.writeAndFlush(calcBuf);
            count++;

//            try {
//                byte[] bytes = is.readNBytes(1024);
//
//                if (bytes.length > 0) {
//
//                } else {
//                    is.reset();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

//    public ByteBuf getData() {
//        try {
//            if (count < 8190) {
//                int[] location = ByteArraySearch.Locate(incomingData, pattern);
//                ByteBuf bf = Unpooled.wrappedBuffer(incomingData);
//                int length = location[count + 1] - location[count];
//                ByteBuf calcBuf = bf.readRetainedSlice(length);
//                log.debug("count{} buf size{} ", count, calcBuf.readableBytes());
//                count++;
//                return calcBuf;
//            } else {
//                log.error("end");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    public static byte[] readFromByteFile(String pathname) throws IOException {
        InputStream is = new FileInputStream(pathname);
        int iAvail = is.available();
        byte[] bytes = new byte[iAvail];
        is.read(bytes);
        is.close();
        return bytes;
    }

}
