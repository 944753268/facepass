package com.canbot.retrofitdemo.domain.retrofitservice;

import com.canbot.retrofitdemo.domain.bean.FaceAdd;
import com.canbot.retrofitdemo.domain.bean.FaceRecognition;
import com.canbot.retrofitdemo.domain.bean.GroupAdd;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ${ping} on 2018/8/13.
 */
public interface GroupaddService {
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/api/group/v1/create")
    Observable< GroupAdd> addgroup(@Body RequestBody body);
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/api/group/v1/delete")
    Observable<GroupAdd> deletegroup(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/api/group/v1/bind")
    Observable<FaceAdd> bindFace(@Body RequestBody body);
    @Multipart
    @POST("/api/face/v1/add")
    Observable<FaceAdd> addface(@Part MultipartBody.Part file);

    @Multipart
    @POST("/api/service/recognize/v1")
    Observable<FaceRecognition> recongitedFace(@Part("group_name") RequestBody firstBody,@Part("face_data") RequestBody secondBody,@Part() List<MultipartBody.Part> parts);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("/api/image/v1/query")
    Observable<ResponseBody> downloadFile(@Query("face_token") String key);
}
