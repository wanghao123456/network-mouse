package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.socks.server.config.ServerConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelInitializerHandler extends ChannelInitializer {

    private ServerConfig serverConfig;

    private EventLoopGroup proxy;

    private SslContext sslContext;

    public ServerChannelInitializerHandler(ServerConfig serverConfig, EventLoopGroup proxy, SslContext sslContext) {
        this.serverConfig = serverConfig;
        this.proxy = proxy;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (serverConfig.isSsl()) {
            ch.pipeline().addFirst(sslContext.newHandler(ch.alloc()));
        }
        ch.pipeline().addLast(
                new IdleStateHandler(serverConfig.getReaderIdleTime(), serverConfig.getWriterIdleTime(), serverConfig.getAllIdleTime()),
                new ProtocolRecognitionDecoder(serverConfig, proxy));
    }
}
