package com.wd.daquan.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.model.bean.DataBean;

/**
 * @author: dukangkang
 * @date: 2018/9/5 18:18.
 * @description: todo ...
 */
public class NewsFragment extends BaseFragment<NewsPresenter, DataBean> implements View.OnClickListener {
    private TextView mNewsHead;
    private TextView mNewsVideo;

    @Override
    public NewsPresenter createPresenter() {
        return new NewsPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_fragment;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mNewsHead = view.findViewById(R.id.news_title_head);
        mNewsVideo = view.findViewById(R.id.news_title_video);


        switchTitle(0);
    }


    @Override
    public void initListener() {
        mNewsHead.setOnClickListener(this);
        mNewsVideo.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.news_title_head:
                switchTitle(0);
                break;
            case R.id.news_title_video:
                switchTitle(1);
                break;
        }
    }


    private void switchTitle(int position) {
        switch (position) {
            case  0:
                mNewsHead.setSelected(true);
                mNewsVideo.setSelected(false);
                break;
            case  1:
                mNewsHead.setSelected(false);
                mNewsVideo.setSelected(true);
                break;
        }
    }

}
