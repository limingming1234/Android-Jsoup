package com.example.administrator.Jsoupread.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/3.
 */

public class gkBean implements Serializable {
    private String targetUrl;
    private String img;
    private String name;


    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
