package com.wh.network.mouse.socks.server;

import com.wh.network.mouse.socks.server.config.ServerConfig;
import com.wh.network.mouse.socks.server.config.UserConfig;
import com.wh.network.mouse.socks.server.handler.ChannelInitializerHandler;
import com.wh.network.mouse.util.ConfigConstants;
import com.wh.network.mouse.util.FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocksServer {

    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;
    private EventLoopGroup proxy = null;

    public void start() {
        try {
            String userConfigFilePath = System.getProperty(ConfigConstants.USER_CONFIG_KEY, ConfigConstants.USER_DEFAULT_CONFIG_FILE_PATH);
            UserConfig.loadUserInfo(userConfigFilePath);
            UserConfig.monitorUserInfo(userConfigFilePath);
            String serverConfigFilePath = System.getProperty(ConfigConstants.SERVER_CONFIG_KEY, ConfigConstants.SERVER_DEFAULT_CONFIG_FILE_PATH);
            ServerConfig serverConfig = FileUtil.readFileToBean(serverConfigFilePath, ServerConfig.class);
            log.info("配置信息加载完毕，详情如下：{}", serverConfig);
            boss = new NioEventLoopGroup(1);
            worker = new NioEventLoopGroup(serverConfig.getWorkThreads());
            proxy = new NioEventLoopGroup(serverConfig.getProxyThreads());

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializerHandler(serverConfig, proxy));
            ChannelFuture channelFuture = serverBootstrap.bind(serverConfig.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("服务启动失败，异常如下：{}", e);
        } finally {
            close();
        }
    }

    public void close() {
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
        if (proxy != null) {
            proxy.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        SocksServer socksServer = new SocksServer();
        socksServer.start();
    }

}
