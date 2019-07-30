package com.wh.network.mouse.socks.server.config;

import com.wh.network.mouse.util.FileUtil;
import io.netty.util.internal.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserConfig {

    private static volatile HashMap<String, UserInfo> userInfoHashMap;

    private static long userConfigFileLastModified;

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

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
        userConfigFileLastModified = FileUtil.lastModified(filePath);
        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(() -> {
            long tempUserConfigFileLastModified = FileUtil.lastModified(filePath);
            if (tempUserConfigFileLastModified > userConfigFileLastModified) {
                userConfigFileLastModified = tempUserConfigFileLastModified;
                loadUserInfo(filePath);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }
}
