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
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
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
public class GroupAidesDetailsActivity extends DqBaseActivity<ChatPresenter, DataBean> implements View.OnClickListener {

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
    //是否是已经添加的
    private boolean isExit;
    private CommDialog mAddAidesDialog;

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

        mGroupAidesLl.setVisibility(View.VISIBLE);
        mNoDataRl = findViewById(R.id.no_data_rl);
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
        if (isExit) {
            mUpdateAidesTv.setText(getString(R.string.added));
            mUpdateAidesTv.setClickable(false);
        } else {
            mUpdateAidesTv.setText(getString(R.string.add));
            mUpdateAidesTv.setClickable(true);
        }
        mDeleteAidesTv.setVisibility(View.GONE);

        if (mPluginBean != null) {
            mGroupAidesLl.setVisibility(View.VISIBLE);
            mNoDataRl.setVisibility(View.GONE);
            GlideUtils.loadHeader(this, mPluginBean.getLogo(), mLogoIv);
            mNameTv.setText(mPluginBean.getPlugin_name());
            mUsedDescTv.setText(mPluginBean.getDescription());
            mUpdateTimeTv.setText(getString(R.string.update_time) + DateUtil.timeToString(mPluginBean.update_time * 1000));
            String companyName = mPluginBean.getCompany_name();
            if (!TextUtils.isEmpty(companyName)) {
                mAuthorTv.setText(getString(R.string.developers) + companyName);
                mDisclaimerTv.setText(getString(R.string.group_aides_disclaimer_start) + companyName
                        + getString(R.string.group_aides_disclaimer_end));
            }
        }
    }


    @Override
    protected void initListener() {
        mUpdateAidesTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_aides_replace_tv:
                //换助手
                if (mPluginBean != null) {
                    showAddAidesDialog(mPluginBean);

                }
                break;
        }
    }

    private void showAddAidesDialog(PluginBean pluginBean) {
        mAddAidesDialog = new CommDialog(this);

        mAddAidesDialog.setTitleGone();
        mAddAidesDialog.setDesc("是否确认将" + pluginBean.getPlugin_name() + "设置为群助手？");
        mAddAidesDialog.setDescTextSize(16);
        mAddAidesDialog.setOkTxt(getString(R.string.confirm1));
        mAddAidesDialog.setOkTxtColor(getResources().getColor(R.color.app_theme_color));
        mAddAidesDialog.show();

        mAddAidesDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                requestDeleteAides(pluginBean.getPlugin_id());
            }
        });

    }

    //添加群助手
    private void requestDeleteAides(String pluginId) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, mGroupId);
        hashMap.put(KeyValue.aides.PLUGIN_ID, pluginId);
        mPresenter.setGroupAidesInfo(DqUrl.url_group_aides_pligin_set, hashMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAddAidesDialog != null) {
            mAddAidesDialog.dismiss();
            mAddAidesDialog = null;
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (entity == null) return;

        if (DqUrl.url_group_aides_pligin_set.equals(url)) {

            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_UPDATE_AIDES, mPluginBean);
            finish();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (entity == null) return;
        entity.bequit();
    }
}