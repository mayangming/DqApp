package com.wd.daquan.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.wd.daquan.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class GetImageUtils {

    public static final int REQUEST_CODE_FROM_CAMERA = 5001;
    public static final int REQUEST_CODE_FROM_ALBUM = 5002;
    public static final int REQUEST_CODE_FROM_CUTTING = 5003;
    /**
     * 存放拍照图片的uri地址
     */
    public static Uri imageUriFromCamera;
    private static File file;

    /**
     * 打开相机拍照获取图片
     */
    public static void pickImageFromCamera(final Activity activity, Activity fragment) {

        imageUriFromCamera = createImageUri(activity);

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
        fragment.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
    }
    /**
     * 打开相机拍照获取图片
     */
    public static void pickImageFromCameras(Context context, Activity fragment) {

        imageUriFromCamera = createImageUri(context);

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
        fragment.startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
    }

    /**
     * 打开本地相册选取图片
     */
    public static void pickImageFromAlbum(final Activity activity, Activity personCenterFragment) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        personCenterFragment.startActivityForResult(intent, REQUEST_CODE_FROM_ALBUM);
    }

    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    /**
     * 根据路径获取图片并压缩, 返回bitmap用于显示 保证图片在100K一下
     *
     * @param filePath 图片所在的路径
     * @param width    图片要显示的宽度
     * @param height   图片要显示的宽度
     * @return 图片Bitmap对象
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // 计算压缩比例
        options.inSampleSize = calculateInSampleSize(options, width, height);
        // 根据压缩比例,压缩图片文件
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        System.out.println("gesmallBitmap");
        // int degree = readPictureDegree(filePath);
        // Bitmap bm = rotateBitmap(bitmap,degree) ;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // 保证图片在500K一下
        while (baos.toByteArray().length > 1024 * 500) {
            baos.reset();
            quality -= 1;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(bais);

        return bitmap;
    }


    /**
     * 计算图片的压缩比例
     *
     * @param options   图片的设置
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;// 不压缩
        if (height > reqHeight || width > reqWidth) {// 是否需要压缩
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            return inSampleSize;
        } else {
            return inSampleSize;
        }
    }



    /**
     * 创建一条图片uri,用于保存拍照后的照片
     */
    private static Uri createImageUri(Context context) {
        String name = "boreWbImg" + System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;
    }

    /**
     * 删除一条图片
     */
    public static void deleteImageUri(Context context, Uri uri) {
        context.getContentResolver().delete(imageUriFromCamera, null, null);
    }

    /**
     * 获取图片文件路径
     */
    public static String getImageAbsolutePath(Context context, Uri uri) {
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri,
                new String[]{MediaStore.Images.Media.DATA});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    /////////////////////Android4.4以上版本特殊处理如下//////////////////////////////////////

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImageAbsolutePath19(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore (and general)
        if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static Bitmap imageCrop(Bitmap bitmap, int width) {
        // 得到图片的宽，高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

//        int width = w > h ? h : w;

        int retX = (w - width) / 2;
        int retY = (h - width) / 2;

        return Bitmap.createBitmap(bitmap, retX, retY, width, width, null, false);
    }

    /**
     *
     * jietu
     * by zht
     */
    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        //把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            result = context.getResources().getDimensionPixelSize(R.dimen.dd_dp25);
        }
        return result;
    }

    public static DisplayMetrics getWindowDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getActionBarHeight(Context context) {
        int[] attrs = {android.R.attr.actionBarSize};
        TypedArray values = context.getTheme().obtainStyledAttributes(attrs);
        int actionBarHeight = values.getDimensionPixelSize(0, 0);
        values.recycle();

        if (actionBarHeight <= 0) {
            actionBarHeight = context.getResources().getDimensionPixelSize(R.dimen.dd_dp54);
        }

        return actionBarHeight;
    }


    public static Bitmap generateImageFromView(View view, int width, int height) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, width, height);
        Bitmap image = Bitmap.createBitmap(view.getDrawingCache());
        view.destroyDrawingCache();

        return image;
    }


    /**
     * 将图片保存到系统相册
     *
     * @param context
     * @param bmp
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        File file = createFile();

        FileOutputStream fos = null;
        boolean isSuccess = false;

        try {
            fos = new FileOutputStream(file);
            isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return isSuccess;
    }

    /**
     * 将图片保存到系统相册
     *
     * @param context 引用对象
     * @param bmp 图片
     * @return File
     */
    public static File saveImage2Gallery(Context context, Bitmap bmp) {
        File file = createFile();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.flush();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;
    }

    /**
     * 创建文件
     * @return
     */
    @NonNull
    private static File createFile() {
        String galleryPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES;
        File galleryDir = new File(galleryPath);
        if (!galleryDir.exists()) {
            galleryDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".png";
        return new File(galleryPath, fileName);
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap getBtimap(View view) {
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
//        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        Bitmap cacheBitmap = view.getDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(cacheBitmap);
        view.destroyDrawingCache();
        return bmp;
    }

}
