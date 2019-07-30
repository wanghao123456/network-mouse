package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.handler.RemoteToClientHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;

public class ProxyChannelInitializerHandler extends ChannelInitializer {

    private ChannelHandlerContext channelHandlerContext;

    public ProxyChannelInitializerHandler(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new RemoteToClientHandler(channelHandlerContext));
    }
}
