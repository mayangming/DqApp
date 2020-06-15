package com.wd.daquan.mine.user;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.da.library.view.DqToolbar;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.rxbus.MsgKey;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.da.library.listener.DialogListener;
import com.wd.daquan.mine.presenter.MinePresenter;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人昵称
 */

public class PersonalInfoSettingActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{
    private String type;//1昵称 2性别 3斗圈号
    private LinearLayout layout_name;
    private EditText edit_nickName;
    private ImageView img_nameClose;
    private LinearLayout layout_gender;
    private LinearLayout layout_genderMale;
    private LinearLayout layout_genderFemale;
    private ImageView img_male;
    private ImageView img_female;
    private LinearLayout layout_qsNum;
    private ImageView img_qsNumAvatar;
    private TextView txt_qsNumName;
    private EditText edit_qsNum;
    private ImageView img_qsNumClose;
    private TextView txt_qsNumIns;
    private TextView txt_qsNumWarn;
    private DqToolbar mToolbar;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.personal_info_setting_activity);
    }

    @Override
    protected void init() {
        type = getIntent().getStringExtra(KeyValue.PERSONAL_SETTING_TYPE);
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        layout_name = findViewById(R.id.personalInfoSettingLayoutName);
        edit_nickName = findViewById(R.id.personalInfoSettingEtName);
        img_nameClose = findViewById(R.id.personalInfoSettingImgName);

        layout_gender = findViewById(R.id.personalInfoSettingLayoutGender);
        layout_genderMale = findViewById(R.id.personalInfoSettingLayoutMale);
        layout_genderFemale = findViewById(R.id.personalInfoSettingLayoutFemale);
        img_male = findViewById(R.id.personalInfoSettingImgMale);
        img_female = findViewById(R.id.personalInfoSettingImgFemale);

        layout_qsNum = findViewById(R.id.personalInfoSettingLayoutQSNum);
        img_qsNumAvatar = findViewById(R.id.personalInfoSettingImgAvatar);
        txt_qsNumName = findViewById(R.id.personalInfoSettingTxtQCNumName);
        edit_qsNum = findViewById(R.id.personalInfoSettingEtQSNum);
        img_qsNumClose = findViewById(R.id.personalInfoSettingImgQCNum);
        txt_qsNumIns = findViewById(R.id.personalInfoSettingTxtQCNumIns);
        txt_qsNumWarn = findViewById(R.id.personalInfoSettingTxtQCNumwarning);

        DqUtils.setEditText(edit_nickName);
        DqUtils.setEditText(edit_qsNum);
    }

    @Override
    protected void initListener() {
        toolbarBack();
        toolbarRightTvOnClick(view -> {
            if(KeyValue.ONE_STRING.equals(type)){//昵称
                clickName();
            }else if(KeyValue.TWO_STRING.equals(type)){//性别
                clickGender();
            }else if(KeyValue.THREE_STRING.equals(type)){//斗圈聊号
                clickSettingQCNum();
            }
        });
        img_nameClose.setOnClickListener(this);
        img_qsNumClose.setOnClickListener(this);
        layout_genderMale.setOnClickListener(this);
        layout_genderFemale.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if(KeyValue.ONE_STRING.equals(type)){//昵称
            mToolbar.setTitle(getString(R.string.personal_info_setting_name));
            layout_name.setVisibility(View.VISIBLE);
            layout_gender.setVisibility(View.GONE);
            layout_qsNum.setVisibility(View.GONE);
            nickNameSetting();
        }else if(KeyValue.TWO_STRING.equals(type)){//性别
            mToolbar.setTitle(getString(R.string.personal_info_gender));
            layout_name.setVisibility(View.GONE);
            layout_gender.setVisibility(View.VISIBLE);
            layout_qsNum.setVisibility(View.GONE);
            genderSetting();
        }else if(KeyValue.THREE_STRING.equals(type)){//斗圈号
            mToolbar.setTitle(getString(R.string.personal_info_qingchat));
            layout_name.setVisibility(View.GONE);
            layout_gender.setVisibility(View.GONE);
            layout_qsNum.setVisibility(View.VISIBLE);
            qingChatNumSetting();
        }
    }
    private void nickNameSetting(){
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        String nick;
        if (nickName.length()>16){
            nick=nickName.substring(0,16);
        }else {
            nick=nickName;
        }
        edit_nickName.setText(nick);
        edit_nickName.setSelection(nick.length());
        if(!TextUtils.isEmpty(nickName)){
            img_nameClose.setVisibility(View.VISIBLE);
        }else{
            img_nameClose.setVisibility(View.GONE);
        }
        edit_nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()==0){
                    img_nameClose.setVisibility(View.GONE);
                }else{
                    img_nameClose.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private String gender;
    private void genderSetting(){

        String sex =  ModuleMgr.getCenterMgr().getSex();
        if (!TextUtils.isEmpty(sex)){
            switch (sex) {
                case KeyValue.ONE_STRING:
                    gender = KeyValue.ONE_STRING;
                    img_male.setVisibility(View.VISIBLE);
                    img_female.setVisibility(View.GONE);
                    break;
                case KeyValue.TWO_STRING:
                    gender = KeyValue.TWO_STRING;
                    img_male.setVisibility(View.GONE);
                    img_female.setVisibility(View.VISIBLE);
                    break;
                default:
                    gender = KeyValue.ONE_STRING;
                    img_male.setVisibility(View.GONE);
                    img_female.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void qingChatNumSetting(){

        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        txt_qsNumName.setText(nickName);
        if (!TextUtils.isEmpty(headPic)) {
            GlideUtils.load(this, headPic + DqUrl.url_avatar_suffix, img_qsNumAvatar);
        }
        edit_qsNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    img_qsNumClose.setVisibility(View.VISIBLE);
                } else {
                    img_qsNumClose.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null == edit_qsNum) {
                    return;
                }
                String content = edit_qsNum.getText().toString();
                boolean flag = isCorrect(content);
                if (!flag) {
                    txt_qsNumWarn.setVisibility(View.VISIBLE);
                    txt_qsNumIns.setVisibility(View.GONE);
                    mToolbar.setRightTvClick(false);
                } else {
                    txt_qsNumWarn.setVisibility(View.GONE);
                    txt_qsNumIns.setVisibility(View.VISIBLE);
                    mToolbar.setRightTvClick(true);
                }
            }
        });
    }
    /**
     * 是否正确
     *
     * @param content
     * @return 以字母开头，使用6-20个字母，数字、下划线或减号
     */
    private boolean isCorrect(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        int len = content.length();
        String target = "(^[A-Za-z][\\w-]*$)";
        Pattern pattern = Pattern.compile(target);
        Matcher m = pattern.matcher(content);

        if (len >= 6 && len <= 20) {
            return m.find();
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.personalInfoSettingImgName://昵称clean
                edit_nickName.setText("");
                img_nameClose.setVisibility(View.GONE);
                break;
            case R.id.personalInfoSettingLayoutMale:
                gender = KeyValue.ONE_STRING;
                img_male.setVisibility(View.VISIBLE);
                img_female.setVisibility(View.GONE);
                break;
            case R.id.personalInfoSettingLayoutFemale:
                gender = KeyValue.TWO_STRING;
                img_male.setVisibility(View.GONE);
                img_female.setVisibility(View.VISIBLE);
                break;
            case R.id.personalInfoSettingImgQCNum://斗圈号clean
                edit_qsNum.setText("");
                img_qsNumClose.setVisibility(View.GONE);
                break;
        }
    }

    private void clickName(){
        String nickName = edit_nickName.getText().toString();
        if(TextUtils.isEmpty(nickName)){
            DqToast.showShort(getString(R.string.personal_info_setting_name_is_null));
            return;
        }
        if(nickName.contains(" ")){
            DqToast.showShort(getString(R.string.personal_info_setting_name_no_space));
            return;
        }
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("nickname", nickName);
        mPresenter.saveUserInfoRequest(DqUrl.url_set_userinfo,linkedHashMap);
    }
    private void clickGender(){
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("sex", gender);
        mPresenter.saveUserInfoRequest(DqUrl.url_set_userinfo,linkedHashMap);
    }

    private void clickSettingQCNum(){
        String content = edit_qsNum.getText().toString();
        String desc = String.format(this.getResources().getString(R.string.personal_info_setting_qc_num_tips), content);
        DialogUtils.showSettingQCNumDialog(this, "", desc, getString(R.string.modify), getString(R.string.determine),
                new DialogListener() {
                    @Override
                    public void onCancel() {

                    }
                    @Override
                    public void onOk() {
                        String content = edit_qsNum.getText().toString();
                        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
                        hashMap.put("dq_num", content);
                        mPresenter.setQSNum(DqUrl.url_set_userinfo, hashMap);
                    }
                });
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(DqUrl.url_set_userinfo.equals(url)){//昵称和性别
            if(KeyValue.ONE_STRING.equals(type)){//昵称
                DqToast.showShort(getString(R.string.save_success));
                String nickName = edit_nickName.getText().toString().trim();

                String uid = ModuleMgr.getCenterMgr().getUID();
                Friend friend = FriendDbHelper.getInstance().getFriend(uid);
                if(friend != null){
                    friend.nickname = nickName;
                    FriendDbHelper.getInstance().update(friend, null);
                }

                ModuleMgr.getCenterMgr().putNickName(nickName);
                MsgMgr.getInstance().sendMsg(MsgType.MT_CENTER_PERSONALINFO_CHANGE, true);
                finish();
            }else if(KeyValue.TWO_STRING.equals(type)){//性别
                DqToast.showShort(getString(R.string.save_success));
                String uid = ModuleMgr.getCenterMgr().getUID();
                Friend friend = FriendDbHelper.getInstance().getFriend(uid);
                if(friend != null){
                    friend.sex = gender;
                    FriendDbHelper.getInstance().update(friend, null);
                }
                ModuleMgr.getCenterMgr().saveSex(gender);
                finish();
            }else if(KeyValue.THREE_STRING.equals(type)){//斗圈号
                // 成功后，保存
                String dqNum = edit_qsNum.getText().toString();
                ModuleMgr.getCenterMgr().putDqNum(edit_qsNum.getText().toString());
                MsgMgr.getInstance().sendMsg(MsgType.MT_CENTER_PERSONALINFO_CHANGE, MsgKey.Personalinfo_Change.Personalinfo_Change_QingChatNum);

                String uid = ModuleMgr.getCenterMgr().getUID();
                Friend friend = FriendDbHelper.getInstance().getFriend(uid);
                if(friend != null){
                    friend.dq_num = dqNum;
                    FriendDbHelper.getInstance().update(friend, null);
                }
                DqToast.showShort(getString(R.string.personal_info_setting_qc_num_suc));
                finish();
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if(KeyValue.THREE_STRING.equals(type)){//斗圈号
            // 失败后继续弹窗
            DialogUtils.showSingleBtnSettingQCNumDialog(this, getString(R.string.personal_info_setting_qc_num_fail),
                    entity.content, getString(R.string.confirm), true);
        }
    }
}
