package com.example.administrator.myapplication.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/4.
 */

public class cnBean implements Serializable {
    private String targetUrl;
    private String name;


    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
