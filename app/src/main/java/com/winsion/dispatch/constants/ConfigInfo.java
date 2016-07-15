package com.winsion.dispatch.constants;

/**
 * Created by admin on 2016/7/4.
 */
public class ConfigInfo {
    // 通信服务器地址
    private static String communicationAddress = null;
    // 软交换服务器地址
    private static String switchBoardAddress = null;
    // 客户端实时消息保留天数
    private static String clientMessageRemain = null;
    // 客户端客运命令保留天数
    private static String clientNoticeRemain = null;

    public static void setCommunicationAddress(String communicationAddress) {
        ConfigInfo.communicationAddress = communicationAddress;
    }

    public static void setClientMessageRemain(String clientMessageRemain) {
        ConfigInfo.clientMessageRemain = clientMessageRemain;
    }

    public static void setSwitchBoardAddress(String switchBoardAddress) {
        ConfigInfo.switchBoardAddress = switchBoardAddress;
    }

    public static void setClientNoticeRemain(String clientNoticeRemain) {
        ConfigInfo.clientNoticeRemain = clientNoticeRemain;
    }

    public static String getCommunicationAddress() {
        return communicationAddress;
    }

    public static String getSwitchBoardAddress() {
        return switchBoardAddress;
    }

    public static String getClientMessageRemain() {
        return clientMessageRemain;
    }

    public static String getClientNoticeRemain() {
        return clientNoticeRemain;
    }
}
