package com.xmwdkk.boothprint;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;


/**
 * @author guochenhome@163.com
 *         data: Created by dell on 2017/11/21.  11;12
 */

public class KlnZxingUtil {

    /**
     * Created by KeithXiaoY on 2016/7/27.
     * 生成一个二维码图像
     *
     * @param url
     * 传入的字符串，通常是一个URL
     * @param widthAndHeight
     * 图像的宽高
     * @return
     */
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;

    public static Bitmap createQRCode(String str, int widthAndHeight){
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                } else {
                    pixels[y * width + x] = WHITE;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    //生成条形码
    public static Bitmap CreateOneDCode(String content){
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.CODE_128, 400, 100);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                } else {
                    pixels[y * width + x] = WHITE;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
