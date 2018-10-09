package com.canbot.retrofitdemo.view.Imp;

import android.graphics.Bitmap;

import com.canbot.retrofitdemo.domain.bean.FaceRecognition;

/**
 * Created by ${ping} on 2018/8/20.
 */
public interface IFaceView {
    void regonizedSuccess(FaceRecognition faceRecognition);
    void registerSuccess();
    void  regonizedFaulire();
    void registerFaulire();
    void onBitmap(Bitmap bitmap);
}
