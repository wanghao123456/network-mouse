package com.wh.network.mouse.socks.client;

import com.wh.network.mouse.socks.client.config.ClientConfig;
import com.wh.network.mouse.util.ConfigConstants;
import com.wh.network.mouse.util.FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocksClient {

    public void start() {
        String filePath = System.getProperty(ConfigConstants.CLIENT_CONFIG_KEY, ConfigConstants.CLIENT_DEFAULT_CONFIG_FILE_PATH);
        ClientConfig clientConfig = FileUtil.readFileToBean(filePath, ClientConfig.class);
        log.info("配置信息加载完毕，详情如下：{}", clientConfig);
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(clientConfig.getWorkThreads());
        EventLoopGroup proxy = new NioEventLoopGroup(clientConfig.getProxyThreads());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(clientConfig.getLocalPort()).sync();
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
        SocksClient socksClient = new SocksClient();
        socksClient.start();
    }

}
