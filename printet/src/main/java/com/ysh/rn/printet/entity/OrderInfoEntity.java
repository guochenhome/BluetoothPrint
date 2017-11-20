package com.ysh.rn.printet.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/11/20.
 */

public class OrderInfoEntity implements Serializable {

    /**
     * 订单title
     */
    private String title;
    /**
     * 订单书名
     */
    private String info;
    /**
     * 订单编码
     */
    private Integer order_number;
    /**
     * 订单编码条
     */
    private Bitmap order_number_code;
    /**
     * 订单时间   暂时使用系统时间
     */
    private String time;
    /**
     * 订单使用地址
     */
    private String address;
    /**
     * 订单商品内容
     */
    private List<GoodsEntity> list;
    /**
     * 订单总金额
     */
    private String all_pirce;
    /**
     * 订单二维码说明
     */
    private String rewema_string;
    /**
     * 订单二维码
     */
    private Bitmap erwema_coder;
    /**
     * 订单问候语
     */
    private String tankinfo;
    /**
     * 订单logo
     */
    private Bitmap logo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getOrder_number() {
        return order_number;
    }

    public void setOrder_number(Integer order_number) {
        this.order_number = order_number;
    }

    public Bitmap getOrder_number_code() {
        return order_number_code;
    }

    public void setOrder_number_code(Bitmap order_number_code) {
        this.order_number_code = order_number_code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<GoodsEntity> getList() {
        return list;
    }

    public void setList(List<GoodsEntity> list) {
        this.list = list;
    }

    public String getAll_pirce() {
        return all_pirce;
    }

    public void setAll_pirce(String all_pirce) {
        this.all_pirce = all_pirce;
    }

    public String getRewema_string() {
        return rewema_string;
    }

    public void setRewema_string(String rewema_string) {
        this.rewema_string = rewema_string;
    }

    public Bitmap getErwema_coder() {
        return erwema_coder;
    }

    public void setErwema_coder(Bitmap erwema_coder) {
        this.erwema_coder = erwema_coder;
    }

    public String getTankinfo() {
        return tankinfo;
    }

    public void setTankinfo(String tankinfo) {
        this.tankinfo = tankinfo;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }
}
