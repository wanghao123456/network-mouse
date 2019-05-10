package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.socks.server.config.ServerConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
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
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RemoteToClientHandler(ctx));
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(msg.dstAddr(), msg.dstPort());
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Socks5CommandResponse commandResponse;
                    if (future.isSuccess()) {
                        ctx.pipeline().addLast(new ClientToRemoteHandler(future));
                        commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, Socks5AddressType.IPv4);
                    } else {
                        commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4);
                    }
                    ctx.writeAndFlush(commandResponse);
                }
            });
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
