package com.da.library.view.pickerios.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.da.library.R;
import com.da.library.view.pickerios.picker.LoopListener;
import com.da.library.view.pickerios.picker.LoopView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by baiyuliang on 2015-11-24.
 */
public class PopOneHelper {

    private Context context;
    private PopupWindow pop;
    private View view;
    private OnClick mOnClick;
    private OnListener onListener;
    private FrameLayout tv;
    private List<String> datas;


    static int item = 0;
    public int selectposition = 0;

    LoopView loopView;


    public PopOneHelper(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.picker_one, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT, true);

        initPop();

        initView();
    }


    private void initPop() {
        pop.setAnimationStyle(android.R.style.Animation_InputMethod);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
        loopView = view.findViewById(R.id.loopView1);
        tv = view.findViewById(R.id.fl_select);
        FrameLayout tvCannnel = view.findViewById(R.id.fl_cannel);
        loopView.setList(new ArrayList<String>());
        loopView.setNotLoop();
        loopView.setCurrentItem(0);
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                PopOneHelper.item = item;
                selectposition = item;
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClick != null) {
                    mOnClick.onClick();
                    pop.dismiss();
                }
            }
        });

        tvCannnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });

    }


    public void setList(List<String> datas) {
        if (datas == null)
            return;
        this.datas = datas;
        loopView.setList(datas);
    }


    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        if (null == datas || datas.size() <= 0) {
            Toast.makeText(context, "请初始化您的数据", Toast.LENGTH_LONG).show();
            return;
        }
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 隐藏监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        pop.setOnDismissListener(onDismissListener);
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    public void setOnClick(OnClick mOnClick) {
        this.mOnClick = mOnClick;

    }

    public interface OnListener {
        void onClickOk(String str, int position);
    }

    public interface OnClick {
        void onClick();
    }

}
