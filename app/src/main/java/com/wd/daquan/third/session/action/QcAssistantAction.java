package com.wd.daquan.third.session.action;

import android.app.Activity;
import android.text.TextUtils;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.bean.GroupAssistAuth;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.da.library.listener.DialogListener;
import com.wd.daquan.net.callback.ObjectCallback;
import com.da.library.tools.Utils;
import com.da.library.view.CommDialog;
import com.netease.nim.uikit.business.session.actions.BaseAction;

import okhttp3.Call;

/**
 * 群助手
 *
 * @author: dukangkang
 * @date: 2019/2/20 11:14.
 * @description: todo ...
 */
public class QcAssistantAction extends BaseAction{

    private GroupInfoBean groupInfo;
    private ChatPresenter chatPresenter;

    public QcAssistantAction() {
        super(R.drawable.chat_expansion_assistant_selector, R.string.input_panel_assistant);
    }

    @Override
    public void onClick() {
        if (Utils.isFastDoubleClick(800)) {
            DqToast.showShort("处理中...");
            return;
        }

        String targetId = getAccount();
        Activity activity = getActivity();

        groupInfo = TeamDbHelper.getInstance().getTeam(targetId);
        if (groupInfo == null) {
            DqToast.showShort("参数错误!");
            return;
        }
        setGroupInfo(groupInfo);
        if (TextUtils.isEmpty(groupInfo.plugin_id)) {
            MsgMgr.getInstance().sendMsg(MsgType.MT_CONVERSATION_GROUP_ASSIST, null);
        } else {
            if ("true".equals(groupInfo.plugin_auth)) {
                reqestAuth();
            } else {
                CommDialog commDialog = DialogUtils.showAuthDialog(activity,
                        activity.getString(R.string.conversation_group_assist_auth_title),
                        (activity.getString(R.string.conversation_group_assist_auth_desc)
                                + "<br/>" + activity.getString(R.string.conversation_group_assist_auth_desc_t)),
                        activity.getString(R.string.confirm));
                commDialog.show();
                commDialog.setDialogListener(new DialogListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onOk() {
                        reqestAuth();
                    }
                });
            }
        }

    }

    private void reqestAuth() {
        if(groupInfo != null){
            if(chatPresenter == null){
                chatPresenter = new ChatPresenter();
            }
            chatPresenter.groupAuth(getAccount(), groupInfo.plugin_id, new ObjectCallback<DataBean<GroupAssistAuth>>() {

                public void onSuccess(Call call, String url, int code, DataBean<GroupAssistAuth> entity) {
                    if (0 == code) {
                        GroupAssistAuth groupAssistAuth = entity.data;
                        if (groupAssistAuth == null) {
                            DqToast.showShort("参数错误!");
                            return;
                        }
                        NavUtils.gotoWebviewActivity(getActivity(), groupInfo.plugin_h5_url, "", groupAssistAuth);
                    }
                }

                @Override
                public void onFailed(Call call, String url, int code, DataBean<GroupAssistAuth> entity, Exception e) {
                    if (entity != null) {
                        entity.bequit();
                    }
                }

            });
        }
    }

    public void setGroupInfo(GroupInfoBean groupInfo) {
        this.groupInfo = groupInfo;
    }
}
