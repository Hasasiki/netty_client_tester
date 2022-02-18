package com.hasasiki.netty_client_tester.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyClient {
    //todo change ip
    static final String HOST = System.getProperty("host", "ip");
    static final int PORT = Integer.parseInt(System.getProperty("port", "4014"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));



    public static void main(String[] args) {
        sendMessage();
    }

    private static void sendMessage() {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //incomingData = readFromByteFile("/Users/hasasikis/B0000006.ens");
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new NettyClientHandler());
                        }
                    });
            bootstrap.bind(8091).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
