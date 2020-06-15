package com.da.library.view.pickerios.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.da.library.R;
import com.da.library.utils.DateUtil;
import com.da.library.utils.StringUtils;
import com.da.library.view.pickerios.picker.LoopListener;
import com.da.library.view.pickerios.picker.LoopView;

import java.util.Calendar;
import java.util.List;


/**
 * Created by baiyuliang on 2015-11-24.
 */
public class PopBirthHelper {

    private Context context;
    private boolean isFuture;
    private PopupWindow pop;
    private View view;
    private OnClickOkListener onClickOkListener;

    private List<String> listYear, listMonth, listDay;
    private String year, month, day;
    private String defalutyear, defalutmonth, defalutday;

    public PopBirthHelper(Context context, boolean isFuture) {
        this.context = context;
        this.isFuture = isFuture;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.picker_birth, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT, true);
        initPop();
        initData();
        initView();
    }


    private void initPop() {
        pop.setAnimationStyle(android.R.style.Animation_InputMethod);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        if (isFuture) {
            listYear = DatePackerUtil.getFutureBirthYearList(3, 2);
        } else {
            listYear = DatePackerUtil.getBirthYearList();
        }

        listMonth = DatePackerUtil.getBirthMonthList();
        listDay = DatePackerUtil.getBirthDay31List();
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        final LoopView loopView1 = (LoopView) view.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) view.findViewById(R.id.loopView2);
        final LoopView loopView3 = (LoopView) view.findViewById(R.id.loopView3);
        loopView1.setList(listYear);
        loopView1.setNotLoop();

        if (isFuture) {
            loopView1.setCurrentItem(3);
        } else {
            Calendar c = Calendar.getInstance();
            loopView1.setCurrentItem(99 - (c.get(Calendar.YEAR) - 1990));//定位到1990年
        }

        loopView2.setList(listMonth);
        loopView2.setNotLoop();

        loopView3.setList(listDay);
        loopView3.setNotLoop();

        //获取当前年月日
        if (isFuture) {
            String time = DateUtil.getCurrentYMD();
            String[] timeSplit = time.split("-");
            if (timeSplit.length > 2) {
                defalutyear = timeSplit[0];
                defalutmonth = timeSplit[1];
                defalutday = timeSplit[2];
                if (listMonth.size() > StringUtils.str2int(defalutmonth) - 1)
                    loopView2.setCurrentItem(StringUtils.str2int(defalutmonth) - 1);
                if (listDay.size() > StringUtils.str2int(defalutday) - 1)
                    loopView3.setCurrentItem(StringUtils.str2int(defalutday) - 1);

            } else {
                loopView2.setCurrentItem(0);
                loopView3.setCurrentItem(0);
            }

        } else {
            loopView2.setCurrentItem(0);
            loopView3.setCurrentItem(0);
        }


        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (item > listYear.size() - 1) {
                    return;
                }
                String select_item = listYear.get(item);
                if (TextUtils.isEmpty(year)) {
                    if (!isFuture) {
                        year = "1990";
                    } else {
                        year = defalutyear;
                    }
                } else {
                    year = select_item.replace("年", "");
                }
                if (!TextUtils.isEmpty(month) && month.equals("02")) {
                    if (DatePackerUtil.isRunYear(year) && listDay.size() != 29) {
                        listDay = DatePackerUtil.getBirthDay29List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    } else if (!DatePackerUtil.isRunYear(year) && listDay.size() != 28) {
                        listDay = DatePackerUtil.getBirthDay28List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    }
                }
            }
        });
        loopView2.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (item > listMonth.size() - 1) {
                    return;
                }
                String select_item = listMonth.get(item);
                if (TextUtils.isEmpty(month)) {
                    if (!isFuture) {
                        month = "01";
                    } else {
                        month = defalutmonth;
                        return;
                    }
                } else {
                    month = select_item.replace("月", "");
                }
                if (month.equals("02")) {
                    if (!TextUtils.isEmpty(year) && DatePackerUtil.isRunYear(year) && listDay
                            .size() != 29) {
                        listDay = DatePackerUtil.getBirthDay29List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    } else if (!TextUtils.isEmpty(year) && !DatePackerUtil.isRunYear(year) &&
                            listDay.size() != 28) {
                        listDay = DatePackerUtil.getBirthDay28List();
                        loopView3.setList(listDay);
                        loopView3.setCurrentItem(0);
                    }
                } else if ((month.equals("01") || month.equals("03") || month.equals("05") ||
                        month.equals("07") || month.equals("08") || month.equals("10") || month
                        .equals("12")) && listDay.size() != 31) {
                    listDay = DatePackerUtil.getBirthDay31List();
                    loopView3.setList(listDay);
                    loopView3.setCurrentItem(0);
                } else if ((month.equals("04") || month.equals("06") || month.equals("09") ||
                        month.equals("11")) && listDay.size() != 30) {
                    listDay = DatePackerUtil.getBirthDay30List();
                    loopView3.setList(listDay);
                    loopView3.setCurrentItem(0);
                }

            }
        });
        loopView3.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if (item > listDay.size() - 1) {
                    return;
                }
                String select_item = listDay.get(item);
                if (TextUtils.isEmpty(day)) {
                    if (!isFuture) {
                        day = "01";
                    } else {
                        day = defalutday;
                    }
                } else {
                    day = select_item.replace("日", "");

                }
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
                        onClickOkListener.onClickOk((StringUtils.isEmpty(year) ? defalutyear :
                                year) +
                                "-" + (StringUtils.isEmpty(month) ? defalutyear : month) + "-" +
                                (StringUtils.isEmpty(day) ? defalutyear : day));
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

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        public void onClickOk(String birthday);
    }

}
