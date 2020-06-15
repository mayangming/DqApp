//package com.meetqs.qingchat.carema.util;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * =====================================
// * 作    者: 陈嘉桐
// * 版    本：1.1.4
// * 创建日期：2017/4/25
// * 描    述：
// * =====================================
// */
//public class FileUtil {
//
//    /**
//     * 保存图片
//     * @param dir
//     * @param uid
//     * @param b
//     * @return
//     */
//    public static String saveBitmap(String dir, String uid, Bitmap b) {
//        File f = new File(dir);
//        if (!f.exists()) {
//            f.mkdir();
//        }
//        long dataTake = System.currentTimeMillis();
//        String jpegName = dir + File.separator + "android_" + uid + "_" + dataTake + ".jpg";
//        try {
//            FileOutputStream fout = new FileOutputStream(jpegName);
//            BufferedOutputStream bos = new BufferedOutputStream(fout);
//            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//            return jpegName;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    public static Bitmap compressImage(Bitmap image, int size) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while ( baos.toByteArray().length / 1024 > size) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
//
//
//    public static boolean deleteFile(String url) {
//        boolean result = false;
//        File file = new File(url);
//        if (file.exists()) {
//            result = file.delete();
//        }
//        return result;
//    }
//
//    public static boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//    }
//}
