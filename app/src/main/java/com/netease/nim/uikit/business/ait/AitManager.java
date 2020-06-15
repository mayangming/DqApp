package com.netease.nim.uikit.business.ait;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.RequestCode;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.Friend;
import com.netease.nim.uikit.business.ait.selector.model.ItemType;

import java.util.List;

/**
 * Created by hzchenkang on 2017/7/10.
 */

public class AitManager implements TextWatcher {

    private Context context;

    private String tid;

    private boolean robot;

    private AitContactsModel aitContactsModel;

    private int curPos;

    private boolean ignoreTextChange = false;

    private AitTextChangeListener listener;

    public AitManager(Context context, String tid, boolean robot) {
        this.context = context;
        this.tid = tid;
        this.robot = robot;
        aitContactsModel = new AitContactsModel();
    }

    public void setTextChangeListener(AitTextChangeListener listener) {
        this.listener = listener;
    }

    public List<String> getAitTeamMember() {
        return aitContactsModel.getAitTeamMember();
    }

    public void reset() {
        aitContactsModel.reset();
        ignoreTextChange = false;
        curPos = 0;
    }

    /**
     * ------------------------------ 增加@成员 --------------------------------------
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.AIT_TEAM && resultCode == Activity.RESULT_OK) {
            Friend friend = data.getParcelableExtra(KeyValue.Ait.ENTITY);
            insertAitMemberInner(friend.uid, friend.getAitName(), ItemType.TEAM_MEMBER, curPos, false);
        }
    }

    public void insertAitMemberInner(Friend friend) {
        if (friend == null) {
            return;
        }
        insertAitMemberInner(friend.uid, friend.getAitName(), ItemType.TEAM_MEMBER, curPos, false);
    }

    public void insertAitMemberInner(GroupMemberBean memberInfo) {
        if (memberInfo == null) {
            return;
        }
        insertAitMemberInner(memberInfo.uid, memberInfo.getGroupDisplayName(), ItemType.TEAM_MEMBER, curPos, true);
    }

    private void insertAitMemberInner(String account, String name, int type, int start, boolean needInsertAitInText) {
        name = name + " ";
        String content = needInsertAitInText ? "@" + name : name;
        if (listener != null) {
            // 关闭监听
            ignoreTextChange = true;
            // update 文本到editText
            listener.onTextAdd(content, start, content.length());
            // 开启监听
            ignoreTextChange = false;
        }

        // update 已有的 aitBlock
        aitContactsModel.onInsertText(start, content);

        int index = needInsertAitInText ? start : start - 1;
        // 添加当前到 aitBlock
        aitContactsModel.addAitMember(account, name, type, index);
    }

    /**
     * 添加@信息
     * @param friend
     */
    public void addAitMemberInner(Friend friend) {
        addAitMemberInner(friend.uid, friend.getName(), ItemType.TEAM_MEMBER, curPos, false);
    }

    private void addAitMemberInner(String account, String name, int type, int start, boolean needInsertAitInText) {
        name = name + " ";
        int index = needInsertAitInText ? start : start - 1;
        // 添加当前到 aitBlock
        aitContactsModel.addAitMember(account, name, type, index);
    }

    /**
     * ------------------------------ editText 监听 --------------------------------------
     */
    // 当删除尾部空格时，删除一整个segment,包含界面上也删除
    private boolean deleteSegment(int start, int count) {
        if (count != 1) {
            return false;
        }
        boolean result = false;
        AitBlock.AitSegment segment = aitContactsModel.findAitSegmentByEndPos(start);
        if (segment != null) {
            int length = start - segment.start;
            if (listener != null) {
                ignoreTextChange = true;
                listener.onTextDelete(segment.start, length);
                ignoreTextChange = false;
            }
            aitContactsModel.onDeleteText(start, length);
            result = true;
        }
        return result;
    }

    /**
     * @param editable 变化后的Editable
     * @param start    text 变化区块的起始index
     * @param count    text 变化区块的大小
     * @param delete   是否是删除
     */
    private void afterTextChanged(Editable editable, int start, int count, boolean delete) {
        curPos = delete ? start : count + start;
        if (ignoreTextChange) {
            return;
        }
        if (delete) {
            int before = start + count;
            if (deleteSegment(before, count)) {
                return;
            }
            aitContactsModel.onDeleteText(before, count);

        } else {
            if (count <= 0 || editable.length() < start + count) {
                return;
            }
            CharSequence s = editable.subSequence(start, start + count);
            if (s == null) {
                return;
            }
            if (s.toString().equals("@")) {
                // 启动@联系人界面
                if (!TextUtils.isEmpty(tid)) {
//                    AitContactSelectorActivity.start(context, tid, robot);
                    NavUtils.gotoAitActivity(context, tid);
                }
            }
            aitContactsModel.onInsertText(start, s.toString());
        }
    }

    private int editTextStart;
    private int editTextCount;
    private int editTextBefore;
    private boolean delete;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        delete = count > after;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.editTextStart = start;
        this.editTextCount = count;
        this.editTextBefore = before;
    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextChanged(s, editTextStart, delete ? editTextBefore : editTextCount, delete);
    }
}
