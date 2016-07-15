package com.winsion.dispatch.constants;

/**
 * Created by yalong on 2016/6/14.
 */
public interface NetConstants {
    // 5.25
    String HOST = "http://172.16.6.18:9212/";
    String USER_NOTE = "userNote/";
    String AUTH = "auth/";
    String FILE = "file/";
    String COMMON_CONFIG = "commonConfig/";
    String PHONE_JOB = "phoneJob/";

    String AUTH_TOKEN = "?authToken=" + UserInfo.getAuthToken();

    String LOGIN = "login";
    String LOGOUT = "logout";
    String CHANGE_PASSWORD = "changePassword";

    String QUERY_ALL = "queryAll" + AUTH_TOKEN;
    String ADD = "add" + AUTH_TOKEN;
    String UPDATE = "update" + AUTH_TOKEN;
    String DELETE = "delete" + AUTH_TOKEN;
    String DOWNLOAD_USER_PICTURE = "downloadUserPicture" + AUTH_TOKEN;
    String QUERY_SYS_CONFIG = "querySysConfig" + AUTH_TOKEN;
    String SEARCH_USER_STATION_ORG = "searchUserStationOrg" + AUTH_TOKEN;
    String JOB_CONTROLLER = "jobController" + AUTH_TOKEN;
}