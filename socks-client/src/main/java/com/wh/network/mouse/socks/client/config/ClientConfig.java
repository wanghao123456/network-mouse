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
    private String remoteHost;
    /**
     * 代理服务port
     */
    private int remotePort;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 工作线程数
     */
    private int workThreads;
    /**
     * 代理线程数
     */
    private int proxyThreads;
    /**
     * 连接超时时间 （单位：秒）
     */
    private int connectTimeout;
}
