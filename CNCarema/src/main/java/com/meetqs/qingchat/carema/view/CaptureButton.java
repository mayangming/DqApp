package com.meetqs.qingchat.carema.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.meetqs.qingchat.carema.listener.CaptureListener;
import com.meetqs.qingchat.carema.util.CheckPermission;
import com.meetqs.qingchat.carema.util.LogUtil;
import com.da.library.constant.AppConfig;

import static com.meetqs.qingchat.carema.view.JCameraView.BUTTON_STATE_BOTH;
import static com.meetqs.qingchat.carema.view.JCameraView.BUTTON_STATE_ONLY_CAPTURE;
import static com.meetqs.qingchat.carema.view.JCameraView.BUTTON_STATE_ONLY_RECORDER;

public class CaptureButton extends View {

    private int state;              //当前按钮状态
    private int button_state;       //按钮可执行的功能状态（拍照,录制,两者）

    public static final int STATE_IDLE = 0x001;        //空闲状态
    public static final int STATE_PRESS = 0x002;       //按下状态
    public static final int STATE_LONG_PRESS = 0x003;  //长按状态
    public static final int STATE_RECORDERING = 0x004; //录制状态
    public static final int STATE_BAN = 0x005;         //禁止状态

    private int progress_color = AppConfig.THEME_COLOR;            //进度条颜色
    private int outside_color = 0xEEDCDCDC;             //外圆背景色
    private int inside_color = 0xFFFFFFFF;              //内圆背景色


    private float event_Y;  //Touch_Event_Down时候记录的Y值


    private Paint mPaint;

    private float strokeWidth;          //进度条宽度
    private int outside_add_size;       //长按外圆半径变大的Size
    private int inside_reduce_size;     //长安内圆缩小的Size

    //中心坐标
    private float center_X;
    private float center_Y;

    private float button_radius;            //按钮半径
    private float button_outside_radius;    //外圆半径
    private float button_inside_radius;     //内圆半径
    private int button_size;                //按钮大小

    private float progress;         //录制视频的进度
    private int duration;           //录制视频最大时间长度
    private int min_duration;       //最短录制时间限制
    private int recorded_time;      //记录当前录制的时间

    private RectF rectF;

    private CaptureListener captureLisenter;        //按钮回调接口
    private RecordCountDownTimer timer;             //计时器

    private boolean isRecordPermission = true;

    private Handler mHandler = new Handler();

    public CaptureButton(Context context) {
        super(context);
    }

    public CaptureButton(Context context, int size) {
        super(context);
        this.button_size = size;
        button_radius = size / 2.0f;

        button_outside_radius = button_radius;
        button_inside_radius = button_radius * 0.75f;

        strokeWidth = size / 15;
        outside_add_size = size / 5;
        inside_reduce_size = size / 8;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        progress = 0;

        state = STATE_IDLE;                //初始化为空闲状态
        button_state = BUTTON_STATE_BOTH;  //初始化按钮为可录制可拍照
        duration = 10 * 1000;              //默认最长录制时间为10s
        min_duration = 1500;              //默认最短录制时间为1.5s

        center_X = (button_size + outside_add_size * 2) / 2;
        center_Y = (button_size + outside_add_size * 2) / 2;

        rectF = new RectF(
                center_X - (button_radius + outside_add_size - strokeWidth / 2),
                center_Y - (button_radius + outside_add_size - strokeWidth / 2),
                center_X + (button_radius + outside_add_size - strokeWidth / 2),
                center_Y + (button_radius + outside_add_size - strokeWidth / 2));

        timer = new RecordCountDownTimer(duration, duration / 360);    //录制定时器


        if (CheckPermission.getRecordState() != CheckPermission.STATE_SUCCESS) {
            isRecordPermission = false;
        }

//        mGestureDetector = new GestureDetector(context, new CNGestureListener());
    }

//    private GestureDetector mGestureDetector = null;

    public void setRecordPermission(boolean recordPermission) {
        isRecordPermission = recordPermission;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button_size + outside_add_size * 2, button_size + outside_add_size * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(outside_color); //外圆（半透明灰色）
        canvas.drawCircle(center_X, center_Y, button_outside_radius, mPaint);

        mPaint.setColor(inside_color);  //内圆（白色）
        canvas.drawCircle(center_X, center_Y, button_inside_radius, mPaint);

        //如果状态为录制状态，则绘制录制进度条
        if (state == STATE_RECORDERING) {
            mPaint.setColor(progress_color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            canvas.drawArc(rectF, -90, progress, false, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i("state = " + state);
                LogUtil.i("ACTION_DOWN ");
                if (event.getPointerCount() > 1 || state != STATE_IDLE)
                    break;
                event_Y = event.getY();     //记录Y值
                state = STATE_PRESS;        //修改当前状态为点击按下
                LogUtil.i("ACTION_DOWN state = " + state);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        optionCarema();
                    }
                }, 100);
                break;
            case MotionEvent.ACTION_MOVE:
                if (captureLisenter != null
                        && state == STATE_RECORDERING
                        && (button_state == BUTTON_STATE_ONLY_RECORDER || button_state == BUTTON_STATE_BOTH)) {
                    //记录当前Y值与按下时候Y值的差值，调用缩放回调接口
                    captureLisenter.recordZoom(event_Y - event.getY());

                    if (!isContain(this, event.getRawX(), event.getRawY())) {
                        handlerUp();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isContain(this, event.getRawX(), event.getRawY())) {
                    handlerUp();
                }
                break;
        }
        return true;
    }


    /**
     * 检查是否在某个VIEW区域内
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isContain(View view, float x, float y) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        if (x >= point[0] && x <= (point[0] + view.getWidth()) && y >= point[1] && y <= (point[1] + view.getHeight())) {
            return true;
        }
        return false;
    }

    public int getState() {
        return state;
    }

    public boolean isRecording() {
        return state == STATE_RECORDERING;
    }

    private void handlerUp() {
        LogUtil.i("ACTION_UP state = " + state);
        mHandler.removeCallbacksAndMessages(null);
        switch (state) {
            case STATE_PRESS:
                LogUtil.i("-------> pictuie up");
                takePicture();
                break;
            case STATE_RECORDERING:
                LogUtil.i("######## video up");
                timer.cancel(); //停止计时器
                recordEnd();    //录制结束
                break;
        }
    }
    /**
     * 拍照
     */
    private void takePicture() {
        if (captureLisenter != null && (button_state == BUTTON_STATE_ONLY_CAPTURE || button_state ==
                BUTTON_STATE_BOTH)) {
            //回调拍照接口
            captureLisenter.takePictures();
            state = STATE_BAN;
        } else {
            state = STATE_IDLE;
        }
    }

    /**
     * 开始录制
     */
    private void recordStart() {
        state = STATE_LONG_PRESS;
        if (captureLisenter != null) {
            captureLisenter.recordStart();
        }
        state = STATE_RECORDERING;
        timer.start();
    }

    /**
     * 结束录制
     */
    private void recordEnd() {
        if (captureLisenter != null) {
            if (recorded_time < min_duration) { // 回调录制时间过短, 当成拍照处理
                long startTime = System.currentTimeMillis();
                captureLisenter.recordShort(recorded_time);
                takePicture();
                Log.w("xxxx", "拍照结束时间：" + (System.currentTimeMillis() - startTime));
            } else { // 回调录制结束
                captureLisenter.recordEnd(recorded_time);
            }
        }
        resetRecordAnim();  //重制按钮状态
    }

    //重制状态
    private void resetRecordAnim() {
        state = STATE_BAN;
        progress = 0;       //重制进度
        invalidate();
        //还原按钮初始状态动画
        resetRecordAnimation(
                button_outside_radius,
                button_radius,
                button_inside_radius,
                button_radius * 0.75f
        );
        LogUtil.i("--> resetRecordAnim");
    }

//    //内圆动画
//    private void startCaptureAnimation(float inside_start) {
//        ValueAnimator inside_anim = ValueAnimator.ofFloat(inside_start, inside_start * 0.75f, inside_start);
//        inside_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                button_inside_radius = (float) animation.getAnimatedValue();
//                invalidate();
//            }
//        });
//        inside_anim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                //回调拍照接口
//                captureLisenter.takePictures();
//                state = STATE_BAN;
//            }
//        });
//        inside_anim.setDuration(100);
//        inside_anim.start();
//    }

    //内外圆动画
    private void startRecordAnimation(float outside_start, float outside_end, float inside_start, float inside_end) {
       long time = System.currentTimeMillis();
        ValueAnimator mOutsizeAnim = ValueAnimator.ofFloat(outside_start, outside_end);
        ValueAnimator mInsizeAnim = ValueAnimator.ofFloat(inside_start, inside_end);
        //外圆动画监听
        mOutsizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_outside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //内圆动画监听
        mInsizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_inside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        //当动画结束后启动录像Runnable并且回调录像开始接口
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LogUtil.i("----- 结束录制动画 ----");
            }
        });
        set.playTogether(mOutsizeAnim, mInsizeAnim);
        set.setDuration(100);
        set.start();

        LogUtil.i("启动动画时间差: " + (System.currentTimeMillis() - time));
    }

    //内外圆动画
    private void resetRecordAnimation(float outside_start, float outside_end, float inside_start, float inside_end) {
        ValueAnimator outsizeAnim = ValueAnimator.ofFloat(outside_start, outside_end);
        ValueAnimator insizeAnim = ValueAnimator.ofFloat(inside_start, inside_end);
        //外圆动画监听
        outsizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_outside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //内圆动画监听
        insizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                button_inside_radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        //当动画结束后启动录像Runnable并且回调录像开始接口
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LogUtil.i("----- 重置录制动画 ----");
            }
        });
        set.playTogether(outsizeAnim, insizeAnim);
        set.setDuration(100);
        set.start();
    }


    //更新进度条
    private void updateProgress(long millisUntilFinished) {
        recorded_time = (int) (duration - millisUntilFinished);
        progress = 360f - millisUntilFinished / (float) duration * 360f;
        invalidate();
    }

    //录制视频计时器
    private class RecordCountDownTimer extends CountDownTimer {
        RecordCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateProgress(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            LogUtil.i("计数器结束");
            updateProgress(0);
            recordEnd();
        }
    }

    long time = 0;
    private void optionCarema() {
        time = System.currentTimeMillis();
        // 没有录制权限
        if (!isRecordPermission) {
            state = STATE_IDLE;
            if (captureLisenter != null) {
                captureLisenter.recordError();
                return;
            }
            return;
        }

        LogUtil.i("检查权限时间：" + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();

        //启动按钮动画，外圆变大，内圆缩小
        startRecordAnimation(
                button_outside_radius,
                button_outside_radius + outside_add_size,
                button_inside_radius,
                button_inside_radius - inside_reduce_size
        );

        recordStart();

        LogUtil.i("开启录制时间差：" + (System.currentTimeMillis() - time));
    }


    //设置最长录制时间
    public void setDuration(int duration) {
        this.duration = duration;
        timer = new RecordCountDownTimer(duration, duration / 360);    //录制定时器
    }

    //设置最短录制时间
    public void setMinDuration(int duration) {
        this.min_duration = duration;
    }

    //设置回调接口
    public void setCaptureLisenter(CaptureListener captureLisenter) {
        this.captureLisenter = captureLisenter;
    }

    //设置按钮功能（拍照和录像）
    public void setButtonFeatures(int state) {
        this.button_state = state;
    }

    //是否空闲状态
    public boolean isIdle() {
        return state == STATE_IDLE ? true : false;
    }

    //设置状态
    public void resetState() {
        state = STATE_IDLE;

        LogUtil.i("---->>>> resetState");
    }
}
