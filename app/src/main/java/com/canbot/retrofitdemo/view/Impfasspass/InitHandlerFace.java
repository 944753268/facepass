package com.canbot.retrofitdemo.view.Impfasspass;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.canbot.retrofitdemo.view.Imp.InitFaceSdk;

import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;

/**
 * Created by ${ping} on 2018/7/4.
 */
public class InitHandlerFace {
    private Context mcontext;
    private int cameraRotation;
    public FacePassModel trackModel;
    public FacePassModel poseModel;
    public FacePassModel blurModel;
    public FacePassModel livenessModel;

    public FacePassModel searchModel;
    public FacePassModel detectModel;
    public FacePassHandler mfacePassHandler=null;
    private InitFaceSdk initFaceSdk;

    public void setInitFaceSdk(InitFaceSdk initFaceSdk) {
        this.initFaceSdk = initFaceSdk;
    }

    public InitHandlerFace(){

    }

    public InitHandlerFace(Context context, int cameraRotation){
        this.mcontext =context;
        this.cameraRotation=cameraRotation;
    }

    public void initFaceHandler(){
        new Thread() {
            @Override
            public void run() {
                while (true ) {
                    Log.e("TAG","初始化开始.....");
                    if (FacePassHandler.isAvailable()) {
                        Log.e("TAG","sdk可用");
                        /* FacePass SDK 所需模型， 模型在assets目录下 */
                        trackModel = FacePassModel.initModel(mcontext.getApplicationContext().getAssets(), "tracker.DT1.4.1.dingding.20180315.megface2.9.bin");
                        poseModel = FacePassModel.initModel(mcontext.getApplicationContext().getAssets(), "pose.alfa.tiny.170515.bin");
                        blurModel = FacePassModel.initModel(mcontext.getApplicationContext().getAssets(), "blurness.v5.l2rsmall.bin");
                        livenessModel = FacePassModel.initModel(mcontext.getApplicationContext().getAssets(), "panorama.facepass.offline.180312.bin");
                        searchModel = FacePassModel.initModel(mcontext.getApplicationContext().getAssets(), "feat.small.facepass.v2.9.bin");
                        detectModel = FacePassModel.initModel(mcontext.getApplicationContext().getAssets(), "detector.mobile.v5.fast.bin");
                        /* SDK 配置 */
                        float searchThreshold = 75f;
                        float livenessThreshold = 70f;
                        boolean livenessEnabled = true;
                        int faceMinThreshold = 150;
                        FacePassPose poseThreshold = new FacePassPose(30f, 30f, 30f);
                        float blurThreshold = 0.2f;
                        float lowBrightnessThreshold = 70f;
                        float highBrightnessThreshold = 210f;
                        float brightnessSTDThreshold = 60f;
                        int retryCount = 2;
                        int rotation = cameraRotation;
                        Log.e(">>>>", "rotation= "  + rotation);
                        String fileRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        FacePassConfig config;
                        try {

                            /* 填入所需要的配置 */
                            config = new FacePassConfig(searchThreshold, livenessThreshold, livenessEnabled,
                                    faceMinThreshold, poseThreshold, blurThreshold,
                                    lowBrightnessThreshold, highBrightnessThreshold, brightnessSTDThreshold,
                                    retryCount, rotation, fileRootPath,
                                    trackModel, poseModel, blurModel, livenessModel, searchModel, detectModel);
                            /* 创建SDK实例 */
                            mfacePassHandler = new FacePassHandler(config);
                            initFaceSdk.initFaceSdk(mfacePassHandler);
                        } catch (FacePassException e) {
                            e.printStackTrace();
                            Log.e("TAG","mfacePassHandler=null");
                            return;
                        }
                        return;
                    }
                    try {
                        /* 如果SDK初始化未完成则需等待 */
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();




    }



}
