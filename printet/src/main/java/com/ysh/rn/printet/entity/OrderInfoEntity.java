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
     * 订单说明
     */
    private String info;
    /**
     * 订单编码
     */
    private String order_number;
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
     * 联系方式
     */
    private String phone;
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
    private String erwema_coder;
    /**
     * 订单问候语
     */
    private String tankinfo;
    /**
     * 订单logo
     */
    private Bitmap logo;


    public OrderInfoEntity(String title, String info, String order_number, Bitmap order_number_code, String time, String address, List<GoodsEntity> list, String all_pirce, String rewema_string, String erwema_coder, String tankinfo, Bitmap logo,String phone) {
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
        this.phone=phone;
    }

    public OrderInfoEntity() {
    }

    protected OrderInfoEntity(Parcel in) {
        title = in.readString();
        info = in.readString();
        order_number = in.readString();
        order_number_code = in.readParcelable(Bitmap.class.getClassLoader());
        time = in.readString();
        address = in.readString();
        list = in.createTypedArrayList(GoodsEntity.CREATOR);
        all_pirce = in.readString();
        rewema_string = in.readString();
        erwema_coder = in.readString();
        tankinfo = in.readString();
        logo = in.readParcelable(Bitmap.class.getClassLoader());
        phone=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(info);
        dest.writeString(order_number);
        dest.writeParcelable(order_number_code, flags);
        dest.writeString(time);
        dest.writeString(address);
        dest.writeTypedList(list);
        dest.writeString(all_pirce);
        dest.writeString(rewema_string);
        dest.writeString(erwema_coder);
        dest.writeString(tankinfo);
        dest.writeParcelable(logo, flags);
        dest.writeString(phone);
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

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
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

    public String getErwema_coder() {
        return erwema_coder;
    }

    public void setErwema_coder(String erwema_coder) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
