package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.CommRespEntity;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupManagerEntity;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.bean.TeamInviteBean;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 群组接口
 */
public interface GroupApi {
    /**
     * 创建群组
     */
    @POST
    Call<DataBean<CreateTeamEntity>> createTeam(@Url String url, @Body RequestBody requestBody);
    /**
     * 查询群组信息
     */
    @POST
    Call<DataBean<GroupInfoBean>> getGroupInfo(@Url String url, @Body RequestBody requestBody);

    @POST
    Call<DataBean<List<TeamBean>>> getTeamList(@Url String url, @Body RequestBody requestBody);

    @POST
    Call<DataBean<List<TeamInviteBean>>> getInviteTeamList(@Url String url, @Body RequestBody requestBody);

    @POST
    Call<DataBean<GroupManagerEntity>> getTeamAdmin(@Url String url, @Body RequestBody requestBody);

    @POST
    Call<DataBean<CommRespEntity>> getTeamInvite(@Url String url, @Body RequestBody requestBody);

}
