package com.wh.network.mouse.socks.server.config;


import lombok.Data;

@Data
public class ServerConfig {

    /**
     * 端口号
     */
    private int port;
    /**
     * 工作线程数
     */
    private int workThreads;
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
