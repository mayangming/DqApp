package com.wd.daquan.mine.collection;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.da.library.constant.IConstant;
import com.da.library.tools.AESHelper;
import com.da.library.widget.CommTitle;
import com.da.library.widget.CommonListDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.alioss.AliOssHelper;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class CollectionActivity extends DqBaseActivity<QCCollectionPresenter, DataBean> implements View.OnClickListener {
    //    private EBSharedPrefManager manager = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
    private CommTitle mCommtitle;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CollectionAdapter mCollectionAdapter;
    private List<CollectionUploadMsgBean> collectionLists = new ArrayList<>();
    private CollectionListBean collectionData = new CollectionListBean();
    private RelativeLayout mNoContentLayout;
    private ImageView imageView_voice;//下载之后用的

    private AliOssHelper mAliOssHelper = null;

    @Override
    public QCCollectionPresenter createPresenter() {
        return new QCCollectionPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.cn_collection_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mAliOssHelper = new AliOssHelper();

        mCommtitle = findViewById(R.id.collectionActivityTitle);
        mRefreshLayout = findViewById(R.id.collectionActivityRefreshLayout);
        mNoContentLayout = findViewById(R.id.collectionActivityNoContentLayout);
        mRecyclerView = findViewById(R.id.collectionActivityRecyclerView);
        mCommtitle.setTitle(getString(R.string.collection));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void initListener() {
        mCommtitle.getLeftIv().setOnClickListener(this);
    }


    @Override
    public void initData() {
        if (null != mPresenter) {
            LinkedHashMap<String, String> collectionListMap = new LinkedHashMap<>();
            collectionListMap.put("page", "1");
            collectionListMap.put("length", "999");
            mPresenter.getCNCollection(DqUrl.url_collection_list, collectionListMap);
        }
        mCollectionAdapter = new CollectionAdapter(KeyValue.TWO);
        mRecyclerView.setAdapter(mCollectionAdapter);
        mCollectionAdapter.setOnClickCollectionListener(new CollectionAdapter.OnClickCollectionListener() {
            @Override
            public void onCollectionListener(CollectionUploadMsgBean data, int position, ImageView view) {//单独处理文字部分
                onItemClick(data, position, view);
            }

            @Override
            public void onCollectionLongClickListener(CollectionUploadMsgBean data, int position) {
                showDialogList(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SealAppContext.getInstance().updateBadgeView(0, null);
    }

    @Override
    public void onClick(View v) {
        if (v == mCommtitle.getLeftIv()) {
            AudioPlayManager.getInstance().stopPlay();
            finish();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (DqUrl.url_collection_list.equals(url)) {//列表
            if (0 == code) {
                collectionData = (CollectionListBean) entity.data;
                collectionLists = collectionData.list;
                if (collectionLists != null && collectionLists.size() > 0) {
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    mNoContentLayout.setVisibility(View.GONE);
                    mCollectionAdapter.setBaseUrl(collectionData.uri);
                    mCollectionAdapter.update(collectionLists);
                } else {
                    mRefreshLayout.setVisibility(View.GONE);
                    mNoContentLayout.setVisibility(View.VISIBLE);
                }
            } else {
                DqToast.showShort(entity.msg);
            }
        } else if (DqUrl.url_collection_delete.equals(url)) {//删除
            if (0 == code) {
                String tag = entity.tag;
                collectionLists.remove(Integer.parseInt(tag));
                if (collectionLists.size() > 0) {
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    mNoContentLayout.setVisibility(View.GONE);
                    mCollectionAdapter.update(collectionLists);
                } else {
                    mRefreshLayout.setVisibility(View.GONE);
                    mNoContentLayout.setVisibility(View.VISIBLE);
                }

            } else {
                DqToast.showShort(entity.msg);
            }

        }
    }

    private void showDialogList(int coPosition) {
        CollectionUploadMsgBean data = collectionLists.get(coPosition);
        final CommonListDialog dialog = new CommonListDialog(this);
        final List<String> itemList = new ArrayList<>();
        final String[] items = new String[]{
                getString(R.string.message_transfer),
                getString(R.string.message_delete),
                getString(R.string.message_copy),
        };
        itemList.clear();
        Collections.addAll(itemList, items);
        if (KeyValue.TWO_STRING.equals(data.type)) {//语音
            itemList.remove(items[0]);
            itemList.remove(items[2]);
        } else if (KeyValue.THREE_STRING.equals(data.type)) {//图片
            itemList.remove(items[2]);
        } else if (KeyValue.FOUR_STRING.equals(data.type)) {//视频
            itemList.remove(items[2]);
        } else if (KeyValue.FIVE_STRING.equals(data.type)) {//文件
            itemList.remove(items[2]);
        }
        dialog.setItems(itemList);
        dialog.show();
        dialog.setListener((String item, int position) -> {
            if (items[0].equals(item)) {//转发
                tranferMessage(data);
            } else if (items[1].equals(item)) {//删除
                deleteItem(data.collection_id, coPosition);
            } else if (items[2].equals(item)) {//复制
                copyTxt(data.content);
            }
        });
    }

    private void tranferMessage(CollectionUploadMsgBean data) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        ShareBean shareBean = new ShareBean();
        shareBean.enterType = IConstant.Share.FORWARDING;
        if (KeyValue.ONE_STRING.equals(data.type)) {//文本
            shareBean.textOrImage = IConstant.Share.TEXT;
//            shareBean.path = AESHelper.encryptString(intentUrl.content);
            shareBean.path = data.content;
            NavUtils.gotoShareActivity(activity, shareBean);
        } else if (KeyValue.THREE_STRING.equals(data.type)) {//图片
            getGlideLocalFile(collectionData.uri + data.content, 1);
        } else if (KeyValue.FOUR_STRING.equals(data.type)) {//视频
        }

    }

    private void deleteItem(String collectionId, int position) {
        if (mPresenter != null) {
            LinkedHashMap<String, String> deleteMap = new LinkedHashMap<>();
            deleteMap.put("collection_id", collectionId);
            mPresenter.deleteCNCollection(DqUrl.url_collection_delete, deleteMap, position);
        }
    }

    private void copyTxt(String txtContent) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        assert clipboard != null;
        String content = txtContent;
        if (!TextUtils.isEmpty(content)) {//对复制内容判空处理
            String decryptConent = AESHelper.decryptString(content);
            if (!TextUtils.isEmpty(decryptConent)) {//解密内容为空 直接赋值解密前原内容
                clipboard.setText(decryptConent);
            } else {
                clipboard.setText(content);
            }
            DqToast.showShort(DqApp.sContext.getResources().getString(R.string.copy_text_success));
        } else {
            DqToast.showShort(DqApp.sContext.getResources().getString(R.string.copy_text_null));
        }
    }
    //1 转发；2 查看大图
    private void getGlideLocalFile(String url, int type){
//        DqApp.getInstance().getThread().execute(new Runnable() {
//            @Override
//            public void run() {
//                File localFile = GlideUtils.loadFile(DqApp.sContext, url);
////                String path = localFile.getAbsolutePath();
////                ImageAttachment imageAttachment = new ImageAttachment();
////                imageAttachment.setPath(path);
//                IMMessage content = MessageBuilder.createImageMessage("", null, localFile);
//                ShareBean shareBean = new ShareBean();
//                shareBean.imMessage = content;
//                if(KeyValue.ONE == type){
//                    NavUtils.gotoShareActivity(CollectionActivity.this, shareBean);
//                }else{
//                    NavUtils.gotoWatchImageAndVideoActivity(CollectionActivity.this, shareBean.imMessage);
//                }
//
//            }
//        });
//        GlideUtils.loadFile(DqApp.sContext, url, new ILoadFileListener() {
//            @Override
//            public void loadFile(@NonNull File file) {
//                IMMessage message = MessageBuilder.createImageMessage("", null, file);
//                ShareBean shareBean = new ShareBean();
//                shareBean.imMessage = message;
//                if(KeyValue.ONE == type){
//                    NavUtils.gotoShareActivity(CollectionActivity.this, shareBean);
//                }else{
//                    NavUtils.gotoWatchImageAndVideoActivity(CollectionActivity.this, shareBean.imMessage);
//                }
//            }
//        });
    }

    private void startInitAudio(File file, ImageView img_voice) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager.getMode() != AudioManager.MODE_NORMAL) {
            if (mAudioManager.getMode() == AudioManager.MODE_IN_CALL || mAudioManager.getMode() == AudioManager.MODE_IN_COMMUNICATION) {
                DqToast.showShort("声音通道正被占用，请稍后再试");
                return;
            }
            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
            playVoice(file, img_voice);
        } else {
            playVoice(file, img_voice);

        }
    }

    private void playVoice(File audioFile, ImageView img_voice) {
        if(audioFile == null) {
            return;
        }
        Uri localUri = Uri.fromFile(audioFile);
        Uri playingUri = AudioPlayManager.getInstance().getPlayingUri();
        if (playingUri != null && playingUri.equals(localUri)) {
            AudioPlayManager.getInstance().stopPlay();
        } else {
            AudioPlayManager.getInstance().startPlay(this, localUri, new StartPlayAudioListener(img_voice));
        }
    }

    private class StartPlayAudioListener implements IAudioPlayListener {
        private ImageView img_voice;

        private StartPlayAudioListener(ImageView imageView) {
            this.img_voice = imageView;
        }

        @Override
        public void onComplete(Uri uri) {
            if (img_voice == null) return;
            img_voice.setImageResource(R.mipmap.cn_icon_voice);
        }

        @Override
        public void onStart(Uri uri) {
            GlideUtils.loadGif(CollectionActivity.this, R.mipmap.cn_voice_playing, img_voice);
        }

        @Override
        public void onStop(Uri uri) {
            if (img_voice == null) return;
            img_voice.setImageResource(R.mipmap.cn_icon_voice);
        }
    }

    @Override
    protected void onDestroy() {
        AudioPlayManager.getInstance().stopPlay();
        super.onDestroy();
    }

    private void onItemClick(CollectionUploadMsgBean data, int position, ImageView imageView){
    }
}
