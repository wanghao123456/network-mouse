package com.wh.network.mouse.socks.client.handler;

import com.wh.network.mouse.handler.CloseEventHandler;
import com.wh.network.mouse.socks.client.config.ClientConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;

public class ChannelInitializerHandler extends ChannelInitializer {

    private ClientConfig clientConfig;

    private EventLoopGroup proxy;

    public ChannelInitializerHandler(ClientConfig clientConfig, EventLoopGroup proxy) {
        this.clientConfig = clientConfig;
        this.proxy = proxy;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                Socks5ServerEncoder.DEFAULT,
                new Socks5InitialRequestDecoder(),
                new Socks5InitialRequestHandler(),
                new Socks5CommandRequestDecoder(),
                new Socks5CommandRequestHandler(proxy, NioSocketChannel.class, clientConfig),
                new CloseEventHandler()
        );
    }
}
