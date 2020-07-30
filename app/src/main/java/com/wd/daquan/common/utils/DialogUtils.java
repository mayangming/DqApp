package com.wd.daquan.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.listener.DialogListener;
import com.wd.daquan.mine.listener.PayDetailListener;
import com.meetqs.qingchat.pickerview.adapter.ArrayWheelAdapter;
import com.meetqs.qingchat.pickerview.builder.OptionsPickerBuilder;
import com.meetqs.qingchat.pickerview.view.OptionsPickerView;
import com.da.library.tools.DateUtil;
import com.da.library.tools.DensityUtil;
import com.da.library.view.CommDialog;

import java.util.List;

/**
 * Created by DELL on 2018/9/12.
 */

public class DialogUtils {

    public static void showNoBindWXDialog(Context context, String title, String content, DialogListener dialogListener) {
        new CommDialog(context, title, content, dialogListener).show();
    }
    public static void showSettingQCNumDialog(Context context, String title, String content, String btn_left, String btn_right,
           DialogListener dialogListener){
        new CommDialog(context, title, content, btn_left, btn_right, dialogListener).show();
    }
    public static void showSingleBtnSettingQCNumDialog(Context context, String title, String content, String btn_right,
                                                       boolean isShowSingleBtn){
        new CommDialog(context, title, content, btn_right, isShowSingleBtn).show();
    }

    public static CommDialog showAuthDialog(Context context, String title, String content, String btn_right){
        CommDialog commDialog = new CommDialog(context, title, content, btn_right);
        commDialog.show();
        return commDialog;
    }


    //SDK切换账号
    public static CommDialog showChangeAcountDialog(Context context, String title, String content, String btn_left, String btn_right,
                                                    DialogListener dialogListener){
        return new CommDialog(context, title, content, btn_left, btn_right, dialogListener);
    }

    /**
     * 底部弹出的dialog
     */
    public static Dialog showBottomDialog(final Activity activity, int type, BottomDialogButtonListener listeners) {
//        listener = listeners;
        View view = LayoutInflater.from(activity).inflate(R.layout.bottom_dlg, null);
        TextView tvHint = view.findViewById(R.id.tv_hint);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancle = view.findViewById(R.id.tv_cancle);

        switch (type) {
            case 1:
                tvHint.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(activity.getResources().getString(R.string.dialog_bottom_picture));
                tvMessage.setTextColor(Color.BLACK);
                tvConfirm.setText(activity.getResources().getString(R.string.dialog_bottom_from_pics));
                tvConfirm.setTextColor(Color.BLACK);
                break;
            case 2:
                tvHint.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);
                tvConfirm.setText(activity.getResources().getString(R.string.setting_logout));
                tvConfirm.setTextColor(Color.RED);
                break;
            case 3:
                tvHint.setVisibility(View.VISIBLE);
                tvHint.setText(activity.getResources().getString(R.string.cache_tips));
                tvMessage.setVisibility(View.GONE);
                tvConfirm.setText(activity.getResources().getString(R.string.cache_empty));
                tvConfirm.setTextColor(Color.RED);
                break;
            case 4://查看红包记录
                tvHint.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText("红包记录");
                tvConfirm.setVisibility(View.GONE);
                break;
        }

        final Dialog dialog = setBottomDialogStyle(activity, view);
        tvMessage.setOnClickListener(view1 -> {
            if (listeners != null) {
                listeners.checkButton(R.id.tv_message);
            }
            dialog.dismiss();
        });
        tvConfirm.setOnClickListener(view12 -> {
            if (listeners != null) {
                listeners.checkButton(R.id.tv_confirm);
            }
            dialog.dismiss();
        });
        tvCancle.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }
    public interface BottomDialogButtonListener {
        void checkButton(int id);
    }
    /**
     * 设置底部dialog 样式
     *
     * @param context 上下文
     * @param view    试图
     * @return Dialog
     */
    @NonNull
    private static Dialog setBottomDialogStyle(Activity context, View view) {
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialog_style); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        view.measure(0, 0);
        lp.width = (int) DensityUtil.getScreenWidth(context); // 宽度
        lp.height = view.getMeasuredHeight();
        //lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    /**
     * 版本dialog
     */
    public static Dialog showCheckDialogs(DialogListener listeners, final Context context, final String version, String title,
                                          String tvcontent, final String updatetyle, final String url) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.check_dlg, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        TextView tvCancel = view.findViewById(R.id.tvCancelCheck);
        TextView tvApprove = view.findViewById(R.id.tvUpCheck);
        TextView tvSwitch = view.findViewById(R.id.tvSwitch);
        TextView tvcontentupdate = view.findViewById(R.id.tvcontentupdate);
        tvSwitch.setText(title);
        tvcontentupdate.setText(tvcontent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if ("1".equals(updatetyle)) {
            tvCancel.setText(context.getString(R.string.cancel));
        } else if ("2".equals(updatetyle)) {
            tvCancel.setText(context.getString(R.string.about_check_exit));
        }
        tvCancel.setOnClickListener(view1 -> {
            switch (updatetyle) {
                case "1":

                    dialog.dismiss();
                    listeners.onCancel();
                    break;
                case "2":
                    ((Activity) context).finish();
                    break;
            }
        });
        tvApprove.setOnClickListener(view12 -> {
            dialog.dismiss();
            NavUtils.gotoDownAppActivity(context, url, updatetyle, version);
        });
        return dialog;
    }

    public static OptionsPickerView showYearToDate(Context context, List<String> listYear, List<String> listMonth, List<String> listDate,
                                                   int date, PayDetailListener listener) {

        OptionsPickerView mPvOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v, wheelOptions) -> {
            String mYearStr = listYear.get(options1);
            String mMonthStr = listMonth.get(options2);
            String mDateStr = listDate.get(options3);
            String mYear = DateUtil.getFormatYear(mYearStr);
            String mMonth = DateUtil.getFormatMonth(mMonthStr);
            String mDate = DateUtil.getFormatDate(context, mDateStr);
            if (!context.getString(R.string.none).equals(mDateStr)) {
                int current = Integer.parseInt(mDate);
                wheelOptions.setCurrentItem(current);
            }
            listener.payDetailClick(mYear, mMonth, mDate);
        }, (options1, options2, options3, wheelOptions, bool) -> {
            if (bool) {
                String mYearStr = listYear.get(options1);
                String mMonthStr = listMonth.get(options2);
                String mYear = mYearStr.substring(0, mYearStr.length() - 1);
                String mMonth = mMonthStr.substring(0, mMonthStr.length() - 1);
                List<String> listStr = DateUtil.getWheelDateList(context, Integer.parseInt(mYear), Integer.parseInt(mMonth));
                wheelOptions.setAdapter(new ArrayWheelAdapter(listStr));
                wheelOptions.setCurrentItem(0);
            }
        })
                .setTitleText(context.getString(R.string.alipay_pay_details_select_time))
                .setDividerColor(context.getResources().getColor(R.color.text_blue))
                .setTextColorCenter(context.getResources().getColor(R.color.text_blue)) //设置选中项文字颜色
                .setContentTextSize(20)
                .setCancelText(context.getString(R.string.cancel))//取消按钮文字
                .setSubmitText(context.getString(R.string.confirm))//确认按钮文字
                .setTitleSize(16)//标题文字大小
                .setSubCalSize(18)//确定取消文字大小
                .setTitleColor(context.getResources().getColor(R.color.text_title_black))//标题文字颜色
                .setSubmitColor(context.getResources().getColor(R.color.text_blue))//确定按钮文字颜色
                .setCyclic(false, false, false)
                .setCancelColor(context.getResources().getColor(R.color.text_title_black))
                .build();
        mPvOptions.setNPicker(listYear, listMonth, listDate);//三级选择器
        mPvOptions.setSelectOptions(listYear.size() - 1, date - 1);
        return mPvOptions;
    }

    /**
     * 群组退出确认
     *
     * @param activity 引用对象
     * @param type     类型
     * @param dialogListener 回调
     */
    public static Dialog showButtomGroupDialog(Activity activity, int type, BottomDialogListener dialogListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_quit_group, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancle);
        switch (type) {
            case 1://退出群组
                tvMessage.setText(activity.getString(R.string.group_quit_confirm_notify));
                tvConfirm.setText(activity.getString(R.string.comm_ok));
                break;
            case 2://解散群组
                tvMessage.setText(activity.getString(R.string.group_dismiss_confirm_notify));
                tvConfirm.setText(activity.getString(R.string.comm_ok));
                break;
        }

        final Dialog dialog = setBottomDialogStyle(activity, view);

        tvCancel.setOnClickListener(v -> dialog.dismiss());

        tvConfirm.setOnClickListener(v -> {
            dialogListener.onClick(type);
            dialog.dismiss();
        });

        return dialog;
    }

    public interface BottomDialogListener {
        void onClick(int type);
    }

    /**
     * 删除聊天记录,认证，清空聊天记录，退出，删除描述
     *
     * @param activity  引用对象
     * @param type      类型
     * @param dialogListener  数据回调接口
     * @return Dialog
     */
    public static Dialog showPurseDialog(final Activity activity, int type, String content, BottomDialogListener dialogListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.purse_dlg, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        switch (type) {
            case 1://关闭群组认证
                tvMessage.setText(activity.getString(R.string.guanmanager));
//                tvMessage.setText(activity.getString(R.string.clean_group_chat_history));
                tvConfirm.setText(activity.getString(R.string.confirm));
                break;
            case 2://清空聊天消息
                tvMessage.setText(activity.getString(R.string.clean_group_chat_history));
                tvConfirm.setText(activity.getString(R.string.confirm));
                tvConfirm.setTextColor(activity.getResources().getColor(R.color.color_ff0000));
                break;
            case 3://取消支付宝授权
                tvMessage.setText(activity.getString(R.string.is_cancel_alipay));
                tvConfirm.setText(activity.getString(R.string.confirm));
                tvConfirm.setTextColor(activity.getResources().getColor(R.color.color_ff0000));
                break;
            case 4://退出确认
                tvMessage.setText(activity.getString(R.string.is_cancle_edit));
                tvConfirm.setText(activity.getString(R.string.confirm));
                tvConfirm.setTextColor(activity.getResources().getColor(R.color.text_blue));
                break;
            case 5://删除描述
                tvMessage.setText(activity.getString(R.string.is_delete_describe));
                tvConfirm.setText(activity.getString(R.string.confirm));
                tvConfirm.setTextColor(activity.getResources().getColor(R.color.text_blue));
                break;
            case 7:
                tvMessage.setText(SpannableStringUtils.addTextColor(activity.getString(R.string.forbid_dispatcher,
                        content), 2, content.length() + 2, activity.getResources().getColor(R.color.color_ff0000)));
                tvConfirm.setText(activity.getString(R.string.confirm));
                break;
            case 8:
                tvMessage.setText(SpannableStringUtils.addTextColor(activity.getString(R.string.move_dispatcher,
                        content), 3, content.length() + 3, activity.getResources().getColor(R.color.color_ff0000)));
                tvConfirm.setText(activity.getString(R.string.confirm));
                break;
            case 9://是否保存
                tvMessage.setText(activity.getString(R.string.is_save_modify));
                tvConfirm.setText(activity.getString(R.string.ac_note_info_save));
                tvConfirm.setTextColor(activity.getResources().getColor(R.color.app_theme_color));
                break;
        }

        final Dialog dialog = setCenterDialogStyle(activity, view);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogListener != null) {
                    dialogListener.onClick(R.id.tv_cancel);
                }
                dialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogListener != null) {
                    dialogListener.onClick(R.id.tv_confirm);
                }
                dialog.dismiss();
            }
        });

        return dialog;
    }

    /**
     * 支付p
     */
    public static Dialog showPayError(final Activity activity, String content, BottomDialogListener dialogListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.purse_dlg, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvMessage.setText(content);
        tvCancel.setText(activity.getString(R.string.retry));
        tvConfirm.setText(activity.getString(R.string.forget_password));
        tvConfirm.setTextColor(activity.getResources().getColor(R.color.app_theme_color));
        final Dialog dialog = setCenterDialogStyle(activity, view);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogListener != null) {
                    dialogListener.onClick(R.id.tv_cancel);
                }
                dialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogListener != null) {
                    dialogListener.onClick(R.id.tv_confirm);
                }
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private static Dialog setCenterDialogStyle(Context cotext, View view) {
        Dialog dialog = new Dialog(cotext, R.style.kangzai_dialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        view.measure(0, 0);
        lp.width = (int) (DensityUtil.getScreenWidth(cotext) * 0.85);
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    //分享确认
    public static void showShareConfirmDialog(Context mContext, String appName, final DialogListener dialogListener) {
        final Dialog dialog = new Dialog(mContext, R.style.conversationsa_dialog);
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_sdk_share_confirm, null);
        TextView btn_exit = (TextView) view.findViewById(R.id.dialogShareExit);
        TextView btn_stay = (TextView) view.findViewById(R.id.dialogEditStay);
        btn_exit.setText(appName);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (DensityUtil.getScreenWidth(DqApp.sContext) * 0.75);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        dialog.show();
        btn_exit.setOnClickListener(new View.OnClickListener() {//退出
            @Override
            public void onClick(View view) {
                dialogListener.onCancel();
                dialog.dismiss();
            }
        });
        btn_stay.setOnClickListener(new View.OnClickListener() {//留在斗圈
            @Override
            public void onClick(View view) {
                dialogListener.onOk();
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示邀请群被拒绝人数显示
     */
    public static void showRefuesInviteDialog(Context context, String desc, String memberNum, DialogListener mDialogListener){
        desc = getGroupInviteContent(desc, memberNum);
        CommDialog commDialog = new CommDialog(context);
        commDialog.setDesc(desc);
        commDialog.setDialogListener(mDialogListener);
        commDialog.setDescTextSize(17);
        commDialog.setSingleBtn();
        commDialog.setTitleGone();
        commDialog.setOkTxt(context.getResources().getString(R.string.confirm));
        commDialog.setOkTxtColor(context.getResources().getColor(R.color.text_blue));
        commDialog.setCanceledOnTouchOutside(false);
        commDialog.show();
    }

    /**
     * 通用对话框,只有内容和确定按钮，没有标题
     */
    public static void showCommonDialogForSure(Context context, String title, String desc, DialogListener mDialogListener){
        CommDialog commDialog = new CommDialog(context);
        commDialog.setDesc(desc);
        commDialog.setTitle(title);
        commDialog.setDialogListener(mDialogListener);
        commDialog.setDescTextSize(17);
        commDialog.setSingleBtn();
        commDialog.setTitleVisible(true);
        commDialog.setOkTxt(context.getResources().getString(R.string.confirm));
        commDialog.setOkTxtColor(context.getResources().getColor(R.color.text_blue));
        commDialog.setCancelTxt(context.getResources().getString(R.string.cancel));
        commDialog.setCancelTxtColor(context.getResources().getColor(R.color.text_blue));
        commDialog.setCanceledOnTouchOutside(false);
        commDialog.show();
    }
    /**
     * 通用对话框,只有内容和确定按钮，没有标题
     */
    public static void showCommonDialogForBoth(Context context,String title, String desc, DialogListener mDialogListener){
        CommDialog commDialog = new CommDialog(context);
        commDialog.setDesc(desc);
        commDialog.setTitle(title);
        commDialog.setDialogListener(mDialogListener);
        commDialog.setDescTextSize(17);
        commDialog.setTitleVisible(true);
        commDialog.setOkTxt(context.getResources().getString(R.string.confirm));
        commDialog.setOkTxtColor(context.getResources().getColor(R.color.text_blue));
        commDialog.setCancelTxt(context.getResources().getString(R.string.cancel));
        commDialog.setCancelTxtColor(context.getResources().getColor(R.color.text_blue));
        commDialog.setCanceledOnTouchOutside(false);
        commDialog.show();
    }

    public static void showGroupInviteRefuesDialog(Context context, String desc, String memberNum, DialogListener mDialogListener){
        desc = getGroupInviteContent(desc, memberNum);
        CommDialog commDialog = new CommDialog(context);
        commDialog.setDialogListener(mDialogListener);
        commDialog.setDesc(desc);
        commDialog.setDescTextSize(17);
        commDialog.getDescTextView().setLineSpacing(10,1);
        commDialog.setSingleBtn();
        commDialog.setOkTxt(context.getResources().getString(R.string.confirm));
        commDialog.setOkTxtColor(context.getResources().getColor(R.color.text_blue));
        commDialog.setTitleVisible(false);
        commDialog.setCanceledOnTouchOutside(false);
        commDialog.show();
    }

    private static String getGroupInviteContent(String name, String memberNum){
        if(TextUtils.isEmpty(memberNum)){
            return "   “" + name + "”" + "等已经关闭直接邀请进群设置，已发送通知，等待对方同意";
        }
        return "   “" + name + "”" + "等" + memberNum + "人已经关闭直接邀请进群设置，已发送通知，等待对方同意";
    }
}
