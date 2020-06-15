package com.wd.daquan.chat.group.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设置群聊号
 * Created by Kind on 2018/9/26.
 */

public class GroupNumberActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    private String mGroupId;
    private EditText edit_mark;
    private boolean isClickConfirm = false;
    /**
     * 输入框文本
     */
    private String editString = null;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_set_mark);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.group_manager_set_mark_hint));
        setBackView();
        getCommTitle().setRightTxt(getString(R.string.complete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
        edit_mark = findViewById(R.id.groupSetMarkEditText);
    }

    @Override
    protected void initData() {
        mGroupId = getIntent().getStringExtra(KeyValue.GROUPID);
    }

    @Override
    protected void initListener() {

    }

    /**
     * 放弃编辑的dialog
     */
    private Dialog mCancleEditDialog = null;


    private void onSubmit() {
        editString = edit_mark.getText().toString();
        if (TextUtils.isEmpty(editString)) {
            DqToast.showShort("请输入内容");
            return;
        }
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(editString.substring(0, 1));
        if (!m.matches()) {
            DqToast.showShort(getString(R.string.first_letter));
            return;
        }
        if (containSpace(editString) || !containSpecChar(editString)) {
            DqToast.showShort("群聊号不应包含空格或者特殊字符");
            return;
        }
        if (!hasNumber(editString)) {
            DqToast.showShort("群聊号应为字母和数字组合");
            return;
        }
        if (editString.length() < 6 || editString.length() > 15) {
            DqToast.showShort("群聊号应为6-15位");
            return;
        }
        settingGroupNumber(editString);
    }

    private CommDialog mRemoveFriendDialog;

    private void onDismissGroupNumber() {
        if (mRemoveFriendDialog != null) {
            mRemoveFriendDialog.dismiss();
            mRemoveFriendDialog = null;
        }
    }

    /**
     * 删除好友
     */
    private void settingGroupNumber(String editString) {
        onDismissGroupNumber();
        mRemoveFriendDialog = new CommDialog(this);
        mRemoveFriendDialog.setTitleVisible(true);
        mRemoveFriendDialog.setTitle(getString(R.string.group_group_confirm_mark));
        mRemoveFriendDialog.setDesc("你确定将群聊号设置为" + editString + "吗？确定后将不可以修改。");
        mRemoveFriendDialog.setOkTxt(getString(R.string.confirm1));
        mRemoveFriendDialog.setOkTxtColor(Color.RED);
        mRemoveFriendDialog.setCancelTxt(getString(R.string.comm_cancel));
        mRemoveFriendDialog.show();

        mRemoveFriendDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                mPresenter.setGroupManagerInfo(mGroupId, editString, 2);
            }
        });
    }

    //特殊字符判断
    public static boolean containSpecChar(CharSequence input) {

        return Pattern.compile("^[a-zA-Z0-9]+$").matcher(input).find();
    }

    //空格判断
    public static boolean containSpace(CharSequence input) {

        return Pattern.compile("\\s+").matcher(input).find();
    }

    // 判断一个字符串是否有数字
    public boolean hasNumber(String content) {
        boolean isHasNumber = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            isHasNumber = true;
        }
        return isHasNumber;
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(edit_mark.getText().toString().trim()) && !isClickConfirm) {
            showNoSetingDialog();
        } else {
            super.finish();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqToast.showShort(entity == null ? "请求出错！" : entity.getContent());
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (entity == null) return;
        if (DqUrl.url_edit_group_info.equals(url)) {
            DqToast.showShort(getString(R.string.set_success));
            Intent intent = new Intent();
            intent.putExtra(KeyValue.GROUP_REMARK, edit_mark.getText().toString().trim());
            setResult(RESULT_OK, intent);
            edit_mark.setText("");
            finish();
        }
    }

    /**
     * 未编辑
     */
    private void showNoSetingDialog() {
        mCancleEditDialog = DialogUtils.showPurseDialog(this, KeyValue.FOUR, "", new DialogUtils.BottomDialogListener() {
            @Override
            public void onClick(int id) {
                if(R.id.tv_confirm == id){
                    isClickConfirm = true;
                    finish();
                }
            }
        });
        mCancleEditDialog.show();
    }

    private void onDismiss() {
        if (mCancleEditDialog != null) {
            mCancleEditDialog.dismiss();
            mCancleEditDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDismiss();
        onDismissGroupNumber();
    }
}
