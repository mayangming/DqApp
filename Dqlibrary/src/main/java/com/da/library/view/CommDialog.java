package com.da.library.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.da.library.listener.DialogListener;
import com.da.library.R;
import com.wd.daquan.glide.GlideUtils;

/**
 * @author: dukangkang
 * @date: 2018/5/14 20:01.
 * @description: todo ...
 * 添加搜索的样式2018/9/14 fanghiz
 */
public class CommDialog extends DialogComponents implements View.OnClickListener {

    private View mView;
    private View mLineView;
    private TextView mTitleTv;
    private TextView mDescTv;
    private TextView mCancelTv;
    private TextView mOkTv;
    private String title;
    private String content;
    private String btn_left;
    private String btn_right;
    private DialogListener mDialogListener;
    private boolean isShowSingleBtn = false;
    private DialogListener dialogListener;
    /**
     * 普通dialog容器,默认显示
     */
    private LinearLayout mCommDialogLl;

    /**
     * 带搜索dialog容器,默认隐藏
     */
    private LinearLayout mCommDialogSearchLl;
    private TextView mSearchTitleTv;
    private EditText mSearchEt;
    private TextView mSearchInputTextCountTv;
    private View mShareTypeLine;
    private TextView mShareTypeTv;
    private ImageView mPortrait;

    public CommDialog(Context context, String title, String content, DialogListener mDialogListener){
        this(context, R.style.conversationsa_dialog);
        this.title = title;
        this.content = content;
        this.mDialogListener = mDialogListener;
        init();
    }
    public CommDialog(Context context, String title, String content, String btn_left, String btn_right,
                       DialogListener mDialogListener){
        super(context, R.style.conversationsa_dialog);
        this.title = title;
        this.content = content;
        this.btn_left = btn_left;
        this.btn_right = btn_right;
        this.mDialogListener = mDialogListener;
        init();
    }

    public CommDialog(Context context, String title, String content, String btn_right){
        super(context, R.style.conversationsa_dialog);
        this.title = title;
        this.content = content;
        this.btn_right = btn_right;
        init();
    }

    public CommDialog(Context context, String title, String content, String btn_right, boolean isShowSingleBtn){
        super(context, R.style.conversationsa_dialog);
        this.title = title;
        this.content = content;
        this.btn_right = btn_right;
        this.isShowSingleBtn = isShowSingleBtn;
        init();
    }
    public CommDialog(Context context) {
        this(context, R.style.conversationsa_dialog);
    }

    public CommDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init(){
        initView();
        initListener();
    }

    private void initView() {
        //mDialog = new Dialog(mContext, R.style.conversationsa_dialog);
        //基本
        mView = LayoutInflater.from(getContext()).inflate(R.layout.comm_dialog, null);
        mCommDialogLl = mView.findViewById(R.id.comm_dialog_ll);
        mTitleTv = mView.findViewById(R.id.comm_dialog_title);
        mDescTv = mView.findViewById(R.id.comm_dialog_desc);
        mPortrait = mView.findViewById(R.id.comm_dialog_portrait);

        //分享
        mShareTypeLine = mView.findViewById(R.id.comm_dialog_share_type_line);
        mShareTypeTv = mView.findViewById(R.id.comm_dialog_share_type_tv);

        //搜索
        mCommDialogSearchLl = mView.findViewById(R.id.comm_dialog_search_ll);
        mSearchTitleTv = mView.findViewById(R.id.comm_dialog_search_title_tv);
        mSearchEt = mView.findViewById(R.id.comm_dialog_search_et);
        mSearchInputTextCountTv = mView.findViewById(R.id.comm_dialog_search_input_text_count_tv);

        mCancelTv = mView.findViewById(R.id.comm_dialog_cancel);
        mLineView = mView.findViewById(R.id.center_line);
        mOkTv = mView.findViewById(R.id.comm_dialog_ok);

        this.setContentView(mView);
        this.setCanceledOnTouchOutside(true);
        initData();
        initListener();
    }
    private void initData(){
        if(TextUtils.isEmpty(title)){
            setTitleVisible(false);
        }else{
            setTitle(title);
        }
        if(!TextUtils.isEmpty(content)){
            setDesc(content);
        }
        if(!TextUtils.isEmpty(btn_left)){
            setCancelTxt(btn_left);
        }
        if(!TextUtils.isEmpty(btn_right)){
            setOkTxt(btn_right);
        }
        if (isShowSingleBtn){
            setSingleBtn();
        }
    }

    public void setTitleCenter() {
        mTitleTv.setGravity(Gravity.CENTER);
    }

    public void setSingleBtn() {
        mCancelTv.setVisibility(View.GONE);
        mLineView.setVisibility(View.GONE);
    }

    private void initListener() {
        mCancelTv.setOnClickListener(this);
        mOkTv.setOnClickListener(this);

        mSearchEt.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = String.valueOf(s);
            mSearchInputTextCountTv.setText(content.length() + "/" + "50字");
        }
    };

    public void setTitleVisible(boolean isShow) {
        mTitleTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setTitleGone() {
        mTitleTv.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }
    public TextView getTitleView() {
        return mTitleTv;
    }

    public void setDesc(String desc) {
        mDescTv.setText(Html.fromHtml(desc));
    }

    public void setDescTextSize(int size) {
        mDescTv.setTextSize(size);
    }
    public TextView getDescTextView() {
        return mDescTv;
    }

    public void setDescSpannableString(SpannableString desc) {
        mDescTv.setText(desc);
    }

    public void setCancelTxt(String txt) {
        mCancelTv.setText(txt);
    }

    public void setCancelTxtColor(int color) {
        mCancelTv.setTextColor(color);
    }

    public void setOkTxt(String txt) {
        mOkTv.setText(txt);
    }

    public void setOkTxtColor(int color) {
        mOkTv.setTextColor(color);
    }

    public void setWidth() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if(null != mView) {
            mView.measure(0, 0);
            lp.width = (int) (getScreenWidth() * 0.85);
            dialogWindow.setAttributes(lp);
        }
    }

    /**
     * 搜索的dialog
     */
    public void setSearchLlVisibility(){
        mCommDialogSearchLl.setVisibility(View.VISIBLE);
        mCommDialogLl.setVisibility(View.GONE);
    }

    public TextView getSearchTitleTv() {
        return mSearchTitleTv;
    }

    public EditText getSearchEt() {
        return mSearchEt;
    }

    public TextView getSearchInputTextCountTv() {
        return mSearchInputTextCountTv;
    }

    public void setSearchTitleTv(String text){
        mSearchTitleTv.setText(text);
    }

    public void setSearchEt(String text){
        mSearchEt.setText(text);
    }


    public void show() {
        if (isValidContext() && !this.isShowing()) {
            super.show();
        }
    }

    private boolean isValidContext (){
        if(getContext() instanceof Activity) {
            if (((Activity) getContext()).isFinishing()){
                return false;
            }
        }
        return true;
    }

    public void dismiss() {
        if(isValidContext()) {
            super.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        int id = v.getId();
        if (id == mCancelTv.getId()) {
            if (null != mDialogListener) {
                mDialogListener.onCancel();
            }
        } else if (id == mOkTv.getId()) {
            if (null != mDialogListener) {
                mDialogListener.onOk();
            }
        }
    }

    public void setDialogListener(DialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    public void setDescCenter() {
        mDescTv.setGravity(Gravity.CENTER);
    }


/**
 * ---------------------------------------------------- 分享-------------------------------------------------------------
  */

    public void setShareTypeLineVisible(boolean isShow){
        mShareTypeLine.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    public void setShareTypeTvVisible(boolean isShow){
        mShareTypeTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    @SuppressLint("SetTextI18n")
    public void setShareTypeTv(String type){
        mShareTypeTv.setText("[" + type + "]");
    }

    public void setSharePortrait(String path){
        mPortrait.setVisibility(View.VISIBLE);
        GlideUtils.loadHeader(getContext(), path, mPortrait);
    }
}
