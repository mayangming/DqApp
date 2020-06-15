package com.wd.daquan.mine.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.da.library.tools.FileUtils;
import com.da.library.utils.AESUtil;
import com.da.library.view.base.BaseDialogFragment;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.GetImageUtils;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.scancode.QRCodeUtils;
import com.wd.daquan.model.BuildConfig;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.io.File;

/**
 * VIP分享页面
 */
public class ShareVipFragment extends BaseDialogFragment{
    private View vipShareSave;
    private ImageView vipShareQrCode;
    private View vipShareDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return setContentView(R.layout.fragment_share_vip);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vipShareDialog = view.findViewById(R.id.vip_share_dialog_bg);
        vipShareQrCode = view.findViewById(R.id.vip_share_qr_code);
        vipShareSave = view.findViewById(R.id.vip_share_save);
        View close = view.findViewById(R.id.vip_share_close);
        close.setOnClickListener(this::onClick);
        vipShareSave.setOnClickListener(this::onClick);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//        String value = bundle.getString("key");
//        DqLog.e("YM","获取的参数:"+value);

        Bitmap qrCode = getVipShareQrCode();
        vipShareQrCode.setImageBitmap(qrCode);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vip_share_save:
                File file = saveQrCodeImage();
                FileUtils.savePhotoAlbum(v.getContext(),file);
                DqToast.showShort("文件保存路径:"+file.getAbsolutePath());
                break;
            case R.id.vip_share_close:
                dismiss();
                break;
        }
    }


    private Bitmap getVipShareQrCode(){
        String url = "";
        try {
            String params = AESUtil.encrypt(ModuleMgr.getCenterMgr().getUID());
            url = BuildConfig.SERVER+DqUrl.url_vip_share_link +params;
        }catch (Exception e){
            e.printStackTrace();
        }

       return QRCodeUtils.createQRImage(url, 300, 300);
    }
    /**
     * 保存二维码图片
     */
    private File saveQrCodeImage() {
        Bitmap bitmap = GetImageUtils.getBtimap(vipShareDialog);
        File file = FileUtils.saveBitmap(DqApp.sContext, bitmap);
        //存入相册
        FileUtils.savePhotoAlbum(DqApp.sContext, file);
        bitmap.recycle();
        return file;
    }
}