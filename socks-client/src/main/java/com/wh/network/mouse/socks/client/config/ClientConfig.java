package com.wh.network.mouse.socks.client.config;


import lombok.Data;

@Data
public class ClientConfig {

    /**
     * 本地端口号
     */
    private int localPort;
    /**
     * 代理服务host
     */
    private int remoteHost;
    /**
     * 代理服务port
     */
    private int remotePort;
    /**
     * 工作线程数
     */
    private int workThreads;
    /**
     * 代理线程数
     */
    private int proxyThreads;
}
