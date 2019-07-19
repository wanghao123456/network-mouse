package com.wh.network.mouse.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientToRemoteHandler extends ChannelInboundHandlerAdapter {

    private ChannelFuture channelFuture;

    public ClientToRemoteHandler(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelFuture.channel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelFuture.channel().close();
    }
}
