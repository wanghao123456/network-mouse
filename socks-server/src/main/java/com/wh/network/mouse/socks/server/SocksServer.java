package com.wh.network.mouse.socks.server;

import com.wh.network.mouse.socks.server.config.ServerConfig;
import com.wh.network.mouse.socks.server.handler.Socks5CommandRequestHandler;
import com.wh.network.mouse.socks.server.handler.Socks5InitialRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocksServer {

    public void start() {
        ServerConfig serverConfig = new ServerConfig();
        log.info("配置信息加载完毕，详情如下：{}", serverConfig);
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(serverConfig.getWorkThreads());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, serverConfig.getConnectTimeout() * 1000)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new IdleStateHandler(serverConfig.getReaderIdleTime(), serverConfig.getWriterIdleTime(), serverConfig.getAllIdleTime()),
                                new LoggingHandler(),
                                Socks5ServerEncoder.DEFAULT,
                                new Socks5InitialRequestDecoder(),
                                new Socks5InitialRequestHandler(),
                                new Socks5PasswordAuthRequestDecoder(),
                                new Socks5CommandRequestDecoder(),
                                new Socks5CommandRequestHandler()
                        );
                    }
                });
    }
}
