package com.canbot.retrofitdemo.domain.imp;

import megvii.facepass.types.FacePassDetectionResult;

/**
 * Created by ${ping} on 2018/8/20.
 */
public interface FaceMoudle {
    void addFace(byte[] mdata);
    void bindFace(String json);
    void  regonizedFace(FacePassDetectionResult mFacePassDetectionResult);


}
