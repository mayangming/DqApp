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
import com.da.library.view.pickerios.picker.LoopListener;
import com.da.library.view.pickerios.picker.LoopView;

import java.util.Calendar;
import java.util.List;


/**
 * Created by baiyuliang on 2015-11-24.
 */
public class PopFutureDateHelper {

    private Context context;
    private PopupWindow pop;
    private View view;
    private OnClickOkListener onClickOkListener;
    private List<String> listYear, listMonth, listDay,listTime;
    private int initHour,monthitem=0,dayitem=0,houritem=0;
    private String year, month, day,hour,initYear,initMonth,initDay;
    boolean isCreate=true;
    LoopView loopView1=null,loopView2=null,loopView3=null;


    public PopFutureDateHelper(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.picker_birth, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
        Calendar c = Calendar.getInstance();
        initYear= c.get(Calendar.YEAR)+"";
        initMonth= (c.get(Calendar.MONTH)+1)>=10?(c.get(Calendar.MONTH)+1)+"":"0"+(c.get(Calendar.MONTH)+1);
        initDay= c.get(Calendar.DAY_OF_MONTH)>=10?c.get(Calendar.DAY_OF_MONTH)+"":"0"+ c.get(Calendar.DAY_OF_MONTH);
        initHour= c.get(Calendar.HOUR_OF_DAY);
        listYear = DateUtil.getBirthYearList();
        listMonth = DateUtil.getBirthMonthList(initYear);
        listDay = DateUtil.getBirthDay31AndWeekList(initYear+"-"+initMonth, Integer.valueOf(initDay));
        listTime = DateUtil.get24HourTimeList(initHour);
    }

    private void initView() {
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOK);
        loopView1 = (LoopView) view.findViewById(R.id.loopView1);
        loopView2 = (LoopView) view.findViewById(R.id.loopView2);
        loopView3 = (LoopView) view.findViewById(R.id.loopView3);
//        loopView4 = (LoopView) view.findViewById(R.id.loopView4);
        loopView1.setList(listYear);
        loopView1.setNotLoop();
        loopView1.setCurrentItem(0);//定位到当前年份
        loopView2.setNotLoop();
        loopView3.setNotLoop();
//        loopView4.setNotLoop();


        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if(isCreate){
                    item=0;
                }
                if(item>listYear.size()-1){
                    return;
                }
                String select_item = listYear.get(item);
                if (TextUtils.isEmpty(year)) {
                    year = initYear;
                } else {
                    year = select_item.replace("年", "");
                }

                if(listMonth==null){
                    monthitem=0;
                    listMonth = DateUtil.getBirthMonthList(initYear);
                    loopView2.setList(listMonth);
                    loopView2.setCurrentItem(0);
                    return;
                }

                changeloopView2();
            }
        });
        loopView2.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if(isCreate){
                    item=0;
                }
                if(item>listMonth.size()-1){
                    return;
                }
                monthitem=item;
                String select_item = listMonth.get(item);

                if (TextUtils.isEmpty(month)) {
                    month = initMonth;
                } else {
                    month = select_item.replace("月", "");
                }
                if(isCreate){
                    month=initMonth;
                    isCreate=false;
                }

                if(null==listDay||listDay.size()==0){
                    getDays();
                }
                changeloopView3();
            }
        });

        loopView3.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                if(isCreate){
                    item=0;
                }
                if(item>listDay.size()-1){
                    return;
                }
                //将天数的Item记录下来  为了年 . 月滚动的时候可以根据Item跳转到相应的天数
                if(null==listDay||listDay.size()==0){
                    dayitem=0;
                }else{
                    dayitem=item;
                }

                String select_item = listDay.get(item);
                if (TextUtils.isEmpty(day)) {
                    day=initDay+ DateUtil.getWeekOfDate(initYear+"-"+initMonth+"-"+initDay);
                } else {
                    day = select_item.replace("日", "");
                }

                boolean ischange=false;
                int i=1;
                String cday=day.substring(0,2);
                if(year.equals(initYear)&&month.equals(initMonth)&&cday.equals(initDay)){
                    ischange=true;
                    i= Integer.valueOf(initHour)+1;
                    listTime= DateUtil.get24HourTimeList(i);
                }else if(listTime.size()<24){
                    ischange=true;
                    listTime= DateUtil.get24HourTimeList(i);
                }


//                if(ischange){
//                    int  toitem=0;
//                    if(listTime.size()>(houritem+1)&&!cday.equals(initDay)){
//                        toitem=houritem;
//                    }
//
//                    loopView4.setList(listTime);
//                    loopView4.setCurrentItem(toitem);
//                }


            }

        });

//        loopView4.setListener(new LoopListener() {
//            @Override
//            public void onItemSelect(int item) {
//                if(isCreate){
//                    item=0;
//                    isCreate=false;
//                }
//                houritem=item;
//                hour = listTime.get(item);
//            }
//        });

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
                        String showday="01";
                        if(day.length()>2){
                            showday=day.substring(0,2);
                        }
                        onClickOkListener.onClickOk(year, month, showday);
                    }
                }, 500);
            }
        });
    }





    private void  changeloopView2(){

        int iMonth=0;//获取用户滚动之前的月份是多少
        if(listMonth.size()>0){
            String strMonth= listMonth.get(0);
            String cmonth=strMonth.replace("月", "");
            iMonth= Integer.valueOf(cmonth);
        }
        if(year.equals(initYear)){
            listMonth = DateUtil.getBirthMonthList(initYear);
        }else{
            listMonth = DateUtil.getBirthMonthList("0");
        }


        //得到当前年限的第一个最早月份
        if(TextUtils.isEmpty(month)){
            month=initMonth;
        }else{
            month=listMonth.get(0).replace("月", "");
        }

        int nowMonth=0;//获取用户现在要滚动的月份是多少
        if(listMonth.size()>0){
            String strMonth= listMonth.get(0);
            String cmonth=strMonth.replace("月", "");
            nowMonth= Integer.valueOf(cmonth);
        }


        int  toitem=0;//默认显示月份为集合的第一个值
        if(year.equals(initYear))  //判断用户选择的年份是否为当前年份   如果是当前年份  判断即将显示的最早月份减去之前显示的最早月份是否大于0(3月-4月)
        {
            if(nowMonth-iMonth>0){
                toitem=monthitem-(nowMonth-iMonth);
            }
        }else  if(iMonth>nowMonth){
            toitem=monthitem+(iMonth-nowMonth);
        }else{
            toitem=monthitem;
        }


        loopView2.setList(listMonth);
        if(listMonth.size()>(toitem)){
            loopView2.setCurrentItem(toitem);
        }

    }




    private void  changeloopView3(){

        //获取之前显示的日期第一个值
        int iday=0;
        if(listDay.size()>0){
            String strday= listDay.get(0);
            String cday=strday.substring(0,2);
            iday= Integer.valueOf(cday);
        }

        getDays();

        //获取将要显示的日期第一个值
        int nowday=0;
        if(listDay.size()>0){
            String strday= listDay.get(0);
            String cday=strday.substring(0,2);
            nowday= Integer.valueOf(cday);
        }



        //默认跳转的下标为0
        int  toitem=0;
        if(year.equals(initYear)&&month.equals(initMonth)) //判断用户选择的年份是否为当前年份月份
        {

            if(nowday-iday>0){
                toitem=dayitem-(nowday-iday);
            }
        }else  if(iday>nowday){
            toitem=dayitem+(iday-nowday);
        }else{
            toitem=dayitem;
        }

        loopView3.setList(listDay);
        if(listDay.size()>(toitem)){
            loopView3.setCurrentItem(toitem);
        }

    }



    private  void getDays(){
        int i=1;
        if(year.equals(initYear)&&month.equals(initMonth)){
            i= Integer.valueOf(initDay);
        }
        if (!TextUtils.isEmpty(month)&&month.equals("02")) {
            if (DateUtil.isRunYear(year)) {
                listDay= DateUtil.getBirthDay29AndWeekList(year+"-"+month,i);

            }else if (!DateUtil.isRunYear(year)) {
                listDay= DateUtil.getBirthDay28AndWeekList(year+"-"+month,i);

            }
        }else if((month.equals("01")||month.equals("03")||month.equals("05")||month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12"))){
            listDay= DateUtil.getBirthDay31AndWeekList(year+"-"+month,i);

        }else if((month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11"))){
            listDay= DateUtil.getBirthDay30AndWeekList(year+"-"+month,i);

        }
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
        public void onClickOk(String year, String month, String day);
    }

}
