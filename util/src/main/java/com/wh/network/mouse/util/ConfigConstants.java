package com.wh.network.mouse.util;

public class ConfigConstants {

    public static final String SERVER_CONFIG_KEY = "server_config_file_path";
    public static final String CLIENT_CONFIG_KEY = "client_config_file_path";
    public static final String USER_CONFIG_KEY = "user_config_file_path";

    public static final String SERVER_DEFAULT_CONFIG_FILE_NAME = "server.json";
    public static final String CLIENT_DEFAULT_CONFIG_FILE_NAME = "client.json";
    public static final String USER_DEFAULT_CONFIG_FILE_NAME = "user.json";

    public static final String CONFIG_PATH = System.getProperty("user.dir") + "/config/";

    public static final String SERVER_DEFAULT_CONFIG_FILE_PATH = CONFIG_PATH + SERVER_DEFAULT_CONFIG_FILE_NAME;
    public static final String CLIENT_DEFAULT_CONFIG_FILE_PATH = CONFIG_PATH + CLIENT_DEFAULT_CONFIG_FILE_NAME;
    public static final String USER_DEFAULT_CONFIG_FILE_PATH = CONFIG_PATH + USER_DEFAULT_CONFIG_FILE_NAME;
}
