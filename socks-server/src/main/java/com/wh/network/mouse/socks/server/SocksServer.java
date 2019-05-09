package com.wh.network.mouse.socks.server;

import com.wh.network.mouse.socks.server.config.ServerConfig;
import com.wh.network.mouse.socks.server.handler.Socks5CommandRequestHandler;
import com.wh.network.mouse.socks.server.handler.Socks5InitialRequestHandler;
import com.wh.network.mouse.socks.server.handler.Socks5PasswordAuthRequestHandler;
import com.wh.network.mouse.util.ConfigConstants;
import com.wh.network.mouse.util.FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
        String filePath = System.getProperty(ConfigConstants.SERVER_CONFIG_KEY, ConfigConstants.SERVER_DEFAULT_CONFIG_FILE_PATH);
        ServerConfig serverConfig = FileUtil.readFileToBean(filePath, ServerConfig.class);
        log.info("配置信息加载完毕，详情如下：{}", serverConfig);
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(serverConfig.getWorkThreads());
        EventLoopGroup proxy = new NioEventLoopGroup(serverConfig.getProxyThreads());

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
                                new Socks5PasswordAuthRequestHandler(),
                                new Socks5CommandRequestDecoder(),
                                new Socks5CommandRequestHandler(proxy, NioSocketChannel.class)
                        );
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(serverConfig.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务启动失败，异常如下：{}", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            proxy.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        SocksServer socksServer = new SocksServer();
        socksServer.start();
    }

}
