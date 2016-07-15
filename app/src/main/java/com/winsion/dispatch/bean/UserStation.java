package com.winsion.dispatch.bean;

/**
 * Created by admin on 2016/7/6.
 */
public class UserStation {
    private String id;

    private String name;

    private String parentId;

    private String distinction;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setParentId(String parentId){
        this.parentId = parentId;
    }
    public String getParentId(){
        return this.parentId;
    }
    public void setDistinction(String distinction){
        this.distinction = distinction;
    }
    public String getDistinction(){
        return this.distinction;
    }
}
