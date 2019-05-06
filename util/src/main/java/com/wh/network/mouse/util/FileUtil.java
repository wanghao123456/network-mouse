package com.wh.network.mouse.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;


@Slf4j
public class FileUtil {

    public static <T> T readFileToBean(String filePath, Class<T> typeClass) {
        return JSON.parseObject(readFileToString(filePath), typeClass);
    }

    public static <T> List<T> readFileToList(String filePath, Class<T> typeClass) {
        return JSON.parseArray(readFileToString(filePath), typeClass);
    }

    private static String readFileToString(String filePath) {
        File configFile = new File(filePath);
        int configFileLength = (int) configFile.length();
        String configContent = null;
        try (FileReader fileReader = new FileReader(configFile, UTF_8)) {
            char[] charBuffer = new char[configFileLength];
            fileReader.read(charBuffer, 0, configFileLength);
            configContent = new String(charBuffer);
        } catch (IOException e) {
            log.error("读取配置文件：{}出现异常，异常如下：{}", filePath, e);
        }
        return configContent;
    }
}
