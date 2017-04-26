package com.imgraduate.entity;

import java.util.ArrayList;

/**
 * Created by zhang on 2017/4/24.
 */

public class Identity {
    private String username;
    private String password;
    private String sex;
    private String type;
    private String school;
    private String province;
    private ArrayList<String> collection;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCollection(ArrayList<String> collection) {
        this.collection = collection;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getCollection() {
        return collection;
    }

    public String getProvince() {
        return province;
    }

    public String getSchool() {
        return school;
    }

    public String getSex() {
        return sex;
    }

    public String getType() {
        return type;
    }
}
