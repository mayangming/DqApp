package com.wd.daquan.mine.presenter;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.ExchangeRecordBean;
import com.wd.daquan.model.bean.ExchangeVipListBean;
import com.wd.daquan.model.bean.VipExchangeResultBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.List;
import java.util.Map;

/**
 * VIP兑换页面解析器
 */
public class VipExchangePresenter extends MinePresenter<VipExchangeIView<DataBean>>{
    /**
     * 获取邀请人数
     */
    public void getVipExchangeRecord(String url, Map<String,String> params){
        RetrofitHelp.getUserApi().getExchangeRecordBean(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<ExchangeRecordBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<ExchangeRecordBean> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<ExchangeRecordBean> entity) {
                failed(url,code,entity);
            }
        });
    }

    /**
     * 获取兑换列表
     */
    public void getExchangeVipList(String url, Map<String,String> params){
        RetrofitHelp.getUserApi().getExchangeVipList(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<List<ExchangeVipListBean>>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<List<ExchangeVipListBean>> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<List<ExchangeVipListBean>> entity) {
                failed(url,code,entity);
            }
        });
    }

    /**
     * 兑换VIP
     */
    public void getExchangeVip(String url, Map<String,String> params){
        RetrofitHelp.getUserApi().getVipExchangeResult(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<VipExchangeResultBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<VipExchangeResultBean> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<VipExchangeResultBean> entity) {
                failed(url,code,entity);
            }
        });
    }
}