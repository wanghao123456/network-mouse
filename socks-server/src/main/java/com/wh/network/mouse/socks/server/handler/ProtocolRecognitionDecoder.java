package com.wh.network.mouse.socks.server.handler;

import com.wh.network.mouse.handler.ExceptionCaughtHandler;
import com.wh.network.mouse.socks.server.config.ServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProtocolRecognitionDecoder extends ByteToMessageDecoder {

    private ServerConfig serverConfig;

    private EventLoopGroup proxy;

    public ProtocolRecognitionDecoder(ServerConfig serverConfig, EventLoopGroup proxy) {
        this.serverConfig = serverConfig;
        this.proxy = proxy;
    }

    private static final byte SOCKS5 = '\u0005';

    private static final byte HTTP = 'G';

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 1) {
            switch (in.getByte(0)) {
                case SOCKS5:
                    ctx.pipeline().addLast(
                            Socks5ServerEncoder.DEFAULT,
                            new Socks5InitialRequestDecoder(),
                            new Socks5InitialRequestHandler(),
                            new Socks5PasswordAuthRequestDecoder(),
                            new Socks5PasswordAuthRequestHandler(),
                            new Socks5CommandRequestDecoder(),
                            new Socks5CommandRequestHandler(proxy, NioSocketChannel.class, serverConfig));
                    break;
                case HTTP:
                    ctx.pipeline().addLast(
                            new HttpServerCodec(),
                            new HttpObjectAggregator(1 << 10),
                            new DisguisedHttpRequestHandler());
                    break;
                default:
                    log.info("接收到支持协议范围之外的报文");
                    ctx.close();
                    return;
            }
            ctx.pipeline().addLast(new ExceptionCaughtHandler());
            out.add(in.retain());
            ctx.pipeline().remove(this);
        }
    }
}