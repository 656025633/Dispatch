package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/6/15.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class MessageBean implements Serializable {
    //消息对象
    private String senderID;
    private String senderNick;
    private String senderHead;
    private String contentType;//1.文字2，语音3.图片
    private String content;

    public String getSenderID() {
        return senderID;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public String getSenderHead() {
        return senderHead;
    }

    public void setSenderHead(String senderHead) {
        this.senderHead = senderHead;
    }
}
