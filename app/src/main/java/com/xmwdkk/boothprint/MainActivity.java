package com.xmwdkk.boothprint;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.ysh.rn.printet.BluetoothController;
import com.ysh.rn.printet.BtService;
import com.ysh.rn.printet.base.AppInfo;
import com.ysh.rn.printet.bt.BluetoothActivity;
import com.ysh.rn.printet.print.PrintMsgEvent;
import com.ysh.rn.printet.print.PrintUtil;
import com.ysh.rn.printet.print.PrinterMsgType;
import com.ysh.rn.printet.util.ToastUtil;

import de.greenrobot.event.EventBus;

/***
 *  Created by liugruirong on 2017/8/3.
 */
public class MainActivity extends BluetoothActivity implements View.OnClickListener, BluetoothController.PrinterInterface {

    TextView tv_bluename;
    TextView tv_blueadress;
    boolean mBtEnable = true;
    int PERMISSION_REQUEST_COARSE_LOCATION = 2;
    /**
     * bluetooth adapter
     */

    BluetoothController bluetoothController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_bluename = (TextView) findViewById(R.id.tv_bluename);
        tv_blueadress = (TextView) findViewById(R.id.tv_blueadress);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        //6.0以上的手机要地理位置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
        EventBus.getDefault().register(MainActivity.this);
        bluetoothController = new BluetoothController(this);
        bluetoothController.setPrinterInterface(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        BluetoothController.init(this);
        bluetoothController.init();
    }


    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        bluetoothController.init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button4:
                startActivity(new Intent(MainActivity.this, SearchBluetoothActivity.class));
                break;
            case R.id.button5:
                if (TextUtils.isEmpty(AppInfo.btAddress)) {
                    ToastUtil.showToast(MainActivity.this, "请连接蓝牙...");
                    startActivity(new Intent(MainActivity.this, SearchBluetoothActivity.class));
                } else {
                    if (bluetoothController.getmAdapter().getState() == BluetoothAdapter.STATE_OFF) {//蓝牙被关闭时强制打开
                        bluetoothController.getmAdapter().enable();
                        ToastUtil.showToast(MainActivity.this, "蓝牙被关闭请打开...");
                    } else {
                        ToastUtil.showToast(MainActivity.this, "打印测试...");
                        Intent intent = new Intent(getApplicationContext(), BtService.class);
                        intent.setAction(PrintUtil.ACTION_PRINT_TEST);
                        startService(intent);
                    }

                }
                break;
            case R.id.button6:
                if (TextUtils.isEmpty(AppInfo.btAddress)) {
                    ToastUtil.showToast(MainActivity.this, "请连接蓝牙...");
                    startActivity(new Intent(MainActivity.this, SearchBluetoothActivity.class));
                } else {
                    ToastUtil.showToast(MainActivity.this, "打印测试...");
                    Intent intent2 = new Intent(getApplicationContext(), BtService.class);
                    intent2.setAction(PrintUtil.ACTION_PRINT_TEST_TWO);
                    startService(intent2);

                }
            case R.id.button:
                if (TextUtils.isEmpty(AppInfo.btAddress)) {
                    ToastUtil.showToast(MainActivity.this, "请连接蓝牙...");
                    startActivity(new Intent(MainActivity.this, SearchBluetoothActivity.class));
                } else {
                    ToastUtil.showToast(MainActivity.this, "打印图片...");
                    Intent intent2 = new Intent(getApplicationContext(), BtService.class);
                    intent2.setAction(PrintUtil.ACTION_PRINT_BITMAP);
                    startService(intent2);

                }
//                startActivity(new Intent(MainActivity.this,TextActivity.class));
                break;
        }

    }

    /**
     * handle printer message
     *
     * @param event print msg event
     */
    public void onEventMainThread(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            ToastUtil.showToast(MainActivity.this, event.msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(MainActivity.this);
    }

    @Override
    public void NoBT() {
        this.tv_bluename.setText("该设备没有蓝牙模块");
        this.mBtEnable = false;
    }

    @Override
    public void BT_NoOpen() {
        this.tv_bluename.setText("蓝牙未打开");
    }

    @Override
    public void BT_NoBind() {
        this.tv_bluename.setText("尚未绑定蓝牙设备");
    }

    @Override
    public void BT_Bind(String name, String address) {
        this.tv_bluename.setText("已绑定蓝牙：" + name);
        this.tv_blueadress.setText(address);
    }
}
