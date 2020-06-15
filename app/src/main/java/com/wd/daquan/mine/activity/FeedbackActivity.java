package com.wd.daquan.mine.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.widget.CommLinkTextView;
import com.da.library.widget.CommTitle;

import java.util.LinkedHashMap;


public class FeedbackActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{

    private EditText et_first;
    private EditText et_second;
    private EditText et_third;
    private Button btn_submit;
    private TextView tv_wordNum;
    private TextView tv_contact;
    private ClipboardManager cm;
    private CommTitle mCommTitle;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.feedback_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.feedbackCommtitle);
        et_first= findViewById(R.id.feedbackEtFirst);
        et_second= findViewById(R.id.feedbackEtSecond);
        et_third= findViewById(R.id.feedbackEtThird);
        btn_submit= findViewById(R.id.feedbackBtn);
        tv_wordNum=findViewById(R.id.feedbackTxtWordNum);
        tv_contact=findViewById(R.id.feedbackTextIns);
        DqUtils.setEditText(et_second);
        DqUtils.setEditText(et_third);
        mCommTitle.setTitle(R.string.feeedback_title);
    }

    @Override
    public void initListener() {
        et_first.setOnClickListener(this);
        et_second.setOnClickListener(this);
        et_third.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        et_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String feedback=et_first.getText().toString().trim();
                String feed=feedback.length()+"/200";
                tv_wordNum.setText(feed);
            }
        });
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        DqUtils.setEditTextInhibitInputSpeChat(et_second);
        initContactsTxt();
    }
    private void initContactsTxt(){
        //底部提示语  快速咨询为超链
        SpannableString str = new SpannableString(getString(R.string.feeedback_more_business));
        //设置属性
        CommLinkTextView commLinkTextView = new CommLinkTextView(this);
        str.setSpan(commLinkTextView, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_contact.setText(str);
        tv_contact.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        tv_contact.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        commLinkTextView.setOnCommLinkTextViewClick(new CommLinkTextView.OnCommLinkTextViewClick() {
            @Override
            public void onLinkTextViewClick() {
                NavUtils.gotoWebviewActivity(FeedbackActivity.this, DqUrl.uri_contact_us,getString(R.string.contact_us));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.feedbackBtn:
                String feedback = et_first.getText().toString().trim();
                String name = et_second.getText().toString().trim();
                String phone = et_third.getText().toString().trim();
                if (TextUtils.isEmpty(feedback)){
                    DqToast.showShort(getString(R.string.feeedback_no_null_content));
                    return;
                }
                if (TextUtils.isEmpty(name)){
                    DqToast.showShort(getString(R.string.feeedback_no_null_name));
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    DqToast.showShort(getString(R.string.feeedback_no_null_phone));
                    return;
                }
//                if (AndroidEmoji.isEmoji(xinname)){
//                    QcToastUtil.makeText(this,"姓名不能为表情图片");
//                    return;
//                }
                if (DqUtils.validatePhoneNumber(phone,this)){
                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                    linkedHashMap.put("nickName", name);
                    linkedHashMap.put("phone", phone);
                    linkedHashMap.put("content", feedback);
                    mPresenter.submitFeedback(DqUrl.url_feedback, linkedHashMap);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (0 == code){
            DqToast.showShort(getString(R.string.feeedback_submit_suc));
            finish();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if(entity == null){
            DqToast.showShort("提交失败，请重试！");
            return;
        }
        DqToast.showShort(entity.content);
    }
}
