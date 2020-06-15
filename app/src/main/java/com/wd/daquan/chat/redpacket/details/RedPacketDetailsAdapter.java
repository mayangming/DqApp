package com.wd.daquan.chat.redpacket.details;

import android.content.Context;
import android.view.ViewGroup;
import com.wd.daquan.R;
import com.wd.daquan.model.bean.Friend;
import com.da.library.controls.recyclerholder.ExBaseQuickAdapter;
import java.util.List;

/**
 * Created by Kind on 2019-05-21.
 */
public class RedPacketDetailsAdapter extends ExBaseQuickAdapter<Friend, RedPacketDetailsHolder> {
    public RedPacketDetailsAdapter(Context context, List<Friend> data) {
        super(context, data);
    }

    @Override
    protected RedPacketDetailsHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new RedPacketDetailsHolder(parent.getContext(), inflate(parent, R.layout.redpacket_details_item));
    }


    @Override
    protected void convert(RedPacketDetailsHolder helper, Friend item) {
        helper.onBindData(item);
    }
}
