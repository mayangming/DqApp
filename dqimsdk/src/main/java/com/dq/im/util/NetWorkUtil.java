package com.dq.im.util;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;

import com.dq.im.bean.EventBusBean;
import com.dq.im.type.EventBusBeanType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络工具类
 * 该类不能使用唯一实例，因为里面涉及到子线程，如果使用唯一实例，会导致变量覆盖，数据混乱的问题
 */
public class NetWorkUtil {
    private boolean isConnect = false;
    private NetWorkConnectIpc netWorkConnectIpc;
    private NetWorkStateReceiver netWorkStateReceiver;
    private Context context;
    public NetWorkUtil(NetWorkConnectIpc netWorkConnectIpc){
        EventBus.getDefault().register(this);
        this.netWorkConnectIpc = netWorkConnectIpc;
    }

    /**
     * 使用这种方式是因为，Android没有直接判断网络可用的方式，只有是否链接的方式
     * 参考链接: https://blog.csdn.net/innerwork/article/details/80776783
     */
    public void isNetWorkConnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket s;
                    s = new Socket();
                    InetAddress host = InetAddress.getByName("8.8.8.8");//国内使用114.114.114.114，如果全球通用google：8.8.8.8
                    s.connect(new InetSocketAddress(host, 53), 5000);//google:53
                    isConnect = s.isConnected();
                    s.close();
                } catch ( IOException e) {
//                    e.printStackTrace();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {//切换为主线程
                    @Override
                    public void run() {
                        if (null != netWorkConnectIpc){
                            netWorkConnectIpc.isConnect(isConnect);
                        }
                    }
                });

            }
        }).start();
    }

    /**
     * 添加网络切换监听
     */
    public void addConnectChangeListener(Context context){
        this.context = context;
        if (null != netWorkStateReceiver){
            return;
        }
        netWorkStateReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_DEFAULT);
        context.registerReceiver(netWorkStateReceiver, filter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBean event) {
        if (EventBusBeanType.TYPE_NET_WORK == event.getStatus()){
            boolean isConnect = (boolean)event.getContent();
            if (null != netWorkConnectIpc) {
                netWorkConnectIpc.connectChange(isConnect);
            }
        }
    };

    public interface NetWorkConnectIpc{
        //该方法需要调用 isNetWorkConnect()触发
        void isConnect(boolean isConnect);
        void connectChange(boolean isConnect);
    }

    /**
     * 清除数据
     */
    public void clear(){
        EventBus.getDefault().unregister(this);
        if (null != context){
            try {
                context.unregisterReceiver(netWorkStateReceiver);
            }catch (Exception e){//关于广播的解绑注册这里强制使用异常进行捕获
                e.printStackTrace();
            }
        }
        netWorkConnectIpc = null;
    }

}