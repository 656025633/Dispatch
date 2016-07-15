package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/6/6.
 * QQ:656025633
 * Company:com.winsion
 * Version:1.0
 * explain:
 */
public class ContractBean extends BaseParent implements Serializable {

    private String userId;
    String name;
    String datte;
    String type;
    String headIcon;

    public String getName() {
        return name;
    }

    public String getDatte() {
        return datte;
    }

    public String getType() {
        return type;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatte(String datte) {
        this.datte = datte;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
