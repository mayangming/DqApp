package com.da.library.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.R;
import com.da.library.listener.ITextChangedListener;
import com.da.library.tools.Utils;

/**
 * @author: dukangkang
 * @date: 2018/4/23 15:48.
 * @description: 拥有编辑框的View
 * modify: 修改样式fangzhi 2018/8/24
 */
@SuppressLint("CustomViewStyleable")
public class CommSearchEditText extends RelativeLayout implements View.OnClickListener {

    private EditText mEditText;
    private ITextChangedListener mChangedListener;
    private ImageView mSearchIconIv;
    private ImageView mSearchBackIv;
    private ImageView mSearchClearIv;
    private TextView mSearchConfirmTv;
    private View mLineView;
    private View mContainerRl;
    private View mBottomLine;
    private boolean isFinish = true;

    //搜索布局背景
    private int mSearchTitleBgColor;
    //搜索返回键背景
    private int mSearchBackBg;
    //搜索控件editText背景
    private int mSearchEtBg;
    //搜索按钮背景
    private int mSearchConfirmTvBg;
    //搜索按钮图标
    private int mSearchBackIcon;
    //搜索文字颜色
    private int mSearchTextColor;
    //搜索控件editText提示文字
    private String mSearchHint;
    //搜索按钮文字
    private String mSearchTvText;
    //搜索按钮文字
    private float mSearchTextSize = 16;
    //默认搜索样式 无返回键自动搜索
    private int mSearchStyle = 0;
    private RelativeLayout mSearchInputRl;


    public CommSearchEditText(Context context) {
        this(context, null);
    }

    public CommSearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommSearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchTitleView);
        mSearchTitleBgColor = typedArray.getColor(R.styleable.SearchTitleView_stvSearchTitleBg, mSearchTitleBgColor);
        mSearchBackBg = typedArray.getResourceId(R.styleable.SearchTitleView_stvSearchBackBg, mSearchBackBg);
        mSearchEtBg = typedArray.getResourceId(R.styleable.SearchTitleView_stvSearchEtBg, mSearchEtBg );
        mSearchConfirmTvBg = typedArray.getResourceId(R.styleable.SearchTitleView_stvSearchConfirmTvBg, mSearchConfirmTvBg);
        mSearchBackIcon = typedArray.getResourceId(R.styleable.SearchTitleView_stvSearchBackIcon, mSearchBackIcon);
        mSearchHint = typedArray.getString(R.styleable.SearchTitleView_stvSearchHint);
        mSearchTvText = typedArray.getString(R.styleable.SearchTitleView_stvSearchTvText);
        mSearchTextSize = typedArray.getDimension(R.styleable.SearchTitleView_stvSearchTextSize, mSearchTextSize);
        mSearchTextColor = typedArray.getColor(R.styleable.SearchTitleView_stvSearchTextColor, mSearchTextColor);

        mSearchStyle = typedArray.getInt(R.styleable.SearchTitleView_stvSearchStyle, mSearchStyle);
        typedArray.recycle();

        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.include_search_edittext_view, this);
        this.mContainerRl = this.findViewById(R.id.rl_container);
        this.mSearchIconIv = this.findViewById(R.id.search_icon_iv);
        this.mSearchBackIv = this.findViewById(R.id.search_back_iv);
        this.mSearchConfirmTv = this.findViewById(R.id.search_confirm_tv);
        this.mEditText = this.findViewById(R.id.search_input_et);
        this.mLineView = this.findViewById(R.id.line_view);
        this.mSearchClearIv = this.findViewById(R.id.search_clear_iv);
        this.mSearchInputRl = this.findViewById(R.id.search_input_rlyt);
        this.mBottomLine = this.findViewById(R.id.search_bottom_line);

        mEditText.requestFocus();
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_DONE);
        mEditText.setMaxEms(11);
        isFinish = true;
    }

    private void initData() {

        switch (mSearchStyle) {
            case 1:
                //有返回键自动搜索
                hasBackAutoSearch();
                break;
            case 2:
                //有返回键，有搜索按钮的搜索
                hasSearchBtn();
                break;
            default:
                //默认，无返回键自动搜索
                defaultSearch();
                break;
        }

        Log.e("TAG", "mSearchStyle ： " + mSearchStyle);
    }

    private void hasSearchBtn() {
        mSearchBackIv.setVisibility(VISIBLE);
        mSearchIconIv.setVisibility(GONE);
        mSearchConfirmTv.setVisibility(VISIBLE);
        mLineView.setVisibility(GONE);

        mSearchBackIv.setImageResource(R.drawable.back_vector_black);
        mContainerRl.setBackgroundColor(mSearchTitleBgColor == 0 ? getResources().getColor(R.color.white): mSearchTitleBgColor);
        mSearchIconIv.setImageResource(R.mipmap.search);
        mSearchInputRl.setBackgroundResource(mSearchEtBg == 0 ? R.drawable.shape_white_bg_4dp : mSearchEtBg);
        mSearchConfirmTv.setBackgroundResource(mSearchConfirmTvBg == 0 ? R.drawable.toolbar_btn_pressed_20dp_selector : mSearchConfirmTvBg);

        if(!TextUtils.isEmpty(mSearchHint)) {
            mEditText.setHint(mSearchHint);
        }

        if(!TextUtils.isEmpty(mSearchTvText)) {
            mSearchConfirmTv.setText(mSearchTvText);
        }

        mSearchConfirmTv.setTextSize(mSearchTextSize);
        mSearchConfirmTv.setTextColor(mSearchTextColor == 0 ? getResources().getColor(R.color.text_blue) : mSearchTextColor);
    }

    private void hasBackAutoSearch() {
        mSearchBackIv.setVisibility(VISIBLE);
        mSearchIconIv.setVisibility(GONE);
        mSearchConfirmTv.setVisibility(GONE);
        mLineView.setVisibility(VISIBLE);
        mBottomLine.setVisibility(GONE);
        mContainerRl.setBackgroundColor(mSearchTitleBgColor == 0 ? getResources().getColor(R.color.white) : mSearchTitleBgColor);

        if(!TextUtils.isEmpty(mSearchHint)) {
            mEditText.setHint(mSearchHint);
        }
    }

    private void defaultSearch() {
        mSearchBackIv.setVisibility(GONE);
        mSearchIconIv.setVisibility(VISIBLE);
        mSearchConfirmTv.setVisibility(GONE);
        mLineView.setVisibility(GONE);
        mBottomLine.setVisibility(VISIBLE);
        mContainerRl.setBackgroundColor(Color.WHITE);

        mSearchInputRl.setBackgroundResource(mSearchEtBg == 0 ? R.drawable.shape_white_bg_4dp : mSearchEtBg);

        if(!TextUtils.isEmpty(mSearchHint)) {
            mEditText.setHint(mSearchHint);
        }
    }

    public TextView getSearchConfirmTv() {
        return mSearchConfirmTv;
    }

    public void setHint(int id ) {
        mEditText.setHint(id);
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

    public void setBackVisibility(boolean isShow) {
        mSearchBackIv.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setIconVisibility(boolean isShow) {
        mSearchIconIv.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setLineVisibility(boolean isShow) {
        mLineView.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

//    public void setSearchConfirmVisibility(boolean isShow) {
//        mSearchConfirmTv.setVisibility(isShow ? VISIBLE : GONE);
//    }

    public void setSearchViewColor(int color) {
        mContainerRl.setBackgroundColor(color);
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setBackCilck(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public void setMaxLength(int length) {
        InputFilter[] filters = {new InputFilter.LengthFilter(length)};
        mEditText.setFilters(filters);
    }

    private void initListener() {
        this.mSearchBackIv.setOnClickListener(this);
        this.mSearchClearIv.setOnClickListener(this);
        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                return actionId == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        mSearchConfirmTv.setOnClickListener(this);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (null != mChangedListener) {
                mChangedListener.beforeChange();
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
            mSearchClearIv.setVisibility(count > 0 ? VISIBLE : INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (null != mChangedListener) {
                mChangedListener.textChanged(editable.toString());
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mSearchBackIv.getId()) {
            Utils.hideSoftInput(getContext(), mEditText);
            if(isFinish) {
                ((Activity) getContext()).finish();
            }else {
                if(null != mSearchViewOnClickListener) {
                    mSearchViewOnClickListener.onBackClick();
                }
            }
        } else if (id == mSearchClearIv.getId()) {
            mEditText.setText("");
        }
    }

    public void setChangedListener(ITextChangedListener changedListener) {
        mChangedListener = changedListener;
    }


    private ISearchViewOnClickListener mSearchViewOnClickListener;
    public interface ISearchViewOnClickListener{
        void onBackClick();
    }

    public void setSearchViewOnClickListener(ISearchViewOnClickListener searchViewOnClickListener) {
        this.mSearchViewOnClickListener = searchViewOnClickListener;
    }
}
