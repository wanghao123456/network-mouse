package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.socks.server.config.ServerConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ChannelInitializerHandler extends ChannelInitializer {

    private ServerConfig serverConfig;

    private EventLoopGroup proxy;

    public ChannelInitializerHandler(ServerConfig serverConfig, EventLoopGroup proxy) {
        this.serverConfig = serverConfig;
        this.proxy = proxy;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                new IdleStateHandler(serverConfig.getReaderIdleTime(), serverConfig.getWriterIdleTime(), serverConfig.getAllIdleTime()),
                Socks5ServerEncoder.DEFAULT,
                new Socks5InitialRequestDecoder(),
                new Socks5InitialRequestHandler(),
                new Socks5PasswordAuthRequestDecoder(),
                new Socks5PasswordAuthRequestHandler(),
                new Socks5CommandRequestDecoder(),
                new Socks5CommandRequestHandler(proxy, NioSocketChannel.class, serverConfig)
        );
    }
}
