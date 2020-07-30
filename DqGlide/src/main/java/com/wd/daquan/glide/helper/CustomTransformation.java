package com.wd.daquan.glide.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;
import com.wd.daquan.glide.GlideUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class CustomTransformation extends BitmapTransformation {

    private int rotateRotationAngle;
    private int radius;

    public CustomTransformation(int rotateRotationAngle, int radius) {
        super();
        this.rotateRotationAngle = rotateRotationAngle;
        this.radius = GlideUtils.dp2px(radius);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform == null) return null;

        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        }

        // 图片设置角度
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, toTransform.getWidth(), toTransform.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 图片旋转
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateRotationAngle);
        Bitmap bitmap = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
        return bitmap;
    }

    private static final String ID = "com.aides.brother.brotheraides.glide.transform.CustomTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomTransformation) {
            CustomTransformation other = (CustomTransformation) o;
            return radius == other.radius;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(),
                Util.hashCode(radius));
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(radius).array();
        messageDigest.update(radiusData);
    }
}
