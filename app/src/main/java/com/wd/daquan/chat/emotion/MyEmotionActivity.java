package com.wd.daquan.chat.emotion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.QCBroadcastManager;
import com.da.library.constant.DirConstants;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.glide.listener.ILoadBitmapListener;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.tools.AppInfoUtils;
import com.da.library.tools.FileUtils;
import com.da.library.widget.CommTitle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/***
 *我添加的自定义表情
 */
public class MyEmotionActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{

    private GridView gridView;
    private CommTitle mCommTitle;
    private MyEmotionsAdapter adapter;
    private List<MyEmotionEntity> listEmotions = new ArrayList<>();
    private RelativeLayout layout_delete;
    private List<Integer> listsId = new ArrayList<>();
    private String picturePath;
    private int height = 0, width = 0;
    private File tempFile;
    private ImageView img_example;
    private QCBroadcastManager broadcastManager;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.myemotions_activity);
    }

    @Override
    protected void init() {
        broadcastManager = new QCBroadcastManager();
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.myEmotionTitle);
        gridView = findViewById(R.id.emotionActivityGridView);
        layout_delete = findViewById(R.id.emotionActivityDelete);
        img_example = findViewById(R.id.emotionActivityImgExample);
        img_example.setOnClickListener(this);
        mCommTitle.setTitle("我添加的表情");
        mCommTitle.setRightVisible(View.GONE);
        mCommTitle.setRightTxt("整理");
    }

    @Override
    public void initListener() {
        layout_delete.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightTv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        tempFile = FileUtils.createFile(this, DirConstants.DIR_EMOTION, "");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

        mPresenter.getMyEmotion(DqUrl.url_myEmotion, linkedHashMap);

    }

    private void gotoChoice(){
        Intent intent = new Intent(this, PicSelectorActivity.class);
        startActivityForResult(intent, KeyValue.PHOTO_REQUEST_GALLERY);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.comm_right_tv:
                if("整理".equals(mCommTitle.getRightTv().getText())) {
                    if(listEmotions != null && listEmotions.size() >0) {
//                        adapter.setData(listEmotions, 2);
                        adapter.setType(2);
                        mCommTitle.setRightTxt("完成");
                        layout_delete.setVisibility(View.VISIBLE);
                    }else{
                        DqToast.showShort("请先添加图片");
                    }
                }else{
//                    adapter.setData(listEmotions, 1);
                    adapter.setType(1);
//                    adapter.clearList();
                    mCommTitle.setRightTxt("整理");
                    layout_delete.setVisibility(View.GONE);
                }
                break;
            case R.id.emotionActivityDelete:
                if(adapter.listsId == null || adapter.listsId.size() == 0)return;
                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                linkedHashMap.put("id", jointId(adapter.listsId));
                mPresenter.deleteEmotion(DqUrl.url_delete_myEmotion, linkedHashMap);
                break;
            case R.id.emotionActivityImgExample:
                gotoChoice();
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(entity == null) return;
        DqToast.showShort(entity.content);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_myEmotion.equals(url)) {//表情列表
            if (0 == code) {
                List<MyEmotionEntity> lists = (List<MyEmotionEntity>) entity.data;
                listEmotions.addAll(lists);
                if(adapter == null) {
                    adapter = new MyEmotionsAdapter(this, listEmotions, 1);
                    gridView.setAdapter(adapter);
                }else{
                    adapter.setData(listEmotions, 1);
                }
                adapter.setAddEmotionListener(new MyEmotionsAdapter.AddEmotionListener() {
                    @Override
                    public void addEmotion() {
                        gotoChoice();
                    }
                });
                adapter.setAddIdListener(new MyEmotionsAdapter.AddIdListener() {
                    @Override
                    public void addID(List<Integer> lists) {
                        listsId.addAll(lists);
                    }
                });

            }
        }else if(DqUrl.url_add_myEmotion.equals(url)) {//添加表情
            broadcastManager.sendBroadcast(KeyValue.UPDATE_EMOTION_LIST);
            if (0 == code) {
                try {
//                    MyEmotionBean myEmotionBean = JsonMananger.jsonToBean(s.getData(), MyEmotionBean.class);
                    MyEmotionEntity myEmotionBean = (MyEmotionEntity) entity.data;

                    if(listEmotions != null && listEmotions.size() > 0){
                        listEmotions.add(myEmotionBean);
                        if(adapter == null)return;
                        adapter.setData(listEmotions, 1);
                    }else{
                        listEmotions.add(myEmotionBean);
                        adapter = new MyEmotionsAdapter(this, listEmotions, 1);
                        gridView.setAdapter(adapter);
                        adapter.setAddEmotionListener(new MyEmotionsAdapter.AddEmotionListener() {
                            @Override
                            public void addEmotion() {
                                gotoChoice();
                            }
                        });
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else if(DqUrl.url_delete_myEmotion.equals(url)) {//删除表情
            broadcastManager.sendBroadcast(KeyValue.UPDATE_EMOTION_LIST);
            if (0 == code) {
                try {
                    if(adapter == null)return;

                    listEmotions.removeAll(adapter.listsId);

                    mCommTitle.setRightTxt("整理");
                    layout_delete.setVisibility(View.GONE);
                    adapter.setData(listEmotions, 1);
                    DqToast.showShort("删除成功");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == KeyValue.PHOTO_REQUEST_GALLERY && data != null){//图库选择
            picturePath = data.getStringExtra("localImgPath");
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            if(bitmap == null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(picturePath, options);
            }
            height = bitmap.getHeight();
            width = bitmap.getWidth();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            if(picturePath.contains(".gif")) {
                if (output.toByteArray().length / 1024 < 200){
                    photos(new File(picturePath), width, height);
                }else{
                    DqToast.showShort("不能超过200kb");
                }
            }else{
                try {
                    Bitmap newBitmap = upImageSize(this, bitmap);
                    File file = FileUtils.saveBitmap(this, newBitmap, DirConstants.DIR_ADD_EMOTION);
                    photos(file, newBitmap.getWidth(), newBitmap.getHeight());
                    img_example.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            bitmap.recycle();

        }else if(requestCode == KeyValue.PHOTO_REQUEST_CUT && data != null) {//拍照
            String deviceCode = AppInfoUtils.getDeviceBrand();
            if(!TextUtils.isEmpty(deviceCode) && "LeEco".equals(deviceCode)){
                if(data.getExtras() == null)return;
            }
            temps(240, 240);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void temps(int width, int height){

        GlideUtils.loadBitmapListener(this, tempFile, new ILoadBitmapListener() {

                    @Override
                    public void onResourceReady(@Nullable Bitmap bitmap) {
                        if (FileUtils.isJpgFile(tempFile)) {
                            File temfile = FileUtils.saveBitmap(MyEmotionActivity.this, bitmap);
                            photos(temfile, width, height);
                        } else {
                            photos(tempFile, width, height);
                        }
                    }
                }

        );
    }

    private void photos(File files, int width, int height) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("filecode", picturePath);
        linkedHashMap.put("typeid", KeyValue.ZERO_STRING);
        linkedHashMap.put("pixW", width + "");
        linkedHashMap.put("pixH", height + "");
        mPresenter.addEmotion(DqUrl.url_add_myEmotion, linkedHashMap, files, "upfile");
    }

    //删除
    private String jointId(List<MyEmotionEntity> lists){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < lists.size(); i++) {
            sb.append(lists.get(i).id+ ",");

        }
        String s = sb.toString().substring(0, sb.toString().length() - 1);
        return s;

    }

    public static Bitmap upImageSize(Context context, Bitmap bmp) {
        if(bmp==null){
            return null;
        }
        int width = 480;
        int height = 480;
        // 计算比例
        float scaleX = (float)width / bmp.getWidth();// 宽的比例
        float scaleY = (float)height / bmp.getHeight();// 高的比例
        //新的宽高
        int newW = 0;
        int newH = 0;
        if(scaleX > scaleY){
            newW = (int) (bmp.getWidth() * scaleY);
            newH = (int) (bmp.getHeight() * scaleY);
        }else if(scaleX <= scaleY){
            newW = (int) (bmp.getWidth() * scaleX);
            newH = (int) (bmp.getHeight() * scaleX);
        }
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }
}
