package com.wd.daquan.mine.presenter;

import com.da.library.utils.DateUtil;
import com.wd.daquan.model.bean.CloudWithdrawRecordEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UserCloudWallet;
import com.wd.daquan.model.bean.WithdrawRecordBean;
import com.wd.daquan.model.bean.WithrawCountEntity;
import com.wd.daquan.model.bean.WithrawRecordEntity;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WalletCloudPresenter 用户零钱功能
 */
public class WalletCloudPresenter extends MinePresenter<WalletCloudIView<DataBean>> {

    /**
     * 获取零钱用户信息
     */
    public void getUserCloudWallet(String url, Map<String,String> params){
        RetrofitHelp.getUserApi().getUserCloudWallet(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<UserCloudWallet>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<UserCloudWallet> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<UserCloudWallet> entity) {
                failed(url,code,entity);
            }
        });
    }

    /**
     * 零钱提现
     */
    public void getUserCashWithdrawal(String url, Map<String,String> params){
        RetrofitHelp.request(url, params, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                failed(url,code,entity);
            }
        });
    }

    /**
     * 零钱提现密码
     */
    public void setUserTransactionPassword(String url, Map<String,String> params){
        RetrofitHelp.request(url, params, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                failed(url,code,entity);
            }
        });
    }

    /**
     * 零钱提现记录
     */
    public void geUserCloudWalletTransactionRecord(String url, Map<String,String> params){
        RetrofitHelp.getUserApi().geUserCloudWalletTransactionRecord(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<CloudWithdrawRecordEntity>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<CloudWithdrawRecordEntity> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<CloudWithdrawRecordEntity> entity) {
                failed(url,code,entity);
            }
        });
    }


    /**
     * 对接口返回的提现记录进行处理
     */
    public List<WithdrawRecordBean> parserWalletTransactionRecord(List<WithdrawRecordBean> recordBeanList,CloudWithdrawRecordEntity withdrawRecordEntity){

        for (WithrawRecordEntity recordEntity : withdrawRecordEntity.getRecordResponseDtos()){
            Calendar date = DateUtil.getStringToCalendar(recordEntity.getRecordTimeStr(),DateUtil.YMDHM);
            int year = date.get(Calendar.YEAR);
            int month = date.get(Calendar.MONTH)+1;//月份要+1
            long timeLong = DateUtil.string2Long(recordEntity.getRecordTimeStr());
            String timeStr = DateUtil.getDateToString(timeLong,DateUtil.CN_FORMAT1);

            WithdrawRecordBean recordBean = new WithdrawRecordBean();
            recordBean.setYear(year);
            recordBean.setMonth(month);
            recordBean.setRecordDate(timeStr);
            List<WithdrawRecordBean.WithdrawRecordSubBean> withdrawRecordSubBeanList;
            WithdrawRecordBean.WithdrawRecordSubBean recordSubBean = recordBean.getWithdrawRecordSubBean();
            recordSubBean.setId(recordEntity.getId());
            recordSubBean.setPaymentNo(recordEntity.getPaymentNo());
            recordSubBean.setRecordId(recordEntity.getRecordId());
            recordSubBean.setRecordTime(recordEntity.getRecordTime());
            recordSubBean.setRecordTimeStr(recordEntity.getRecordTimeStr());
            recordSubBean.setRecordType(recordEntity.getRecordType());
            recordSubBean.setRemark(recordEntity.getRemark());
            recordSubBean.setStatus(recordEntity.getStatus());
            recordSubBean.setSubject(recordEntity.getSubject());
            recordSubBean.setTotalAmount(recordEntity.getTotalAmount());
            recordSubBean.setUid(recordEntity.getUid());
            recordSubBean.setWxReturnCode(recordEntity.getWxReturnCode());
            if (recordBeanList.contains(recordBean)){//对象内部重写了equals方法，只判断月份是否相同
                int index = recordBeanList.indexOf(recordBean);
                WithdrawRecordBean recordBeanTemp = recordBeanList.get(index);
                long incomeAmount = recordBeanTemp.getIncomeAmount();//收入
                long expenditureAmount = recordBeanTemp.getExpenditureAmount();//支出
                if ("0".equals(recordEntity.getRecordType())){//存入
                    incomeAmount += recordEntity.getTotalAmount();
                }else {//提现
                    expenditureAmount += recordEntity.getTotalAmount();
                }
                recordBeanTemp.setIncomeAmount(incomeAmount);
                recordBeanTemp.setExpenditureAmount(expenditureAmount);
                withdrawRecordSubBeanList =  recordBeanTemp.getRecordSubBeans();
                withdrawRecordSubBeanList.add(recordSubBean);
            }else {
                if ("0".equals(recordEntity.getRecordType())){//存入
                    recordBean.setIncomeAmount(recordEntity.getTotalAmount());
                }else {//提现
                    recordBean.setExpenditureAmount(recordEntity.getTotalAmount());
                }
                withdrawRecordSubBeanList = new ArrayList<>();
                withdrawRecordSubBeanList.add(recordSubBean);
                Map<String,WithrawCountEntity> withrawCountEntityMap = parserWithdrawCount(withdrawRecordEntity.getMonthExpenditureStatis(),withdrawRecordEntity.getMonthIncomeStatis());
                recordBean.setWithrawCountEntityMap(withrawCountEntityMap);
                recordBean.setRecordSubBeans(withdrawRecordSubBeanList);
                recordBeanList.add(recordBean);
            }
        }
        return recordBeanList;
    }

    /**
     * 提现记录中每月的统计
     * @param monthExpenditureStatis 支出
     * @param monthIncomeStatis 收入
     * @return 合并处理后的数据
     */
    private Map<String,WithrawCountEntity> parserWithdrawCount(List<WithrawCountEntity> monthExpenditureStatis,List<WithrawCountEntity> monthIncomeStatis){
        Map<String,WithrawCountEntity> result = new HashMap<>();
        for (WithrawCountEntity entity : monthExpenditureStatis){
            entity.setExpenditure(entity.getSumTotalAmount());
            result.put(entity.getMonth(),entity);
        }
        for (WithrawCountEntity entity : monthIncomeStatis){
            if (result.containsKey(entity.getMonth())){
                result.get(entity.getMonth()).setIncome(entity.getSumTotalAmount());
            }else {
                entity.setIncome(entity.getSumTotalAmount());
                result.put(entity.getMonth(),entity);
            }
        }
        return result;
    }
}