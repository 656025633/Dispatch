package com.winsion.dispatch.bean;

/**
 * Created by admin on 2016/7/4.
 */
public class ConfigBean {
    private String sysKey;

    private String value;

    public void setSysKey(String sysKey){
        this.sysKey = sysKey;
    }
    public String getSysKey(){
        return this.sysKey;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
