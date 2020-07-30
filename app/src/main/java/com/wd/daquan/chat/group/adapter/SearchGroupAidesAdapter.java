package com.wd.daquan.chat.group.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.chat.group.adapter.holder.SearchGroupAidesHolder;
import com.wd.daquan.chat.group.bean.PluginBean;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2019/1/7 11:14
 * @Description:
 */
public class SearchGroupAidesAdapter extends CommRecyclerViewAdapter<PluginBean, SearchGroupAidesHolder> {

    private String mPluginId;

    @Override
    protected SearchGroupAidesHolder onBindView(@NonNull ViewGroup parent, int viewType) {
        return new SearchGroupAidesHolder(mInflater.inflate(R.layout.item_group_aides_list_adapter, parent, false));
    }

    @Override
    protected void onBindData(@NotNull @NonNull SearchGroupAidesHolder holder, int position) {
        PluginBean item = getItem(position);
        if(item == null) return;

        GlideUtils.loadHeader(mContext, item.logo, holder.portrait);
        holder.name.setText(item.getPlugin_name());
        holder.desc.setText(item.getDescription());

        if(item.getPlugin_id().equals(mPluginId)) {
            holder.add.setText(mContext.getResources().getString(R.string.added));
            holder.add.setTextColor(mContext.getResources().getColor(R.color.app_txt_999999));
            holder.add.setBackgroundColor(Color.WHITE);
            holder.add.setClickable(false);
        }else {
            holder.add.setText(mContext.getResources().getString(R.string.add));
            holder.add.setTextColor(mContext.getResources().getColor(R.color.text_blue_pressed));
            holder.add.setBackgroundResource(R.drawable.cn_web_login_btn_bg_selecter);
            holder.add.setClickable(true);
        }

        if(holder.add.isClickable()) {
            holder.add.setOnClickListener(v -> {
                if(mAidesAdapterListener != null) {
                    mAidesAdapterListener.onAddClick(item);
                }
            });
        }else {
            holder.add.setOnClickListener(v -> {
                //消费点击事件，不处理
            });
        }

        holder.itemView.setOnClickListener(v -> {
            if(mAidesAdapterListener != null) {
                mAidesAdapterListener.onItemClick(item);
            }
        });
    }

    public void setPluginId(String pluginId) {
        this.mPluginId = pluginId;
    }

    public interface ISearchGroupAidesAdapterListener {
        void onItemClick(PluginBean pluginBean);
        void onAddClick(PluginBean pluginBean);
    }

    private ISearchGroupAidesAdapterListener mAidesAdapterListener;

    public void setAidesAdapterListener(ISearchGroupAidesAdapterListener aidesAdapterListener) {
        this.mAidesAdapterListener = aidesAdapterListener;
    }
}
