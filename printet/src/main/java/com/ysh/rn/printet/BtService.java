package com.ysh.rn.printet;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ysh.rn.printet.entity.OrderInfoEntity;
import com.ysh.rn.printet.print.GPrinterCommand;
import com.ysh.rn.printet.print.PrintPic;
import com.ysh.rn.printet.print.PrintQueue;
import com.ysh.rn.printet.print.PrintUtil;
import com.ysh.rn.printet.printutil.PrintOrderDataMaker;
import com.ysh.rn.printet.printutil.PrinterWriter;
import com.ysh.rn.printet.printutil.PrinterWriter58mm;

import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by liuguirong on 8/1/17.
 * <p/>
 * print ticket service
 */
public class BtService extends IntentService {

    public BtService() {
        super("BtService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BtService(String name) {
        super(name);
    }

    private OrderInfoEntity entity;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TEST)) {
            if (intent.getParcelableExtra(PrintUtil.ACTION_PRINT_ENTITY) != null) {
                entity = intent.getParcelableExtra(PrintUtil.ACTION_PRINT_ENTITY);
                printTest(entity);
            } else {
                Log.i("打印机", "传递数据为null");
            }
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TEST_TWO)) {
            printTesttwo(3);
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_BITMAP)) {
            if (intent.getParcelableExtra(PrintUtil.ACTION_PRINT_ENTITY) != null) {
                entity = intent.getParcelableExtra(PrintUtil.ACTION_PRINT_ENTITY);
                printBitmapTest(entity.getOrder_number_code());
            } else {
                Log.i("打印机", "传递数据为null");
            }

        }

    }

    private void printTest(OrderInfoEntity entity) {
        PrintOrderDataMaker printOrderDataMaker = new PrintOrderDataMaker(this, "", PrinterWriter58mm.TYPE_58, PrinterWriter.HEIGHT_PARTING_DEFAULT, entity);
        ArrayList<byte[]> printData = (ArrayList<byte[]>) printOrderDataMaker.getPrintData(PrinterWriter58mm.TYPE_58);
        PrintQueue.getQueue(getApplicationContext()).add(printData);
    }

    /**
     * 打印几遍
     *
     * @param num
     */
    private void printTesttwo(int num) {
        try {
            ArrayList<byte[]> bytes = new ArrayList<byte[]>();
            for (int i = 0; i < num; i++) {
                String message = "蓝牙打印测试\n蓝牙打印测试\n蓝牙打印测试\n\n";
                bytes.add(GPrinterCommand.reset);
                bytes.add(message.getBytes("gbk"));
                bytes.add(GPrinterCommand
                        .print);
                bytes.add(GPrinterCommand.print);
                bytes.add(GPrinterCommand.print);
            }
            PrintQueue.getQueue(getApplicationContext()).add(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void print(byte[] byteArrayExtra) {
        if (null == byteArrayExtra || byteArrayExtra.length <= 0) {
            return;
        }
        PrintQueue.getQueue(getApplicationContext()).add(byteArrayExtra);
    }

    private void printBitmapTest(Bitmap bitmap) {
        PrintPic printPic = PrintPic.getInstance();
        printPic.init(bitmap);
        if (null != bitmap) {
            if (bitmap.isRecycled()) {
                bitmap = null;
            } else {
                bitmap.recycle();
                bitmap = null;
            }
        }
        byte[] bytes = printPic.printDraw();
        ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
        printBytes.add(GPrinterCommand.reset);
        printBytes.add(GPrinterCommand.print);
        printBytes.add(bytes);
        Log.e("BtService", "image bytes size is :" + bytes.length);
        printBytes.add(GPrinterCommand.print);
        PrintQueue.getQueue(getApplicationContext()).add(bytes);
    }
}