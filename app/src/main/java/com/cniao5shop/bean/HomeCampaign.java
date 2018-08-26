package com.cniao5shop.bean;

import java.io.Serializable;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/28
 * @Description: ${todo}
 */

public class HomeCampaign implements Serializable {

    private long id;
    private String title;
    private Campaign cpOne;
    private Campaign cpTwo;
    private Campaign cpThree;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Campaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaign cpOne) {
        this.cpOne = cpOne;
    }

    public Campaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Campaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaign cpThree) {
        this.cpThree = cpThree;
    }
}
