package com.wd.daquan.common.scancode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wd.daquan.DqApp;
import com.wd.daquan.common.utils.CNLog;
import com.da.library.tools.DensityUtil;

import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zht
 * 二维码扫描工具类
 */
public class QRCodeUtils {

    /**
     * 识别二维码
     * @param path
     * @param analyzeCallback
     */
    public static void recognitionQrcode(String path, AnalyzeCallback analyzeCallback) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
        //CNLog.w("xxxx", "原图width x height: " + mBitmap.getWidth() + " x " + mBitmap.getHeight());
        getResult(mBitmap, analyzeCallback);
    }

    /**
     * 被识别图片缩放
     * @param bmp
     * @param radio
     *  缩放比
     * @return
     */
    private static Bitmap scale(Bitmap bmp, float radio) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float ratio = 1;
        if (width > 400) {
            ratio = radio;
        } else {
            int screenWidth = DensityUtil.getScreenWidth(DqApp.sContext);
            ratio = (float) (screenWidth / width);
        }
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 20, width, height - 20, matrix, false);
        CNLog.w("xxxx", "缩放后width x height: " + bitmap.getWidth() + " x " + bitmap.getHeight());
        return bitmap;
    }

    /**
     * 验证二维码内容是否有效
     * @param scanResult
     * @return
     */
    private static boolean isValidCode(String scanResult) {
        String target = "^[0-9]*$";
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(scanResult);
        return matcher.find();
    }

    /**
     * 处理识别逻辑
     * @param bitmap
     * @param analyzeCallback
     */
    public static void getResult(Bitmap bitmap, AnalyzeCallback analyzeCallback) {
        Bitmap tmpBitmap = bitmap;
        String scanResult = "";
        scanResult = doQrCode(bitmap, scanResult);
        CNLog.w("xxxx", "isValidCode = " + isValidCode(scanResult));
        if (TextUtils.isEmpty(scanResult) || isValidCode(scanResult)) {
            CNLog.w("xxxx", "第二次进行缩放识别");
            // 进行缩放
            bitmap = scale(bitmap, 0.4f);
            scanResult = doQrCode(bitmap, scanResult);
            if (TextUtils.isEmpty(scanResult)) {
                CNLog.w("xxxx", "第三次进行缩放识别");
                bitmap = scale(tmpBitmap, 0.3f);
                scanResult = doQrCode(bitmap, scanResult);
                if (TextUtils.isEmpty(scanResult)) {
                    if (analyzeCallback != null) {
                        analyzeCallback.onFailed();
                    } else {
                        if (analyzeCallback != null) {
                            analyzeCallback.onSuccess(bitmap, scanResult);
                        }
                    }
                } else {
                    if (analyzeCallback != null) {
                        analyzeCallback.onSuccess(bitmap, scanResult);
                    }
                }
            } else {
                if (analyzeCallback != null) {
                    analyzeCallback.onSuccess(bitmap, scanResult);
                }
            }
        } else {
            if (analyzeCallback != null) {
                analyzeCallback.onSuccess(bitmap, scanResult);
            }
        }
    }

    /**
     * 解码
     * @param bitmap
     * @param scanResult
     * @return
     */
    private static String doQrCode(Bitmap bitmap, String scanResult) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>(3);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<>();
        if (decodeFormats.isEmpty()) {
            decodeFormats = new Vector<>();

            // 这里设置可扫描的类型，我这里选择了都支持
//            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(bitmap))));
            if (rawResult!=null){
                scanResult=rawResult.getText();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("xxxx", "二维码Error: "+ e.toString());
        }
        return scanResult;
    }

    /**
     * 生成二维码图片
     * @param text
     * @param w
     * @param h
     * @param logo
     * @return
     */
    public static Bitmap createImage(String text,int w,int h,Bitmap logo) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        try {
            Bitmap scaleLogo = getScaleLogo(logo,w,h);

            int offsetX = w / 2;
            int offsetY = h / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (w - scaleWidth) / 2;
                offsetY = (h - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if(x >= offsetX && x < offsetX + scaleWidth && y>= offsetY && y < offsetY + scaleHeight){
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        if(pixel == 0){
                            if(bitMatrix.get(x, y)){
                                pixel = 0xff000000;
                            }else{
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    }else{
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = 0xff000000;
                        } else {
                            pixels[y * w + x] = 0xffffffff;
                        }
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getScaleLogo(Bitmap logo,int w,int h) {
        if (logo == null) return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 6 / logo.getWidth(), h * 1.0f / 6 / logo.getHeight());
        matrix.postScale(scaleFactor, scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }


    /**
     * 生成二维码
     */
    public static Bitmap createQRImage(String url, int width, int height) {
        try {
            // 判断URL合法性
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>(3);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, ""+0);
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String test(Bitmap bitmap, AnalyzeCallback analyzeCallback) {
//        String scanResult = "";
////        byte[] intentUrl = getYUV420sp(bitmap.getWidth(), bitmap.getHeight(), bitmap);
//        byte[] intentUrl = rgb2YUV(bitmap);
//        // 处理
//        try {
//            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
//            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//            hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
//            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(intentUrl, bitmap.getWidth(), bitmap.getHeight(),
//                    0, 0,
//                    bitmap.getWidth(),
//                    bitmap.getHeight());
//            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
//            QRCodeReader reader2 = new QRCodeReader();
//            Result result = reader2.decode(bitmap1, hints);
//            if (null != result) {
//                scanResult = result.getText();
//                if (analyzeCallback != null) {
//                    analyzeCallback.onSuccess(bitmap, scanResult);
//                }
//            } else {
//                if (analyzeCallback != null) {
//                    analyzeCallback.onFailed();
//                }
//            }
//
//        } catch (Exception e) {
//            EBLog.w("xxxx", "error");
//            e.printStackTrace();
//            if (analyzeCallback != null) {
//                analyzeCallback.onFailed();
//            }
//        }
//
//        bitmap.recycle();
//        bitmap = null;
//        return scanResult;
//    }
//
//    public static byte[] rgb2YUV(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int[] pixels = new int[width * height];
//        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//
//        int len = width * height;
//        byte[] yuv = new byte[len * 3 / 2];
//        int y, u, v;
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                int rgb = pixels[i * width + j] & 0x00FFFFFF;
//
//                int r = rgb & 0xFF;
//                int g = (rgb >> 8) & 0xFF;
//                int b = (rgb >> 16) & 0xFF;
//
//                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
//                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
//                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
//
//                y = y < 16 ? 16 : (y > 255 ? 255 : y);
//                u = u < 0 ? 0 : (u > 255 ? 255 : u);
//                v = v < 0 ? 0 : (v > 255 ? 255 : v);
//
//                yuv[i * width + j] = (byte) y;
////                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
////                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
//            }
//        }
//        return yuv;
//    }

    /**
     * 解析二维码结果
     */
    public interface AnalyzeCallback{

        public void onSuccess(Bitmap mBitmap, String result);

        public void onFailed();
    }
}
