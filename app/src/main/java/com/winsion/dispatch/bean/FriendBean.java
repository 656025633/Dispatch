package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/6/7.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public class FriendBean implements Serializable {
    private String headImage;
    private String name;
    private String location;
    private String title;
    private String date;

    public String getHeadImage() {
        return headImage;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
