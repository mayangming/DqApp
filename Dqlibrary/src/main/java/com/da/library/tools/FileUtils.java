package com.da.library.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.da.library.DqLibConfig;
import com.da.library.constant.DirConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * 2019/5/10 fangzhi重构
 */
public class FileUtils {
    private static String TAG = "FileUtils";
    //    private static final String HOST = "http://t.file.sy.qingic.com/";
    public final static String PATTERN = "yyyyMMddHHmmss";

    /**
     * 是否需要下载文件
     *
     * @return -1：文件不存在
     * 1：本地文件已经保存到相册
     */
    public static long isDownloadFile(Context context, long fileSize, String localPath) {
        long curDownloadSize;
        File localFile = new File(localPath);
        if (localFile.exists()) {
            curDownloadSize = localFile.length();
            if (fileSize == curDownloadSize) { // 直接保存到相册
                savePhotoAlbum(context, localFile);
                return 1;
            } else if (curDownloadSize > fileSize) { // 删掉文件重新下载
                localFile.delete();
                Log.w("xxxx", "本地文件被删除：");
                return 0;
            } else { // 断点下载
                return curDownloadSize;
            }
        } else {
            return 0;
        }
    }
    /**
     * 是否需要下载文件
     *
     * @return -1：文件不存在
     * 1：本地文件已经保存到相册
     */
    public static long isDownloadFile(Context context, File file) {
        if(file == null) {
            return -1;
        }
        if (file.exists()) {
            savePhotoAlbum(context, file);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 存入相册
     */
    public static void savePhotoAlbum(Context context, File file) {
        //保存图片后发送广播通知更新数据库
        Uri uri = Uri.fromFile(file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }


    /**
     * 获取网络文件大小
     */
    public static long getNetworkFileSize(String url) {
        URL mUrl;
        try {
            mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            //处理下载读取长度为-1 问题
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            // 获取文件大小

            return conn.getContentLength();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static File writeCNFile(String fileName, InputStream in) {
        int indexEnd = fileName.lastIndexOf(".");
        String fileFormat = "";
        if(indexEnd > 0) {
            fileFormat = fileName.substring(indexEnd + 1, fileName.length());
        }
        return writeCNFile(fileName, DirConstants.DIR_FILE, fileFormat, in);
    }

    /**
     *
     * @param fileName 文件名
     * @param fileDir 文件夹
     * @param fileFormat 文件后缀格式
     * @param inputStream 输入流
     */
    public static File writeCNFile(String fileName, String fileDir, String fileFormat, InputStream inputStream) {
        OutputStream os = null;
        try {
            int indexStart = fileName.lastIndexOf("/");
            int indexEnd = fileName.lastIndexOf(".");
            if(indexEnd > 0) {
                fileName = fileName.substring(indexStart + 1, indexEnd + 1) + fileFormat;
            }else {
                fileName = fileName.substring(indexStart + 1, fileName.length()) + "." + fileFormat;
            }

            File file = new File(fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }

            File targetFile = new File(file, fileName);
            os = new FileOutputStream(targetFile, true);
            int len;
            byte[] buf = new byte[1024 * 8];
            while ((len = inputStream.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
            return targetFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                assert os != null;
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    // 包名/file/qc/images/消息ID.jpg

    /**
     * 保存bitmap图片到本地
     */
    public static File saveBitmap(Context context, Bitmap bitmap) {
        return saveBitmap(context, bitmap, DirConstants.DIR_IMAGES);
    }

    /**
     * 保存bitmap图片到本地
     * @param fileDir 文件夹
     */
    public static File saveBitmap(Context context, Bitmap bitmap, String fileDir) {
        return saveBitmap(context, bitmap, fileDir, "");
    }

    /**
     * 保存bitmap图片到本地
     * @param fileDir 文件夹
     * @param fileName 文件名
     */
    public static File saveBitmap(Context context, Bitmap bitmap, String fileDir, String fileName) {
        File file = createFile(context, fileDir, fileName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @NonNull
    public static File createFile(Context context, String fileName){
        return createFile(context, DirConstants.DIR_FILE, fileName);
    }

    @NonNull
    public static File createImg(Context context){
        return createFile(context, DirConstants.DIR_CAMERA, null);
    }

    @NonNull
    public static File createFile(Context context, String fileDir, String fileName){
        return createFile(context, fileDir, fileName, ".jpg");
    }

    /**
     *
     * @param fileDir 文件夹
     * @param fileName 文件名
     * @param fileFormat 文件类型
     */
    @NonNull
    public static File createFile(Context context, String fileDir, String fileName, String fileFormat){

        File dir = new File(fileDir);
        if(TextUtils.isEmpty(fileName)) {
            fileName = "android_" + System.currentTimeMillis() + fileFormat;
        }else {
            fileName = fileName + fileFormat;
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, fileName);
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, fileName);
        }
    }

    /**
     * 是不是jpg格式
     */
    public static boolean isJpgFile(File file) {
        try {
            FileInputStream bin = new FileInputStream(file);
            int b[] = new int[4];
            b[0] = bin.read();
            b[1] = bin.read();
            bin.skip(bin.available() - 2);
            b[2] = bin.read();
            b[3] = bin.read();
            bin.close();
            return b[0] == 255 && b[1] == 216 && b[2] == 255 && b[3] == 217;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getInternalCachePath(Context context, @NonNull String dir) {
        File cacheDir = new File(context.getCacheDir().getPath() + File.separator + dir);
        if (!cacheDir.exists()) {
            boolean result = cacheDir.mkdir();
            Log.w("clll", "getInternalCachePath = " + cacheDir.getPath() + ", result = " + result);
        }

        return cacheDir.getPath();
    }

    public static File copyFile(Context context, String oldPath) {
        return copyFile(context, oldPath, DirConstants.DIR_FILE);
    }

    public static File copyFile(Context context, String oldPath, String firDir) {
        return copyFile(context, oldPath, firDir, ".jpg");
    }

    /**
     *
     * @param oldPath 旧文件路径
     * @param firDir 新文件夹
     * @param fileFormat 文件类型
     */
    public static File copyFile(Context context, String oldPath, String firDir, String fileFormat) {

        File oldFile = new File(oldPath);
        File targetFile = createFile(context, firDir, "", fileFormat);

        return copyFile(oldFile, targetFile);
    }

    @NonNull
    public static File copyFile(@NonNull File oldFile, @NonNull File targetFile) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(oldFile).getChannel();
            outputChannel = new FileOutputStream(targetFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputChannel != null) {
                try {
                    outputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return targetFile;
    }
    /**
     * 保存图片，获取文件路径
     * @param dir 文件夹
     * @param uid 文件名扩展字段
     */
    public static String saveBitmap(String dir, String uid, Bitmap bitmap) {

        String fileName = uid + "_" + System.currentTimeMillis();
        File file = saveBitmap(DqLibConfig.sContext, bitmap, dir, fileName);
       return file.getAbsolutePath();
    }

    /**
     * 获取uri文件地址
     */
    public static String getRealFilePath(Context context, Uri uri ) {
        if ( null == uri ) return null;
        String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals(scheme) ) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme) ) {
            Cursor cursor =context.getContentResolver().query( uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA },
                    null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
