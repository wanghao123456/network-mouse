package com.wh.network.mouse.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;

public class ClientToRemoteListener implements ChannelFutureListener {

    private ChannelHandlerContext ctx;
    private DefaultSocks5CommandRequest msg;

    public ClientToRemoteListener(ChannelHandlerContext ctx, DefaultSocks5CommandRequest msg) {
        this.ctx = ctx;
        this.msg = msg;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        Socks5CommandResponse commandResponse;
        if (future.isSuccess()) {
            ctx.pipeline().addLast(new ClientToRemoteHandler(future));
            commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, msg.dstAddrType());
        } else {
            commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, msg.dstAddrType());
        }
        ctx.writeAndFlush(commandResponse);
    }
}
