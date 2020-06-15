package com.wd.daquan.discover;

import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.common.constant.KeyValue;

import org.jetbrains.annotations.NotNull;

public class DiscoverAdapter extends CommRecyclerViewAdapter<DiscoverMenuEntity, DiscoverViewHolder> {

    private DiscoverOnItemListener mCNDiscoverOnItemListener;

    @Override
    protected DiscoverViewHolder onBindView(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.qc_discover_item, parent, false);
        return new DiscoverViewHolder(convertView);
    }

    @Override
    protected void onBindData(@NotNull DiscoverViewHolder holder, int position) {
        DiscoverMenuEntity discoverMenuBean = allList.get(position);

        if (null == discoverMenuBean) return;
        holder.txt.setText(discoverMenuBean.title);
        if (KeyValue.ONE_STRING.equals(discoverMenuBean.id)) {//视频
            holder.img.setImageResource(R.mipmap.qc_icon_discover_bd);
        } else if (KeyValue.TWO_STRING.equals(discoverMenuBean.id)) {//小游戏
            holder.img.setImageResource(R.mipmap.qc_icon_discover_game);
        } else {
            holder.img.setImageResource(R.mipmap.ic_launcher);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCNDiscoverOnItemListener.onDiscoverItemClick(discoverMenuBean);
            }
        });
    }

    public void setDiscoverOnItemListener(DiscoverOnItemListener mCNDiscoverOnItemListener) {
        this.mCNDiscoverOnItemListener = mCNDiscoverOnItemListener;
    }

    public interface DiscoverOnItemListener {
        void onDiscoverItemClick(DiscoverMenuEntity discoverMenuBean);
    }

}
