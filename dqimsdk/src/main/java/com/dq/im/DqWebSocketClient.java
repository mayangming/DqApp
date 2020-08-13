package com.dq.im;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.dq.im.config.HttpConfig;
import com.dq.im.ipc.DqWebSocketListener;
import com.dq.im.ipc.DqWebSocketMessageFilter;
import com.dq.im.model.ImContentDeserializer;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.third_system.ViVoPushManager;
import com.dq.im.third_system.XiaoMiPushManager;
import com.dq.im.type.SocketStatus;
import com.dq.im.util.NetWorkUtil;
import com.dq.im.util.SimpleNetWorkConnectCallBack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import http.log.SimpleInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * 斗圈WebSocket客户端
 */
public class DqWebSocketClient {
    private static final int RETRY_CONNECT = 0;//重连
    public static final int REGISTER_MI_SUCCESS = 1;//小米注册通知
    public static final int REGISTER_VIVO_SUCCESS = 2;//VIVO注册通知
    public static final int REGISTER_OPPO_SUCCESS = 3;//OPPO注册通知
    private OkHttpClient mOkHttpClient;
    private Request request;
    private EchoWebSocketListener socketListener;
    private Gson gson;
    private WebSocket dqWebSocket;
    private boolean isConnectIng = false;//WebSocket是否在链接过程中
    private DqWebSocketListener dqWebSocketListener;
    private int retryDuration = 5 * 1000;//重连间隔，两次重连间隔多久
    private int retryCount = 3;//重连次数，当超过该次数后不再重连，-1为一直重连
    private int retryCountCurrent = 0;//当前重连次数
    private boolean alwaysRetry = false;//是否一直重连
    private boolean netWorkConnect = false;//网络链接状态，true是已经链接，false是没有链接
    private NetWorkUtil netWorkUtil;
    private String userId = "";//用户登陆的ID
    private boolean socketConnecting = false;//socket是否在建立链接过程中，防止建立链接过程中进行再次建立链接
    private static DqWebSocketClient dqWebSocketClient;//socket客户端

    private @SocketStatus
    int socketCurrentStatus = SocketStatus.SOCKET_STATUS_NO_CONNECT;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RETRY_CONNECT:
                    Log.e("斗圈","开始重连:"+retryCountCurrent);
                    retryCountCurrent++;
                    build();
//                    if (alwaysRetry || retryCountCurrent < retryCount){
//                        Log.e("斗圈","开始重连:"+retryCountCurrent);
//                        retryCountCurrent++;
//                        build();
//                    }else if (retryCountCurrent >= retryCount){
//                        Log.e("斗圈","重连次数已超过阈值,当前重连次数:"+retryCountCurrent);
//                        socketListener.clear();
//                        handler.removeMessages(RETRY_CONNECT);
//                    }
                    break;
                case REGISTER_MI_SUCCESS:
                    Log.e("YM","注册UserId成功");
                    MiPushClient.setAlias(ImProvider.context, userId, null);
                    XiaoMiPushManager.getXiaoMiPushManager().sendMessage("xiaomi",userId);
                    break;
                case REGISTER_VIVO_SUCCESS:

                    PushClient.getInstance(ImProvider.context).bindAlias(userId, new IPushActionListener() {
                        @Override
                        public void onStateChanged(int i) {
                            Log.e("YM","VIVO绑定别名结果:"+i);
                            ViVoPushManager.getViVoPushManager().sendMessage("vivo",userId);
                        }
                    });
                    break;
                case REGISTER_OPPO_SUCCESS:
                    
                    break;
            }
        }
    };

    /**
     * 创建单例模式
     * @param userId
     * @return
     */
    public static DqWebSocketClient createSocketInstance(String userId){
        if (null == dqWebSocketClient){
            dqWebSocketClient = new DqWebSocketClient(userId);
        }
        return dqWebSocketClient;
    }

    public static DqWebSocketClient getInstance(){
        if (null == dqWebSocketClient){
            throw new IllegalStateException("需要先使用createSocketInstance(String userId)初始化Socket链接！");
        }
        return dqWebSocketClient;
    }
    public static DqWebSocketClient getInstance2(){
        return dqWebSocketClient;
    }


    public void sendHandlerMessage(int what,Object object){
        handler.sendEmptyMessage(what);
    }


    private DqWebSocketClient(String userId) {
        this.userId = userId;
        initGson();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new SimpleInterceptor());
        mOkHttpClient = builder
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();
//        Request.Builder requestBuild = new Request.Builder();
//        requestBuild.header("Sec-WebSocket-Version","14");
//        requestBuild.addHeader("Sec-WebSocket-Version","15");
//        Request request = requestBuild.url("ws://echo.websocket.org").build();

//        String socketServer = "ws://192.168.72.115:6999?userId="+ userId;
        String socketServer = HttpConfig.getInstance().getHTTP_SERVER() + userId;
        Log.e("YM","链接的socket地址:"+socketServer);
        request = new Request.Builder().url(socketServer).build();
//        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        socketListener = new EchoWebSocketListener();
        socketListener.setUserId(userId);
        netWorkUtil = new NetWorkUtil(new SimpleNetWorkConnectCallBack() {
            @Override
            public void isConnect(boolean isConnect) {
//                Toast.makeText(ImProvider.context,"网络状态:"+isConnect,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectChange(boolean isConnect) {
                super.connectChange(isConnect);
//                Toast.makeText(ImProvider.context,"网络状态切换:"+isConnect,Toast.LENGTH_SHORT).show();
                netWorkConnect = isConnect;
                if (isConnect){
                    retryCountCurrent = 0;
                    Log.e("YM","开始建立链接");
                    build();
                }
            }
        });
        netWorkUtil.isNetWorkConnect();
        netWorkUtil.addConnectChangeListener(ImProvider.context);
    }

    public void switchUserId(String userId){
        Log.e("YM","切换账号");
        Log.e("YM","socketConnecting:"+socketConnecting);
        Log.e("YM","isConnectIng:"+isConnectIng);
        this.userId = userId;
        String socketServer = ResourceBundle.getBundle("appConfig").getString("webSocketServer") + userId;
        request = new Request.Builder().url(socketServer).build();
//        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        socketListener.setUserId(userId);
    }

    private void initGson(){
        gson = new GsonBuilder()
                .registerTypeAdapter(P2PMessageBaseModel.class, new ImContentDeserializer())
                .create();
    }

    /**
     * 获取当前Socket链接状态
     */
    public @SocketStatus int getSocketStatus(){
        return socketCurrentStatus;
    }

    public void setDqWebSocketListener(final DqWebSocketListener dqWebSocketListener) {
        this.dqWebSocketListener = dqWebSocketListener;
        socketListener.setDqWebSocketListener(new DqWebSocketMessageFilter() {
            @Override
            public void connectFail(String fileConnect) {
                Log.e("YM","链接失败");
                dqWebSocketListener.connectFail(fileConnect);
                isConnectIng = false;
                socketConnecting = false;
                socketCurrentStatus = SocketStatus.SOCKET_STATUS_FAIL;
                if (!"Socket closed".equals(fileConnect)){
                    if (null != handler){
                        handler.sendEmptyMessageDelayed(RETRY_CONNECT,retryDuration);
                    }
                }
            }

            @Override
            public void connectSuccess(WebSocket webSocket) {
                dqWebSocketListener.connectSuccess();
                dqWebSocket = webSocket;
                isConnectIng = true;
                socketConnecting = false;
                socketCurrentStatus = SocketStatus.SOCKET_STATUS_CONNECTING;
            }

            @Override
            public void connectClose() {
                dqWebSocketListener.connectClose();
                isConnectIng = false;
                socketConnecting = false;
                socketCurrentStatus = SocketStatus.SOCKET_STATUS_CLOSE;
            }

            @Override
            public void onMessageReceiver(String message) {
//                P2PMessageBaseModel notificationModel = ImParserUtils.getP2PMessageBaseModel(message);
//                if ("6".equals(notificationModel.getType())){
//                    buildNotification(notificationModel);
//                }else {
//                    dqWebSocketListener.onMessageReceiver(message);
//                }
                dqWebSocketListener.onMessageReceiver(message);
            }
        });
    }



    public void build(){
        if (TextUtils.isEmpty(userId)){
            Log.e("YM","没有用户账户，不允许建立链接！");
            return;
        }
        Log.e("YM","build中的socketConnecting值:"+socketConnecting);
        if (socketConnecting){
            Log.e("YM","斗圈Socket正在建立链接中，不允许再次建立链接！");
            return;
        }
        socketConnecting = true;
        if (isConnectIng){
            if (null != dqWebSocketListener){
                Log.e("YM","斗圈Socket已经建立了链接,不允许再次建立链接！");
                dqWebSocketListener.connectFail("斗圈Socket已经建立了链接,不允许再次建立链接！");
            }
            return;
        }
        mOkHttpClient.newWebSocket(request, socketListener);
//        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    public boolean sendMessage(String json){
        boolean isResult = false;
        if (null != socketListener){
            isResult  = socketListener.senMessage(json);
//            Log.e("YM","发送消息的结果:"+isResult);
        }
        return isResult;
    }

    private void buildNotification(P2PMessageBaseModel notificationModel){
//        ImNotificationModel imNotificationModel = (ImNotificationModel)notificationModel.getImContentModel();
//        NotificationUtil notificationUtil = NotificationUtil.getInstance();
//        NotificationUtil.NotificationUtilBuild build = notificationUtil.createBuild();
//        build.title = imNotificationModel.getTitle();
//        build.content = imNotificationModel.getDescription();
//        build.context = ImProvider.context;
//        if (!TextUtils.isEmpty(imNotificationModel.getLinkUrl())){
//            Log.e("YM","链接有值:"+imNotificationModel.getLinkUrl());
//            build.intent = goDqWeb(imNotificationModel.getLinkUrl(),imNotificationModel.getTitle());
//        }
//        notificationUtil.showNotification(build);
    }

    private Intent goDqWeb(String url, String title){
        String WEBVIEW_URL = "webview_url";
        String WEBVIEW_TITLE = "webview_title";
        Intent intent = new Intent();
        ComponentName cn = new ComponentName("com.wd.daquan", "com.wd.daquan.common.activity.WebViewActivity");
        intent.putExtra(WEBVIEW_URL,url);
        intent.putExtra(WEBVIEW_TITLE,title);
        intent.setComponent(cn);
        return intent;
    }

    /**
     * WebSocket是否在链接
     * @return true，链接中
     */
    public boolean isConnectIng() {
        return isConnectIng;
    }

    /**
     * 取消WebSocket链接
     */
    public void cancel(){
        if (null != dqWebSocket){
            dqWebSocket.cancel();
            dqWebSocket.close(1000,"onClose");
        }
        if (null != dqWebSocketListener){
            socketListener.clear();
        }
        handler.removeCallbacksAndMessages(null);
//        handler = null;
        if (null != netWorkUtil){
            netWorkUtil.clear();
        }
        socketConnecting = false;
        isConnectIng = false;
//        dqWebSocketClient = null;
//        dqWebSocket = null;
    }

    /**
     * 设置是否一直重连
     */
    public void setAlwaysRetry(boolean alwaysRetry) {
        this.alwaysRetry = alwaysRetry;
    }

    /**
     * 设置重连次数
     */
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * 两次重连间隔时间
     */
    public void setRetryCountCurrent(int retryCountCurrent) {
        this.retryCountCurrent = retryCountCurrent;
    }

}