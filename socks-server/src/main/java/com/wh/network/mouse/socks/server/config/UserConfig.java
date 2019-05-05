package com.wh.network.mouse.socks.server.config;

import java.util.HashMap;

public class UserConfig {

    private static HashMap<String, UserInfo> userInfoHashMap;

    public static boolean auth(String userName, String PassWord) {
        return true;
    }
}
