package com.wh.network.mouse.socks.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5InitialRequest msg) throws Exception {
        log.info("初始化socks5连接，详细信息：{}", msg);
        if (msg.decoderResult().isSuccess()) {
            Socks5InitialResponse socks5InitialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
            ctx.writeAndFlush(socks5InitialResponse);
            ctx.pipeline().remove(this).remove(Socks5InitialRequestDecoder.class);
        } else {
            log.info("解析socks5协议失败，详细信息：{}", msg);
            ctx.close();
        }
    }
}
