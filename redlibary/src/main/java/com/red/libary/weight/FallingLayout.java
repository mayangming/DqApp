package com.red.libary.weight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.red.libary.R;
import com.red.libary.animation.FallingPathAnimation;
import com.red.libary.ipc.FallingOnClickIpc;

import java.lang.ref.WeakReference;
import java.util.Random;

/** 飘落动画区域
 * 提示：
 * 红包的移动速度要和红包生成速度及红包的动画时长相配合
 * Created by ys on 2017/2/7 0007.
 */

public class FallingLayout extends RelativeLayout {

    private static final String TAG = "FallingLayout";

    private AttributeSet attrs;
    private int defStyleAttr;

    private int mHeight;    //动画区域高度
    private int mWidth;     //动画区域宽度
    private int dHeight = -1;    //飘落物体出处的高度，-1时使用默认图片的宽高
    private int dWidth = -1;     //飘落物体出处的宽度，-1时使用默认图片的宽高
    private int initX;      //初始X坐标
    private int pointX;     //飘落时X坐标点的偏移
    private int redPackageCountSpeed = 500;      //刷新速率，速率越小，同一时间出现的红包就越多，范围在100-1000之间
    private int moveSpeed = 300;//移动速率，值越小，移动越快，建议值在100-动画时长 之间
    private Config mConfig;
    private FallingHandler mFallingHandler;
    private FallingThread  mFallingThread;
    private FallingOnClickIpc fallingOnClickIpc;
    private volatile boolean isStopRedRain;//是否结束红包雨，默认为false
    public FallingLayout(Context context) {
        this(context,null);
    }

    public FallingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FallingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        setDefaultSize();
    }

    private void init() {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FallingLayout,defStyleAttr,0);

        mConfig = Config.fromTypeArray(a,initX,0,pointX,dWidth,dHeight);
        a.recycle();
    }

    /**
     *为单个漂浮物添加事件监听
     * @param fallingOnClickIpc
     */
    public void setFallingOnClickIpc(FallingOnClickIpc fallingOnClickIpc) {
        this.fallingOnClickIpc = fallingOnClickIpc;
    }

    public void setRedPackageCoundSpeed(int speed){
        this.redPackageCountSpeed = speed;
    }

    public void refreshSpeed(int speed){
        moveSpeed = speed;
    }

    /**
     * 设置漂浮物的高度
     * @param dHeight
     */
    public void setdHeight(int dHeight) {
        this.dHeight = dHeight;
    }

    /**
     * 设置漂浮物的宽度
     * @param dWidth
     */
    public void setdWidth(int dWidth) {
        this.dWidth = dWidth;
    }

    private void setDefaultSize(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.heart0);
        dWidth = bitmap.getWidth();
        dHeight = bitmap.getHeight();

        pointX = dWidth;
        bitmap.recycle();
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取动画区域的宽高，这里要注意，测量之后才有宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        initX = mWidth - dWidth;
    }

    /**
    R.mipmap.heart0,R.mipmap.heart1,R.mipmap.heart2,R.mipmap.heart3,
    R.mipmap.heart4,R.mipmap.heart5,R.mipmap.heart6,R.mipmap.heart7,
    R.mipmap.money0,R.mipmap.money1,R.mipmap.money2,R.mipmap.money3,R.mipmap.money4,
    R.mipmap.snow0,R.mipmap.snow1,R.mipmap.snow2,R.mipmap.snow3,
     **/
//    int[] drawableIds = {
//            R.mipmap.heart0,R.mipmap.heart1,R.mipmap.heart2,R.mipmap.heart3,
//            R.mipmap.money0,R.mipmap.money1,R.mipmap.money2,R.mipmap.money3,
//            R.mipmap.water0,R.mipmap.water4,R.mipmap.water2,R.mipmap.water3,
//            R.mipmap.snow0,R.mipmap.snow1,R.mipmap.snow2,R.mipmap.snow3};
//            随机图片集合
    int[] drawableIds = {
//            R.mipmap.money0,R.mipmap.money1,R.mipmap.money2,R.mipmap.money3
//            R.mipmap.img_red_packet,R.mipmap.img_red_packet_1,R.mipmap.img_red_packet_2,R.mipmap.img_red_packet_3
            R.mipmap.img_red_packet_10
    };

    /**
     *  添加单个漂移物
     */
    private Random random = new Random();
    public void addFallingBody(){
        FallingBodyView fallingBodyView = new FallingBodyView(getContext());
        fallingBodyView.setDrawable(drawableIds[random.nextInt(drawableIds.length)]);
        fallingBodyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDailog();
                if (null != fallingOnClickIpc){
                    fallingOnClickIpc.onClickFalling();
                }
                v.clearAnimation();
            }
        });
        //创建动画
        if(mConfig == null) {
            init();
            mConfig.animDuration = moveSpeed;
            mConfig.animLength = mHeight;
        }
        FallingPathAnimation pathAnimation = new FallingPathAnimation(mConfig);
        pathAnimation.start(fallingBodyView,this);
    }

    private long nowTime,lastTime;
    final static int[] sizeTable = {9,99,999,9999,99999,
            999999,9999999,99999999,999999999, Integer.MAX_VALUE};
    public static int sizeOfInt(int x){
        for (int i = 0; ; i++) {
            if(x <= sizeTable[i])
                return i + 1;
        }
    }

    private AlertDialog dialog;
    public void showDailog(){
        String msg;
        int index = new Random().nextInt(5);
        switch (index){
            case 1:
                msg = "普天同庆，一等奖！！！";
                break;
            case 2:
                msg = "手气不错哦，二等奖！";
                break;
            case 3:
                msg = "三等奖！哎呦不错哦！";
                break;
            case 4:
                msg = "群众安慰奖！";
                break;
            default:
                msg = "遗憾，再接再厉！";
                break;
        }
        if(dialog == null) {
            dialog = new AlertDialog.Builder(getContext())
                    .setMessage(msg)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
        }else{
            dialog.setMessage(msg);
        }
        dialog.show();
    }

    public boolean isRedRainIng(){
        return isStopRedRain;
    }


    public void startRedRain(){
        addFallingBody(1);
    }

    public void addFallingBody(int size){
//        switch (sizeOfInt(size)){
//            case 1:
//                size = size % 10;
//                break;
//            default:
//                size = size % 100;
//        }
        if(size == 0) return;
        nowTime = System.currentTimeMillis();
//        long time = nowTime - lastTime;
//        if(lastTime == 0){
//            time = 10 * 1000; //第一次分为2秒显示完
//        }
//        time = time / (size + 15);
        if(mFallingThread == null){
            mFallingThread = new FallingThread();
        }
        if(mFallingHandler == null){
            mFallingHandler = new FallingHandler(this);
            mFallingHandler.post(mFallingThread);
        }
        isStopRedRain = true;
        mFallingThread.addTask(redPackageCountSpeed,size);
//        lastTime = nowTime;
    }

    public void stopRedRain(){
        isStopRedRain = false;
    }

    /**
     *  清除动画
     */
    public void clean(){
        if(mFallingThread != null){
            mFallingThread.clean();
        }
    }

    /**
     *  回收动画
     */
    public void release(){
        if(mFallingHandler != null){
            mFallingHandler.removeCallbacks(mFallingThread);
            mFallingThread = null;
            mFallingHandler = null;
        }
    }

    public static class Config{
        public int initX;           //初始X坐标
        public int initY;           //初始Y坐标
        public int xRand;           //x坐标随机范围
        public int animLengthRand;  //漂移距离随机范围
        public int animLength;      //漂移距离
        public int bezierFactor;    //贝塞尔因子 弧度
        public int xPointFactor;    //x坐标弧度
        public int rainWidth;       //飘落物的宽度
        public int rainHeight;      //飘落物的大小
        public int animDuration;    //动画时长

        static public Config fromTypeArray(
                TypedArray typedArray, int x, int y, int pointX, int rainWidth, int rainHeight){
            Config config = new Config();
            Resources res = typedArray.getResources();
            config.initX = (int) typedArray.getDimension(R.styleable.FallingLayout_initX,x);
            config.initY = (int) typedArray.getDimension(R.styleable.FallingLayout_initY,y);
            config.xRand = (int) typedArray.getDimension(R.styleable.FallingLayout_xRand,
                    res.getDimensionPixelOffset(R.dimen.falling_anim_bezier_x_rand));
            config.animLengthRand = (int) typedArray.getDimension(R.styleable.FallingLayout_animLengthRand,
                    res.getDimensionPixelOffset(R.dimen.falling_anim_length_rand));
            config.animLength = (int) typedArray.getDimension(R.styleable.FallingLayout_animLength,
                    res.getDimensionPixelOffset(R.dimen.falling_anim_length));
            config.bezierFactor = typedArray.getInteger(R.styleable.FallingLayout_bezierFactor,
                    res.getInteger(R.integer.falling_anim_bezier_factor));
            config.animDuration = typedArray.getInteger(R.styleable.FallingLayout_anim_duration,
                    res.getInteger(R.integer.falling_anim_duration));
            config.xPointFactor = pointX;
            config.rainWidth = rainWidth;
            config.rainHeight = rainHeight;
            return config;
        }
    }

    /**
     *  定义Handler
     */
    public class FallingHandler extends Handler {
        public final static int MSG_SHOW = 1;
        WeakReference<FallingLayout> wf;
        public FallingHandler(FallingLayout layout){
            wf = new WeakReference<FallingLayout>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FallingLayout layout = wf.get();
            if(layout == null) return;
            switch (msg.what){
                case MSG_SHOW:
                    addFallingBody();
                    break;
                default:
                    break;
            }
        }
    }



    public class FallingThread implements Runnable {

        private long time = 0;//该值会影响同时出现红包的数量，建议在300以上
        private int allSize = 0;

        //添加任务
        public void addTask(long time,int size){
            this.time = time;
            allSize += size;
        }

        //清除任务
        public void clean(){
            allSize = 0;
        }

        @Override
        public void run() {
            if(mFallingHandler == null) return;
            //通过线程 postMessage ，不停的addFallingBody
//            if(allSize > 0){
//                mFallingHandler.sendEmptyMessage(FallingHandler.MSG_SHOW);
//                allSize--;
//            }
            if (isStopRedRain){
                mFallingHandler.sendEmptyMessage(FallingHandler.MSG_SHOW);
            }
            postDelayed(this,time);
        }
    }
}
