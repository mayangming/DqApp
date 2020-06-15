package com.wd.daquan.mine.collection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;


public class CollectionTxtViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout layout_txt;
    public ReadMoreTextView txtTxtContent;
    public TextView txtTxtName;
    public TextView txtTxtTime;
    public TextView txtTxtUnfold;

    public CollectionTxtViewHolder(View view) {
        super(view);
        layout_txt = view.findViewById(R.id.collectionItemTxtLayout);
        txtTxtContent = view.findViewById(R.id.collectionItemTxtContent);
        txtTxtName = view.findViewById(R.id.collectionItemTxtName);
        txtTxtTime = view.findViewById(R.id.collectionItemTxtTime);
        txtTxtUnfold = view.findViewById(R.id.collectionItemTxtUnfold);
    }
}
