package com.da.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.da.library.R;


public class MainLoading extends Dialog {
	public MainLoading(Context context) {
		super(context);
	}

	public MainLoading(Context context, int themeResId) {
		super(context, themeResId);
	}

	public static class Builder{
		private Context context;
		private String message;
		private int themeId;
		private boolean isShowMessage=true;
		private boolean isCancelable=true;
		private boolean isCancelOutside=false;


		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTheme(int id) {
			this.themeId = id;
			return this;
		}
		/**
		 * 设置提示信息
		 * @param message
		 * @return
		 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * 设置是否显示提示信息
		 * @param isShowMessage
		 * @return
		 */
		public Builder setShowMessage(boolean isShowMessage) {
			this.isShowMessage = isShowMessage;
			return this;
		}

		/**
		 * 设置是否可以按返回键取消
		 * @param isCancelable
		 * @return
		 */

		public Builder setCancelable(boolean isCancelable){
			this.isCancelable=isCancelable;
			return this;
		}

		/**
		 * 设置是否可以取消
		 * @param isCancelOutside
		 * @return
		 */
		public Builder setCancelOutside(boolean isCancelOutside){
			this.isCancelOutside=isCancelOutside;
			return this;
		}

		private TextView mMsgTv;

		public MainLoading create() {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.loading_dialog, null);
			MainLoading loadingDailog = null;
			if (themeId != 0) {
				loadingDailog = new MainLoading(context, themeId);
			} else {
				loadingDailog = new MainLoading(context, R.style.loading_dialog);
			}
			mMsgTv = (TextView) view.findViewById(R.id.tipTextView);
			if (isShowMessage) {
				mMsgTv.setText(message);
			} else {
				mMsgTv.setVisibility(View.GONE);
			}
			loadingDailog.setContentView(view);
			loadingDailog.setCancelable(isCancelable);
			loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
			return loadingDailog;
		}

		public void setDesc(String desc) {
			mMsgTv.setText(desc);
			mMsgTv.setVisibility(View.VISIBLE);
		}

	}
}
