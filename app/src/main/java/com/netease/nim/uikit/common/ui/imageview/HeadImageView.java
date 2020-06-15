package com.netease.nim.uikit.common.ui.imageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.api.NimUIKit;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.third.helper.TeamHelper;
import com.wd.daquan.third.helper.UserInfoHelper;

/**
 * Created by huangjun on 2015/11/13.
 */
//public class HeadImageView extends CircleImageView {
@SuppressLint("AppCompatCustomView")
public class HeadImageView extends ImageView {

    public static final int DEFAULT_AVATAR_THUMB_SIZE = (int) NimUIKit.getContext().getResources().getDimension(R.dimen.avatar_max_size);
    public static final int DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE = (int) NimUIKit.getContext().getResources().getDimension(R.dimen.avatar_notification_size);
    private static final int DEFAULT_AVATAR_RES_ID = R.drawable.user_avatar;

    public HeadImageView(Context context) {
        super(context);
    }

    public HeadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 加载单人用户头像
     */
    public void loadUserAvatar(String account) {
        String headPic = UserInfoHelper.getHeadPic(account);
        doLoadImage(headPic, DEFAULT_AVATAR_RES_ID, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * 加载群组内成员头像
     */
    public void loadTeamMemberAvatar(String sessionId, String account) {
        String headPic = TeamHelper.getTeamMemberHeadPic(sessionId, account);
        doLoadImage(headPic, DEFAULT_AVATAR_RES_ID, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * 加载群组头像
     */
    public void loadTeamAvatar(String account) {
        String headPic = TeamHelper.getTeamHeadPic(account);
        doLoadImage(headPic, R.drawable.team_avatar, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param message 消息
     */
    public void loadAvatar(ImMessageBaseModel message) {
        if (ImType.typeOfValue(message.getType()) == ImType.P2P) {
            String account = message.getFromUserId();
            loadUserAvatar(account);
        } else {
            loadTeamMemberAvatar(message.getMsgIdServer(), message.getFromUserId());
        }
    }

    /**
     * ImageLoader异步加载
     */
    private void doLoadImage(final String url, final int defaultResId, final int thumbSize) {
        /*
         * 若使用网易云信云存储，这里可以设置下载图片的压缩尺寸，生成下载URL
         * 如果图片来源是非网易云信云存储，请不要使用NosThumbImageUtil
         */
//        RequestOptions requestOptions = new RequestOptions()
//                .centerCrop()
//                .placeholder(defaultResId)
//                .error(defaultResId)
//                .override(thumbSize, thumbSize)
//                .transforms(new CenterCrop(), new R);
//        Glide.with(getContext().getApplicationContext()).asBitmap()
//                .load(thumbUrl)
//                .apply(requestOptions)
//                .into(this);
        GlideUtils.loadHeader(DqApp.sContext, url, this);
    }

    /**
     * 解决ViewHolder复用问题
     */
    public void resetImageView() {
        setImageBitmap(null);
    }
}
