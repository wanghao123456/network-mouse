package com.wh.network.mouse.socks.client.handler;

import com.wh.network.mouse.handler.RemoteToClientHandler;
import com.wh.network.mouse.socks.client.config.ClientConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.ssl.SslContext;

import java.net.InetSocketAddress;

public class ProxyChannelInitializerHandler extends ChannelInitializer {

    private ClientConfig clientConfig;
    private SslContext sslContext;
    private ChannelHandlerContext channelHandlerContext;

    public ProxyChannelInitializerHandler(ClientConfig clientConfig, SslContext sslContext, ChannelHandlerContext channelHandlerContext) {
        this.clientConfig = clientConfig;
        this.sslContext = sslContext;
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (clientConfig.isSsl()) {
            ch.pipeline().addFirst(sslContext.newHandler(ch.alloc()));
        }
        ch.pipeline().addLast(
                new Socks5ProxyHandler(new InetSocketAddress(clientConfig.getRemoteHost(), clientConfig.getRemotePort()),
                        clientConfig.getUserName(), clientConfig.getPassWord()),
                new RemoteToClientHandler(channelHandlerContext));
    }
}
