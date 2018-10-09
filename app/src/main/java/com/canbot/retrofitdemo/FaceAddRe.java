package com.canbot.retrofitdemo;

import com.canbot.retrofitdemo.domain.retrofitservice.GroupaddService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${ping} on 2018/8/13.
 */
public class FaceAddRe {

    private static Retrofit retrofit;

    public static GroupaddService getServiceInstance() {
        if (retrofit == null) {
            synchronized (FaceAddRe.class) {
                if (retrofit == null) {
                    new FaceAddRe();
                }
            }
        }
        return retrofit.create(GroupaddService.class);

    }

    private FaceAddRe() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);//连接 超时时间
        builder.writeTimeout(20,TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(30,TimeUnit.SECONDS);//读操作，超时间

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl("http://www.canbot.vip:18080")
                .addConverterFactory(GsonConverterFactory.create())//设置Json
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//设置rxjava
                .build();//增加返回值为实
//        return retrofit.create(GroupaddService.class);
    }
}
