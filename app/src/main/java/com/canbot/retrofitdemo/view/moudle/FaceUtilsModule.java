package com.canbot.retrofitdemo.view.moudle;

import android.content.Context;

import com.canbot.retrofitdemo.domain.executor.FaceUtils;
import com.canbot.retrofitdemo.JsonUtils;
import com.canbot.retrofitdemo.MainActivity;
import com.canbot.retrofitdemo.persenter.persenter.ImpFacePersenter;
import com.canbot.retrofitdemo.view.Impfasspass.InitHandlerFace;
import com.canbot.retrofitdemo.view.Impfasspass.ShowFace;
import com.canbot.retrofitdemo.view.activity.FaceReActivity;
import com.canbot.retrofitdemo.view.custom.CameraManager;
import com.canbot.retrofitdemo.view.permission.QuestPermission;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ${ping} on 2018/8/15.
 */

@Module
public class FaceUtilsModule {
    private MainActivity activity;
    private FaceReActivity faceReActivity;
    private ImpFacePersenter facePersenter;
    private Context context;
    private int cameraRotation;

    public FaceUtilsModule(ImpFacePersenter facePersenter){
        this .facePersenter =facePersenter;
    }

    //注入的类
    public FaceUtilsModule(MainActivity activity){
        this.activity=activity;
    }
    //注入的类
    public FaceUtilsModule(FaceReActivity faceReActivity, Context context, int cameraRotation){
        this.context=context;
        this.cameraRotation=cameraRotation;
        this.faceReActivity=faceReActivity;

    }
    @Provides//提供获取实例化对象的方法
    FaceUtils getFaceUtils(){
        return new FaceUtils();
    }
    @Provides
    JsonUtils getJsonUtils(){
        return  new JsonUtils();
    }

    @Provides
    QuestPermission  getPermission() {
        return new QuestPermission();
    }
    @Provides
    CameraManager getManager(){
    return new CameraManager();
    }
    @Provides
    InitHandlerFace getHandlerFace(Context context, int cameraRotation){
        return new InitHandlerFace(context,cameraRotation);
    }
    //参数提供者
    @Provides
    public int  getCameraRotation(){
        return cameraRotation;
    }
    //参数提供者
    @Provides
    public Context getContext(){
        return context;
    }
    @Provides
    ShowFace getShowFace(){
        return  new ShowFace();
    }
    @Provides
    ImpFacePersenter getFacePersenter(){
        return new ImpFacePersenter();
    }
}
