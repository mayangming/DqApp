package com.wd.daquan.chat.group.adapter;

import android.content.Context;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.adapter.CommAdapter;
import com.da.library.holder.CommHolder;
import com.da.library.tools.AESHelper;
import com.da.library.tools.DateUtil;
import com.dq.im.model.ImMessageBaseModel;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.wd.daquan.R;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.db.helper.TeamDbHelper;

/**
 * Created by Kind on 2018/9/25.
 */

public class GroupSearchChatAdapter extends CommAdapter<ImMessageBaseModel, GroupSearchChatAdapter.GroupSearchChatHolder> {

    private String input;
    private OnConversationsItemClickListener mOnConversationsItemClickListener;
    private Context mContext;
    public GroupSearchChatAdapter(Context context){
        super(context);
        this.mContext = context;
    }
    public void setData(String input){
        this.input = input;
    }

    @Override
    public GroupSearchChatHolder onCreateViewHolder(View convertView, ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_search_chat_item, parent, false);
        return new GroupSearchChatHolder(view);
    }


    @Override
    public void onBindViewHolder(GroupSearchChatHolder groupSearchChatHolder, int position) {
        if (mList == null || null == groupSearchChatHolder) {
            return;
        }
        ImMessageBaseModel imMessage = getItem(position);

        GroupInfoBean groupInfo = TeamDbHelper.getInstance().getTeam(imMessage.getMsgIdServer());
        String group_name = imMessage.getMsgIdServer();
        String group_pic = "";
        if(groupInfo != null){
            group_name = groupInfo.group_name;
            group_pic = groupInfo.group_pic;
        }

        groupSearchChatHolder.nickName.setText(group_name);

        MoonUtil.identifyFaceExpressionAndTags(mContext, groupSearchChatHolder.content, AESHelper.decryptString(imMessage.getSourceContent()), ImageSpan.ALIGN_BOTTOM, 0.45f);


//        SpannableString changemsgContent = SpannableStringUtils.addTextColor(imMessage.getContent(), imMessage.getContent().indexOf(input),
//                input.length() + imMessage.getContent().indexOf(input), mContext.getResources().getColor(R.color.titlecolor));
//        groupSearchChatHolder.content.setText(changemsgContent);
        groupSearchChatHolder.time.setText(DateUtil.timeToString(imMessage.getCreateTime()));

        GlideUtils.loadHeader(mContext, group_pic, groupSearchChatHolder.mAvatar);



//        UIMessage uiMessage = (UIMessage) allList.get(position);
//        groupSearchChatHolder.nickName.setText(uiMessage.getUserInfo().getName());
//        TextMessage txtMsg = (TextMessage) uiMessage.getContent();
//        String msgContent = EasyAES.decryptString(txtMsg.getContent());
//        SpannableString changemsgContent = SpannableStringUtils.addTextColor(msgContent, msgContent.indexOf(input),
//                input.length() + msgContent.indexOf(input), mContext.getResources().getColor(R.color.titlecolor));
//        groupSearchChatHolder.content.setText(changemsgContent);
//        groupSearchChatHolder.time.setText(DateUtil.timeToString(uiMessage.getSentTime()));
//        BaseImgView.ImageLoaderLoadimg(groupSearchChatHolder.mAvatar, uiMessage.getUserInfo().getPortraitUri().toString(),
//                R.mipmap.user_avatar, R.mipmap.user_avatar, R.mipmap.user_avatar, 60, 60, 4);
        groupSearchChatHolder.itemGroupSearchChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnConversationsItemClickListener.onConversationsItemClick(imMessage);
            }
        });
    }

    public class GroupSearchChatHolder extends CommHolder{
        public RelativeLayout itemGroupSearchChatLayout;
        public ImageView mAvatar;
        public TextView nickName;
        public TextView content;
        public TextView time;

        public GroupSearchChatHolder(View view) {
            super(view);
            itemGroupSearchChatLayout = view.findViewById(R.id.itemGroupSearchChatLayout);
            mAvatar = view.findViewById(R.id.itemGroupSearchChatAvatar);
            nickName = view.findViewById(R.id.itemGroupSearchChatNickName);
            content = view.findViewById(R.id.itemGroupSearchChatContent);
            time = view.findViewById(R.id.itemGroupSearchChatTime);
        }
    }

    public interface OnConversationsItemClickListener{
        void onConversationsItemClick(ImMessageBaseModel uiMessage);
    }
    public void setOnConversationsItemClickListener(OnConversationsItemClickListener mOnConversationsItemClickListener){
        this.mOnConversationsItemClickListener = mOnConversationsItemClickListener;
    }
}
