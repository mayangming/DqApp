package com.wd.daquan.login.splash;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.wd.daquan.R;

/**
 * 引导页的Fragment
 */
public class SplashFragment extends Fragment{
    public static String SPLASH_RES_KEY = "res";
    public static String SPLASH_IS_SKIP_KEY = "isSkip";
    private SplashFragmentListener splashFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SplashFragmentListener){
            splashFragmentListener = (SplashFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString() + " must implement SplashFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建页面
        return inflater.inflate(R.layout.fragment_splash,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int resId = getArguments().getInt(SPLASH_RES_KEY,R.mipmap.splash1);
        boolean isSkip = getArguments().getBoolean(SPLASH_IS_SKIP_KEY,false);

        //页面创建完毕的逻辑
        ImageView splashBg = view.findViewById(R.id.splash_img);
        splashBg.setImageResource(resId);
        Button isSkipBtn = view.findViewById(R.id.go_main);
        if (isSkip){
            isSkipBtn.setVisibility(View.VISIBLE);
        }else {
            isSkipBtn.setVisibility(View.GONE);
        }
        isSkipBtn.setOnClickListener(v -> {
            if (null != splashFragmentListener){
                splashFragmentListener.skipListener();
            }
        });
    }

    public interface SplashFragmentListener{
        void skipListener();
    }

}