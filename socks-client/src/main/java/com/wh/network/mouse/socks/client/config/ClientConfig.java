package com.wh.network.mouse.socks.client.config;


import lombok.Data;

@Data
public class ClientConfig {

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
