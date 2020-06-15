package com.wd.daquan.chat.group.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wd.daquan.R;
import com.da.library.adapter.CommBaseSelectAdapter;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.chat.group.adapter.holder.LongTimeHolder;
import com.wd.daquan.chat.group.bean.UnreceivedEntity;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.third.helper.TeamHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2019/1/20 15:47.
 * @description:
 * 长时间未领取红包
 */
public class GroupLongTimeAdapter extends CommBaseSelectAdapter<UnreceivedEntity.PacketEntity, LongTimeHolder> {

    private List<String> mUnreceiveList = new ArrayList<>();

    @Override
    protected LongTimeHolder onBindView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_longtime_rp_item, null);
        return new LongTimeHolder(view);
    }

    @Override
    protected void onBindData(@NotNull LongTimeHolder holder, int position) {
        UnreceivedEntity.PacketEntity packetEntity = allList.get(position);
        if (null == packetEntity) {
            return;
        }

        Gson gson = new Gson();
        String redPacketExtra = "";
        if (null != packetEntity.redpacket_extra && packetEntity.redpacket_extra.size() > 0) {
            redPacketExtra = gson.toJson(packetEntity.redpacket_extra);
        }
        GlideUtils.loadHeader(holder.mHead.getContext(), packetEntity.headpic, holder.mHead);

        String userName = TeamHelper.getDisplayNameWithoutMe(packetEntity.group_id, packetEntity.create_uid);
        holder.mName.setText(userName);
        holder.mTime.setText(packetEntity.create_time);
        holder.mGreeting.setText(packetEntity.greetings);
        // 支付宝红包字样和icon,目前不设置，
        holder.mRepType.setText("支付宝红包");

        String uid = ModuleMgr.getCenterMgr().getUID();
//        if (!TextUtils.isEmpty(redPacketExtra) && !"[]".equals(redPacketExtra)) {// 私包
//            if (redPacketExtra.contains(uid)) {
//                if (unReceive(packetEntity.redpacket_id) || packetEntity.isReceive()) {
//                    holder.mRpRlyt.setBackgroundResource(R.mipmap.qc_received_private_left);
//                } else {
//                    holder.mRpRlyt.setBackgroundResource(R.mipmap.qc_unreceived_private_left);
//                }
//            } else {
//                holder.mRpRlyt.setBackgroundResource(R.mipmap.qc_received_private_left);
//            }
//        } else { // 普通红包
//            if (unReceive(packetEntity.redpacket_id) || packetEntity.isReceive()) {
//                holder.mRpRlyt.setBackgroundResource(R.mipmap.qc_received_left);
//            } else {
//                holder.mRpRlyt.setBackgroundResource(R.mipmap.qc_unreceived_left);
//            }
//        }

        holder.mRpRlyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLongTimeListener != null) {
                    mOnLongTimeListener.clickLongTimeRp(packetEntity);
                }
            }
        });
    }

    public void addUnreceive(String rpId) {
        if (TextUtils.isEmpty(rpId)) {
            return;
        }

        if (!mUnreceiveList.contains(rpId)) {
            mUnreceiveList.add(rpId);
        }
    }

    private boolean unReceive(String rpId) {
        return mUnreceiveList.contains(rpId);
    }

    private OnLongTimeListener mOnLongTimeListener = null;

    public interface OnLongTimeListener {
        void clickLongTimeRp(UnreceivedEntity.PacketEntity packetEntity);
    }

    public void setOnLongTimeListener(OnLongTimeListener onLongTimeListener) {
        mOnLongTimeListener = onLongTimeListener;
    }
}
