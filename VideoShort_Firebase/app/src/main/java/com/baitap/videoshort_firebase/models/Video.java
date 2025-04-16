package com.baitap.videoshort_firebase.models;

import java.io.Serializable;

public class Video implements Serializable {
    private String title;
    private String desc;
    private String url;

    public Video() {
        // Bắt buộc có constructor rỗng để Firebase mapping
    }

    public Video(String title, String desc, String url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    public String getTitle() { return title; }
    public String getDesc() { return desc; }
    public String getUrl() { return url; }

    public void setTitle(String title) { this.title = title; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setUrl(String url) { this.url = url; }
}
