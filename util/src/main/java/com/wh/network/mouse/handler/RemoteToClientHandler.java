package com.wh.network.mouse.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RemoteToClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext channelHandlerContext;

    public RemoteToClientHandler(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelHandlerContext.writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelHandlerContext.close();
    }
}
