package com.cniao5shop.bean;

import java.io.Serializable;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/28
 * @Description: ${todo}
 */

public class Campaign implements Serializable {
    private Long id;
    private String title;
    private String imgUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
