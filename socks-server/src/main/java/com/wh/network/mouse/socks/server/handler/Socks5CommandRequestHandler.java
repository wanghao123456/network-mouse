package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.handler.ClientToRemoteListener;
import com.wh.network.mouse.socks.server.config.ServerConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;

public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {

    private EventLoopGroup eventLoopGroup;
    private Class channelClass;
    private ServerConfig serverConfig;

    public Socks5CommandRequestHandler(EventLoopGroup eventLoopGroup, Class<? extends Channel> channelClass, ServerConfig serverConfig) {
        this.eventLoopGroup = eventLoopGroup;
        this.channelClass = channelClass;
        this.serverConfig = serverConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5CommandRequest msg) throws Exception {
        if (Socks5CommandType.CONNECT.equals(msg.type())) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(channelClass)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, serverConfig.getConnectTimeout() * 1000)
                    .handler(new ProxyChannelInitializerHandler(ctx));

            ChannelFuture channelFuture = bootstrap.connect(msg.dstAddr(), msg.dstPort());
            channelFuture.addListener(new ClientToRemoteListener(ctx, msg));
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
