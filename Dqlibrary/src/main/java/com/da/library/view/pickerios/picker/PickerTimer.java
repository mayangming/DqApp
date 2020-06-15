package com.da.library.view.pickerios.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.da.library.R;

import java.util.ArrayList;


/**
 * 城市Picker
 * 
 * @author zd
 * 
 */
public class PickerTimer extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker pickerTimer_Hour;
	private ScrollerNumberPicker pickerTimer_Minute;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempTimerHourIndex = -1;
	private int tempTimerMinuteIndex = -1;
	private ArrayList<String> listHour = new ArrayList<String>();
	private ArrayList<String> listMinute = new ArrayList<String>();
	private ArrayList<String> DefalutlistMinute = new ArrayList<String>();

	public PickerTimer(Context context, AttributeSet attrs) {
		super(context, attrs);
		for (int i = 1; i <= 24; i++) {
			if (i < 10) {
				listHour.add("0" + i);
			} else {
				listHour.add("" + i);
			}
		}
		for (int i = 0; i < 60; i++) {
			if (i < 10) {
				listMinute.add("0" + i);
			} else {
				listMinute.add("" + i);
			}
		}
	}

	public PickerTimer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		for (int i = 1; i <= 24; i++) {
			listHour.add(i + "");
		}
		for (int i = 0; i < 60; i++) {
			if (i < 10) {
				listMinute.add("0" + i);
			} else {
				listMinute.add("" + i);
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.picker_timer, this);
		// 获取控件引用
		// 初始化滑动选择器控件 并且设置数据 显示默认位置 start
		pickerTimer_Hour = (ScrollerNumberPicker) findViewById(R.id.pickerTimer_Hour);
		pickerTimer_Minute = (ScrollerNumberPicker) findViewById(R.id.pickerTimer_Minute);
		pickerTimer_Hour.setData(listHour);
		pickerTimer_Hour.setDefault(0);
		pickerTimer_Minute.setData(listMinute);
		pickerTimer_Minute.setDefault(0);
		// //初始化滑动选择器控件 并且设置数据 显示默认位置 end
		pickerTimer_Hour.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (tempTimerHourIndex != id||listHour.size()>1&&id==0) {
					System.out.println("endselect");
					String selectDay = pickerTimer_Hour.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					// 添加时间数组
					if (id == 0) {
						pickerTimer_Minute.setData(DefalutlistMinute);
					} else {
						pickerTimer_Minute.setData(listMinute);
					}
					pickerTimer_Minute.setDefault(0);

					int lastDay = Integer.valueOf(pickerTimer_Hour
							.getListSize());
					if (id > lastDay) {
						pickerTimer_Hour.setDefault(lastDay - 1);
					}

				}
				tempTimerHourIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});
		pickerTimer_Minute.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (tempTimerMinuteIndex != id) {
					String selectDay = pickerTimer_Minute.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = pickerTimer_Minute.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					int lastDay = Integer.valueOf(pickerTimer_Minute
							.getListSize());
					if (id > lastDay) {
						pickerTimer_Minute.setDefault(lastDay - 1);
					}
				}
				tempTimerMinuteIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
	}

	@SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getHuorValue() {
		return pickerTimer_Hour.getSelectedText();
	}

	public String getMinuteValue() {
		return pickerTimer_Minute.getSelectedText();
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}

	/******* 设置上限小时值 *******/
	public void setHourUpperlimitInitialValue(String initialValue) {
		listHour = new ArrayList<String>();
		int intinitialValue = Integer.valueOf(initialValue);
		for (int i = intinitialValue; i <= 23; i++) {
			if (i < 10) {
				listHour.add("0" + i);
			} else {
				listHour.add("" + i);
			}
		}
		pickerTimer_Hour.setData(listHour);
	}

	/******* 设置上限分钟值 *******/
	public void setMinuteUpperlimitInitialValue(String initialValue) {
		DefalutlistMinute = new ArrayList<String>();
		int intinitialValue = Integer.valueOf(initialValue);
		for (; intinitialValue < 60; intinitialValue++) {
			if (intinitialValue < 10) {
				DefalutlistMinute.add("0" + intinitialValue);
			} else {
				DefalutlistMinute.add("" + intinitialValue);
			}
		}
	}

	/******* 设置默认小时值 *******/
	public void setHourInitialValue(String initialValue) {
		for (int i = 0, n = listHour.size(); i < n; i++) {
			if (initialValue.equals(listHour.get(i))) {
				if (pickerTimer_Hour != null)
					pickerTimer_Hour.setDefault(i);
				break;
			}
		}
	}

	/******* 设置默认分钟值 *******/
	public void setMinuteInitialValue(String initialValue) {
		if (pickerTimer_Hour.getSelectedText().equals(listHour.get(0))) {
			pickerTimer_Minute.setData(DefalutlistMinute);
			for (int i = 0, n = DefalutlistMinute.size(); i < n; i++) {
				if (initialValue.equals(DefalutlistMinute.get(i))) {
					if (pickerTimer_Minute != null)
						pickerTimer_Minute.setDefault(i);
					break;
				}
			}
		} else {
			pickerTimer_Minute.setData(listMinute);
			for (int i = 0, n = listMinute.size(); i < n; i++) {
				if (initialValue.equals(listMinute.get(i))) {
					if (pickerTimer_Minute != null)
						pickerTimer_Minute.setDefault(i);
					break;
				}
			}
		}

	}

}
