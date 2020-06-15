package com.da.library.controls.custombuttom;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.da.library.controls.custombuttom.bean.CustomButtom;
import com.da.library.view.base.BaseDialogFragment;
import com.da.library.R;

import java.util.List;

/**
 * 底部弹出框
 * Created by Kind on 2019/3/21.
 */
public class CustomButtomDialog extends BaseDialogFragment implements
        AdapterView.OnItemClickListener, View.OnClickListener {

    private boolean externalControl = false;
    private int gravity = -1;

    private List<CustomButtom> datas;
    private boolean isShow = true;
    private View view = null;

    private ListView dialog_listview;
    private OnItemListener itemListener;

    public CustomButtomDialog() {
        settWindowAnimations(R.style.AnimDownInDownOut);
        setGravity(Gravity.BOTTOM);
        setDialogWidthSizeRatio(-1);
        setDimAmount(0.0f);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.custom_buttom_dialog);
        oninitView();
        return getContentView();
    }

    /**
     * 是否外部控制关闭弹出框
     * @param externalControl
     */
    public void setExternalControl(boolean externalControl){
        this.externalControl = externalControl;
    }

    public void setAdapterGravity(int gravity){
        this.gravity = gravity;
    }

    //isShow 是否显示取消按钮
    public void setData(List<CustomButtom> datas, boolean isShow) {
        setData(datas, isShow, null);
    }

    public void setData(List<CustomButtom> datas, boolean isShow, View view) {
        this.datas = datas;
        this.isShow = isShow;
        this.view = view;
    }

    private void oninitView() {
        if (view != null) {
            LinearLayout listview_head = (LinearLayout) findViewById(R.id.buttom_dialog_listview_head);
            listview_head.addView(view);
            listview_head.setVisibility(View.VISIBLE);
        }

        if (datas == null || datas.size() < 1) {
            dismiss();
            return;
        }

        dialog_listview = (ListView) findViewById(R.id.buttom_dialog_listview);
        if (isShow) {
            View viewFooter = getLayoutInflater().inflate(R.layout.custom_buttom_dialog_footer, null);
            dialog_listview.addFooterView(viewFooter);
            viewFooter.findViewById(R.id.custom_buttom_cancel).setOnClickListener(this);
        }
        CustomButtomDialogAdapter adapter = new CustomButtomDialogAdapter(getContext(), datas);
        adapter.setGravity(gravity);
        dialog_listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog_listview.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
            dismiss();
        if (itemListener != null) {
            itemListener.onDialogClick(v);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!externalControl){
            dismiss();
        }
        if (itemListener != null) {
            itemListener.onDialogItemClick(parent, view, position, id);
        }
    }

    public void setOnItemListener(OnItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public interface OnItemListener {
        void onDialogItemClick(AdapterView<?> parent, View view, int position, long id);

        void onDialogClick(View v);
    }

}
