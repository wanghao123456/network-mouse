package com.wh.network.mouse.socks.server.config;

import com.wh.network.mouse.util.FileUtil;
import io.netty.util.internal.StringUtil;

import java.util.HashMap;
import java.util.List;

public class UserConfig {

    private static volatile HashMap<String, UserInfo> userInfoHashMap;

    public static UserInfo auth(String userName, String passWord) {
        if (!StringUtil.isNullOrEmpty(userName) && !StringUtil.isNullOrEmpty(passWord)) {
            UserInfo userInfo = userInfoHashMap.get(userName);
            if (userInfo != null && passWord.equals(userInfo.getPassWord())) {
                return userInfo;
            }
        }
        return null;
    }

    public static void loadUserInfo(String filePath) {
        List<UserInfo> userInfoList = FileUtil.readFileToList(filePath, UserInfo.class);
        HashMap<String, UserInfo> tempUserInfoHashMap = new HashMap<>(userInfoList.size());
        for (UserInfo userInfo : userInfoList) {
            tempUserInfoHashMap.put(userInfo.getUserName(), userInfo);
        }
        userInfoHashMap = tempUserInfoHashMap;
    }

    public static void monitorUserInfo(String filePath) {
    }
}
