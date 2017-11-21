package com.ysh.rn.printet.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/11/20.
 */

public class OrderInfoEntity implements Parcelable {

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


    public OrderInfoEntity(String title, String info, Integer order_number, Bitmap order_number_code, String time, String address, List<GoodsEntity> list, String all_pirce, String rewema_string, Bitmap erwema_coder, String tankinfo, Bitmap logo) {
        super();
        this.title = title;
        this.info = info;
        this.order_number = order_number;
        this.order_number_code = order_number_code;
        this.time = time;
        this.address = address;
        this.list = list;
        this.all_pirce = all_pirce;
        this.rewema_string = rewema_string;
        this.erwema_coder = erwema_coder;
        this.tankinfo = tankinfo;
        this.logo = logo;
    }

    protected OrderInfoEntity(Parcel in) {
        title = in.readString();
        info = in.readString();
        if (in.readByte() == 0) {
            order_number = null;
        } else {
            order_number = in.readInt();
        }
        order_number_code = in.readParcelable(Bitmap.class.getClassLoader());
        time = in.readString();
        address = in.readString();
        list = in.createTypedArrayList(GoodsEntity.CREATOR);
        all_pirce = in.readString();
        rewema_string = in.readString();
        erwema_coder = in.readParcelable(Bitmap.class.getClassLoader());
        tankinfo = in.readString();
        logo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(info);
        if (order_number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(order_number);
        }
        dest.writeParcelable(order_number_code, flags);
        dest.writeString(time);
        dest.writeString(address);
        dest.writeTypedList(list);
        dest.writeString(all_pirce);
        dest.writeString(rewema_string);
        dest.writeParcelable(erwema_coder, flags);
        dest.writeString(tankinfo);
        dest.writeParcelable(logo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel in) {
            return new OrderInfoEntity(in);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };

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
