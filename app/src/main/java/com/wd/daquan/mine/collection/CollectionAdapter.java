package com.wd.daquan.mine.collection;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.CNLog;
import com.wd.daquan.common.utils.JsonParseUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.glide.helper.ImageHelp;
import com.da.library.tools.DateUtil;
import com.netease.nim.uikit.business.session.emoji.AndroidEmoji;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class CollectionAdapter extends CommRecyclerViewAdapter<CollectionUploadMsgBean, RecyclerView.ViewHolder> {
    private OnClickCollectionListener mOnClickCollectionListener;
    private String baseUrl;
    private int type;//1为会话页面 2为其它
    public CollectionAdapter(int type) {
        this.type = type;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        if(KeyValue.ZERO == viewType){//文字
            View convertView = mInflater.inflate(R.layout.cn_collection_item_txt, parent, false);
            return new CollectionTxtViewHolder(convertView);
        }else if(KeyValue.ONE == viewType){
            View convertView = mInflater.inflate(R.layout.cn_collection_item_voice, parent, false);
            return new CollectionVoiceViewHolder(convertView);
        }else{
            View convertView = mInflater.inflate(R.layout.cn_collection_item_img, parent, false);
            return new CollectionImgViewHolder(convertView);
        }
    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {

        if (null == allList || null == holder) {
            return;
        }
        CollectionUploadMsgBean  data = allList.get(position);
        int collectionType = getItemViewType(position);
        if(KeyValue.ZERO == collectionType){//文字
            CollectionTxtViewHolder mCollectionViewHolder = (CollectionTxtViewHolder) holder;
            mCollectionViewHolder.txtTxtContent.setExtendTxtClick(mCollectionViewHolder.txtTxtUnfold);
            mCollectionViewHolder.txtTxtContent.setTrimExpandedText("");
            mCollectionViewHolder.txtTxtContent.setTrimCollapsedText("");
            mCollectionViewHolder.txtTxtContent.setText(AndroidEmoji.ensure(data.content), TextView.BufferType.NORMAL);
            mCollectionViewHolder.txtTxtName.setText(setDisplayName(data.from_uid, data.from_nickname));
            mCollectionViewHolder.txtTxtTime.setText(DateUtil.transferTimeNew(Long.parseLong(data.create_time)));
            mCollectionViewHolder.layout_txt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(KeyValue.TWO != type)return true;
                    mOnClickCollectionListener.onCollectionLongClickListener(data, position);
                    return true;
                }
            });
            if(KeyValue.ONE == type){//单独处理会话的收藏 目前还不知道为什么点击没有反应
                mCollectionViewHolder.layout_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClickCollectionListener.onCollectionListener(data, position, null);
                    }
                });
                mCollectionViewHolder.txtTxtContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClickCollectionListener.onCollectionListener(data, position, null);
                    }
                });
            }

        }else if(KeyValue.ONE == collectionType){//语音和文件
            CollectionVoiceViewHolder mCollectionViewHolder = (CollectionVoiceViewHolder) holder;
            mCollectionViewHolder.txtVoiceName.setText(setDisplayName(data.from_uid, data.from_nickname));
            mCollectionViewHolder.txtVoiceTime.setText(DateUtil.transferTimeNew(Long.parseLong(data.create_time)));
            if(KeyValue.TWO_STRING.equals(data.type)){//语音
                mCollectionViewHolder.txtVoiceVoiceTime.setVisibility(View.VISIBLE);
                mCollectionViewHolder.img_voice.setImageResource(R.mipmap.cn_icon_voice);
                try {
                    JSONObject json = JsonParseUtils.getJSONObject(data.extra);
                    String duration="";
                    if(json.has("duration")){
                        duration = json.getString("duration");
                    }else{
                        duration = data.extra;
                    }
                    if(Integer.parseInt(duration) >= 10) {
                        mCollectionViewHolder.txtVoiceVoiceTime.setText("00:" + duration);
                    }else{
                        mCollectionViewHolder.txtVoiceVoiceTime.setText("00:0" + duration);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if(KeyValue.FIVE_STRING.equals(data.type)){//文件
                mCollectionViewHolder.txtVoiceVoiceTime.setText(data.content.substring(KeyValue.COLLETION_DIRECTION.length(),
                        data.content.length()));
                mCollectionViewHolder.img_voice.setImageResource(FileTypeUtils.fileTypeImageId(data.content));
            }else{}
            mCollectionViewHolder.layout_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickCollectionListener.onCollectionListener(data, position, mCollectionViewHolder.img_voice);
                }
            });
            mCollectionViewHolder.layout_voice.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnClickCollectionListener.onCollectionLongClickListener(data, position);
                    return true;
                }
            });
        }else{//图片和视频
            CollectionImgViewHolder mCollectionViewHolder = (CollectionImgViewHolder) holder;
            mCollectionViewHolder.txtImgName.setText(setDisplayName(data.from_uid, data.from_nickname));
            mCollectionViewHolder.txtImgTime.setText(DateUtil.transferTimeNew(Long.parseLong(data.create_time)));
            CNLog.d("clll", "收藏地址：" + baseUrl + data.content);
            if(KeyValue.THREE_STRING.equals(data.type)){//图片
                mCollectionViewHolder.txtImgImgName.setVisibility(View.GONE);
                mCollectionViewHolder.collectionImgIcon.setVisibility(View.GONE);
                GlideUtils.load(mContext, baseUrl + data.content, mCollectionViewHolder.collectionImg,
                        R.mipmap.qc_defalut_image_loading, R.mipmap.loading_fail, 200, 200);
            }else if(KeyValue.FOUR_STRING.equals(data.type)){//视频
                mCollectionViewHolder.txtImgImgName.setVisibility(View.GONE);
                mCollectionViewHolder.collectionImgIcon.setVisibility(View.VISIBLE);
                if(data.content.contains("android")){//android
                    try {
                        String path = baseUrl + data.content + KeyValue.OSS_VIDEO_FRAME_ANDROID;
                        JSONObject jsonObject = JsonParseUtils.getJSONObject(data.extra);
                        String orientation = "-1";
                        if (jsonObject.has("orientation")) {
                            orientation = jsonObject.getString("orientation");
                        }
                        int i = Integer.parseInt(orientation);
                        GlideUtils.loadBitmapListener(DqApp.sContext, path, bitmap -> {
                            mCollectionViewHolder.collectionImg.setImageBitmap(ImageHelp.rotation(bitmap, i));
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{//ios
                    String path = baseUrl + data.content + KeyValue.OSS_VIDEO_FRAME_IOS;
                    GlideUtils.loadBitmapListener(DqApp.sContext, path, bitmap -> {
                        Bitmap tempBitMAP = ImageHelp.rotation(bitmap, -1);
                        mCollectionViewHolder.collectionImg.setImageBitmap(tempBitMAP);
                    });
                }

            }else{}
            mCollectionViewHolder.layout_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickCollectionListener.onCollectionListener(data, position, null);
                }
            });
            mCollectionViewHolder.layout_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnClickCollectionListener.onCollectionLongClickListener(data, position);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return allList.size();
    }

    @Override
    public int getItemViewType(int position) {
        CollectionUploadMsgBean  data = (CollectionUploadMsgBean) allList.get(position);
        if(KeyValue.ONE_STRING.equals(data.type)) {//文字
            return 0;
        }else if(KeyValue.TWO_STRING.equals(data.type) || KeyValue.FIVE_STRING.equals(data.type)){//语音和文件
            return 1;
        }else{//图片和视频
            return 2;
        }
    }
    //要显示的名字
    private String setDisplayName(String uid, String displayName){
        Friend friend = FriendDbHelper.getInstance().getFriend(uid);
        if(friend != null && !TextUtils.isEmpty(friend.getRemarks())){
            return friend.getRemarks();
        }else{
            if(TextUtils.isEmpty(displayName)){
                return "";
            }
            return displayName;
        }
    }

    public interface OnClickCollectionListener{
        void onCollectionListener(CollectionUploadMsgBean data, int position, ImageView imageView);
        void onCollectionLongClickListener(CollectionUploadMsgBean data, int position);
    }
    public void setOnClickCollectionListener(OnClickCollectionListener mOnClickCollectionListener){
        this.mOnClickCollectionListener = mOnClickCollectionListener;
    }

}
