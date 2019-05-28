package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.socks.server.config.UserConfig;
import com.wh.network.mouse.socks.server.config.UserInfo;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Socks5PasswordAuthRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5PasswordAuthRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5PasswordAuthRequest msg) throws Exception {
        UserInfo userInfo = UserConfig.auth(msg.username(), msg.password());
        if (userInfo != null) {
            log.info("用户：{} 身份验证通过", msg.username());
            Socks5PasswordAuthResponse socks5PasswordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS);
            ctx.writeAndFlush(socks5PasswordAuthResponse);
            if (userInfo.isLimit()) {
                ctx.pipeline().addFirst(new ChannelTrafficShapingHandler(userInfo.getWriteLimit() << 10,
                        userInfo.getReadLimit() << 10, userInfo.getCheckInterval() * 1000, userInfo.getMaxTime() * 1000));
            }
        } else {
            log.info("用户：{} 身份验证失败，详细信息：{}", msg.username(), msg);
            Socks5PasswordAuthResponse socks5PasswordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
            ctx.writeAndFlush(socks5PasswordAuthResponse).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
