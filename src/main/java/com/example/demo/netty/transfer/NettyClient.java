package com.example.demo.netty.transfer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;


public class NettyClient {
    public static void main(String[] args) {
        EventLoopGroup loopGroup=new NioEventLoopGroup();
        try{
            //客户端启动对象
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline=socketChannel.pipeline();
                            pipeline.addLast("encoder",new ProtobufEncoder());
                            //TODO
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            System.out.println("客户端已连接");
            ChannelFuture future=bootstrap.connect("127.0.0.1",9998).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            loopGroup.shutdownGracefully();
        }
    }

}
