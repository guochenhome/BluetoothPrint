package com.ysh.rn.printet;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.TextUtils;

import com.ysh.rn.printet.print.PrintUtil;


/**
 * Created by liuguirong on 8/1/17.
 */

public class BluetoothController {

    public PrinterInterface printerInterface;

    public Context context;

    public void setPrinterInterface(PrinterInterface printerInterface) {
        this.printerInterface = printerInterface;
    }
    BluetoothAdapter mAdapter;

    public BluetoothAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(BluetoothAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public BluetoothController(Context context) {
        this.context = context;
    }

    public  void init() {
        if (null == mAdapter) {
           this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (null == mAdapter) {
//            activity.tv_bluename.setText("该设备没有蓝牙模块");
//            activity.mBtEnable = false;
            printerInterface.NoBT();
            return;
        }
        if (!mAdapter.isEnabled()) {
            //没有在开启中也没有打开
//            if ( activity.mAdapter.getState()!=BluetoothAdapter.STATE_TURNING_ON  && activity.mAdapter.getState()!=BluetoothAdapter.STATE_ON ){
            if (mAdapter.getState() == BluetoothAdapter.STATE_OFF) {//蓝牙被关闭时强制打开
                mAdapter.enable();

            } else {
//                activity.tv_bluename.setText("蓝牙未打开");
                printerInterface.BT_NoOpen();
                return;
            }
        }
        String address = PrintUtil.getDefaultBluethoothDeviceAddress(context);
        if (TextUtils.isEmpty(address)) {
//            activity.tv_bluename.setText("尚未绑定蓝牙设备");
            this.printerInterface.BT_NoBind();
            return;
        }
        String name = PrintUtil.getDefaultBluetoothDeviceName(context);
//        activity.tv_bluename.setText("已绑定蓝牙：" + name);
//        activity.tv_blueadress.setText(address);
        this.printerInterface.BT_Bind(name,address);

    }

    public static boolean turnOnBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.enable();
        }
        return false;
    }

    public interface PrinterInterface {
        public void NoBT();

        public void BT_NoOpen();

        public void BT_NoBind();

        public void BT_Bind(String name,String address);
    }
}
