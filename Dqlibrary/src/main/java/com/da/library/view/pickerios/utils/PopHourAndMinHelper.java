package com.da.library.view.pickerios.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
public class PopHourAndMinHelper {

    private Context context;
    private PopupWindow pop;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> listHour, listMin;
    private String hour, min;
    private int maxmin;

    public PopHourAndMinHelper(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.picker_hour_min, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT, true);
        initPop();
    }


    private void initPop() {
        pop.setAnimationStyle(android.R.style.Animation_InputMethod);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopViewhour = (LoopView) view.findViewById(R.id.loopView1);
        final LoopView loopViewmin = (LoopView) view.findViewById(R.id.loopView2);
        if (null == listHour) {
            listHour = new ArrayList<String>();
        }
        loopViewhour.setList(listHour);
        loopViewhour.setNotLoop();
        loopViewhour.setCurrentItem(0);
        loopViewhour.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (item > listHour.size() - 1) {
                    return;
                }
                hour = listHour.get(item);
                //如果是第一个索引  通过最大分钟数  获取时间
                if (item == 0) {
                    listMin = DatePackerUtil.getMin(maxmin);
                } else {
                    listMin = DatePackerUtil.getMin(0);
                }

                loopViewmin.setList(listMin);
                loopViewmin.setNotLoop();
                loopViewmin.setCurrentItem(0);

            }
        });

        //通过最大值获取可选分钟数
        listMin = DatePackerUtil.getMin(maxmin);
        loopViewmin.setList(listMin);
        loopViewmin.setNotLoop();
        loopViewmin.setCurrentItem(0);
        loopViewmin.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (item > listMin.size() - 1) {
                    return;
                }
                min = listMin.get(item);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClickOkListener.onClickOk(hour + ":" + min);
                    }
                }, 500);
            }
        });
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        if (null == listHour || listHour.size() <= 0) {
            Toast.makeText(context, "请初始化您的数据", Toast.LENGTH_LONG).show();
            return;
        }
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    public void setListItem(List<String> listItem, int maxmin) {
        this.listHour = listItem;
        if (maxmin < 59) {
            this.maxmin = maxmin + 1;
        } else {
            this.maxmin = maxmin;
        }
        initView();
    }

    /**
     * 隐藏监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        pop.setOnDismissListener(onDismissListener);
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        public void onClickOk(String str);
    }

}
