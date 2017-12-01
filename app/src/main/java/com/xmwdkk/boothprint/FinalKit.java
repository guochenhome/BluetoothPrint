package com.xmwdkk.boothprint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具包
 */
public final class FinalKit {

    public static Context getContext() {
        if (App.get().getCurrentActivity() == null) {
            return App.get();
        }
        return App.get().getCurrentActivity();
    }

    /**
     * 从资源文件中获取带有{0}和{1,number,short}和{2,number,#.#}格式的字符串并格式化
     *
     * @param rsId 字符串资的ID
     * @param args 替换的大括号内的替换值
     * @return 返回格式化后的字符串
     */
    public static String getString(int rsId, Object... args) {
        MessageFormat mf = new MessageFormat(getContext().getResources().getText(rsId).toString());
        return mf.format(args);
    }

    public static String getString(String pattern, Object... args) {
        MessageFormat mf = new MessageFormat(pattern);
        return mf.format(args);
    }

//    /**
//     * 直接将界面上的值格式化在输出
//     *
//     * @param tv   需要设置的控件
//     * @param args 格式化需要的参数
//     */
//    public static void fill(TextView tv, Object... args) {
//        String text = StringUtils.isBlank(tv.getHint()) ? tv.getText().toString() : tv.getHint().toString();
//        MessageFormat mf = new MessageFormat(text);
//        if (args == null) {
//            for (Object arg : args) {
//                if (arg == null) arg = "";
//            }
//        }
//        tv.setText(mf.format(args));
//    }

    //==========================
    //文件缓存目录等处理
    //==========================
    public static boolean isExternalStorageRemovable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static File getExternalCacheDir() {
        File cacheFile = null;
        if (hasExternalCacheDir()) {
            cacheFile = getContext().getExternalCacheDir();
        }
        if (cacheFile == null) {
            // Before Froyo we need to construct the external cache dir ourselves
            final String cacheDir = "/Android/data/" + getContext().getPackageName() + "/cache/";
            cacheFile = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
        return cacheFile;
    }

    public static String getDiskCacheDir(String... folders) {
        return getDiskCacheFile(folders) + File.separator;
    }

    public static File getDiskCacheFile(String... folders) {
        String cacheRootPath = FinalKit.isExternalStorageRemovable() ? FinalKit.getExternalCacheDir().getPath()
            : getContext().getCacheDir().getPath();
        if (folders != null) {
            StringBuilder temp = new StringBuilder(cacheRootPath);
            for (String s : folders) {
                temp.append(File.separator).append(s);
            }
            cacheRootPath = temp.toString();
        }
        File file = new File(cacheRootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 将Assets中的指定文件复制到SD卡中
     *
     * @param srcFileName    源文件目录及名称
     * @param targetFileName 目标文件目录及名称
     */
    public static void copyAssets(String srcFileName, String targetFileName) {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(targetFileName);
            myInput = getContext().getAssets().open(srcFileName);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取目录大小
     */
    public static long getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {// 如果是文件则直接返回其大小,以“兆”为单位
                return file.length();
            }
        } else {
            return 0l;
        }
    }

    /**
     * 获取可用空间
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableSpace(File dir) {
        try {
            final StatFs stats = new StatFs(dir.getPath());
            return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    // bt字节参考量
    public static final long SIZE_BT = 1024L;
    // KB字节参考量
    public static final long SIZE_KB = SIZE_BT * 1024L;
    // MB字节参考量
    public static final long SIZE_MB = SIZE_KB * 1024L;
    // GB字节参考量
    public static final long SIZE_GB = SIZE_MB * 1024L;
    // TB字节参考量
    public static final long SIZE_TB = SIZE_GB * 1024L;
    //保留小叔的位数
    public static final int SACLE = 2;

    /**
     * 获取文件的大小自动处理成适合的单位
     */
    public static String getDataSize(long longSize) {
        if (longSize >= SIZE_KB && longSize < SIZE_MB) {
            BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_KB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            return result + "MB";
        } else if (longSize >= SIZE_BT && longSize < SIZE_KB) {
            return longSize / SIZE_BT + "KB";
        } else if (longSize >= 0 && longSize < SIZE_BT) {
            return longSize + "B";
        } else if (longSize >= SIZE_MB && longSize < SIZE_GB) {
            BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_MB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            return result + "GB";
        } else {
            BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_GB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            return result + "TB";
        }
    }

    // ===============================
    // 开始共享参数公用处理方法
    // ===============================
    private static SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(getPackageName(), getContext().MODE_PRIVATE);
    }

    public static void clearShared() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean fetchBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static boolean fetchBoolean(String key) {
        return fetchBoolean(key, false);
    }

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String fetchString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public static String fetchString(String key) {
        return fetchString(key, null);
    }

    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int fetchInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public static int fetchInt(String key) {
        return fetchInt(key, 0);
    }

    public static void putFloat(String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static Float fetchFloat(String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    public static Float fetchFloat(String key) {
        return fetchFloat(key, 0);
    }
    //==========================
    //使用Uri获取文件路径
    //==========================

    @SuppressLint("NewApi")
    public static String getFilePathFromContentUri(final Uri uri, final Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                    Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The getContext().
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = getContext().getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
            new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    //========================
    // 获取应用的一些基本属性的工具方法
    //========================

//    /**
//     * 获取机器的mac地址 需要添加权限 <br/>
//     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
//     */
//    public static String getMacAddress() {
//        WifiManager wifi = (WifiManager) getContext().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();
//    }

    /**
     * 取得版本号
     */
    public static String getVersion() {
        try {
            PackageInfo manager = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static int getVersionCode() {
        try {
            PackageInfo manager = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            return manager.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static String getIMEI() {
        return ((TelephonyManager) getContext().getSystemService(Activity.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取应用的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 获取配置文件中的META配置数据的值
     */
    public static String getMetaValue(String metaKey) {
        Bundle metaData = null;
        String resultValue = null;
        if (getContext() == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(getPackageName(),
                PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                resultValue = String.valueOf(metaData.get(metaKey));
            }
        } catch (NameNotFoundException e) {
        }
        return resultValue;
    }

    /**
     * 获取手机系统的默认语言设置,返回一个默认的语言代码及配置的第一项
     */
    public static String getLocale() {
        return Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
    }

    /**
     * 检查网络文件是否可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) { // 能联网
            return true;
        } else { // 不能联网
            return false;
        }
    }

    /**
     * 判断应用是否在运行
     */
    public static boolean isAppRunning() {
        ActivityManager am = (ActivityManager) getContext().getSystemService(getContext().ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String PACKAGENAME = getPackageName();
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(PACKAGENAME)
                || info.baseActivity.getPackageName().equals(PACKAGENAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 重启应用
     */
    public static void restartApplication(Context context) {
        if (context != null) {
            //1.第一种
            Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getContext().startActivity(i);
            //2.第三种
            //PendingIntent intent = PendingIntent.getActivity(activity.getBaseContext(), 0, new Intent(activity.getIntent()), activity.getIntent().getFlags());
            //AlarmManager mgr = (AlarmManager) activity.getSystemService(getContext().ALARM_SERVICE);
            //mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
            System.exit(2);
        }
    }
    //=========================
    //常用的数学公式工具类
    //=========================

    /**
     * 根据半径和角度计算圆上的点
     */
    public static Point getPointOnCircle(double radius, int angle) {
        Point point = new Point();
        point.x = (int) (radius * Math.cos(angle * Math.PI / 180));// 关键在这，把角度换成弧度。
        point.y = (int) (radius * Math.sin(angle * Math.PI / 180));
        return point;
    }

    /**
     * 根据经纬度计算两点距离
     */
    public static String getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lon1 * Math.PI / 180 - lon2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
            * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return getDistance(s);
    }

    public static String getDistance(double distance) {
        if (distance < 0) {
            return distance + "米";
        } else if (distance < 1000) {
            return ((int) distance) + "米";
        } else {
            return ((int) (distance / 1000)) + "千米";
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    // ===============================
    // 数据验证的方法
    // ===============================

    /**
     * 手机号验证
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 电话号码验证
     *
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p = Pattern
            .compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)"); // 验证手机号
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 邮箱验证
     *
     * @return 验证通过返回true
     */
    public static boolean isEmail(String str) {
        Pattern p = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"); // 验证邮箱
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void requestFocusWithInputMethod(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

}
