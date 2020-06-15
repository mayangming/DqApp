package com.wd.daquan.net.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 20:45.
 * @description: todo ...
 */
public class ImageParser extends BaseParser {
    public ImageParser() {
        super(ParseType.IMAGE);
    }

    @Override
    public Bitmap parse(Response response) {
        Bitmap bmp = null;
        if (response.isSuccessful()) {
            try {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    bmp = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                    bmp = null;
                } finally {
                    if (null != is) {
                        is.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bmp;
    }
}
