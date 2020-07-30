package com.wd.daquan.common.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.da.library.constant.IConstant;
import com.da.library.widget.CommTitle;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.daquan.R;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.helper.DialogHelper;
import com.wd.daquan.common.presenter.SharePresenter;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.third.session.extension.CardAttachment;
import com.wd.daquan.third.session.extension.QcExpressionAttachment;
import com.wd.daquan.third.session.extension.SdkShareAttachment;

/**
 * @Author: 方志
 * @Time: 2018/9/25 11:56
 * @Description: 分享基类
 */
public abstract class ShareBaseActivity extends DqBaseActivity<SharePresenter, DataBean>
        implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    protected CommTitle mTitleLayout;
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected View mNoDataTv;
    protected ImMessageBaseModel mIMMessage;
    protected IMContentDataModel mAttachment;
    protected String mEnterType;
    protected String mTextOrImage;
    protected String mPath;
    protected ShareBean mShareBean;

    @Override
    protected SharePresenter createPresenter() {
        return new SharePresenter();
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.share_title_layout);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.share_recycler_view);
        mNoDataTv = findViewById(R.id.no_data_tv);
    }

    protected void setTitleName(String name) {
        if (null != mTitleLayout) {
            mTitleLayout.setTitle(name);
        }
    }

    @Override
    protected void initData() {
        mShareBean = getIntent().getParcelableExtra(IConstant.Share.SHARE_BAEN);
        mEnterType = mShareBean.enterType;
        mTextOrImage = mShareBean.textOrImage;
        mPath = mShareBean.path;
        mIMMessage = mShareBean.imMessage;
        if (mIMMessage != null) {
            mAttachment = mIMMessage.getContentData();
        }

        if (mAttachment == null) {
            mAttachment = mShareBean.attachment;
        }

    }

    @Override
    protected void initListener() {
        mTitleLayout.getLeftIv().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mTitleLayout.getLeftIvId()) {
            finish();
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if (entity == null) return;
        requestSuccess(url, code, entity);

    }

    protected void requestSuccess(String url, int code, DataBean entity) {
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if (entity == null) return;
        DqUtils.bequit(entity, this);
        requestFailed(url, code, entity);
    }

    protected void requestFailed(String url, int code, DataBean entity) {
    }

    /**
     * 分享到单个会话
     */
    protected void shareMessage(ShareItemBean shareItemBean) {
        if (null != shareItemBean) {
            //分享自定义消息
            shareItemBean.shareMessage = mIMMessage;
            shareItemBean.attachment = mAttachment;

            if (null != mAttachment) {
                shareMoreMessage(shareItemBean);
                DialogHelper.getInstance().showShareDialog(getActivity(), shareItemBean, mEnterType);
            } else {
                //分享文本图片消息
                DialogHelper.getInstance().showShareDialog(getActivity(), shareItemBean, mEnterType, mTextOrImage, mPath);
            }

        }
    }

    /**
     * 分享到多个会话
     */
    protected void shareMoreMessage(ShareItemBean shareItemBean) {
        if (null != mAttachment) {
            //分享自定义消息
            shareItemBean.attachment = mAttachment;
            shareItemBean.shareMessage = mIMMessage;
            if (mAttachment instanceof CardAttachment) {
                shareItemBean.shareType = "名片";
                shareItemBean.messageDesc = "个人名片";
            } else if (mAttachment instanceof MessageVideoBean) {
                shareItemBean.shareType = "视频";
                shareItemBean.messageDesc = "视频";
            } else if (mAttachment instanceof MessagePhotoBean) {
                shareItemBean.shareType = "图片";
                shareItemBean.messageDesc = "图片";
            } else if (mAttachment instanceof QcExpressionAttachment) {
                shareItemBean.shareType = "表情";
                shareItemBean.messageDesc = "表情";
            } else if (mAttachment instanceof SdkShareAttachment) {
                shareItemBean.shareType = "链接";
                shareItemBean.messageDesc = "链接";
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogHelper.getInstance().onDestroy();
    }
}
