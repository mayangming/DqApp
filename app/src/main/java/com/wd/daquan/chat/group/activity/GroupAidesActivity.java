package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.bean.PluginBean;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.da.library.listener.DialogListener;
import com.da.library.tools.DateUtil;
import com.da.library.view.CommDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2019/1/4 14:19
 * @Description: 群助手页面
 */
@SuppressLint("SetTextI18n")
public class GroupAidesActivity extends DqBaseActivity<ChatPresenter, DataBean> implements QCObserver, View.OnClickListener {

    private LinearLayout mGroupAidesLl;
    private ImageView mLogoIv;
    private TextView mNameTv;
    private TextView mUpdateAidesTv;
    private TextView mDeleteAidesTv;
    //作用描述
    private TextView mUsedDescTv;
    private TextView mUpdateTimeTv;
    private TextView mDisclaimerTv;
    private TextView mAuthorTv;
    //关键字
//    private TextView mHasKeyworTv;
//    private RelativeLayout mNoKeywordRl;
    private RelativeLayout mNoDataRl;
    //群组id
    private String mGroupId;
    private PluginBean mPluginBean;
    private CommDialog mDeleteAidesDialog;
    //是否是已经添加的
    private boolean isExit;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_aides);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mGroupAidesLl = findViewById(R.id.group_aides_ll);
        mLogoIv = findViewById(R.id.group_aides_logo_iv);
        mNameTv = findViewById(R.id.group_aides_name_tv);
        mUpdateAidesTv = findViewById(R.id.group_aides_replace_tv);
        mDeleteAidesTv = findViewById(R.id.group_aides_delete_tv);

        mUsedDescTv = findViewById(R.id.group_aides_used_desc_tv);
        mUpdateTimeTv = findViewById(R.id.group_aides_update_time_tv);
        mAuthorTv = findViewById(R.id.group_aides_author_tv);
        mDisclaimerTv = findViewById(R.id.group_aides_disclaimer_tv);

//        mHasKeyworTv = findViewById(R.id.group_aides_has_keyword_tv);
//        mNoKeywordRl = findViewById(R.id.group_aides_no_keyword_rl);
//        mNoKeywordRl.setVisibility(View.GONE);

        mNoDataRl = findViewById(R.id.no_data_rl);

        mGroupAidesLl.setVisibility(View.VISIBLE);
        mNoDataRl.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        getCommTitle().setTitle(getString(R.string.group_of_aides));
        setBackView();

        mPluginBean = getIntent().getParcelableExtra(KeyValue.aides.PLUGIN_BEAN);
        mGroupId = getIntent().getStringExtra(KeyValue.GROUP_ID);
        isExit = getIntent().getBooleanExtra(KeyValue.aides.PLUGIN_IS_EXIT, false);

        loadAidesData();
    }

    private void loadAidesData() {
        if (mPluginBean != null && !TextUtils.isEmpty(mPluginBean.plugin_id)) {

            mGroupAidesLl.setVisibility(View.VISIBLE);
            mNoDataRl.setVisibility(View.GONE);
            GlideUtils.loadHeader(this, mPluginBean.getLogo(), mLogoIv);
            mNameTv.setText(mPluginBean.getPlugin_name());
            mUsedDescTv.setText(mPluginBean.getDescription());
            mUpdateTimeTv.setText(getString(R.string.update_time) + DateUtil
                    .timeToString(mPluginBean.update_time * 1000));
            String companyName = mPluginBean.getCompany_name();
            if (!TextUtils.isEmpty(companyName)) {
                mAuthorTv.setText(getString(R.string.developers) + companyName);
                mDisclaimerTv.setText(getString(R.string.group_aides_disclaimer_start) + companyName
                        + getString(R.string.group_aides_disclaimer_end));
            }
        } else {
            mGroupAidesLl.setVisibility(View.GONE);
            mNoDataRl.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void initListener() {
        mUpdateAidesTv.setOnClickListener(this);
        mDeleteAidesTv.setOnClickListener(this);
        findViewById(R.id.group_aides_add_keyword_tv).setOnClickListener(this);
        findViewById(R.id.group_aides_add_aides_tv).setOnClickListener(this);

        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_aides_replace_tv:
                //换助手
                if (mPluginBean != null) {
                    NavUtils.gotoSearchGroupAidesActivity(this, mGroupId, mPluginBean.getPlugin_id());
                }

                break;
            case R.id.group_aides_delete_tv:
                //删除助手
                showDeleteAidesDialog();
                break;
            case R.id.group_aides_add_keyword_tv:
                //添加关键字

                break;
            case R.id.group_aides_add_aides_tv:
                //添加助手
                NavUtils.gotoSearchGroupAidesActivity(this, mGroupId, "");
                break;
        }
    }

    private void showDeleteAidesDialog() {
        mDeleteAidesDialog = new CommDialog(this);

        mDeleteAidesDialog.setTitleGone();
        mDeleteAidesDialog.setDesc(getString(R.string.group_aides_delete_confirm));
        mDeleteAidesDialog.setDescTextSize(16);
        mDeleteAidesDialog.setOkTxt(getString(R.string.confirm1));
        mDeleteAidesDialog.setOkTxtColor(getResources().getColor(R.color.color_ff0000));
        mDeleteAidesDialog.show();

        mDeleteAidesDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                requestDeleteAides();
            }
        });

    }

    //删除群助手
    private void requestDeleteAides() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, mGroupId);
        hashMap.put(KeyValue.aides.PLUGIN_ID, "");
        mPresenter.setGroupAidesInfo(DqUrl.url_group_aides_pligin_set, hashMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
        if (mDeleteAidesDialog != null) {
            mDeleteAidesDialog.dismiss();
            mDeleteAidesDialog = null;
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if (MsgType.MT_GROUP_UPDATE_AIDES.equals(key)) {
            mPluginBean = (PluginBean) value;
            loadAidesData();
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (entity == null) return;

        if (DqUrl.url_group_aides_pligin_set.equals(url)) {
            if (mPluginBean != null) {
                mPluginBean.plugin_id = "";
            }
            loadAidesData();
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_DELETE_AIDES, null);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (entity == null) return;
        entity.bequit();
    }


}
