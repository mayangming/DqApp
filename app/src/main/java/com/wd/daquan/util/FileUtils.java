package com.wd.daquan.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.dq.im.ImProvider;
import com.wd.daquan.DqApp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    /**
     * InputStream转化为byte[]数组
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException,OutOfMemoryError {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
    /**
     *
     * <p>Title: toByteArray</p>
     * <p>Description: 传统的IO流方式</p>
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                bos.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }
    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    public static void getPath(Context context){
        Log.e("YM","开始读取本地图片");
        Cursor imageCursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[4] + " DESC");
        if (null == imageCursor){
            Log.e("YM","查询不到本地图片路径");
            return;
        }
        imageCursor.moveToFirst();
        while (imageCursor.moveToNext()){
            String path = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
            String name = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
            int id = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
            String folderPath = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
            String folderName = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
//Android Q 公有目录只能通过Content Uri + id的方式访问，以前的File路径全部无效，如果是Video，记得换成MediaStore.Videos
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                path  = MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI
                        .buildUpon()
                        .appendPath(String.valueOf(id)).build().toString();
            }
            Log.e("YM","查询的路径path:"+path);
            Log.e("YM","查询的路径id:"+id);
            Log.e("YM","查询的路径name:"+name);
            Log.e("YM","查询的路径folderPath:"+folderPath);
            Log.e("YM","查询的路径folderName:"+folderName);
        }

        Log.e("YM","开始读取本地图片---end");
    }

    /**
     * 文件是否存在
     * @param uri 本地Uri路径
     */
    public static boolean fileExists(Uri uri){
        DocumentFile videoDocumentFile = DocumentFile.fromSingleUri(DqApp.sContext, uri);
        return videoDocumentFile.exists();
    }

    /**
     * 文件是否存在
     * @param localPath 本地路径，会转成Uri再进行判断
     */
    public static boolean fileExists(String localPath){
        boolean isExists = false;
        if (TextUtils.isEmpty(localPath)){
            return isExists;
        }
        if (Build.VERSION.SDK_INT>=29) {//android 10
            Uri uri = Uri.parse(localPath);
            DocumentFile videoDocumentFile = DocumentFile.fromSingleUri(DqApp.sContext, uri);
            isExists = videoDocumentFile.exists();
        }else {
            File file = new File(localPath);
            isExists = file.exists();
        }
        return isExists;
    }

    /**
     * 保存Bitmap到本地
     * @param bitmap
     * @return
     */
    public static Uri saveBitmap(Bitmap bitmap){
        Uri uri = createImageFile();
        ImProvider.context.grantUriPermission(ImProvider.context.getPackageName(), uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            ParcelFileDescriptor imageFd = ImProvider.context.getContentResolver().openFileDescriptor(uri, "w");
            FileDescriptor thumb = imageFd.getFileDescriptor();
            FileOutputStream fileOutputStream = new FileOutputStream(thumb);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }
    private static Uri createImageFile(){
        Uri uri;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.png", timeStamp);
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,imageFileName);
        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, "image/png");//修改文件类型，默认为jpg
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            uri = ImProvider.context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            uri = ImProvider.context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
        }
        Log.e("YM","文件路径:"+uri.toString());
        return uri;
    }
    /**
     * 获取文件名字的后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName){
        if (TextUtils.isEmpty(fileName)){
            fileName = "example.png";
        }
        String result = fileName;
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex);
        return result;
    }
}