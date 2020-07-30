package com.wd.daquan.contacts.activity;

import android.content.ClipboardManager;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.da.library.constant.IConstant;
import com.wd.daquan.contacts.adapter.GroupPersonalInfoShowAdapter;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.log.DqToast;
import com.da.library.widget.CommTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/26 10:31
 * @Description: 群成员个人资料
 */
public class GroupPersonalInfoActivity extends DqBaseActivity<ContactPresenter, DataBean<GroupMemberBean>> implements View.OnClickListener {

    private CommTitle mTitleLayout;
    private TextView mGroupNicknameTv;
    private TextView mPhoneNumberTV;
    private TextView mWeiXinNumberTv;
    private TextView mZhiFuBaoTv;
    private TextView mDescTitleTv;
    private RecyclerView mRecyclerView;
    private GroupPersonalInfoShowAdapter mAdapter;
    private ClipboardManager cmb;

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.personal_info_title_layout);
        mGroupNicknameTv = findViewById(R.id.tv_group_nickname);
        mPhoneNumberTV = findViewById(R.id.tv_mobile_phone_number);
        mWeiXinNumberTv = findViewById(R.id.tv_wei_xin_number);
        mZhiFuBaoTv = this.findViewById(R.id.tv_zhi_fu_bao);
        mRecyclerView = findViewById(R.id.desc_recycler_view);
        mDescTitleTv = findViewById(R.id.tv_desc_title);

        mDescTitleTv.setVisibility(View.VISIBLE);



    }

    @Override
    protected void initData() {
        setHeader();

        String userId = getIntent().getStringExtra(IConstant.UserInfo.UID);
        String groupId = getIntent().getStringExtra(IConstant.UserInfo.GROUP_ID);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroupPersonalInfoShowAdapter();
        mRecyclerView.setAdapter(mAdapter);

        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(groupId)) {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.UserInfo.OTHER_UID, userId);
            hashMap.put(IConstant.UserInfo.GROUP_ID, groupId);
            mPresenter.getGroupUserInfo(DqUrl.url_select_group_member, hashMap, true);
        }


    }

    /**
     * 初始化标题栏
     */
    public void setHeader() {
        mTitleLayout.setTitle(getString(R.string.detaile_info));
        mTitleLayout.getRightTv().setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mTitleLayout.getLeftIv().setOnClickListener(this);

        //初始化粘贴板
        cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        mGroupNicknameTv.setOnLongClickListener((View v) -> {
            TextView textView = (TextView) v;
            //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
            cmb.setText(textView.getText().toString().trim());
            DqToast.showShort(getResources().getString(R.string.copy_text_success));
            return true;
        });
        mPhoneNumberTV.setOnLongClickListener(v -> {
            TextView textView = (TextView) v;
            cmb.setText(textView.getText().toString().trim());
            DqToast.showShort(getResources().getString(R.string.copy_text_success));
            return true;
        });
        mWeiXinNumberTv.setOnLongClickListener(v -> {
            TextView textView = (TextView) v;
            cmb.setText(textView.getText().toString().trim());
            DqToast.showShort(getResources().getString(R.string.copy_text_success));
            return true;
        });
        mZhiFuBaoTv.setOnLongClickListener(v -> {
            TextView textView = (TextView) v;
            cmb.setText(textView.getText().toString().trim());
            DqToast.showShort(getResources().getString(R.string.copy_text_success));
            return true;
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onFailed(String url, int code, DataBean<GroupMemberBean> entity) {
        if(null == entity) return;
        mDescTitleTv.setVisibility(View.GONE);
        DqUtils.bequit(entity, this);
    }

    /**
     * 加载群成员个人信息成功
     *
     * @param dataEntity 返回数据
     */
    private void onSuccessPersonalInfo(DataBean<GroupMemberBean> dataEntity) {
        if (KeyValue.OK.equalsIgnoreCase(dataEntity.status)) {
            GroupMemberBean userInfo = dataEntity.data;
            if (null != userInfo) {
                //群昵称
                if (TextUtils.isEmpty(userInfo.remarks)) {
                    mGroupNicknameTv.setText(R.string.not_set_personal_info);
                    mGroupNicknameTv.setTextColor(getResources().getColor(R.color.color_3c74ba));
                } else {
                    mGroupNicknameTv.setText(userInfo.remarks);
                }
                //电话
                if (TextUtils.isEmpty(userInfo.phone)) {
                    mPhoneNumberTV.setText(R.string.not_set_personal_info);
                    mPhoneNumberTV.setTextColor(getResources().getColor(R.color.color_3c74ba));
                } else {
                    mPhoneNumberTV.setText(userInfo.phone);
                }
                //微信
                if (TextUtils.isEmpty(userInfo.wechat_account)) {
                    mWeiXinNumberTv.setText(R.string.not_set_personal_info);
                    mWeiXinNumberTv.setTextColor(getResources().getColor(R.color.color_3c74ba));
                } else {
                    mWeiXinNumberTv.setText(userInfo.wechat_account);
                }
                //支付宝
                if (TextUtils.isEmpty(userInfo.alipay_account)) {
                    mZhiFuBaoTv.setText(R.string.not_set_personal_info);
                    mZhiFuBaoTv.setTextColor(getResources().getColor(R.color.color_3c74ba));
                } else {
                    mZhiFuBaoTv.setText(userInfo.alipay_account);
                }

                //描述
                List<String> descriptions = userInfo.description;
                if (null != descriptions && descriptions.size() > 0) {
                    mAdapter.update(descriptions);
                } else {
                    mDescTitleTv.setVisibility(View.GONE);
                }
            }
        } else {
            mDescTitleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean<GroupMemberBean> entity) {
        if(null == entity) return;
        if(DqUrl.url_select_group_member.equals(url)) {
            onSuccessPersonalInfo(entity);
        }
    }
}
