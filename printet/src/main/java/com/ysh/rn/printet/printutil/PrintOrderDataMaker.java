package com.ysh.rn.printet.printutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;


import com.ysh.rn.printet.R;
import com.ysh.rn.printet.entity.OrderInfoEntity;
import com.ysh.rn.printet.print.GPrinterCommand;
import com.ysh.rn.printet.print.PrintPic;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 测试数据生成器
 * Created by guochen on 8/1/17.
 */

public class PrintOrderDataMaker  implements PrintDataMaker {


    private String qr;
    private int width;
    private int height;
    Context btService;

    private OrderInfoEntity orderInfoEntity;

    public PrintOrderDataMaker(Context btService, String qr, int width, int height, OrderInfoEntity entity) {
        this.qr = qr;
        this.width = width;
        this.height = height;
        this.btService = btService;
        this.orderInfoEntity = entity;
    }


    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();

        try {
            PrinterWriter printer;
            printer = type == PrinterWriter58mm.TYPE_58 ? new PrinterWriter58mm(height, width) : new PrinterWriter80mm(height, width);
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
            /**
             * 打印编码条形码
             */
            if (this.orderInfoEntity.getOrder_number_code() != null) {
                ArrayList<byte[]> image1 = printer.getImageByte(this.orderInfoEntity.getOrder_number_code());
                data.addAll(image1);
            }

            printer.setAlignLeft();
            printer.printLine();
            printer.printLineFeed();

            printer.printLineFeed();
            printer.setAlignCenter();
            printer.setEmphasizedOn();
            printer.setFontSize(1);
            printer.print(this.orderInfoEntity.getTitle());
            printer.printLineFeed();
            printer.setEmphasizedOff();
            printer.printLineFeed();


            printer.printLineFeed();
            printer.setFontSize(0);
            printer.setAlignCenter();
            printer.print("订单编号：" + this.orderInfoEntity.getOrder_number());
            printer.printLineFeed();

            printer.setAlignCenter();
            printer.print(new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                    .format(new Date(System.currentTimeMillis())));
            printer.printLineFeed();
            printer.printLine();

            printer.printLineFeed();
            printer.setAlignLeft();
            printer.print("订单状态: " + "已接单");
            printer.printLineFeed();
            printer.print("用户昵称: " + "周末先生");
            printer.printLineFeed();
            printer.print("用餐人数: " + "10人");
            printer.printLineFeed();
            printer.print("用餐桌号:" + "A3" + "号桌");
            printer.printLineFeed();
            printer.print("预定时间：" + "2017-10-1 17：00");
            printer.printLineFeed();
            printer.print("预留时间：30分钟");
            printer.printLineFeed();
            printer.print("联系方式：" + "18910489488");
            printer.printLineFeed();
            printer.printLine();
            printer.printLineFeed();

            printer.setAlignLeft();
            printer.print("备注：" + "记得留位置");
            printer.printLineFeed();
            printer.printLine();

            printer.printLineFeed();

            printer.setAlignCenter();
            printer.print("购买信息");
            printer.printLineFeed();
            printer.setAlignCenter();
            printer.printInOneLine("药品", "数量", "单价", 0);
            printer.printLineFeed();
            for (int i = 0; i < 3; i++) {

                printer.printInOneLine("干锅包菜", "X" + 3, "￥" + 30, 0);
                printer.printLineFeed();
            }
            printer.printLineFeed();
            printer.printLine();
            printer.printLineFeed();
            printer.setAlignLeft();
            printer.printInOneLine("菜品总额：", "￥" + 100, 0);


            printer.setAlignLeft();
            printer.printInOneLine("优惠金额：", "￥" + "0.00"
                    , 0);
            printer.printLineFeed();

            printer.setAlignLeft();
            printer.printInOneLine("订金/退款：", "￥" + "0.00"
                    , 0);
            printer.printLineFeed();


            printer.setAlignLeft();
            printer.printInOneLine("总计金额：", "￥" + 90, 0);
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();
            printer.setAlignCenter();
            printer.print("谢谢惠顾，欢迎再次光临！");
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.feedPaperCutPartial();

            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<byte[]> getPrintImage(int type, Bitmap bitmap) {
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
        return printBytes;
    }
}
