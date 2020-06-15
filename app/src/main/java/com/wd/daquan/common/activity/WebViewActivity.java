package com.wd.daquan.common.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.da.library.widget.CommTitle;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.bean.GroupAssistAuth;
import com.wd.daquan.common.constant.Constants;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.DqUtils;

import java.net.URLEncoder;


public class WebViewActivity extends DqBaseActivity implements View.OnClickListener {
    private String webViewUrl;
    private String webViewTitle;
    private GroupAssistAuth groupAssistAuth;
    private WebView mWv;
    private CommTitle mCommTitle;
    private LinearLayout webViewRoot;
//    private MainLoading mLoadingDialog;
//    private boolean mIsPageLoading;
//    private boolean isDoTask = false;

    @Override
    protected Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.webview_activity);
    }

    @Override
    protected void init() {
        webViewUrl = getIntent().getStringExtra(Constants.WEBVIEW_URL);
        webViewTitle = getIntent().getStringExtra(Constants.WEBVIEW_TITLE);
        groupAssistAuth = (GroupAssistAuth) getIntent().getSerializableExtra(Constants.GROUP_ASSIST_AUTH);
    }

    @Override
    public void initView() {
        webViewRoot = findViewById(R.id.webview_root);
        mCommTitle = findViewById(R.id.webviewTitle);
//        mWv = findViewById(R.id.webviewWV);
        mWv = DqApp.getInstance().getWebView();
        webViewRoot.addView(mWv);
    }

    @Override
    public void initListener() {
        initWebViewSettings();
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (webViewTitle != null) {
            mCommTitle.setTitle(webViewTitle);
        }
        loadCookieUrl(webViewUrl);
    }


    public void loadCookieUrl(String webViewUrl) {
        Handler handler = new Handler();
//        MainLoading.Builder builder1=new MainLoading.Builder(this)
//                .setMessage("")
//                .setCancelable(false);
//        builder1.setShowMessage(false);
//        mLoadingDialog=builder1.create();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mLoadingDialog.dismiss();
//            }
//        },10000);
//        if (!mLoadingDialog.isShowing()) {
//            mLoadingDialog.show();
//        }
        mWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
//                    mLoadingDialog.dismiss();
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //mCommTitle.setTitle(title);
            }
        });
    }

    boolean isRedirect = false;
    boolean isPageOk = false;

    private void initWebViewSettings() {
        DqUtils.setWebSettings(mWv, this);
        mWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                isRedirect = false;
                if (isPageOk) {
                    if (!TextUtils.isEmpty(url)) {//拦截跳转 6.0以下
//                    if(url.equals(webViewUrl)){
                        if (url.contains(DqUrl.url_help_filter)) {
                            mWv.loadUrl(url);
                        } else {
                            try {
//                            isDoTask = true;
                                intentUri(url);
                            } catch (Exception e) {
                                intentUri(webViewUrl);
                            }
                        }
                    }
                    return true;
                }
//                if(isDoTask)return false;
//                if(mIsPageLoading) {
//                    mVWv.loadUrl(url);
//                    return false;
//                }
//                if(!TextUtils.isEmpty(url)){//拦截跳转 6.0以下
////                    if(url.equals(webViewUrl)){
//                    if(url.contains(QCURL.URL_HELP)){
//                        mVWv.loadUrl(url);
//                    }else {
//                        try {
//                            isDoTask = true;
//                            intentUri(url);
//                        } catch (Exception e) {
//                            intentUri(webViewUrl);
//                        }
//                    }
//                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                mIsPageLoading= true;
                isRedirect = true;
                isPageOk = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mIsPageLoading= false;
//                isDoTask = false;
                isPageOk = isRedirect;
            }


            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }


            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //用javascript隐藏系统定义的404页面信息
//                String intentUrl = "";
//                view.loadUrl("javascript:document.body.innerHTML=\"" + intentUrl + "\"");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message content);// 进行其他处理
            }
        });
//        mWv.loadUrl(webViewUrl);

        loadUrl(webViewUrl);
    }

    private void loadUrl(String webViewUrl) {
        if (groupAssistAuth != null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();//拼接post提交参数
                stringBuilder.append("plugin_id=").append(URLEncoder.encode(groupAssistAuth.auth_data.plugin_id, "UTF-8")).append("&")
                        .append("group_id=").append(URLEncoder.encode(groupAssistAuth.auth_data.group_id, "UTF-8")).append("&")
                        .append("unionid=").append(URLEncoder.encode(groupAssistAuth.auth_data.unionid, "UTF-8")).append("&")
                        .append("owner_unionid=").append(URLEncoder.encode(groupAssistAuth.auth_data.owner_unionid, "UTF-8")).append("&")
                        .append("nonce=").append(URLEncoder.encode(groupAssistAuth.auth_data.nonce, "UTF-8")).append("&")
                        .append("sign=").append(URLEncoder.encode(groupAssistAuth.auth_data.sign, "UTF-8"));
                String params = stringBuilder.toString();
                mWv.postUrl(webViewUrl, params.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mWv.loadUrl(webViewUrl);
        }
    }

    //跳转
    private void intentUri(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
                    Log.w("chuiniu", "xxxxxxxx visible");
                    if (null != mWv && null != mWv.getWindowToken()) {
                        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mWv.getWindowToken(), 0);
                    }
                } else {
                    Log.w("chuiniu", "xxxxxxxx gone");
                }
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWv.canGoBack()) {
            mWv.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWv) {
            mWv.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWv) {
            mWv.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mWv) {
            ViewGroup parent = (ViewGroup) mWv.getParent();
            if (null != parent) {
                parent.removeView(mWv);
                mWv.removeAllViews();
                mWv.destroy();
                mWv = null;
                DqApp.getInstance().initWebView();//然后为下一次使用做准备
            }
        }
        super.onDestroy();
    }
}
