package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.util.ConfigConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class DisguisedHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.MOVED_PERMANENTLY);
        defaultFullHttpResponse.headers().add(HttpHeaders.Names.LOCATION, ConfigConstants.MOVED_PERMANENTLY_URL);
        ctx.writeAndFlush(defaultFullHttpResponse);
        ctx.close();
    }
}
