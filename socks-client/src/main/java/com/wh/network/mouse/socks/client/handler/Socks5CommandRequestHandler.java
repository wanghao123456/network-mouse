package com.wh.network.mouse.socks.client.handler;

import com.wh.network.mouse.handler.ClientToRemoteListener;
import com.wh.network.mouse.handler.RemoteToClientHandler;
import com.wh.network.mouse.socks.client.config.ClientConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import io.netty.handler.proxy.Socks5ProxyHandler;

import java.net.InetSocketAddress;

public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {

    private EventLoopGroup eventLoopGroup;
    private Class channelClass;
    private ClientConfig clientConfig;

    public Socks5CommandRequestHandler(EventLoopGroup eventLoopGroup, Class<? extends Channel> channelClass, ClientConfig clientConfig) {
        this.eventLoopGroup = eventLoopGroup;
        this.channelClass = channelClass;
        this.clientConfig = clientConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5CommandRequest msg) throws Exception {
        if (Socks5CommandType.CONNECT.equals(msg.type())) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(channelClass)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientConfig.getConnectTimeout() * 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new Socks5ProxyHandler(new InetSocketAddress(clientConfig.getRemoteHost(), clientConfig.getRemotePort()),
                                            clientConfig.getUserName(), clientConfig.getPassWord()),
                                    new RemoteToClientHandler(ctx));
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(msg.dstAddr(), msg.dstPort());
            channelFuture.addListener(new ClientToRemoteListener(ctx, msg));
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
