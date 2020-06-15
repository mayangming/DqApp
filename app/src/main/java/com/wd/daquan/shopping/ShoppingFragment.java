package com.wd.daquan.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.discover.NewsPresenter;
import com.wd.daquan.model.bean.DataBean;

/**
 * @author: dukangkang
 * @date: 2018/9/5 18:18.
 * @description: todo ...
 */
public class ShoppingFragment extends BaseFragment<NewsPresenter, DataBean> {
    @Override
    public NewsPresenter createPresenter() {
        return new NewsPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.shopping_fragment;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        TextView tv_text = view.findViewById(R.id.tv_text);
        tv_text.setText("我是商城页面");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
//        mPresenter.getDiscoverList(DqUrl.url_disvover_menu, new LinkedHashMap<>());
    }

}
