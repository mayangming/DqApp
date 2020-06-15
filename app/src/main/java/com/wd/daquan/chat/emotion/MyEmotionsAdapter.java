package com.wd.daquan.chat.emotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

//import com.cn.glide.GlideUtils;

public class MyEmotionsAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyEmotionEntity> lists = new ArrayList<>();
    private AddEmotionListener mAddEmotionListener;
    private int type;
    public List<MyEmotionEntity> listsId = new ArrayList<>();
    private AddIdListener mAddIdListener;
    public MyEmotionsAdapter(Context context, List<MyEmotionEntity> lists, int type) {
        this.mContext = context;
        this.lists = lists;
        this.type = type;
    }
    @Override
    public int getCount() {
        if (lists != null) return lists.size() + 1;
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (lists == null) {
            return null;
        }else if (position >= lists.size()) {
            return null;
        }else{
            return lists.get(position - 1);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_emotions, null);
        RelativeLayout layout = convertView.findViewById(R.id.itemEmotionsLayout);
        ImageView mImageView = convertView.findViewById(R.id.itemEmotionsImg);
        CheckBox mCheckBox = convertView.findViewById(R.id.itemEmotionsCheckbox);
        try {
            if(isShowAddItem(position)){
                mCheckBox.setVisibility(View.GONE);
                mImageView.setImageResource(R.mipmap.comm_add_btn);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == (getCount()-1)) {
                            mAddEmotionListener.addEmotion();
                        }
                    }
                });
            }else {
                if(type == 1) {
                    mCheckBox.setVisibility(View.GONE);
                }else{
                    mCheckBox.setVisibility(View.VISIBLE);
                }
                final MyEmotionEntity mContent = lists.get(position);
                GlideUtils.loadGif(DqApp.sContext, mContent.remote_path, mImageView, 4);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listsId.contains(mContent)) {
                            listsId.remove(mContent);
                            mCheckBox.setChecked(false);
                        }else{
                            mCheckBox.setChecked(true);
                            listsId.add(mContent);
                        }
                    }
                });
                mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listsId.contains(mContent)) {
                            listsId.remove(mContent);
                        }else{
                            listsId.add(mContent);
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    private boolean isShowAddItem(int position)
    {
        int size = lists == null ? 0 : lists.size();
        return position == size;
    }

    public void setData(List<MyEmotionEntity> lists, int type){
        this.lists = lists;
        this.type = type;
        notifyDataSetChanged();
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setAddEmotionListener(AddEmotionListener mAddEmotionListener){
        this.mAddEmotionListener = mAddEmotionListener;
    }
    public interface AddEmotionListener{
        void addEmotion();
    }

    public void setAddIdListener(AddIdListener mAddEmotionListener){
        this.mAddIdListener = mAddIdListener;
    }
    public interface AddIdListener{
        void addID(List<Integer> listsId);
    }

    public void clearList(){
        listsId = new ArrayList<>();
    }
}
