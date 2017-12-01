package com.ysh.rn.printet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 商品 基础类
 * Created by guochen on 2017/11/20.
 */

public class GoodsEntity implements Parcelable{


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

    public GoodsEntity(String name, Integer count, String price, Boolean price_show) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.price_show = price_show;
    }

    public GoodsEntity() {
    }

    protected GoodsEntity(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        price = in.readString();
        byte tmpPrice_show = in.readByte();
        price_show = tmpPrice_show == 0 ? null : tmpPrice_show == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(count);
        }
        dest.writeString(price);
        dest.writeByte((byte) (price_show == null ? 0 : price_show ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsEntity> CREATOR = new Creator<GoodsEntity>() {
        @Override
        public GoodsEntity createFromParcel(Parcel in) {
            return new GoodsEntity(in);
        }

        @Override
        public GoodsEntity[] newArray(int size) {
            return new GoodsEntity[size];
        }
    };

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
