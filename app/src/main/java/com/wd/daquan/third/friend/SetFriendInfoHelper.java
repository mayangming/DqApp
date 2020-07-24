package com.wd.daquan.third.friend;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;

import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置好友信息
 */
public class SetFriendInfoHelper implements Presenter.IView<DataBean> {
    private boolean isShowDialog = false;

    private Activity mActivity;
    private CommDialog mCommDialog;
    private String mUserId;
    private FriendPresenter mPresenter;

    public SetFriendInfoHelper(Activity activity) {
        mActivity = activity;
        mPresenter = new FriendPresenter();
        mPresenter.attachView(this);
    }

    public void init(Activity activity){
        mActivity = activity;
        mPresenter = new FriendPresenter();
        mPresenter.attachView(this);
    }


    public void addFriend(String uid) {
        mUserId = uid;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.TO_UID, uid);
        hashMap.put("source", "1");
        hashMap.put("type", "0");
        mPresenter.addFriend(DqUrl.url_friend_request, hashMap);
    }

    private void addFriendForOk() {
        if (mCommDialog == null) {
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.TO_UID, mUserId);
        hashMap.put("source", "1");
        hashMap.put("to_say", mCommDialog.getSearchEt().getText().toString());
        mPresenter.addFriend(DqUrl.url_friend_request, hashMap);
    }

    /**
     * 添加黑名单功能
     */
    private void addBlack(String uid) {
        if (mCommDialog == null) {
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.TO_UID, uid);
        mPresenter.addFriend(DqUrl.url_add_black, hashMap);
    }

    public void showAddBlackDialog(String uid) {
        mUserId = uid;

        if(mActivity == null) {
            return;
        }
        mCommDialog = new CommDialog(mActivity);
        mCommDialog.setTitleGone();
        mCommDialog.setDesc(DqApp.getStringById(R.string.blacklist_dialog_content_end));
        mCommDialog.setOkTxt(DqApp.getStringById(R.string.determine));
        mCommDialog.show();

        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                addBlack(uid);
            }
        });


    }

    public void removeBlack(String uid) {
        mUserId = uid;
        DqToast.showShort("移除黑名单失败,没有云信功能");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String tipMessage) {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (entity == null) return;
        if (DqUrl.url_friend_request.equals(url)) {
            if (KeyValue.Code.ADD_FRIEND_ERR == code) {
                isShowDialog = true;
                showAddFriend();
            } else {
                DqToast.showShort(entity.content);
            }
        } else {
            DqUtils.bequit(entity, mActivity);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (mActivity == null) {
            return;
        }
        if (DqUrl.url_friend_request.equals(url)) {
            //发送好友申请
            if (isShowDialog) {
                DqToast.showShort(DqApp.sContext.getResources().getString(R.string.add_friend_re_txt));
                if (mFriendListener != null) {
                    mFriendListener.onAddFriend(false);
                }
            } else {
                DqToast.showShort(DqApp.sContext.getResources().getString(R.string.add_successful));
                Friend friend = (Friend) entity.data;
//                removeBlack(mUserId);好友添加成功
                if (null != friend) {
                    friend.whether_friend = "0";
                    //入库
                    FriendDbHelper.getInstance().update(friend, null);
                    MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_ADD_FRIEND, "");
                }
            }
        } else if (DqUrl.url_remove_blacklist.equals(url)) {
            //移除黑名单
            DqToast.showShort("移除黑名单成功");
            FriendDbHelper.getInstance().getFriend(mUserId, friend -> {
                friend.whether_black = "1";
                FriendDbHelper.getInstance().update(friend, null);
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_REMOVE_BLACK_LIST, mUserId);
            });

        } else if (DqUrl.url_add_black.equals(url)) {
            //加入黑名单
            DqToast.showShort("加入黑名单成功");

            FriendDbHelper.getInstance().getFriend(mUserId, friend -> {
                friend.whether_black = "0";
                FriendDbHelper.getInstance().update(friend, null);
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_ADD_BLACK_LIST, mUserId);
                //以下删除的代码主要是出现在拉黑功能不完善的时候，需要把聊天的数据删掉
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_REMOVE_FRIEND, mUserId);
            });
        } else if (DqUrl.url_delete_friend.equals(url)) {
            //删除好友
//            DqToast.showShort(DqApp.getStringById(R.string.delete_success));
//            FriendDbHelper.getInstance().delete(mUserId);
//            MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_REMOVE_FRIEND, mUserId);
            FriendDbHelper.getInstance().getFriend(mUserId, friend -> {
                friend.whether_friend = "0";
                FriendDbHelper.getInstance().update(friend, null);
                //以下删除的代码主要是出现在拉黑功能不完善的时候，需要把聊天的数据删掉
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_REMOVE_FRIEND, mUserId);
            });
        }
    }

    private void showAddFriend() {
        if (mActivity == null) {
            return;
        }
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        mCommDialog = new CommDialog(mActivity);
        mCommDialog.setSearchLlVisibility();
        mCommDialog.setSearchEt("我是" + nickName);
        mCommDialog.setOkTxt(DqApp.getStringById(R.string.determine));
        mCommDialog.setOkTxtColor(mActivity.getResources().getColor(R.color.text_blue));
        mCommDialog.show();
        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {
                isShowDialog = false;
            }

            @Override
            public void onOk() {
                addFriendForOk();
                isShowDialog = false;
            }
        });
    }

    /**
     * 删除好友
     */
    public void deleteFriend(String uid, String userName) {
        mUserId = uid;
        if (mActivity == null) {
            return;
        }
        String desc = String.format(DqApp.getStringById(R.string.remove_friend_dialog_desc), userName);
        SpannableString spannableString = SpannableStringUtils.addTextColor(desc, 4,
                userName.length() + 4, Color.RED);
        mCommDialog = new CommDialog(mActivity);
        mCommDialog.setTitleGone();
        mCommDialog.setDescSpannableString(spannableString);
        mCommDialog.setOkTxt(DqApp.getStringById(R.string.delete));
        mCommDialog.setOkTxtColor(Color.RED);
        mCommDialog.show();
        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                delFriend(DqUrl.url_delete_friend);
            }
        });
    }

    private void delFriend(String url) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.TO_UID, mUserId);
        mPresenter.delFriend(url, hashMap);
    }

    private FriendListener mFriendListener;

    public void setFriendListener(FriendListener friendListener) {
        mFriendListener = friendListener;
    }

    public interface FriendListener {
        void onAddFriend(boolean success);
    }

    public void detach(){
        if(mPresenter != null) {
            mPresenter.detachView();
        }
        if(mCommDialog != null && mCommDialog.isShowing()) {
            mCommDialog.dismiss();
            mCommDialog = null;
        }
    }
}
