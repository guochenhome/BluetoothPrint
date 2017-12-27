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

public class PrintOrderDataMaker implements PrintDataMaker {


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
            PrinterWriter printer = type == PrinterWriter58mm.TYPE_58 ? new PrinterWriter58mm(height, width) : new PrinterWriter80mm(height, width);
            PrintPic printPic = PrintPic.getInstance();
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());

            /**
             * title
             */
            printer.setAlignCenter();
            printer.setEmphasizedOn();
            printer.setFontSize(1);
            printer.print(this.orderInfoEntity.getTitle());
            printer.printLineFeed();
            printer.setEmphasizedOff();
            printer.printLineFeed();

            /**
             * 订单说明
             */
            printer.printLineFeed();
            printer.setFontSize(0);
            printer.setAlignCenter();
            printer.print(this.orderInfoEntity.getInfo() + "\n");
            printer.printLineFeed();

            data.add(printer.getDataAndReset());

            /**
             * 打印编码条形码
             */
            if (this.orderInfoEntity.getOrder_number_code()!=null&&!"".equals(this.orderInfoEntity.getOrder_number_code())) {
                ArrayList<byte[]> image = printer.getImageByte(this.orderInfoEntity.getOrder_number_code());
                data.addAll(image);
            }


            /**
             * 虚线
             */
            printer.setAlignLeft();
            printer.printLine();
            printer.printLineFeed();

            printer.printLineFeed();
            printer.setAlignLeft();
            printer.print("订单编号: " + this.orderInfoEntity.getOrder_number());
            printer.printLineFeed();
            printer.print("订单时间: " + new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
            printer.printLineFeed();
            printer.print("地址    : " + this.orderInfoEntity.getAddress());
            printer.printLineFeed();
            printer.print("联系方式:" + this.orderInfoEntity.getPhone());
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();
//----------------------------------------------------药品信息----------------------------------------------------------------------------------
            if (orderInfoEntity.getList() != null && orderInfoEntity.getList().size() > 0) {
                printer.setAlignCenter();
                printer.print("药品信息\n");
                printer.printLineFeed();

                printer.setAlignLeft();
                if (orderInfoEntity.getAll_pirce() != null && !orderInfoEntity.getAll_pirce().equals("")) {
                    printer.printInOneLine("药品", "数量", "单价", 0);
                } else {
                    printer.printInOneLine("药品", "数量", 0);
                }
                printer.printLineFeed();

                for (int i = 0; i < orderInfoEntity.getList().size(); i++) {
                    /*
                    显示价格和不显示价格
                     */
                    if (orderInfoEntity.getAll_pirce() != null && !orderInfoEntity.getAll_pirce().equals("")) {
                        printer.printInOneLine(orderInfoEntity.getList().get(i).getName(), "X" + orderInfoEntity.getList().get(i).getCount(), orderInfoEntity.getList().get(i).getPrice(), 0);
                        printer.printLineFeed();
                    } else {
                        printer.printInOneLine(orderInfoEntity.getList().get(i).getName(), "X" + orderInfoEntity.getList().get(i).getCount(), 0);
                        printer.printLineFeed();
                    }

                }
                printer.printLineFeed();
                printer.printLine();
                printer.printLineFeed();
            }
//======================================================总价格模块===============================================================================
            if (orderInfoEntity.getAll_pirce() != null && !orderInfoEntity.getAll_pirce().equals("")) {
                printer.setAlignLeft();
                printer.printInOneLine("总计：","", orderInfoEntity.getAll_pirce(), 0);
                printer.printLine();
                printer.printLineFeed();
            }
//=========================================患者姓名=====================================================================================
            if (orderInfoEntity.getName() != null && !orderInfoEntity.getName().equals("")) {
                printer.setAlignLeft();
                printer.print("患者姓名：" + orderInfoEntity.getName());
                printer.printLineFeed();
                printer.printLine();
                printer.printLineFeed();
            }
            //=============================诊断说明============================================================================
            if (orderInfoEntity.getExplain() != null && !orderInfoEntity.getExplain().equals("")) {
                printer.setAlignLeft();
                printer.print("疾病诊断：" + orderInfoEntity.getExplain());
                printer.printLineFeed();
                printer.printLine();
                printer.printLineFeed();
            }

//=====================================================二维码模块==============================================================
            if (orderInfoEntity.getErwema_coder() != null && !orderInfoEntity.getErwema_coder().equals("")) {
                printer.setAlignCenter();
                printer.print(this.orderInfoEntity.getRewema_string());
                printer.printLineFeed();
                data.add(printer.getDataAndReset());

                ArrayList<byte[]> image1 = printer.getImageByte(this.orderInfoEntity.getErwema_coder());
                data.addAll(image1);

                printer.printLine();
            }
//========================================欢迎语==及签名==============================================================
            printer.setAlignCenter();
            printer.print(this.orderInfoEntity.getTankinfo() + "\n");
            printer.printLineFeed();
            printer.print("\n");
            printer.printLineFeed();
            printer.print("\n");
            printer.printLineFeed();
            printer.print("\n");
            printer.printLineFeed();

            //============================================================================================================
            // =====================================底单部分=============================================================
            //============================================================================================================
            if(orderInfoEntity.getIsRes()) {
                /**
                 * title
                 */
                printer.setAlignCenter();
                printer.setEmphasizedOn();
                printer.setFontSize(1);
                printer.print(this.orderInfoEntity.getTitle()+"(底单)");
                printer.printLineFeed();
                printer.setEmphasizedOff();
                printer.printLineFeed();

                /**
                 * 订单说明
                 */
                printer.printLineFeed();
                printer.setFontSize(0);
                printer.setAlignCenter();
                printer.print(this.orderInfoEntity.getInfo() + "\n");
                printer.printLineFeed();
                /**
                 * 虚线
                 */
                printer.setAlignLeft();
                printer.printLine();
                printer.printLineFeed();
                /**
                 * 信息部分
                 */
                printer.printLineFeed();
                printer.setAlignLeft();
                printer.print("订单编号: " + this.orderInfoEntity.getOrder_number());
                printer.printLineFeed();
                printer.print("订单时间: " + new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
                printer.printLineFeed();
                printer.print("地址    : " + this.orderInfoEntity.getAddress());
                printer.printLineFeed();
                printer.print("联系方式:" + this.orderInfoEntity.getPhone());
                printer.printLineFeed();

                printer.printLine();
                printer.printLineFeed();

                //----------------------------------------------------药品信息----------------------------------------------------------------------------------
                if (orderInfoEntity.getList() != null && orderInfoEntity.getList().size() > 0) {
                    printer.setAlignCenter();
                    printer.print("药品信息\n");
                    printer.printLineFeed();

                    printer.setAlignLeft();
                    if (orderInfoEntity.getAll_pirce() != null && !orderInfoEntity.getAll_pirce().equals("")) {
                        printer.printInOneLine("药品", "数量", "单价", 0);
                    } else {
                        printer.printInOneLine("药品", "数量", 0);
                    }
                    printer.printLineFeed();

                    for (int i = 0; i < orderInfoEntity.getList().size(); i++) {
                    /*
                    显示价格和不显示价格
                     */
                        if (orderInfoEntity.getAll_pirce() != null && !orderInfoEntity.getAll_pirce().equals("")) {
                            printer.printInOneLine(orderInfoEntity.getList().get(i).getName(), "X" + orderInfoEntity.getList().get(i).getCount(), orderInfoEntity.getList().get(i).getPrice(), 0);
                            printer.printLineFeed();
                        } else {
                            printer.printInOneLine(orderInfoEntity.getList().get(i).getName(), "X" + orderInfoEntity.getList().get(i).getCount(), 0);
                            printer.printLineFeed();
                        }

                    }
                    printer.printLineFeed();
                    printer.printLine();
                    printer.printLineFeed();
                }
                //============================患者姓名================================================
                if (orderInfoEntity.getName() != null && !orderInfoEntity.getName().equals("")) {
                    printer.setAlignLeft();
                    printer.print("患者姓名：" + orderInfoEntity.getName());
                    printer.printLineFeed();
                    printer.printLine();
                    printer.printLineFeed();
                }
                //=============================诊断说明============================================================================
                if (orderInfoEntity.getExplain() != null && !orderInfoEntity.getExplain().equals("")) {
                    printer.setAlignLeft();
                    printer.print("疾病诊断：" + orderInfoEntity.getExplain());
                    printer.printLineFeed();
                    printer.printLine();
                    printer.printLineFeed();
                }
                printer.print("\n");
                printer.printLineFeed();
                printer.print("\n");
                printer.printLineFeed();

            }

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
