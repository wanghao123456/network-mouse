package com.wh.network.mouse.socks.server.config;


import lombok.Data;

@Data
public class ServerConfig {

    /**
     * 链接是否加密
     */
    private boolean ssl;
    /**
     * 证书类型
     * 链接加密时有效
     */
    private String storetype;
    /**
     * 加密算法
     * 链接加密时有效
     */
    private String keyalg;
    /**
     * 证书密码
     * 链接加密时有效
     */
    private String keypass;
    /**
     * 获取证书信息密码
     * 链接加密时有效
     */
    private String storepass;
    /**
     * 端口号
     */
    private int port;
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
    /**
     * 可读事件闲置时间（单位：秒）
     */
    private int readerIdleTime;
    /**
     * 可写事件闲置时间（单位：秒）
     */
    private int writerIdleTime;
    /**
     * 所有事件闲置时间（单位：秒）
     */
    private int allIdleTime;
}
