package com.example.administrator.Jsoupread.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/4.
 */

public class historyBean implements Serializable {
    private  String name;
    private  String targeturl;
    private  String source;
    private  String time;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public  String getTargeturl() {
        return targeturl;
    }
    public void setTargeturl(String targeturl) {
        this.targeturl = targeturl;
    }
    public  String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
}
