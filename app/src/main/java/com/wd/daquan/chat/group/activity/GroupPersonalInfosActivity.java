package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.da.library.tools.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 群个人信息
 * Created by Kind on 2018/9/25.
 */

public class GroupPersonalInfosActivity extends DqBaseActivity<ChatPresenter, DataBean>
        implements View.OnClickListener {

    private EditText mEtGroupNickname;
    private EditText mEtMobilePhoneNumber;
    private EditText mEtZhiFuBao;
    private EditText mEtWeiXinNumber;
    private ImageView mIvClearNickname;
    private ImageView mIvClearPhoneNumber;
    private ImageView mIvClearWeiXinNumber;
    private ImageView mIvClearZhifubao;
    private LinearLayout mLlDescContainer;
    private LinearLayout mAddDescribeLlyt;
    private NestedScrollView mScrollView;

    private List<EditText> mEditTextList = new ArrayList<>();
    /**
     * 描述输入文字集合
     */
    private LinkedHashMap<View, String> map = new LinkedHashMap<>();
    /**
     * 个人昵称
     */
    private String mNickName;
    /**
     * 个人电话号码
     */
    private String mPhoneNumber;
    /**
     * 个人支付宝
     */
    private String mZhiFuBao;
    /**
     * 个人微信号
     */
    private String mWeiXinNumber;
    /**
     * 群组id
     */
    private String mGroupId;

    /**
     * 放弃编辑的dialog
     */
    private Dialog mExitConfirmDialog = null;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.cn_group_personal_infos_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.edit_personal_info));
        getCommTitle().setTitleTextLength(10);
        setBackView();
        getCommTitle().setRightTxt(getString(R.string.complete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadData();
            }
        });
        getCommTitle().setRightTxtColor(getResources().getColor(R.color.white));

        mEtGroupNickname = this.findViewById(R.id.et_group_nickname);
        mEtMobilePhoneNumber = this.findViewById(R.id.et_mobile_phone_number);
        mEtZhiFuBao = this.findViewById(R.id.et_zhi_fu_bao);
        mEtWeiXinNumber = this.findViewById(R.id.et_wei_xin_number);
        mLlDescContainer = this.findViewById(R.id.ll_desc_container);
        mIvClearNickname = this.findViewById(R.id.iv_clear_nickname);
        mIvClearPhoneNumber = this.findViewById(R.id.iv_clear_phone_number);
        mIvClearWeiXinNumber = this.findViewById(R.id.iv_clear_wei_xin_number);
        mIvClearZhifubao = this.findViewById(R.id.iv_clear_zhifubao);
        mScrollView = this.findViewById(R.id.scroll_view);
        mAddDescribeLlyt = this.findViewById(R.id.ll_add_describe);

        initDialog();
    }

    @Override
    protected void initData() {
        getCommTitle().setRightTxtColor(getResources().getColor(R.color.white));
        mGroupId = getIntent().getStringExtra(KeyValue.GROUPID);

        loadInputContext();
        showOrHideClearBtn(mNickName, mIvClearNickname);
        showOrHideClearBtn(mPhoneNumber, mIvClearPhoneNumber);
        showOrHideClearBtn(mZhiFuBao, mIvClearZhifubao);
        showOrHideClearBtn(mWeiXinNumber, mIvClearWeiXinNumber);

        showLoading();
        GroupMemberBean teamMember = MemberDbHelper.getInstance().getTeamMember(mGroupId, ModuleMgr.getCenterMgr().getUID());
        if (teamMember != null) {
            onMemberInfo(teamMember);
        } else {
            initDescribe(getString(R.string.empty));
            mEtGroupNickname.setFocusable(true);
            mEtGroupNickname.requestFocus();
        }
    }

    @Override
    protected void initListener() {
        //群昵称输入监听
//        mEtGroupNickname.setFilters(new InputFilter[]{mMmptyFilter});
        mEtGroupNickname.addTextChangedListener(mEtGroupNicknameListener);
        //手机号输入监听
        mEtMobilePhoneNumber.addTextChangedListener(mEtMobilePhoneListener);

        //支付宝账户输入监听
//        mEtZhiFuBao.setFilters(new InputFilter[]{mMmptyFilter, mChineseFilter});
        mEtZhiFuBao.addTextChangedListener(mEtZhiFuBaoListener);
        //微信号输入监听
//        mEtWeiXinNumber.setFilters(new InputFilter[]{mMmptyFilter, mChineseFilter});
        mEtWeiXinNumber.addTextChangedListener(mEtWeiXinNumberListener);

        mIvClearPhoneNumber.setOnClickListener(this);
        mIvClearNickname.setOnClickListener(this);
        mIvClearWeiXinNumber.setOnClickListener(this);
        mIvClearZhifubao.setOnClickListener(this);
        mAddDescribeLlyt.setOnClickListener(this);
    }

    /**
     * 加载群成员个人信息成功
     *
     * @param memberInfo
     */
    private void onMemberInfo(GroupMemberBean memberInfo) {
        //群昵称
        if (!TextUtils.isEmpty(memberInfo.remarks)) {
            mEtGroupNickname.setText(memberInfo.remarks);
            //给昵称设置焦点
            mEtGroupNickname.setSelection(memberInfo.remarks.trim().length());
        }

        //电话
        if (!TextUtils.isEmpty(memberInfo.phone)) {
            mEtMobilePhoneNumber.setText(memberInfo.phone);
        }

        //微信
        if (!TextUtils.isEmpty(memberInfo.wechat_account)) {
            mEtWeiXinNumber.setText(memberInfo.wechat_account);
        }

        //支付宝
        if (!TextUtils.isEmpty(memberInfo.alipay_account)) {
            mEtZhiFuBao.setText(memberInfo.alipay_account);
        }

        //描述
        List<String> descriptions = memberInfo.description;
        if (null != descriptions && descriptions.size() > 0) {
            int min = Math.min(10, descriptions.size());
            for (int i = 0; i < min; i++) {
                initDescribe(descriptions.get(i));
            }
        } else {
            initDescribe(getString(R.string.empty));
            mEtGroupNickname.setFocusable(true);
            mEtGroupNickname.requestFocus();
        }
    }

    /**
     * 群昵称输入监听
     */
    private TextWatcher mEtGroupNicknameListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            limitEditTextInputLength(s.toString(), mEtGroupNickname, 16);
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();
            showOrHideClearBtn(input, mIvClearNickname);
//            changeConfirmColor(input, mNickName);
        }
    };
    /**
     * 手机号输入监听
     */
    private TextWatcher mEtMobilePhoneListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            limitEditTextInputLength(s.toString(), mEtMobilePhoneNumber, 11);

        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();
            showOrHideClearBtn(input, mIvClearPhoneNumber);
//            changeConfirmColor(input, mPhoneNumber);
        }
    };
    /**
     * 支付宝账户输入监听
     */
    private TextWatcher mEtZhiFuBaoListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            limitEditTextInputLength(s.toString(), mEtZhiFuBao, 20);
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();
            showOrHideClearBtn(input, mIvClearZhifubao);
//            changeConfirmColor(input, mZhiFuBao);
        }
    };
    /**
     * 微信号输入监听
     */
    private TextWatcher mEtWeiXinNumberListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            limitEditTextInputLength(s.toString(), mEtWeiXinNumber, 20);
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();
            showOrHideClearBtn(input, mIvClearWeiXinNumber);
//            changeConfirmColor(input, mWeiXinNumber);
        }
    };
    /**
     * 删除描述的dialog
     */
    private Dialog mDeleteDescDialog = null;
    private View deleteView = null;
    /**
     * handler
     */
    private Handler mHandler = new Handler();

//    /**
//     * 空格限制
//     */
//    private InputFilter mMmptyFilter = new InputFilter() {
//
//        Pattern pattern = Pattern.compile(" ");
//
//        @Override
//        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
//            Matcher matcher = pattern.matcher(charSequence);
//            if (!matcher.find()) {
//                return null;
//            } else {
//                return "";
//            }
//        }
//    };
//
//    /**
//     * 中文限制
//     */
//    private InputFilter mChineseFilter = new InputFilter() {
//
//        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]");
//
//        @Override
//        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
//            Matcher matcher = pattern.matcher(charSequence);
//            if (!matcher.find()) {
//                return null;
//            } else {
//                return "";
//            }
//        }
//    };

//    /**
//     * 判断新输入的内容和初始化的内容是否一致
//     * 而改变确定按钮的颜色
//     *
//     * @param newText 新输入的内容
//     * @param oldText 之前赋值的内容
//     */
//    private void changeConfirmColor(String newText, String oldText) {
//        if (newText.equals(oldText)) {
//            tvTopRighttitle.setTextColor(getResources().getColor(R.color.colormain_8C8C8C));
//            tvTopRighttitle.setClickable(false);
//        } else {
//            tvTopRighttitle.setTextColor(getResources().getColor(R.color.white));
//            tvTopRighttitle.setClickable(true);
//        }
//    }

    /**
     * 限制editext 输入长度
     *
     * @param input    输入内容
     * @param editText 输入视图
     * @param length   限制长度
     */
    private void limitEditTextInputLength(String input, EditText editText, int length) {
        if (input.length() > length) {
            editText.setText(input.substring(0, length));
            editText.setSelection(length);
            switch (length) {
                case 11://手机号
                    DqToast.showShort(getResources().getString(R.string.group_phone_max_hint));
                    break;
                case 16://群昵称
                    DqToast.showShort(getResources().getString(R.string.group_nickname_max_hint));
                    break;
                case 20://微信号
                    if (editText.equals(mEtWeiXinNumber)) {
                        DqToast.showShort(getResources().getString(R.string.group_weixin_max_hint));
                    } else {
                        DqToast.showShort(getResources().getString(R.string.group_alipay_max_hint));
                    }
                    break;
                case 70://描述
                    DqToast.showShort(getResources().getString(R.string.group_user_desc_max_hint));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //添加描述
            case R.id.ll_add_describe:
                addDescribe();
                break;
            //清空昵称
            case R.id.iv_clear_nickname:
                mEtGroupNickname.setText(getString(R.string.empty));
                break;
            //清空手机号
            case R.id.iv_clear_phone_number:
                mEtMobilePhoneNumber.setText(getString(R.string.empty));
                break;
            //清空微信号
            case R.id.iv_clear_wei_xin_number:
                mEtWeiXinNumber.setText(getString(R.string.empty));
                break;
            //清空支付宝账号
            case R.id.iv_clear_zhifubao:
                mEtZhiFuBao.setText(getString(R.string.empty));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEtGroupNickname.setFocusable(true);
        mEtGroupNickname.requestFocus();
    }

    /**
     * 放弃编辑退出
     */
    private void back() {
        //boolean clickable = tvTopRighttitle.isClickable();
        loadInputContext();
        if (TextUtils.isEmpty(mNickName) && TextUtils.isEmpty(mPhoneNumber) && TextUtils.isEmpty(mWeiXinNumber)
                && TextUtils.isEmpty(mZhiFuBao)) {
            finish();

        } else {
            if (mExitConfirmDialog == null) {
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }
                mExitConfirmDialog = DialogUtils.showPurseDialog(activity, 4, "", dialogListener);
            }

            if (!mExitConfirmDialog.isShowing()) {
                mExitConfirmDialog.show();
            }
        }
    }

    /**
     * 放弃编辑的dialog监听
     */
    DialogUtils.BottomDialogListener dialogListener = new DialogUtils.BottomDialogListener() {
        @Override
        public void onClick(int id) {
            if (id == R.id.tv_confirm) {
                finish();
                return;
            }
        }
    };


    @Override
    public void onFailed(String url, int code, DataBean entity) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        if (DqUrl.url_set_remark.equals(url)) {
            DqToast.showShort(getString(R.string.replace_personal_info_error));
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        Activity activity = getActivity();
        if (activity == null || entity == null) {
            return;
        }

        if (entity.isSuccess() && DqUrl.url_set_remark.equals(url)) {
            DqToast.showShort(getString(R.string.replace_personal_info_success));
            //TODO 暂时没用
            String name = mEtGroupNickname.getText().toString().trim();
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_MY_GROUP_INFO, null);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExitConfirmDialog != null) {
            mExitConfirmDialog.dismiss();
            mExitConfirmDialog = null;
        }

        if (mDeleteDescDialog != null) {
            mDeleteDescDialog.dismiss();
            mDeleteDescDialog = null;
        }
    }

    public void setDeleteView(View deleteView) {
        this.deleteView = deleteView;
    }

    /**
     * 初始化dialog
     */
    @SuppressLint("InflateParams")
    private void initDialog() {
        mDeleteDescDialog = new Dialog(this, R.style.kangzai_dialog);
        //删除描述dialog view
        View view = LayoutInflater.from(this).inflate(R.layout.purse_dlg, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvConfirm.setText(getString(R.string.comm_ok));
        tvMessage.setText(getString(R.string.is_delete_describe));
        //删除描述dialog 取消事件
        tvCancel.setOnClickListener(v -> mDeleteDescDialog.dismiss());

        //删除描述dialog 确定事件
        tvConfirm.setOnClickListener(v -> {
            mLlDescContainer.removeView(deleteView);
            map.remove(deleteView);
            mDeleteDescDialog.dismiss();
        });

        mDeleteDescDialog.setContentView(view);

        Window window = mDeleteDescDialog.getWindow();
        // 获取对话框当前的参数值
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        view.measure(0, 0);
        lp.width = (int) (DensityUtil.getScreenWidth(this) * 0.85);
        window.setAttributes(lp);
    }

    /**
     * 动态加载描述，最多10条，默认一条
     */
    private void initDescribe(String desc) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        //描述布局
        View view = LayoutInflater.from(activity).inflate(R.layout.cn_personal_info_edit_item, mLlDescContainer, false);
        ImageView ivDeletedesc = view.findViewById(R.id.iv_delete_desc);
        EditText etInput = view.findViewById(R.id.et_input);
        ImageView ivClearInput = view.findViewById(R.id.iv_clear_input);
        etInput.setText(desc);
        mLlDescContainer.addView(view);
        map.put(view, desc);
        mEditTextList.add(etInput);

        //删除评论，弹出dialog确认
        ivDeletedesc.setOnClickListener(v -> {
            if (mLlDescContainer.getChildCount() < KeyValue.TWO) {
                DqToast.showShort(getString(R.string.at_least_describe));
                return;
            }

            if (!mDeleteDescDialog.isShowing()) {
                setDeleteView(view);
                mDeleteDescDialog.show();
            }
        });

        showOrHideClearBtn(desc, ivClearInput);
        //删除评论，清空评论
        ivClearInput.setOnClickListener(v -> etInput.setText(""));
        //删除评论，文字变化
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                limitEditTextInputLength(s.toString(), etInput, 70);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                showOrHideClearBtn(input, ivClearInput);
//                changeConfirmColor(input, mWeiXinNumber);
                map.put(view, input);
            }
        });
    }

    /**
     * 判断输入框的内容是否显示清空按钮
     *
     * @param text      输入框内容
     * @param imageView 清空按钮
     */
    private void showOrHideClearBtn(String text, ImageView imageView) {
        imageView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始读取输入框内容
     */
    private void loadInputContext() {
        mNickName = mEtGroupNickname.getText().toString().trim();
        mPhoneNumber = mEtMobilePhoneNumber.getText().toString().trim();
        mZhiFuBao = mEtZhiFuBao.getText().toString().trim();
        mWeiXinNumber = mEtWeiXinNumber.getText().toString().trim();
    }

    /**
     * 上传群组个人数据
     */
    private void upLoadData() {
        loadInputContext();

        List<String> datas = new ArrayList<>();
        for (View key : map.keySet()) {
            String value = map.get(key);
            if (!TextUtils.isEmpty(value)) {
                datas.add(value);
            }
        }
        String descs = new Gson().toJson(datas);
        if (!TextUtils.isEmpty(mGroupId)) {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(KeyValue.TYPE, KeyValue.THREE_STRING);
            hashMap.put(KeyValue.GROUP_ID, mGroupId);
            hashMap.put(KeyValue.REMARKS, mNickName);
            hashMap.put(KeyValue.PHONE, mPhoneNumber);
            hashMap.put(KeyValue.WECHAT_ACCOUNT, mWeiXinNumber);
            hashMap.put(KeyValue.ALIPAY_ACCOUNT, mZhiFuBao);
            hashMap.put(KeyValue.DESCRIPTION, descs);

            mPresenter.upLoadGroupPersonalInfo(DqUrl.url_set_remark, hashMap);
        }
    }

    /**
     * 添加描述,最多10条
     */
    private void addDescribe() {
        int childCount = mLlDescContainer.getChildCount();
        if (childCount < 10) {
            initDescribe(getString(R.string.empty));
            mHandler.post(() -> mScrollView.fullScroll(ScrollView.FOCUS_DOWN));
        } else {
            DqToast.showShort(getString(R.string.max_item));
        }

        int size = mEditTextList.size();
        if (size > 1) {
            EditText editText2 = mEditTextList.get(size - 1);
            mHandler.post(() -> {
                editText2.setFocusable(true);
                editText2.requestFocus();
            });
        }
    }

    /**
     * 空白处关闭键盘
     *
     * @param ev 触摸事件
     * @return true/false
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                assert v != null;
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回键
     *
     * @param keyCode 值
     * @param event   按键
     * @return true/false
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token token
     */
    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击键盘外关闭键盘
     *
     * @param event 点击事件
     * @return true/false
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
