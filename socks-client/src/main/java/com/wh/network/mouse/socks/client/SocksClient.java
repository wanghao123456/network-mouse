package com.wh.network.mouse.socks.client;

import com.wh.network.mouse.socks.client.config.ClientConfig;
import com.wh.network.mouse.socks.client.handler.ClientChannelInitializerHandler;
import com.wh.network.mouse.util.ConfigConstants;
import com.wh.network.mouse.util.FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Slf4j
public class SocksClient {

    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;
    private EventLoopGroup proxy = null;

    public void start() {
        try {
            ClientConfig clientConfig = getClientConfig();
            SslContext sslContext = getSslContext(clientConfig);
            boss = new NioEventLoopGroup(1);
            worker = new NioEventLoopGroup(clientConfig.getWorkThreads());
            proxy = new NioEventLoopGroup(clientConfig.getProxyThreads());

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ClientChannelInitializerHandler(clientConfig, proxy, sslContext));
            ChannelFuture channelFuture = serverBootstrap.bind(clientConfig.getLocalPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("服务启动失败，异常如下：{}", e);
        } finally {
            close();
        }
    }

    private ClientConfig getClientConfig() {
        String filePath = System.getProperty(ConfigConstants.CLIENT_CONFIG_KEY, ConfigConstants.CLIENT_DEFAULT_CONFIG_FILE_PATH);
        ClientConfig clientConfig = FileUtil.readFileToBean(filePath, ClientConfig.class);
        log.info("配置信息加载完毕，详情如下：{}", clientConfig);
        return clientConfig;
    }

    private SslContext getSslContext(ClientConfig clientConfig) {
        if (clientConfig.isSsl()) {
            String keystoreConfigFilePath = System.getProperty(ConfigConstants.KEYSTORE_CONFIG_KEY, ConfigConstants.KEYSTORE_DEFAULT_CONFIG_FILE_PATH);
            try (FileInputStream keyStoreFileInputStream = new FileInputStream(keystoreConfigFilePath)) {
                KeyStore keyStore = KeyStore.getInstance(clientConfig.getStoretype());
                keyStore.load(keyStoreFileInputStream, clientConfig.getStorepass().toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(clientConfig.getKeyalg());
                trustManagerFactory.init(keyStore);
                return SslContextBuilder.forClient().trustManager(trustManagerFactory).build();
            } catch (Exception e) {
                throw new RuntimeException("加载SSL证书异常", e);
            }
        }
        return null;
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
        SocksClient socksClient = new SocksClient();
        socksClient.start();
    }

}
