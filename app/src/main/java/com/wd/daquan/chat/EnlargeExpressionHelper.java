package com.wd.daquan.chat;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wd.daquan.R;
import com.wd.daquan.glide.GlideUtils;

/**
 * @author: dukangkang
 * @date: 2018/9/15 20:10.
 * @description: todo ...
 */
public class EnlargeExpressionHelper {
    private int width = 0;
    private int height = 0;
    private String mPath = "";
    private Context mContext = null;
    private Dialog mDialog = null;
    private ImageView mImageView = null;

    public EnlargeExpressionHelper(Context context) {
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.enlarge_expression_dialog, null);
        mDialog = new Dialog(context, R.style.dialog_un_fullscreen);
        mDialog.setContentView(view);
        mImageView = (ImageView) view.findViewById(R.id.enlarge_expression_iv);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public EnlargeExpressionHelper setWidth(int width) {
        this.width = width;
        return this;
    }

    public EnlargeExpressionHelper setHeight(int height) {
        this.height = height;
        return this;
    }

    public EnlargeExpressionHelper setPath(String path) {
        mPath = path;
        return this;
    }

    public EnlargeExpressionHelper build() {
        int w = 240;
        int h = 240;
        if (width > 0 && height > 0) {
            w = width;
            h = height;
        }

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
        rlp.width = w * 2;
        rlp.height = h * 2;
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mImageView.setLayoutParams(rlp);

        if (!TextUtils.isEmpty(mPath) && mPath.contains(".gif")) {
            GlideUtils.loadGif(mContext, mPath, mImageView, R.mipmap.qc_defalut_image_loading, R.mipmap.qc_default_image_error, w, h);
        } else {
            GlideUtils.load(mContext, mPath, mImageView);
//            mImageView.setImageResource(R.mipmap.qc_default_image_error);
        }
        return this;
    }

    public void show() {
        if (mDialog == null) {
            return;
        }

        mDialog.show();
    }
}
