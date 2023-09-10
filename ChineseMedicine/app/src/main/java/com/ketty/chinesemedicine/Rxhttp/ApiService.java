package com.ketty.chinesemedicine.Rxhttp;

import com.ketty.chinesemedicine.entity.Result;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    @GET()
    Observable<Result> getMethod(@Url String url);

    @FormUrlEncoded
    @POST()
    Observable<Result> postMethod(@Url String url, @FieldMap Map<String, Object> map);

    @POST()
    Observable<Result> postMethod(@Url String url, @Body RequestBody Body);

}
