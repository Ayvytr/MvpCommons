package com.wanshare.wscomponent.update.model;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UpdateServer {

    /**
     * 获取公共列表
     * @return
     */
    @GET("/api/v1/appUpGrade")
    Observable<VersionEntity> getVersion(@Query("equipType") Integer equipType);

}
