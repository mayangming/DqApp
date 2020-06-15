package com.da.library.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.da.library.R;

import java.util.Arrays;


public class PayKeyboardView extends PopupWindow implements View.OnClickListener {
    /**
     * 扩展的item
     */
    private String extra = "";
    /**
     * 支付金额
     */
    private String payMoney;
    /**
     * 类型
     */
    private String type;
    private View mMenuView;
    private OnPopWindowClickListener listener;
    //当前密码
    private String mCurrPwd = "";
    //自定义View之密码框
    private PwdInputView mPwdView;

    public PayKeyboardView(Activity context, String payMoney, String type, OnPopWindowClickListener listener) {
        this.listener = listener;
        this.payMoney = payMoney;
        this.type = type;
        initView(context);
    }

    /**
     *
     * @param payMoney 支付金额
     * @param type 支付类型
     * @param extra 是否显示零钱item
     * @param listener 密码输入监听
     */
    public PayKeyboardView(Activity context, String payMoney, String type, String extra, OnPopWindowClickListener listener) {
        this.listener = listener;
        this.payMoney = payMoney;
        this.type = type;
        this.extra = extra;
        initView(context);
    }

    private void initView(Activity context) {
        initViewInputPsw(context);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initViewInputPsw(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        mMenuView = inflater.inflate(R.layout.pay_pwd_popup_window, null);
        ImageView closeIv = mMenuView.findViewById(R.id.pay_key_board_close_iv);
        TextView moneyTypeTv = mMenuView.findViewById(R.id.pay_key_board_money_type_tv);
        TextView amountTv = mMenuView.findViewById(R.id.pay_key_board_amount_tv);
        mPwdView = mMenuView.findViewById(R.id.pwd_view);
        LinearLayout walletLl = mMenuView.findViewById(R.id.pay_key_board_wallet_ll);
        TextView extraTv = mMenuView.findViewById(R.id.pay_key_board_extra_tv);

        walletLl.setVisibility(TextUtils.isEmpty(extra) ? View.GONE : View.VISIBLE);
        amountTv.setVisibility(TextUtils.isEmpty(payMoney) ? View.GONE : View.VISIBLE);
        amountTv.setText(payMoney);
        moneyTypeTv.setText(type);
        //显示零钱金额
        extraTv.setText(extra);

        //关闭
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setAdapter(context);
    }

    private void setAdapter(Activity context) {
        RecyclerView recyclerView = mMenuView.findViewById(R.id.pay_key_board_recycler_view);
        String[] keys = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "delete"};

        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        //设置分割线
        DividerItemDecoration dividerVertical = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        DividerItemDecoration dividerHorizontal = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
        dividerVertical.setDrawable(context.getResources().getDrawable(R.drawable.divider_item_horizontal_shape));
        dividerHorizontal.setDrawable(context.getResources().getDrawable(R.drawable.divider_item_vertical_shape));
        recyclerView.addItemDecoration(dividerVertical);
        recyclerView.addItemDecoration(dividerHorizontal);

        PayKeyBoardAdapter adapter = new PayKeyBoardAdapter();
        recyclerView.setAdapter(adapter);
        adapter.update(Arrays.asList(keys));
    }

    @Override
    public void onClick(View v) {
        listener.onPopWindowClickListener(mCurrPwd, false);
        dismiss();
    }


    public interface OnPopWindowClickListener {
        void onPopWindowClickListener(String pwd, boolean complete);
    }

    private class PayKeyBoardAdapter extends CommRecyclerViewAdapter<String, PayKeyBoardHolder> {

        @Override
        protected PayKeyBoardHolder onBindView(ViewGroup parent, int viewType) {
            return new PayKeyBoardHolder(mInflater.inflate(R.layout.item_gridview_keyboard, parent, false));
        }

        @Override
        protected void onBindData(@NonNull PayKeyBoardHolder holder, int position) {
            final String item = getItem(position);
            if ("delete".equals(item)) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.itemView.setBackgroundResource(R.drawable.pay_key_board_delete_selector);
                holder.key.setVisibility(View.INVISIBLE);
            } else if (TextUtils.isEmpty(item)) {
                holder.delete.setVisibility(View.GONE);
                holder.itemView.setBackgroundResource(R.drawable.pay_key_board_delete_selector);
                holder.key.setVisibility(View.INVISIBLE);
            } else {
                holder.delete.setVisibility(View.GONE);
                holder.itemView.setBackgroundResource(R.drawable.comm_item_selector);
                holder.key.setVisibility(View.VISIBLE);
                holder.key.setText(item);
            }

            onItemClick(holder, item);
        }
    }

    private void onItemClick(@NonNull PayKeyBoardHolder holder, final String item) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是空不处理
                if (!TextUtils.isEmpty(item)) {
                    if ("delete".equals(item)) {
                        if(mCurrPwd.length() <= 0) {
                            return;
                        }
                        String pwd = mPwdView.getPwd();
                        mCurrPwd = pwd.substring(0, pwd.length() - 1);
                        mPwdView.setData(mCurrPwd);
                        listener.onPopWindowClickListener(mCurrPwd, false);
                    } else {
                        //非删除按钮
                        mCurrPwd += item;
                        mPwdView.setData(mCurrPwd);
                        if (mCurrPwd.length() == mPwdView.getMaxCount()) {
                            listener.onPopWindowClickListener(mCurrPwd, true);
                            dismiss();
                        } else {
                            listener.onPopWindowClickListener(mCurrPwd, false);
                        }
                    }
                }
            }
        });
    }

    private static class PayKeyBoardHolder extends RecyclerView.ViewHolder {
        TextView key;
        ImageView delete;

        PayKeyBoardHolder(View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.tv_key);
            delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
