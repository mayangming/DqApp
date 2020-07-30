package com.wd.daquan.chat.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.da.library.view.base.BaseDialogFragment;
import com.turntableview.ITurntableListener;
import com.turntableview.TurntableView;
import com.wd.daquan.R;

import java.util.ArrayList;

/**
 * 红大转盘抽奖的Dialog
 */
public class TurntableLotteryFragment extends BaseDialogFragment{

    private ImageView mIvGo;
    private TurntableView mTurntable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return setContentView(R.layout.fragment_turntable_lottery);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvGo = view.findViewById(R.id.iv_node);
        mTurntable = (TurntableView) findViewById(R.id.turntable);
        //开始抽奖
        mIvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTurntable.startRotate(new ITurntableListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(getContext(), "开始抽奖", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEnd(int position, String name) {
                        Log.e("YM","抽奖结束抽中第" + (position + 1) + "位置的奖品:" + name);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void changeColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ff8584"));
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(Color.parseColor("#000000"));
        mTurntable.setBackColor(colors);
    }

    private void changeDatas() {
        int num = 6;
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            names.add("第" + (i + 1));
            bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }
        mTurntable.setDatas(num, names, bitmaps);
    }
}