package com.wd.daquan.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;


/**
 */
public class CommSwitchButton extends RelativeLayout {

    private String mTitle;
    private String mDesc;
    private boolean showDesc = false; // 默认描述不可见
    private boolean showSwitch = true;  // 默认swithButton 可见

    private SwitchButton mSwitchButton = null;
    private TextView mTitleTv;
    private TextView mDescTv;
//    private View mEmptyTv;

    public CommSwitchButton(Context context) {
        super(context);
        init(context, null);
    }

    public CommSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttributeSet(context, attrs);
        initView(context);
        initData();
        initListener();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.comm_switch_btn_layout, this);
        this.mSwitchButton = this.findViewById(R.id.comm_switch_btn);
        this.mTitleTv = this.findViewById(R.id.comm_switch_title);
        this.mDescTv = this.findViewById(R.id.comm_switch_desc);
//        this.mEmptyTv = this.findViewById(R.id.comm_switch_empty);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommSwitchButton);
        if (typedArray != null) {
            mTitle = typedArray.getString(R.styleable.CommSwitchButton_swBtnTitle);
            mDesc = typedArray.getString(R.styleable.CommSwitchButton_swBtnDesc);
            showDesc = typedArray.getBoolean(R.styleable.CommSwitchButton_swBtnDescVisible, false);
            showSwitch = typedArray.getBoolean(R.styleable.CommSwitchButton_swBtnVisible, true);
            typedArray.recycle();
        }
    }

    private void initData() {
        setTitle(mTitle);
        setDescription(mDesc);
        if (showSwitch) {
            setSwitchVisible(View.VISIBLE);
        } else {
            setSwitchVisible(View.GONE);
        }

        if (showDesc) {
            setDescriptionVisible(View.VISIBLE);
        } else {
            setDescriptionVisible(View.GONE);
        }
    }

    private void initListener() {
        mSwitchButton.setOnCheckedChangeListener(mCheckedChangeListener);

    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setTitle(int id) {
        mTitleTv.setText(DqApp.getStringById(id));
    }

    public void setDescription(String desc) {
        mDescTv.setText(desc);
    }

    public void setDescription(int id) {
        mDescTv.setText(DqApp.getStringById(id));
    }

    public void setSwitchVisible(int visible) {
        mSwitchButton.setVisibility(visible);
    }

    public void setDescriptionVisible(int visible) {
        mDescTv.setVisibility(visible);
//        mEmptyTv.setVisibility(visible);
    }

    public void setCheckedNoEvent(boolean isChecked) {
//        mSwitchButton.setCheckedNoEvent(isChecked);
        mSwitchButton.setChecked(isChecked);
    }

    public void setCheckedImmediatelyNoEvent(boolean isChecked) {
//        mSwitchButton.setCheckedImmediatelyNoEvent(isChecked);
//        mSwitchButton.setChecked();
    }

    public void setChecked(boolean isChecked) {
        mSwitchButton.setChecked(isChecked);
    }

    public boolean isChecked() {
        return mSwitchButton.isChecked();
    }

    private OnSwChangedListener mSwChangedListener = null;
    public void setOnSwChangedListener(OnSwChangedListener listener) {
        mSwChangedListener = listener;
    }

//    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if (mSwChangedListener != null) {
//                mSwChangedListener.onChanged(getId(), buttonView, isChecked);
//            }
//        }
//    };

    private SwitchButton.OnCheckedChangeListener mCheckedChangeListener = new SwitchButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            if (mSwChangedListener != null) {
                mSwChangedListener.onChanged(getId(), view, isChecked);
            }
        }
    };

    public interface OnSwChangedListener {
        void onChanged(int id, View v, boolean checkState);
    }

}
