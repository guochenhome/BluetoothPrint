package com.ysh.rn.printet.entity;

import java.io.Serializable;

/**
 * 商品 基础类
 * Created by guochen on 2017/11/20.
 */

public class GoodsEntity implements Serializable{


    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品数量
     */
    private Integer count;
    /**
     * 商品价格
     */
    private String price;
    /**
     * 是否显示商品价格
     */
    private Boolean price_show;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getPrice_show() {
        return price_show;
    }

    public void setPrice_show(Boolean price_show) {
        this.price_show = price_show;
    }
}
