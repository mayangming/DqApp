package com.wd.daquan.mine.alipay;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.da.library.widget.CommTitle;

public class AliPayWebviewActivity extends DqBaseActivity implements View.OnClickListener {

    private WebView webview;
    private CommTitle mCommTitle;

    @Override
    protected Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.alipay_webview_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        webview = findViewById(R.id.alipayWebViewWeb);
        mCommTitle = findViewById(R.id.alipayWebViewCommtitle);
        mCommTitle.setTitle(getString(R.string.alipay));
    }

    @Override
    public void initListener() {
        DqUtils.setWebSettings(webview, this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        String mAplayUrl = getIntent().getStringExtra(KeyValue.APLAY_URL);
        webview.loadUrl(mAplayUrl);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("platformapi/startapp")) {
                    startAlipayActivity(url);
                    // android  6.0 两种方式获取intent都可以跳转支付宝成功
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        && (url.contains("platformapi") && url.contains("startapp"))) {
//                    startAlipayActivity(url);
                    webview.loadUrl(url);
                } else {
                    webview.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                finish();
            }
        });
        webview.setDownloadListener((s, s1, s2, s3, l) -> NavUtils.gotobrowser(AliPayWebviewActivity.this,s));
    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mCommTitle.getLeftIv()){
            finish();
        }
    }
}
