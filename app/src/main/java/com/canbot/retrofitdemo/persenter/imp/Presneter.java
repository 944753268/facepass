package com.canbot.retrofitdemo.persenter.imp;

import android.graphics.Bitmap;

import com.canbot.retrofitdemo.domain.bean.FaceAdd;
import com.canbot.retrofitdemo.domain.bean.FaceRecognition;

/**
 * Created by ${ping} on 2018/8/20.
 */
public interface Presneter  {
    void addFaceSuccess(FaceAdd faceAdd);
    void binFaceSuccess(FaceAdd faceAdd);
    void regonizedSuccess(FaceRecognition faceRecognition);
    void regonizedFailure( String e);
    void onregisterFailure( String e);
    void loadPictureSuccess(Bitmap bitmap);
}
