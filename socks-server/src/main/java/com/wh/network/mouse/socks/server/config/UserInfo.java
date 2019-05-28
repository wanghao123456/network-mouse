package com.wh.network.mouse.socks.server.config;


import lombok.Data;

@Data
public class UserInfo {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 是否限速（非必须，默认不限速）
     */
    private boolean limit;
    /**
     * 当限速时有效
     * 用户下行速度（非必须，单位：kb/s）
     */
    private int writeLimit;
    /**
     * 当限速时有效
     * 用户上行速度（非必须，单位：kb/s）
     */
    private int readLimit;
    /**
     * 当限速时有效
     * 限速检测时间间隔（非必须，单位：秒）
     */
    private int checkInterval;
    /**
     * 当限速时有效
     * 最大延迟时间（非必须，单位：秒）
     */
    private int maxTime;

}
